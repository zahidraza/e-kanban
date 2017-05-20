import { createStore, combineReducers, applyMiddleware } from "redux";

import logger from "redux-logger";
import thunk from "redux-thunk";
import promise from "redux-promise-middleware";

import nav from "./reducers/nav";
import misc from "./reducers/misc";
import user from "./reducers/user";
import category from "./reducers/category";
import inventory from "./reducers/inventory";
import section from "./reducers/section";
import supplier from "./reducers/supplier";
import order from './reducers/order';

const reducer = combineReducers({ nav, misc, user, category, section, supplier, inventory, order});

const middleware = applyMiddleware(promise(), thunk, logger());

export default createStore(reducer, middleware);
