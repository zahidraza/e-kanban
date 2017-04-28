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
  console.log('authenticate');

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

}
 
export function getUsers () {
  console.log("getUsers()");
  this.setState({fetching: true});

  axios.get(window.serviceHost + '/users', {headers: getHeaders()})
  .then((response) => {
    if (response.status == 200 && response.data._embedded) {
      this.setState({users: response.data._embedded.userDtoList});
    }
    this.setState({fetching: false});
  }).catch( (err) => {
    console.log(err); 
    this.setState({fetching: false});
  });
}

export function addUser (user) {
  console.log('addUser');
  return function (dispatch) {
    axios.post(window.serviceHost + '/users', JSON.stringify(user), {headers: getHeaders()})
    .then((response) => {
      console.log(response);
      if (response.status == 201) {
        dispatch({type: u.USER_ADD_SUCCESS, payload: {user: response.data}});
      }
    }).catch( (err) => {
      console.log(err);
      dispatch({type: u.USER_ADD_FAIL});
    });
  };
  
}

export function updateUser (user) {
  console.log('updateUser');
  return function (dispatch) {
    axios.put(window.serviceHost + '/users/' + user.id, JSON.stringify(user), {headers: getHeaders()})
    .then((response) => {
      console.log(response);
      if (response.status == 200) {
        dispatch({type: u.USER_EDIT_SUCCESS, payload: {user: response.data}});
      }
    }).catch( (err) => {
      console.log(err);
      dispatch({type: u.USER_EDIT_FAIL});
    });
  };
}

export function removeUser (user) {
  console.log('removeUser');
  return function (dispatch) {
    axios.delete(window.serviceHost + '/users/' + user.id, {headers: getHeaders()})
    .then((response) => {
      if (response.status == 204 || response.status == 200) {
        dispatch({type: u.USER_REMOVE_SUCCESS, payload: {user}});
      }
    }).catch( (err) => {
      console.log(err);
      dispatch({type: u.USER_REMOVE_FAIL});
    });
  };
}

export function test (msg) {
  console.log('test()');
  //console.log('is Mounted: '+this.isMounted());
  console.log(msg);
  console.log(this.state.msg);
  this.setState({msg:msg});
}
