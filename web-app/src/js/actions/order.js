import axios from "axios";
import {getHeaders} from  '../utils/restUtil';
import {ORDER_CONSTANTS as c, INVENTORY_CONSTANTS as ic} from  '../utils/constants';

axios.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  if (error.response.status == 401) {
    delete sessionStorage.session;
  }
  return Promise.reject(error);
});

export function syncOrder() {
  return function (dispatch) {
    dispatch({type: c.ORDER_SYNC_PROGRESS});
    axios.get(window.serviceHost + '/orders?after=' + sessionStorage.orderSync)
    .then((response) => {
      if (response.status == 200) {
        dispatch({type: c.ORDER_SYNC_SUCCESS, payload: { order: response.data}});
      }
    }).catch( (err) => {
      console.log(err);
      dispatch({type: c.ORDER_SYNC_FAIL});
    });
  };
}

export function generateOrder (order) {
  console.log('generateOrder');
  return function (dispatch) {
    dispatch({type: c.ORDER_ADD_PROGRESS});
    console.log(order);
    axios.post(window.serviceHost + '/orders', JSON.stringify(order),{headers: getHeaders()})
    .then((response) => {
      console.log(response);
      if (response.status == 201) {
        dispatch({type: c.ORDER_ADD_SUCCESS, payload: {order: response.data}});
        dispatch({type: ic.INVENTORY_REFRESH, payload: {order}});
      }
    }).catch( (err) => {
      console.log(err);
      dispatch({type: c.ORDER_ADD_FAIL});
    });
  };
}

export function updateOrder (order) {
  console.log('updateOrder');
  return function (dispatch) {
    dispatch({type: c.ORDER_BUSY, payload: {busy: true}});
    console.log(order);
    axios.put(window.serviceHost + '/orders/' + order.orderId, JSON.stringify(order),{headers: getHeaders()})
    .then((response) => {
      console.log(response);
      if (response.status == 200) {
        dispatch({type: c.ORDER_EDIT_SUCCESS, payload: {order: response.data}});
      }
    }).catch( (err) => {
      console.log(err);
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      dispatch({type: c.ORDER_EDIT_FAIL});
    });
  };
}
