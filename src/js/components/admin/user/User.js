import React, { Component } from 'react';
import { connect } from 'react-redux';
import { localeData } from '../../../reducers/localization';
import {initialize}  from '../../../actions/misc';
import {addUser,removeUser,updateUser}  from '../../../actions/user';
import {USER_CONSTANTS as u, USER_ROLES as ur} from '../../../utils/constants';

import AppHeader from '../../AppHeader';
import Add from "grommet/components/icons/base/Add";
import Box from 'grommet/components/Box';
import Button from 'grommet/components/Button';
import Edit from "grommet/components/icons/base/Edit";
import Footer from 'grommet/components/Footer';
import Form from 'grommet/components/Form';
import FormField from 'grommet/components/FormField';
import FormFields from 'grommet/components/FormFields';
import Header from 'grommet/components/Header';
import Heading from 'grommet/components/Heading';
//import HelpIcon from 'grommet/components/icons/base/Help';
import Layer from 'grommet/components/Layer';
import List from 'grommet/components/List';
import ListItem from 'grommet/components/ListItem';
import Section from 'grommet/components/Section';
import Select from 'grommet/components/Select';
import Spinning from 'grommet/components/icons/Spinning';
import Table from 'grommet/components/Table';
import TableRow from 'grommet/components/TableRow';
import Trash from "grommet/components/icons/base/Trash";
import View from "grommet/components/icons/base/View";


class User extends Component {
  
  constructor () {
    super();
    this.state = {
      initializing: false,
      viewing: false,
      users: [],
      errors: [],
      user: {},
      roles: [ur.ROLE_ADMIN,ur.ROLE_PURCHASE,ur.ROLE_STORE,ur.ROLE_USER]
    };
    this.localeData = localeData();
  }

  componentWillMount () {
    console.log('componentWillMount');
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }else {
      this.setState({users: this.props.user.users});
    }
  }

  componentWillReceiveProps (nextProps) {
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');
    }
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({initializing: false});
      this.setState({users: nextProps.user.users});
    }

    if (this.props.user.toggleStatus != nextProps.user.toggleStatus) {
      this.setState({users: nextProps.user.users});
    }
  }

  _addUser () {
    this.props.dispatch(addUser(this.state.user));
  }

  _updateUser () {
    this.props.dispatch(updateUser(this.state.user));
  }

  _onChangeInput ( event ) {
    var user = this.state.user;
    user[event.target.getAttribute('name')] = event.target.value;
    this.setState({user: user});
  }

  _onRoleFilter (event) {
    console.log('_onRoleFilter');
    const user = this.state.user;
    user.role = event.value;
    this.setState({user: user});
  }

  _onAddClick () {
    console.log('_onAddClick');
    const user = this.state.user;
    user.role = 'Select Role';
    this.setState({ user: user});
    this.props.dispatch({type: u.USER_ADD_FORM_TOGGLE, payload: {adding: true}});
  }

  _onRemoveClick (index) {
    this.props.dispatch(removeUser(this.state.users[index]));
  }

  _onViewClick (index) {
    console.log('_onViewClick');
    const users = this.state.users;
    this.setState({viewing: true, user: users[index]});
  }

  _onEditClick (index) {
    console.log('_onEditClick');
    const users = this.state.users;
    this.setState({user: users[index]});
    this.props.dispatch({type: u.USER_EDIT_FORM_TOGGLE, payload: {editing: true}});
  }

  _onCloseLayer (layer) {
    console.log('_onCloseLayer');
    if( layer == 'add')
      this.props.dispatch({type: u.USER_ADD_FORM_TOGGLE, payload: {adding: false}});
    else if (layer == 'view')
      this.setState({viewing: false, user: {}});
    else if (layer == 'edit')
      this.props.dispatch({type: u.USER_EDIT_FORM_TOGGLE, payload: {editing: false}});
  }

  render() {
    const {adding,editing,users} = this.props.user;
    let {viewing, user, errors, roles,initializing } = this.state;
    
    if (initializing) {
      return (
        <Box pad={{vertical: 'large'}}>
          <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
            <Spinning /> Initializing Application ...
          </Box>
        </Box>
      );
    }

    //const busy = adding ? <Spinning /> : null;

    //const loading = fetching ? (<Spinning />) : null;
    const userItems = users.map((user, index)=>{
      return (
        <TableRow key={index}  >
          <td >{user.name}</td>
          <td >{user.role}</td>
          <td style={{textAlign: 'right', padding: 0}}>
            <Button icon={<View />} onClick={this._onViewClick.bind(this,index)} />
            <Button icon={<Edit />} onClick={this._onEditClick.bind(this,index)} />
            <Button icon={<Trash />} onClick={this._onRemoveClick.bind(this,index)} />
          </td>
        </TableRow>
      );
    });

    const layerAdd = (
      <Layer hidden={!adding} onClose={this._onCloseLayer.bind(this, 'add')}  closer={true} align="center">
        <Form>
          <Header><Heading tag="h3" strong={true}>Add New User</Heading></Header>
          <FormFields>
            <FormField label="User Name" error={errors[0]}>
              <input type="text" name="name" value={user.name} onChange={this._onChangeInput.bind(this)} />
            </FormField>
            <FormField label="Email" error={errors[2]}>
              <input type="email" name="email" value={user.email} onChange={this._onChangeInput.bind(this)} />
            </FormField>
            <FormField label="Mobile Number" error={errors[3]}>
              <input type="text" name="mobile" value={user.mobile} onChange={this._onChangeInput.bind(this)} />
            </FormField>
            <FormField>
              <Select options={roles} value={user.role} onChange={this._onRoleFilter.bind(this)}/>
            </FormField>
          </FormFields>
          <Footer pad={{"vertical": "medium"}} >
            <Button label="Add" primary={true}  onClick={this._addUser.bind(this)} />
          </Footer>
        </Form>
      </Layer>
    );

    const layerView = (
      <Layer hidden={!viewing}  onClose={this._onCloseLayer.bind(this, 'view')}  closer={true} align="center">
        <Box size="medium"  pad={{vertical: 'none', horizontal:'small'}}>
          <Header><Heading tag="h3" strong={true} >User Details</Heading></Header>
          <List>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> User Name </span>
              <span className="secondary">{user.name}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Email Id </span>
              <span className="secondary">{user.email}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Mobile </span>
              <span className="secondary">{user.mobile}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Role </span>
              <span className="secondary">{user.role}</span>
            </ListItem>
          </List>
        </Box>
        <Box pad={{vertical: 'medium', horizontal:'small'}} />
      </Layer>
    );

    const layerEdit = (
      <Layer hidden={!editing} onClose={this._onCloseLayer.bind(this, 'edit')}  closer={true} align="center">
        <Form>
          <Header><Heading tag="h3" strong={true}>Update User Details</Heading></Header>
          <FormFields >
            <FormField label="User Name" error={errors[0]}>
              <input type="text" name="name" value={user.name} onChange={this._onChangeInput.bind(this)} />
            </FormField>
            <FormField label="Email" error={errors[1]}>
              <input type="email" name="email" value={user.email} onChange={this._onChangeInput.bind(this)} />
            </FormField>
            <FormField label="Mobile Number" error={errors[2]}>
              <input type="text" name="mobile" value={user.mobile} onChange={this._onChangeInput.bind(this)} />
            </FormField>
            <FormField>
              <Select options={roles} name="role" value={user.role} onChange={this._onRoleFilter.bind(this)}/>
            </FormField>
          </FormFields>
          <Footer pad={{"vertical": "medium"}} >
            <Button label="Update" primary={true}  onClick={this._updateUser.bind(this)} />
          </Footer>
        </Form>
      </Layer>
    );

    return (
      <Box>
        <AppHeader page={this.localeData.label_user}/>
        <Section direction="column" size="xxlarge" pad={{vertical: 'large', horizontal:'small'}}>
          <Box size="large" alignSelf="center" >
            <Table>
              <thead>
                <tr>
                  <th>User Name</th>
                  <th>Role</th>
                  <th style={{textAlign: 'right'}}>ACTION</th>
                </tr>
              </thead>
              <tbody>
                {userItems}
              </tbody>
            </Table>
          </Box>
          {/*<Box size="xsmall" alignSelf="center" pad={{horizontal:'medium'}}>{loading}</Box>*/}
          <Box size="small" alignSelf="center" pad={{vertical:'large'}}>
            <Button icon={<Add />} label="Add User" primary={true} a11yTitle="Add item" onClick={this._onAddClick.bind(this)}/>
          </Box>
        </Section>
        {layerAdd}
        {layerView}
        {layerEdit}
      </Box>
    );
  }
}


User.contextTypes = {
  router: React.PropTypes.object.isRequired
};

let select = (store) => {
  return { user: store.user, misc: store.misc};
};

export default connect(select)(User);
