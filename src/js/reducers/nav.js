import { NAV_ACTIVATE } from "../utils/constants";

const initialState = {
  active: false,
  itemsAdmin:[
    { path: '/dashboard', label: 'Dashboard'},
    { path: '/user', label: 'User'},
    { path: '/category', label: 'Category'},
    { path: '/subCategory', label: 'Sub Category'},
    { path: '/section', label: 'Section'},
    { path: '/supplier', label: 'Supplier'},
    { path: '/product', label: 'Product'},
    { path: '/test', label: 'Test Page'}
  ],
  itemsStore:[
     { path: '/dashboard', label: 'Dashboard'},
     { path: '/stock', label: 'Stock'},
     { path: '/inward', label: 'Inward Scan'},
     { path: '/outward', label: 'Outward Scan'},
     { path: '/barcode', label: 'Generate Barcode'},
     { path: '/awaiting', label: 'Awaiting Order'}
     
  ],
  itemsPurchase:[
     { path: '/dashboard', label: 'Dashboard'},
     { path: '/tracking', label: 'Tracking'},
     { path: '/order', label: 'Order'}
  ],
  itemsUser:[
     { path: '/dashboard', label: 'Dashboard'}
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
