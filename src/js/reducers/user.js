import {USER_CONSTANTS as c} from "../utils/constants";

const initialState = {
  busy: false,
  adding: false,
  editing: false,
  users:[],
  errors: [],
  toggleStatus: true
};

const handlers = { 
  [c.INITIALIZE_USER]: (_, action) => ({users: action.payload.users, loaded: true, toggleStatus: !_.toggleStatus}),
  [c.USER_AUTH_NOT_PROGRESS]: (_, action) => ({busy: false}),
  [c.USER_AUTH_PROGRESS]: (_, action) => ({busy: true}),
  [c.USER_AUTH_SUCCESS]: (_, action) => {
    let users = _.users;
    if (users.length == 0) {
      console.log('users not loaded');
      return ({});
    }
    let i = users.findIndex(u => u.email == action.payload.username);
    const user = users[i];
    window.sessionStorage.username = user.name;
    window.sessionStorage.email = user.email;
    window.sessionStorage.access_token = action.payload.data.access_token;
    window.sessionStorage.refresh_token = action.payload.data.refresh_token;
    window.sessionStorage.role = user.role;
    window.sessionStorage.session = true;
    return ({busy: false});
  },
  [c.USER_AUTH_FAIL]: (_, action) => ({busy: false}),
  [c.USER_BAD_REQUEST]: (_, action) => {
    let errors = {};
    action.payload.errors.forEach(err => {
      errors[err.field] = err.message;
    });
    return ({errors, busy: false});
  },
  [c.USER_ADD_FORM_TOGGLE]: (_, action) => ({adding: action.payload.adding, busy: false, errors: {}}),
  [c.USER_ADD_PROGRESS]: (_, action) => ({busy: true, errors: {}}),
  [c.USER_ADD_SUCCESS]: (_, action) => {
    let users = _.users;
    users.push(action.payload.user);
    return ({adding: false, busy: false, toggleStatus: !_.toggleStatus, users: users});
  },
  [c.USER_ADD_FAIL]: (_, action) => ({adding: false, busy: false}),
  [c.USER_EDIT_FORM_TOGGLE]: (_, action) => ({editing: action.payload.editing, busy: false, errors: {}}),
  [c.USER_EDIT_PROGRESS]: (_, action) => ({busy: true, errors: {}}),
  [c.USER_EDIT_SUCCESS]: (_, action) => {
    let users = _.users;
    let i = users.findIndex(s => s.id == action.payload.user.id);
    users[i] = action.payload.user;
    return ({editing: false, busy: false,toggleStatus: !_.toggleStatus, users: users});
  },
  [c.USER_EDIT_FAIL]: (_, action) => ({editing: false, busy: false}),
  [c.USER_REMOVE_SUCCESS]: (_, action) => {
    let users = _.users.filter(s => s.id != action.payload.user.id);
    return ({toggleStatus: !_.toggleStatus,users: users});
  }
};

export default function user (state = initialState, action) {
  let handler = handlers[action.type];
  if( !handler ) return state;
  return { ...state, ...handler(state, action) };
}
