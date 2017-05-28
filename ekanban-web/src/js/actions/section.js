import axios from "axios";
import {getHeaders} from  '../utils/restUtil';
import {SECTION_CONSTANTS as c} from  '../utils/constants';

axios.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  if (error.response.status == 401) {
    delete sessionStorage.session;
  }
  return Promise.reject(error);
});

export function addSection (section) {
  return function (dispatch) {
    dispatch({type: c.SECTION_ADD_PROGRESS});
    axios.post(window.serviceHost + '/sections', JSON.stringify(section), {headers: getHeaders()})
    .then((response) => {
      if (response.status == 201) {
        dispatch({type: c.SECTION_ADD_SUCCESS, payload: {section: response.data}});
      }
    }).catch( (err) => {
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      if (err.response.status == 400) {
        dispatch({type: c.SECTION_BAD_REQUEST, payload: {errors: err.response.data}});
      }else {
        dispatch({type: c.SECTION_ADD_FAIL});
      }
    });
  };
}

export function updateSection (section) {
  return function (dispatch) {
    dispatch({type: c.SECTION_EDIT_PROGRESS});
    axios.put(window.serviceHost + '/sections/' + section.id, JSON.stringify(section),{headers: getHeaders()})
    .then((response) => {
      if (response.status == 200) {
        dispatch({type: c.SECTION_EDIT_SUCCESS, payload: {section: response.data}});
      }
    }).catch( (err) => {
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      if (err.response.status == 400) {
        dispatch({type: c.SECTION_BAD_REQUEST, payload: {errors: err.response.data}});
      }else {
        dispatch({type: c.SECTION_EDIT_FAIL});
      }
    });
  };
}

export function removeSection (section) {
  return function (dispatch) {
    axios.delete(window.serviceHost + '/sections/' + section.id, {headers: getHeaders()})
    .then((response) => {
      dispatch({type: c.SECTION_REMOVE_SUCCESS, payload: {section: section}});
    }).catch( (err) => {
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      dispatch({type: c.SECTION_REMOVE_FAIL});
    });
  };
}
