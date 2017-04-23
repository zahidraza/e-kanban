import {INVENTORY_CONSTANTS as c} from "../utils/constants";
import {getBinId} from "../utils/miscUtil";

const initialState = {
  inventory:[],
  toggleStatus: true,
  message: ''
};

const handlers = { 
  [c.INITIALIZE_INVENTORY]: (_, action) => {
    let inventory = action.payload.inventory;

    inventory = inventory.map(inv => {
      return {...inv, binId: getBinId(inv.categoryId, inv.subCategoryId, inv.productId, inv.binNo)};
    });

    return ({inventory, toggleStatus: !_.toggleStatus});
  },
  [c.INVENTORY_EDIT_PROGRESS]: (_, action) => ({message: ''}),
  [c.INVENTORY_EDIT_SUCCESS]: (_, action) => {
    let inventory = _.inventory;
    let i = inventory.findIndex(inv => inv.id == action.payload.inventory.id);
    let inv = inventory[i];
    inv.binState = action.payload.inventory.binState;
    inventory[i] = inv;
    const message = "Bin Scanned Successfully";
    return ({toggleStatus: !_.toggleStatus, inventory, message});
  },
  [c.INVENTORY_EDIT_FAIL]: (_, action) => ({message: 'Some error occured while scanning Bin.'})
};

export default function inventory (state = initialState, action) {
  let handler = handlers[action.type];
  if( !handler ) return state;
  return { ...state, ...handler(state, action) };
}
