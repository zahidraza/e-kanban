import axios from "axios";
import {getHeaders} from  '../utils/restUtil';
import {SUB_CATEGORY_CONSTANTS as c} from  '../utils/constants';

axios.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  if (error.response.status == 401) {
    delete sessionStorage.session;
  }
  return Promise.reject(error);
});

export function addSubCategory (url,subCategory) {
  return function (dispatch) {
    dispatch({type: c.SUB_CATEGORY_ADD_PROGRESS});
    axios.post(url, JSON.stringify(subCategory), {headers: getHeaders()})
    .then((response) => {

      if (response.status == 201) {
        dispatch({type: c.SUB_CATEGORY_ADD_SUCCESS, payload: {subCategory: response.data}});
      }
    }).catch( (err) => {
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      if (err.response.status == 400) {
        dispatch({type: c.SUB_CATEGORY_BAD_REQUEST, payload: {errors: err.response.data}});
      }else {
        dispatch({type: c.SUB_CATEGORY_ADD_FAIL});
      }
    });
  };
}

export function updateSubCategory (url,subCategory) {
  return function (dispatch) {
    dispatch({type: c.SUB_CATEGORY_EDIT_PROGRESS});
    axios.put(url, JSON.stringify(subCategory),{headers: getHeaders()})
    .then((response) => {
      if (response.status == 200) {
        dispatch({type: c.SUB_CATEGORY_EDIT_SUCCESS, payload: {subCategory: response.data}});
      }
    }).catch( (err) => {
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      if (err.response.status == 400) {
        dispatch({type: c.SUB_CATEGORY_BAD_REQUEST, payload: {errors: err.response.data}});
      }else {
        dispatch({type: c.SUB_CATEGORY_EDIT_FAIL});
      }
    });
  };
}

export function removeSubCategory (subCategory) {

  return function (dispatch) {
    axios.delete(subCategory._links.self.href, {headers: getHeaders()})
    .then((response) => {
      dispatch({type: c.SUB_CATEGORY_REMOVE_SUCCESS, payload: {subCategory: subCategory}});
    }).catch( (err) => {
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      dispatch({type: c.SUB_CATEGORY_REMOVE_FAIL});
    });
  };
}
