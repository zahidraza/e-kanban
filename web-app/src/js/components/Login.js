import React, { Component } from 'react';
import { connect } from 'react-redux';
import { localeData } from '../reducers/localization';
import {initialize,navActivate} from '../actions/misc';
import {authenticate} from '../actions/user';
import {USER_CONSTANTS as u} from '../utils/constants';

//Components
import Box from 'grommet/components/Box';
import Button from 'grommet/components/Button';
import Footer from 'grommet/components/Footer';
import Form from 'grommet/components/Form';
import FormField from 'grommet/components/FormField';
import FormFields from 'grommet/components/FormFields';
//import Header from 'grommet/components/Header';
import Heading from 'grommet/components/Heading';
//import Layer from 'grommet/components/Layer';
import Spinning from 'grommet/components/icons/Spinning';

class Login extends Component {
  constructor () {
    super();
    this.state = {
      initializing: false,
      credential: {},
      errors: [],
      isForgot: false,
      email: '',
      changing: false,  //changing password
      errorMsg: ''
    };

    this.localeData = localeData();
    this._renderForgotPasswdLayer = this._renderForgotPasswdLayer.bind(this);
  }

  componentWillMount () {
    console.log('componentWillMount');
    this.props.dispatch(navActivate(false));
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }
    this.props.dispatch({type: u.USER_AUTH_NOT_PROGRESS});
  }

  componentWillReceiveProps (nextProps) {
    console.log('componentWillReceiveProps');
    if (nextProps.misc.initialized) {
      this.setState({initializing: false});
    }
    if (this.props.user.busy && !nextProps.user.busy && sessionStorage.session == undefined) {
      this.setState({errorMsg: "Incorrect email or password, try again!"});
    }
    if (sessionStorage.session == 'true') {
      this.context.router.push('/dashboard');
    }
  }

  _login () {
    const {credential} = this.state;
    this.setState({errorMsg: ''});
    this.props.dispatch(authenticate(credential.email, credential.password));
  }

  _forgotPasswordClick () {

  }

  _forgotPassword () {

  }

  _onChange (event) {
    let { credential } = this.state;
    credential[event.target.getAttribute('name')] = event.target.value;
    this.setState({credential: credential});
  }

  _onChangeInput (e) {
    this.setState({email: e.target.value});
  }

  _onCloseLayer () {
    this.setState({isForgot: false, errors:[]});
  }

  _renderForgotPasswdLayer () {
    return null;
    /*return (
      <Layer onClose={this._onCloseLayer.bind(this)}  closer={true} align="center">
        <Form>
          <Header><Heading tag="h3" strong={true}>Forgot Password</Heading></Header>
          <FormFields>
              <FormField label="Email Id" error={this.state.errors[0]} >
                <input type="email" value={this.state.email} onChange={this._onChangeInput.bind(this)} />
              </FormField>
          </FormFields>
          <Footer pad={{"vertical": "medium"}} >
            <Button icon={busy} label="Submit" primary={true}  onClick={this._forgotPassword.bind(this)} />
          </Footer>
        </Form>
      </Layer>
    );*/
  }

  render () {

    const { initializing, credential, isForgot, errorMsg } = this.state;

    if (initializing) {
      return (
        <Box pad={{vertical: 'large'}}>
          <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
            <Spinning /> Initializing Application ...
          </Box>
        </Box>
      );
    }

    const layerForgotPassword = isForgot ? this._renderForgotPasswdLayer() : null;

    const busyIcon = this.props.user.busy ? <Spinning /> : null;
    return (
      
      <Box pad={{horizontal: 'large', vertical: "large"}} wrap={true}  full="vertical" texture="url(/andon-system/static/img/cover.jpg)" >
        <Box align="end" justify="end" pad={{"horizontal": "large", vertical:"large", between:"large"}}>
          <Box size="auto"  align="center" separator="all" justify="center" colorIndex="light-1" pad={{"horizontal": "medium", vertical:"medium", between:"medium"}} >
            
            <Heading tag="h1">{this.localeData.app_name_full} {this.localeData.app_version}</Heading>
            {busyIcon}
            <Form>
              <FormFields>
                <FormField label={this.localeData.login_email}>
                  <input type="text" name="email" value={credential.email} onChange={this._onChange.bind(this)} />
                </FormField>
                <FormField label={this.localeData.login_password}>
                  <input type="password" name="password" value={credential.password} onChange={this._onChange.bind(this)} />
                </FormField>
              </FormFields>
              <a style={{color:'blue'}} onClick={this._forgotPasswordClick.bind(this)}>Forgot password?</a>
              <p style={{color:'red'}} >{errorMsg}</p>
              <Footer pad={{"vertical": "small"}}>
                <Button label="Login" fill={true} primary={true}  onClick={this._login.bind(this)} /> <br/>
              </Footer>
            </Form>
            <Box> (c) 2017 {this.localeData.company_name}</Box>
          </Box>
        </Box>
        {layerForgotPassword}
      </Box>
    );
  }
}

Login.contextTypes = {
  router: React.PropTypes.object.isRequired
};

let select = (store) => {
  return { nav: store.nav, misc: store.misc, user: store.user };
};

export default connect(select)(Login);


// <Box pad={{horizontal: 'large', vertical: "large"}} wrap={true}  full="vertical" texture="url(/andon-system/static/img/cover.jpg)" >
