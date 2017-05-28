import {ORDER_CONSTANTS as c} from "../utils/constants";

const initialState = {
  busy: false,
  syncing: false,
  ordering: false,
  orders:[],
  toggleStatus: true
};

const handlers = {
  [c.INITIALIZE_ORDER]: (_, action) => {
    const resp = action.payload.orders;
    sessionStorage.orderSync = resp.orderSync;
    return ({orders: resp.orders, toggleStatus: !_.toggleStatus});
  },
  [c.ORDER_BUSY]: (_, action) => ({busy: action.payload.busy}),
  [c.ORDER_SYNC_PROGRESS]: (_, action) => ({syncing: true}),
  [c.ORDER_SYNC_SUCCESS]: (_, action) => {
    let orders = _.orders;
    const resp = action.payload.order;
    sessionStorage.orderSync = resp.orderSync;
    resp.orders.forEach(o => {
      orders.push(o);
    });
    return ({orders, toggleStatus: !_.toggleStatus, syncing: false});
  },
  [c.ORDER_SYNC_FAIL]: (_, action) => ({syncing: false}),
  [c.ORDER_ADD_FORM_TOGGLE]: (_, action) => ({ordering: action.payload.ordering, busy: false}),
  [c.ORDER_ADD_PROGRESS]: (_, action) => ({busy: true}),
  [c.ORDER_ADD_SUCCESS]: (_, action) => {
    let orders = _.orders;
    orders.push(action.payload.order);
    return ({ordering: false, busy: false, toggleStatus: !_.toggleStatus, orders: orders});
  },
  [c.ORDER_ADD_FAIL]: (_, action) => ({ordering: false, busy: false}),
  [c.ORDER_EDIT_SUCCESS]: (_, action) => {
    let orders = _.orders;
    const order = action.payload.order;
    const i = orders.findIndex(o => o.id == order.id);
    if (i != -1) {
      orders[i] = order;
    }
    alert('Bin Inward Scan done successfully.');
    return ({busy: false, toggleStatus: !_.toggleStatus, orders: orders});
  },
  [c.ORDER_EDIT_FAIL]: (_, action) => ({busy: false}),
  [c.ORDER_FOLLOWUP_SUCCESS]: (_, action) => {
    let orders = _.orders;
    const odrs = action.payload.orders;
    let odr;
    odrs.forEach((o,i) => {
      odr = orders.find(order => order.id == o.id);
      odr.followedUp = o.followedUp;
    });
    return ({busy: false, toggleStatus: !_.toggleStatus, orders: orders});
  },
  [c.ORDER_FOLLOWUP_FAIL]: (_, action) => ({busy: false})
};

export default function order (state = initialState, action) {
  let handler = handlers[action.type];
  if( !handler ) return state;
  return { ...state, ...handler(state, action) };
}
