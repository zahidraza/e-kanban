import {SUPPLIER_CONSTANTS as c} from "../utils/constants";

const initialState = {
  loaded: false,
  fetching: false,
  adding: false,
  editing: false,
  suppliers:[],
  supplier: {},
  filter: {},
  sort: 'name:asc',
  toggleStatus: true
};

const handlers = { 
  [c.INITIALIZE_SUPPLIER]: (_, action) => ({suppliers: action.payload.suppliers, loaded: true, toggleStatus: !_.toggleStatus}),
  // [c.SUPPLIER_FETCH_PROGRESS]: (_, action) => ({fetching: true}),
  // [c.SUPPLIER_FETCH_SUCCESS]: (_, action) => ({loaded: true, fetching: false,toggleStatus: !_.toggleStatus, suppliers: action.payload.suppliers}),
  // [c.SUPPLIER_FETCH_FAIL]: (_, action) => ({fetching: false}),
  [c.SUPPLIER_ADD_FORM_TOGGLE]: (_, action) => ({adding: action.payload.adding}),
  [c.SUPPLIER_ADD_SUCCESS]: (_, action) => {
    let suppliers = _.suppliers;
    suppliers.push(action.payload.supplier);
    return ({adding: false,toggleStatus: !_.toggleStatus, suppliers: suppliers});
  },
  [c.SUPPLIER_ADD_FAIL]: (_, action) => ({adding: false}),
  [c.SUPPLIER_EDIT_FORM_TOGGLE]: (_, action) => ({editing: action.payload.editing, supplier: action.payload.supplier}),
  [c.SUPPLIER_EDIT_SUCCESS]: (_, action) => {
    let suppliers = _.suppliers;
    let i = suppliers.findIndex(e=> e.id == action.payload.supplier.id);
    suppliers[i] = action.payload.supplier;
    return ({editing: false,toggleStatus: !_.toggleStatus, suppliers: suppliers});
  },
  [c.SUPPLIER_EDIT_FAIL]: (_, action) => ({editing: false}),
  [c.SUPPLIER_REMOVE_SUCCESS]: (_, action) => {
    let suppliers = _.suppliers.filter((c)=> c.id != action.payload.supplier.id);
    return ({toggleStatus: !_.toggleStatus,suppliers: suppliers});
  },
  [c.SUPPLIER_FILTER]: (_, action) => ({filter: action.payload.filter, toggleStatus: !_.toggleStatus}),
  [c.SUPPLIER_SORT]: (_, action) => ({sort: action.payload.sort, toggleStatus: !_.toggleStatus})
};

export default function supplier (state = initialState, action) {
  let handler = handlers[action.type];
  if( !handler ) return state;
  return { ...state, ...handler(state, action) };
}
