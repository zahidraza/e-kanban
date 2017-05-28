import axios from "axios";
import {getHeaders} from  '../utils/restUtil';
import { USER_CONSTANTS as u } from '../utils/constants';


axios.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  if (error.response.status == 401) {
    delete sessionStorage.session;
  }
  return Promise.reject(error);
});

export function authenticate (username, password) {

  return function (dispatch) {
    dispatch({type: u.USER_AUTH_PROGRESS});
    const config = {
      method: 'post',
      url: window.baseUrl + "/oauth/token",
      headers: {'Authorization': 'Basic ' + btoa('client:secret')},
      params: {
        grant_type: 'password',
        username: username,
        password: password
      }
    };

    axios(config)
    .then((response) => {
      if (response.status == 200) {
        dispatch({type: u.USER_AUTH_SUCCESS, payload: {username, data: response.data}});
      }else{
        console.log(response);
      }
    }).catch( (err) => {
      console.log(err);
      dispatch({type: u.USER_AUTH_FAIL});
    });
  };
  
}

export function changePassword (credential) {
  return function (dispatch) {
    dispatch({type: u.USER_BUSY, payload: {busy: true}});
    axios.put(window.serviceHost + '/misc/change_password?email=' +  credential.email + "&oldPassword=" + credential.oldPassword + "&newPassword=" + credential.newPassword, null, {headers: getHeaders()})
    .then((response) => {
      if (response.status == 200) {
        if (response.data.status == "SUCCESS") {
          //dispatch({type: u.USER_CHANGE_PASSWD, payload: {message: 'Password changed successfully.'}});
          alert('Password changed successfully.');
        }else{
          //dispatch({type: u.USER_CHANGE_PASSWD, payload: {message: 'Incorrect credential. try again!'}});
          alert('Incorrect credential. try again!');
        }
        dispatch({type: u.USER_BUSY, payload: {busy: false}});
      }
    }).catch( (err) => {
      if (err.response.status != 401) {
        //dispatch({type: u.USER_CHANGE_PASSWD, payload: {message: 'Some error occured changing password. Try again Later.'}});
        alert('Some error occured changing password. Try again Later.');
      }
      dispatch({type: u.USER_BUSY, payload: {busy: false}});
    });
  };
}

export function addUser (user) {
  return function (dispatch) {
    dispatch({type: u.USER_ADD_PROGRESS});
    axios.post(window.serviceHost + '/users', JSON.stringify(user), {headers: getHeaders()})
    .then((response) => {
      if (response.status == 201) {
        dispatch({type: u.USER_ADD_SUCCESS, payload: {user: response.data}});
      }
    }).catch( (err) => {
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      if (err.response.status == 400) {
        dispatch({type: u.USER_BAD_REQUEST, payload: {errors: err.response.data}});
      } else {
        dispatch({type: u.USER_ADD_FAIL});
      }
    });
  };
}

export function updateUser (user) {
  return function (dispatch) {
    dispatch({type: u.USER_EDIT_PROGRESS});
    axios.put(window.serviceHost + '/users/' + user.id, JSON.stringify(user), {headers: getHeaders()})
    .then((response) => {
      if (response.status == 200) {
        dispatch({type: u.USER_EDIT_SUCCESS, payload: {user: response.data}});
        alert('User details updated successfully.');
      }
    }).catch( (err) => { 
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      if (err.response.status == 400) {
        dispatch({type: u.USER_BAD_REQUEST, payload: {errors: err.response.data}});
      } else {
        dispatch({type: u.USER_EDIT_FAIL});
      }
    });
  };
}

export function removeUser (user) {
  return function (dispatch) {
    axios.delete(window.serviceHost + '/users/' + user.id, {headers: getHeaders()})
    .then((response) => {
      if (response.status == 204 || response.status == 200) {
        dispatch({type: u.USER_REMOVE_SUCCESS, payload: {user}});
      }
    }).catch( (err) => {
      if (err.response.status == 409) {
        alert(err.response.data.message);
      }
      dispatch({type: u.USER_REMOVE_FAIL});
    });
  };
}
