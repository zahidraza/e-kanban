import {MISC_CONSTANTS as c} from "../utils/constants";

const initialState = {
  initialized: false
};

const handlers = { 
  [c.STORE_INITIALIZED]: (_, action) => ({initialized: action.payload.initialized})
};

export default function section (state = initialState, action) {
  let handler = handlers[action.type];
  if( !handler ) return state;
  return { ...state, ...handler(state, action) };
}
