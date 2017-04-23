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
  console.log('addSubCategory');

  return function (dispatch) {
    console.log(subCategory);
    axios.post(url, JSON.stringify(subCategory), {headers: getHeaders()})
    .then((response) => {
      console.log(response);
      if (response.status == 201) {
        dispatch({type: c.SUB_CATEGORY_ADD_SUCCESS, payload: {subCategory: response.data}});
      }
    }).catch( (err) => {
      console.log(err);
      dispatch({type: c.SUB_CATEGORY_ADD_FAIL});
    });
  };
}

export function updateSubCategory (url,subCategory) {
  console.log('updateSubCategory');
  return function (dispatch) {
    console.log(subCategory);
    axios.put(url, JSON.stringify(subCategory),{headers: getHeaders()})
    .then((response) => {
      console.log(response);
      if (response.status == 200) {
        dispatch({type: c.SUB_CATEGORY_EDIT_SUCCESS, payload: {subCategory: response.data}});
      }
    }).catch( (err) => {
      console.log(err);
      dispatch({type: c.SUB_CATEGORY_EDIT_FAIL});
    });
  };
}

export function removeSubCategory (subCategory) {
  console.log('removeSubCategory');
  return function (dispatch) {
    console.log(subCategory);

    axios.delete(subCategory._links.self.href, {headers: getHeaders()})
    .then((response) => {
      console.log(response);
      dispatch({type: c.SUB_CATEGORY_REMOVE_SUCCESS, payload: {subCategory: subCategory}});
    }).catch( (err) => {
      console.log(err);
      dispatch({type: c.SUB_CATEGORY_REMOVE_FAIL});
    });
  };
}

