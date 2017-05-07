import {INVENTORY_CONSTANTS as c, BIN_STATE as bs} from "../utils/constants";
import {getProductId} from "../utils/miscUtil";

const initialState = {
  syncing: false,
  inventory:[],
  pendingMap: new Map(),
  stockMap: new Map(),
  toggleStatus: true,
  message: ''
};

const handlers = { 
  [c.INITIALIZE_INVENTORY]: (_, action) => {
    const resp = action.payload.inventory;
    let stockMap = _.stockMap;
    let pendingMap = _.pendingMap;
    sessionStorage.invSync = resp.invSync;
    let inventory = resp.inventory;

    inventory = inventory.map(inv => {
      const productId = getProductId(inv.categoryId, inv.subCategoryId, inv.productId);
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
          pendingMap.set(productId,{bins: String(inv.binNo), createdAt: inv.lastUpdated});
        }else{
          let bins = value.bins + "," + String(inv.binNo);
          let createdAt = value.lastUpdated > inv.lastUpdated ? value.lastUpdated: inv.lastUpdated;
          pendingMap.set(productId,{bins, createdAt});
        }
      }
      return {...inv, prodId: productId};
    });
    return ({inventory, pendingMap, stockMap, toggleStatus: !_.toggleStatus, syncing: false});
  },
  [c.INVENTORY_REFRESH]: (_, action) => {
    const order = action.payload.order;
    let pendingMap = _.pendingMap;
    let value = pendingMap.get(order.prodId);
    value.bins = value.bins.substring(order.bins.length+1,value.bins.length);
    pendingMap.set(order.prodId,value);
    return ({pendingMap,toggleStatus: !_.toggleStatus});
  },
  [c.INVENTORY_EDIT_PROGRESS]: (_, action) => ({message: ''}),
  [c.INVENTORY_EDIT_SUCCESS]: (_, action) => {
    let inventory = _.inventory;
    let i = inventory.findIndex(inv => inv.id == action.payload.inventory.id);
    let inv = inventory[i];
    inv.binState = action.payload.inventory.binState;
    inv.lastUpdated = action.payload.inventory.lastUpdated;
    inventory[i] = inv;
    const message = "Bin Scanned Successfully";
    return ({toggleStatus: !_.toggleStatus, inventory, message});
  },
  [c.INVENTORY_EDIT_FAIL]: (_, action) => ({message: 'Some error occured while scanning Bin.'}),
  [c.INVENTORY_SYNC_PROGRESS]: (_, action) => ({syncing: true}),
  [c.INVENTORY_SYNC_FAIL]: (_, action) => ({syncing: false})
};

export default function inventory (state = initialState, action) {
  let handler = handlers[action.type];
  if( !handler ) return state;
  return { ...state, ...handler(state, action) };
}
