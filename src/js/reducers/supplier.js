import {SUPPLIER_CONSTANTS as c} from "../utils/constants";

const initialState = {
  busy: false,
  adding: false,
  editing: false,
  suppliers:[],
  supplier: {},
  errors: {},
  filter: {},
  sort: 'name:asc',
  toggleStatus: true
};

const handlers = { 
  [c.INITIALIZE_SUPPLIER]: (_, action) => ({suppliers: action.payload.suppliers, loaded: true, toggleStatus: !_.toggleStatus}),
  [c.SUPPLIER_BAD_REQUEST]: (_, action) => {
    let errors = {};
    action.payload.errors.forEach(err => {
      errors[err.field] = err.message;
    });
    return ({errors, busy: false});
  },
  [c.SUPPLIER_ADD_FORM_TOGGLE]: (_, action) => ({adding: action.payload.adding, busy: false, errors: {}}),
  [c.SUPPLIER_ADD_PROGRESS]: (_, action) => ({busy: true, errors: {}}),
  [c.SUPPLIER_ADD_SUCCESS]: (_, action) => {
    let suppliers = _.suppliers;
    suppliers.push(action.payload.supplier);
    return ({adding: false, busy: false,toggleStatus: !_.toggleStatus, suppliers: suppliers});
  },
  [c.SUPPLIER_ADD_FAIL]: (_, action) => ({adding: false, busy: false}),
  [c.SUPPLIER_EDIT_FORM_TOGGLE]: (_, action) => ({editing: action.payload.editing, supplier: action.payload.supplier, busy: false, errors: {}}),
  [c.SUPPLIER_EDIT_PROGRESS]: (_, action) => ({busy: true, errors: {}}),
  [c.SUPPLIER_EDIT_SUCCESS]: (_, action) => {
    let suppliers = _.suppliers;
    let i = suppliers.findIndex(e=> e.id == action.payload.supplier.id);
    suppliers[i] = action.payload.supplier;
    return ({editing: false, busy: false, toggleStatus: !_.toggleStatus, suppliers: suppliers});
  },
  [c.SUPPLIER_EDIT_FAIL]: (_, action) => ({editing: false, busy: false}),
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
