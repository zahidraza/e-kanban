import axios from "axios";
import {getHeaders} from  '../utils/restUtil';
import {CATEGORY_CONSTANTS as c} from  '../utils/constants';

axios.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  if (error.response.status == 401) {
    delete sessionStorage.session;
  }
  return Promise.reject(error);
});

export function addCategory (category) {
  return function (dispatch) {
    dispatch({type: c.CATEGORY_ADD_PROGRESS});
    axios.post(window.serviceHost + '/categories', JSON.stringify(category), {headers: getHeaders()})
    .then((response) => {
      if (response.status == 201) {
        dispatch({type: c.CATEGORY_ADD_SUCCESS, payload: {category: response.data}});
      }
    }).catch( (err) => {
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      if (err.response.status == 400) {
        dispatch({type: c.CATEGORY_BAD_REQUEST, payload: {errors: err.response.data}});
      } else {
        dispatch({type: c.CATEGORY_ADD_FAIL});
      }
    });
  };
}

export function updateCategory (category) {
  return function (dispatch) {
    dispatch({type: c.CATEGORY_EDIT_PROGRESS});
    axios.put(category._links.self.href, JSON.stringify(category),{headers: getHeaders()})
    .then((response) => {
      if (response.status == 200) {
        dispatch({type: c.CATEGORY_EDIT_SUCCESS, payload: {category: response.data}});
      }
    }).catch( (err) => {
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      if (err.response.status == 400) {
        dispatch({type: c.CATEGORY_BAD_REQUEST, payload: {errors: err.response.data}});
      } else {
        dispatch({type: c.CATEGORY_EDIT_FAIL});
      }
    });
  };
}

export function removeCategory (category) {
  return function (dispatch) {
    axios.delete(category._links.self.href, {headers: getHeaders()})
    .then((response) => {
      dispatch({type: c.CATEGORY_REMOVE_SUCCESS, payload: {category: category}});
    }).catch( (err) => {
      if (err.response.status == 409) {
        alert(err.response.data.message);
      } 
      dispatch({type: c.CATEGORY_REMOVE_FAIL});
    });
  };
}

