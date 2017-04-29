import {SECTION_CONSTANTS as c} from "../utils/constants";

const initialState = {
  busy: false,
  adding: false,
  editing: false,
  sections:[],
  errors: {},
  toggleStatus: true
};

const handlers = { 
  [c.INITIALIZE_SECTION]: (_, action) => ({sections: action.payload.sections, loaded: true, toggleStatus: !_.toggleStatus}),
  [c.SECTION_BAD_REQUEST]: (_, action) => {
    let errors = {};
    action.payload.errors.forEach(err => {
      errors[err.field] = err.message;
    });
    return ({errors, busy: false});
  },
  [c.SECTION_ADD_FORM_TOGGLE]: (_, action) => ({adding: action.payload.adding, busy: false, errors: {}}),
  [c.SECTION_ADD_PROGRESS]: (_, action) => ({busy: true, errors: {}}),
  [c.SECTION_ADD_SUCCESS]: (_, action) => {
    let sections = _.sections;
    sections.push(action.payload.section);
    return ({adding: false, busy: false, toggleStatus: !_.toggleStatus, sections: sections});
  },
  [c.SECTION_ADD_FAIL]: (_, action) => ({adding: false, busy: false}),
  [c.SECTION_EDIT_FORM_TOGGLE]: (_, action) => ({editing: action.payload.editing, busy: false, errors: {}}),
  [c.SECTION_EDIT_PROGRESS]: (_, action) => ({busy: true, errors: {}}),
  [c.SECTION_EDIT_SUCCESS]: (_, action) => {
    let sections = _.sections;
    let i = sections.findIndex(s => s.id == action.payload.section.id);
    sections[i] = action.payload.section;
    return ({editing: false, busy: false, toggleStatus: !_.toggleStatus, sections: sections});
  },
  [c.SECTION_EDIT_FAIL]: (_, action) => ({editing: false, busy: false}),
  [c.SECTION_REMOVE_SUCCESS]: (_, action) => {
    let sections = _.sections.filter(s => s.id != action.payload.section.id);
    return ({toggleStatus: !_.toggleStatus,sections: sections});
  }
};

export default function section (state = initialState, action) {
  let handler = handlers[action.type];
  if( !handler ) return state;
  return { ...state, ...handler(state, action) };
}
