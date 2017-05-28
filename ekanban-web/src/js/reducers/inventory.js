import {INVENTORY_CONSTANTS as c,ORDER_CONSTANTS as o, BIN_STATE as bs} from "../utils/constants";
import {getProductId, getBinId} from "../utils/miscUtil";

const initialState = {
  syncing: false,
  inventory:[],
  pendingMap: new Map(),
  stockMap: new Map(),
  toggleStatus: true,
  message: '',
  busy: false,
  ordering: false,
  orders:[]
};

const handlers = { 
  [c.INITIALIZE_INVENTORY]: (_, action) => {
    const resp = action.payload.inventory;
    let stockMap = new Map();
    let pendingMap = new Map();
    sessionStorage.invSync = resp.invSync;
    let inventory = resp.inventory;

    inventory = inventory.map(inv => {
      const productId = getProductId(inv.categoryId, inv.subCategoryId, inv.productId);
      const binId = getBinId(inv.categoryId, inv.subCategoryId, inv.productId, inv.binNo);
      if (inv.binState === bs.STORE) {
        let value = stockMap.get(productId);
        if (value == undefined) {
          stockMap.set(productId,String(inv.binNo));
        }else{
          value = value + "," + String(inv.binNo);
          stockMap.set(productId,value);
        }
      } else if (inv.binState === bs.PURCHASE) {
        let value = pendingMap.get(productId);
        if (value == undefined) {
          value = [{binNo: inv.binNo, createdAt: inv.lastUpdated}];
        }else{
          value.push({binNo: inv.binNo, createdAt: inv.lastUpdated});
        }
        value.sort((inv1,inv2) => inv1.createdAt - inv2.createdAt);
        pendingMap.set(productId,value);
      }
      return {...inv, prodId: productId, binId};
    });
    return ({inventory, pendingMap, stockMap, toggleStatus: !_.toggleStatus, syncing: false});
  },
  /*Called after generating order to check whether pending inv exist for the product */
  [c.INVENTORY_REFRESH]: (_, action) => {
    const order = action.payload.order;
    let pendingMap = _.pendingMap;
    let value = pendingMap.get(order.prodId);
    value.bins = value.bins.substring(order.bins.length+1,value.bins.length);
    if (value.bins.length == 0) {
      pendingMap.delete(order.prodId);
    }else {
      pendingMap.set(order.prodId,value);
    }
    return ({pendingMap,toggleStatus: !_.toggleStatus});
  },
  /*Inventory update required for outward scan, as no order is generated here */
  [c.INVENTORY_EDIT_PROGRESS]: (_, action) => ({message: ''}),
  [c.INVENTORY_EDIT_SUCCESS]: (_, action) => {
    let inventory = _.inventory;
    let stockMap = _.stockMap;
    let pendingMap = _.pendingMap;
    //updating inventory
    let i = inventory.findIndex(inv => inv.id == action.payload.inventory.id);
    let inv = inventory[i];
    inv.binState = action.payload.inventory.binState;
    inv.lastUpdated = action.payload.inventory.lastUpdated;
    inventory[i] = inv;
    //updating stockMap and pendingMap 
    const productId = inv.prodId;
    stockMap.delete(productId);
    pendingMap.delete(productId);
    const binsOfProduct = inventory.filter(b => b.prodId == productId);
    binsOfProduct.forEach(b => {
      if (b.binState === bs.STORE) {
        let value = stockMap.get(productId);
        if (value == undefined) {
          stockMap.set(productId,String(b.binNo));
        }else{
          value = value + "," + String(b.binNo);
          stockMap.set(productId,value);
        }
      } else if (b.binState === bs.PURCHASE) {
        let value = pendingMap.get(productId);
        if (value == undefined) {
          value = [{binNo: inv.binNo, createdAt: inv.lastUpdated}];
        }else{
          value.push({binNo: inv.binNo, createdAt: inv.lastUpdated});
        }
        value.sort((inv1,inv2) => inv1.createdAt - inv2.createdAt);
        pendingMap.set(productId,value);
      }
    });
    alert('Bin Outward Scan done successfully.');
    return ({toggleStatus: !_.toggleStatus, inventory,stockMap,pendingMap});
  },
  [c.INVENTORY_EDIT_FAIL]: (_, action) => ({message: 'Some error occured while scanning Bin.'}),
  [c.INVENTORY_SYNC_PROGRESS]: (_, action) => ({syncing: true}),
  //TODO: inventory sync succes
  [c.INVENTORY_SYNC_FAIL]: (_, action) => ({syncing: false}),
  //////////////////////// Order  /////////////////////////////////
  [o.INITIALIZE_ORDER]: (_, action) => {
    const resp = action.payload.orders;
    sessionStorage.orderSync = resp.orderSync;
    return ({orders: resp.orders, toggleStatus: !_.toggleStatus});
  },
  [o.ORDER_BUSY]: (_, action) => ({busy: action.payload.busy}),
  [o.ORDER_SYNC_PROGRESS]: (_, action) => ({syncing: true}),
  [o.ORDER_SYNC_SUCCESS]: (_, action) => {
    let orders = _.orders;
    const resp = action.payload.order;
    sessionStorage.orderSync = resp.orderSync;
    let idx;
    resp.orders.forEach(o => {
      idx = orders.findIndex(odr => odr.id == o.id);
      if (idx == -1)  orders.push(o);
      else orders[idx] = o;
    });
    return ({orders, toggleStatus: !_.toggleStatus, syncing: false});
  },
  [o.ORDER_SYNC_FAIL]: (_, action) => ({syncing: false}),
  [o.ORDER_ADD_FORM_TOGGLE]: (_, action) => ({ordering: action.payload.ordering, busy: false}),
  [o.ORDER_ADD_PROGRESS]: (_, action) => ({busy: true}),
  [o.ORDER_ADD_SUCCESS]: (_, action) => {
    let orders = _.orders;
    let inventory = _.inventory;
    const order = action.payload.order;
    //update order
    orders.push(order);
    //update inventory
    let bins = order.bins.split(',');
    console.log(bins);
    let invs = inventory.filter(inv => inv.productId == order.productId && bins.includes(String(inv.binNo)));
    console.log(invs);
    invs.forEach(inv => {
      let iv = inventory.find(i => i.id == inv.id);
      if (iv != undefined)
        iv.binState = bs.ORDERED;
    });
    return ({ordering: false, busy: false, toggleStatus: !_.toggleStatus, orders, inventory});
  },
  [o.ORDER_ADD_FAIL]: (_, action) => ({ordering: false, busy: false}),
  /*Called while Inward scan, order update will be called for one bin only. update inventory as well */
  [o.ORDER_EDIT_SUCCESS]: (_, action) => {
    let orders = _.orders;
    let inventory = _.inventory;
    //Update order
    const order = action.payload.order;
    const bin = action.payload.bin;
    const i = orders.findIndex(o => o.id == order.id);
    if (i != -1) {
      orders[i] = order;
    }
    //update inventory
    let inv = inventory.find(inv => inv.productId == order.productId && inv.binNo == parseInt(bin));
    if (inv != undefined) {
      inv.binState = bs.STORE;
    }
    alert('Bin Inward Scan done successfully.');
    return ({busy: false, toggleStatus: !_.toggleStatus, order, inventory});
  },
  [o.ORDER_EDIT_FAIL]: (_, action) => ({busy: false}),
  [o.ORDER_FOLLOWUP_SUCCESS]: (_, action) => {
    let orders = _.orders;
    const odrs = action.payload.orders;
    let odr;
    odrs.forEach((o,i) => {
      odr = orders.find(order => order.id == o.id);
      odr.followedUp = o.followedUp;
    });
    return ({busy: false, toggleStatus: !_.toggleStatus, orders: orders});
  },
  [o.ORDER_FOLLOWUP_FAIL]: (_, action) => ({busy: false})
};

export default function inventory (state = initialState, action) {
  let handler = handlers[action.type];
  if( !handler ) return state;
  return { ...state, ...handler(state, action) };
}
