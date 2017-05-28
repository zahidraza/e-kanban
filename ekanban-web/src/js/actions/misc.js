import axios from "axios";

import {MISC_CONSTANTS as m, USER_CONSTANTS as u, CATEGORY_CONSTANTS as c,SECTION_CONSTANTS as sec, SUPPLIER_CONSTANTS as sup,
  INVENTORY_CONSTANTS as i,ORDER_CONSTANTS as o,NAV_ACTIVATE} from  '../utils/constants';

export function initialize () {
  
  return function (dispatch) {

    axios.all([
      axios.get(window.serviceHost + '/categories?expand=true'),
      axios.get(window.serviceHost + '/sections'),
      axios.get(window.serviceHost + '/suppliers'),
      axios.get(window.serviceHost + '/users'),
      axios.get(window.serviceHost + '/inventory'),
      axios.get(window.serviceHost + '/orders')
    ])
    .then(axios.spread(function (categories, sections,suppliers, users, inventory, orders) {
      if ('_embedded' in categories.data) {
        dispatch({type: c.INITIALIZE_CATEGORY, payload: { categories: categories.data._embedded.categoryList }});
      }
      dispatch({type: sec.INITIALIZE_SECTION, payload: {sections:  sections.data}});
      dispatch({type: sup.INITIALIZE_SUPPLIER, payload: { suppliers: suppliers.data }});
      dispatch({type: u.INITIALIZE_USER, payload: { users: users.data._embedded.userDtoList }});
      dispatch({type: i.INITIALIZE_INVENTORY, payload: { inventory: inventory.data}});
      dispatch({type: o.INITIALIZE_ORDER, payload: {orders: orders.data}});
      dispatch({type: m.STORE_INITIALIZED, payload: {initialized: true}});      
    }))
    .catch( (err) => {
      console.log(err); 
    });

  };
}

export function navActivate (active) {
  return { type: NAV_ACTIVATE, payload: {active}};
}
