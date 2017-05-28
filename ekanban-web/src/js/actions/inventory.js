import axios from "axios";
import {getHeaders} from  '../utils/restUtil';
import {INVENTORY_CONSTANTS as c} from  '../utils/constants';

axios.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  if (error.response.status == 401) {
    delete sessionStorage.session;
  }
  return Promise.reject(error);
});

export function syncInventory() {
  return function (dispatch) {
    dispatch({type: c.INVENTORY_SYNC_PROGRESS});
    axios.get(window.serviceHost + '/inventory')
    .then((response) => {
      if (response.status == 200) {
        dispatch({type: c.INITIALIZE_INVENTORY, payload: { inventory: response.data}});
      }
    }).catch( (err) => {
      console.log(err);
      dispatch({type: c.INVENTORY_SYNC_FAIL});
    });
  };
}

export function updateInventory (inventory) {
  return function (dispatch) {
    dispatch({type: c.INVENTORY_EDIT_PROGRESS});
    axios.patch(inventory.url, JSON.stringify(inventory),{headers: getHeaders()})
    .then((response) => {
      if (response.status == 200) {
        dispatch({type: c.INVENTORY_EDIT_SUCCESS, payload: {inventory: response.data}});
      }
    }).catch( (err) => {
      console.log(err);
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      dispatch({type: c.INVENTORY_EDIT_FAIL});
    });
  };
}
