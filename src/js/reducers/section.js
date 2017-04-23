import {SECTION_CONSTANTS as c} from "../utils/constants";

const initialState = {
  loaded: false,
  fetching: false,
  adding: false,
  editing: false,
  sections:[],
  toggleStatus: true
};

const handlers = { 
  [c.INITIALIZE_SECTION]: (_, action) => ({sections: action.payload.sections, loaded: true, toggleStatus: !_.toggleStatus}),
  // [c.SECTION_FETCH_PROGRESS]: (_, action) => ({fetching: true}),
  // [c.SECTION_FETCH_SUCCESS]: (_, action) => ({loaded: true, fetching: false,toggleStatus: !_.toggleStatus, sections: action.payload.sections}),
  // [c.SECTION_FETCH_FAIL]: (_, action) => ({fetching: false}),
  [c.SECTION_ADD_FORM_TOGGLE]: (_, action) => ({adding: action.payload.adding}),
  [c.SECTION_ADD_SUCCESS]: (_, action) => {
    let sections = _.sections;
    sections.push(action.payload.section);
    return ({adding: false,toggleStatus: !_.toggleStatus, sections: sections});
  },
  [c.SECTION_ADD_FAIL]: (_, action) => ({adding: false}),
  [c.SECTION_EDIT_FORM_TOGGLE]: (_, action) => ({editing: action.payload.editing}),
  [c.SECTION_EDIT_SUCCESS]: (_, action) => {
    let sections = _.sections;
    let i = sections.findIndex(s => s.id == action.payload.section.id);
    sections[i] = action.payload.section;
    return ({editing: false,toggleStatus: !_.toggleStatus, sections: sections});
  },
  [c.SECTION_EDIT_FAIL]: (_, action) => ({editing: false}),
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
