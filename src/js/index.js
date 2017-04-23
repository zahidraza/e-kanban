import '../scss/index.scss';

import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { Router} from "react-router";
import history from './history';
import axios from "axios";

import routes from "./routes";
import store from "./store";

(function () {
  // const baseUrl = location.protocol+'//'+location.hostname+(location.port ? ':'+location.port: '');
  // window.serviceHost = baseUrl + "/vmi/api";
  window.serviceHost = "http://localhost:8000/api";

  //axios.defaults.baseURL = 'http://localhost:8000/api';
  //axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
  axios.defaults.headers.post['Content-Type'] = 'application/json';


})();

let element = document.getElementById('content');
ReactDOM.render((
  <div>
    <Provider store={store} >
        <Router routes={routes} history={history} />
    </Provider>
  </div>
), element);

document.body.classList.remove('loading');
