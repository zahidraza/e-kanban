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

export function updateInventory (inventory) {
  console.log('updateInventory');
  return function (dispatch) {
    dispatch({type: c.INVENTORY_EDIT_PROGRESS});
    console.log(inventory);
    axios.patch(inventory.url, JSON.stringify(inventory),{headers: getHeaders()})
    .then((response) => {
      console.log(response);
      if (response.status == 200) {
        dispatch({type: c.INVENTORY_EDIT_SUCCESS, payload: {inventory: response.data}});
      }
    }).catch( (err) => {
      console.log(err);
      dispatch({type: c.INVENTORY_EDIT_FAIL});
    });
  };
}

