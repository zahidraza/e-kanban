import { NAV_ACTIVATE } from "../utils/constants";

const initialState = {
  active: false,
  itemsAdmin:[
    { path: '/dashboard', label: 'Home'},
    { path: '/user', label: 'User'},
    { path: '/category', label: 'Category'},
    { path: '/subCategory', label: 'Sub Category'},
    { path: '/section', label: 'Section'},
    { path: '/supplier', label: 'Supplier'},
    { path: '/product', label: 'Product'},
    { path: '/test', label: 'Test Page'}
  ],
  itemsStore:[
     { path: '/dashboard', label: 'Home'},
     { path: '/inward', label: 'Inward Scan'},
     { path: '/outward', label: 'Outward Scan'},
     { path: '/barcode', label: 'Generate Barcode'},
     { path: '/test', label: 'Test Page'}
  ],
  itemsPurchase:[
     { path: '/dashboard', label: 'Home'},
     { path: '/tracking', label: 'Tracking'},
     { path: '/order', label: 'Order'},
     { path: '/test', label: 'Test Page'}
  ],
  itemsUser:[
     { path: '/dashboard', label: 'Home'}
  ]
};

const handlers = { 
  [NAV_ACTIVATE]: (_, action) => ({active: action.payload.active})

};

export default function section (state = initialState, action) {
  let handler = handlers[action.type];
  if( !handler ) return state;
  return { ...state, ...handler(state, action) };
}
