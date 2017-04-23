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

export function getSuppliers () {
  console.log("getSuppliers()");

  return function (dispatch) {
    dispatch({type:c.SUPPLIER_FETCH_PROGRESS});

    axios.get(window.serviceHost + '/suppliers')
    .then((response) => {
      if (response.status == 200 && response.data._embedded) {
        dispatch({type: c.SUPPLIER_FETCH_SUCCESS, payload: {suppliers: response.data._embedded.supplierList}});
      }
    }).catch( (err) => {
      console.log(err); 
      dispatch({type: c.SUPPLIER_FETCH_FAIL});
    });
  };
}

export function addSupplier (supplier) {
  console.log('addSupplier');

  return function (dispatch) {
    console.log(supplier);
    axios.post(window.serviceHost + '/suppliers', JSON.stringify(supplier), {headers: getHeaders()})
    .then((response) => {
      console.log(response);
      if (response.status == 201) {
        dispatch({type: c.SUPPLIER_ADD_SUCCESS, payload: {supplier: response.data}});
      }
    }).catch( (err) => {
      console.log(err);
      dispatch({type: c.SUPPLIER_ADD_FAIL});
    });
  };
}

export function updateSupplier (supplier) {
  console.log('updateSupplier');
  return function (dispatch) {
    console.log(supplier);
    axios.put(supplier._links.self.href, JSON.stringify(supplier),{headers: getHeaders()})
    .then((response) => {
      console.log(response);
      if (response.status == 200) {
        dispatch({type: c.SUPPLIER_EDIT_SUCCESS, payload: {supplier: response.data}});
      }
    }).catch( (err) => {
      console.log(err);
      dispatch({type: c.SUPPLIER_EDIT_FAIL});
    });
  };
}

export function removeSupplier (supplier) {
  console.log('removeSupplier');
  return function (dispatch) {
    console.log(supplier);

    axios.delete(supplier._links.self.href, {headers: getHeaders()})
    .then((response) => {
      console.log(response);
      dispatch({type: c.SUPPLIER_REMOVE_SUCCESS, payload: {supplier: supplier}});
    }).catch( (err) => {
      console.log(err);
      dispatch({type: c.SUPPLIER_REMOVE_FAIL});
    });
  };
}

