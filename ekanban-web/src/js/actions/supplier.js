import axios from "axios";
import {getHeaders} from  '../utils/restUtil';
import {SUPPLIER_CONSTANTS as c} from  '../utils/constants';

axios.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  if (error.response.status == 401) {
    delete sessionStorage.session;
  }
  return Promise.reject(error);
});

export function addSupplier (supplier) {
  return function (dispatch) {
    dispatch({type: c.SUPPLIER_ADD_PROGRESS});
    axios.post(window.serviceHost + '/suppliers', JSON.stringify(supplier), {headers: getHeaders()})
    .then((response) => {
      if (response.status == 201) {
        dispatch({type: c.SUPPLIER_ADD_SUCCESS, payload: {supplier: response.data}});
      }
    }).catch( (err) => {
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      if (err.response.status == 400) {
        dispatch({type: c.SUPPLIER_BAD_REQUEST, payload: {errors: err.response.data}});
      }else {
        dispatch({type: c.SUPPLIER_ADD_FAIL});
      }
    });
  };
}

export function updateSupplier (supplier) {
  return function (dispatch) {
    dispatch({type: c.SUPPLIER_EDIT_PROGRESS});
    axios.put(window.serviceHost + '/suppliers/' + supplier.id, JSON.stringify(supplier),{headers: getHeaders()})
    .then((response) => {
      if (response.status == 200) {
        dispatch({type: c.SUPPLIER_EDIT_SUCCESS, payload: {supplier: response.data}});
      }
    }).catch( (err) => {
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      if (err.response.status == 400) {
        dispatch({type: c.SUPPLIER_BAD_REQUEST, payload: {errors: err.response.data}});
      }else {
        dispatch({type: c.SUPPLIER_EDIT_FAIL});
      }
    });
  };
}

export function removeSupplier (supplier) {
  return function (dispatch) {
    axios.delete(window.serviceHost + '/suppliers/' + supplier.id, {headers: getHeaders()})
    .then((response) => {
      dispatch({type: c.SUPPLIER_REMOVE_SUCCESS, payload: {supplier: supplier}});
    }).catch( (err) => {
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      dispatch({type: c.SUPPLIER_REMOVE_FAIL});
    });
  };
}
