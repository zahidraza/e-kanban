import React, { Component } from 'react';
import { connect } from 'react-redux';
import { localeData } from '../../../reducers/localization';
import {initialize}  from '../../../actions/misc';
import {addUser,removeUser,updateUser}  from '../../../actions/user';
import {USER_CONSTANTS as u, USER_ROLES as ur} from '../../../utils/constants';

import AppHeader from '../../AppHeader';
import UserFilter from './UserFilter';
import Add from "grommet/components/icons/base/Add";
import Box from 'grommet/components/Box';
import Button from 'grommet/components/Button';
import Edit from "grommet/components/icons/base/Edit";
import Footer from 'grommet/components/Footer';
import Form from 'grommet/components/Form';
import FormField from 'grommet/components/FormField';
import FormFields from 'grommet/components/FormFields';
import FilterControl from 'grommet-addons/components/FilterControl';
import Header from 'grommet/components/Header';
import Heading from 'grommet/components/Heading';
import HelpIcon from 'grommet/components/icons/base/Help';
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
import Title from 'grommet/components/Title';
import Search from 'grommet/components/Search';


class User extends Component {

  constructor () {
    super();
    this.state = {
      initializing: false,
      searchText: '',
      page: 1,
      filteredCount: 0,
      unfilteredCount: 0,
      filterActive: false,
      viewing: false,
      users: [],
      user: {},
      roles: [ur.ROLE_USER,ur.ROLE_PURCHASE,ur.ROLE_STORE,ur.ROLE_ADMIN]
    };
    this.localeData = localeData();
    this._loadUser = this._loadUser.bind(this);
    this._userSort = this._userSort.bind(this);
    this._renderLayerView = this._renderLayerView.bind(this);
    this._renderLayerAdd= this._renderLayerAdd.bind(this);
    this._renderLayerEdit = this._renderLayerEdit.bind(this);
    this._onMore = this._onMore.bind(this);
  }

  componentWillMount () {
    console.log('componentWillMount');
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }else {
      const {users,filter,sort} = this.props.user;
      this._loadUser(users,filter,sort,1);
    }
  }

  componentWillReceiveProps (nextProps) {
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');
    }
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({initializing: false});
      const {users,filter,sort} = nextProps.user;
      this._loadUser(users,filter,sort,1);
    }

    if (this.props.user.toggleStatus != nextProps.user.toggleStatus) {
      const {users,filter,sort} = nextProps.user;
      this._loadUser(users,filter,sort,1);
    }
  }

  _loadUser (users,filter,sort,page) {
    const unfilteredCount = users.length;
    if ('role' in filter) {
      const roleFilter = filter.role;
      users = users.filter(u => roleFilter.includes(u.role));
    }
    users = this._userSort(users,sort);
    let filteredCount = users.length;
    users = users.slice(0,15*page);
    this.setState({users,page,filteredCount, unfilteredCount});
  }

  _userSort (users,sort) {
    const [sortProperty,sortDirection] = sort.split(':');
    users = users.sort((a,b) => {
      if (sortProperty == 'name' && sortDirection == 'asc') {
        return (a.name.toLowerCase() < b.name.toLowerCase()) ? -1 : 1;
      } else if (sortProperty == 'name' && sortDirection == 'desc') {
        return (a.name.toLowerCase() > b.name.toLowerCase()) ? -1 : 1;
      }
    });
    return users;
  }

  _addUser () {
    console.log('_addUser');
    this.props.dispatch(addUser(this.state.user));
  }

  _updateUser () {
    this.props.dispatch(updateUser(this.state.user));
  }

  _onChangeInput ( event ) {
    console.log('_onChangeInput');
    var user = this.state.user;
    user[event.target.getAttribute('name')] = event.target.value;
    this.setState({user: user});
  }

  _onFilterActivate () {
    this.setState({filterActive: true});
  }

  _onFilterDeactivate () {
    this.setState({filterActive: false});
  }

  _onRoleFilter (event) {
    const user = this.state.user;
    user.role = event.value;
    this.setState({user: user});
  }

  _onAddClick () {
    console.log('_onAddClick');
    let user = {};
    user.role = ur.ROLE_USER;
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
    this.setState({user: {...users[index]}});
    this.props.dispatch({type: u.USER_EDIT_FORM_TOGGLE, payload: {editing: true}});
  }

  _onHelpClick () {
    console.log('_onHelpClick');
  }

  _onSearch (event) {
    console.log('_onSearch');
    let {users,filter,sort} = this.props.user;
    let value = event.target.value;
    users = users.filter(u => u.name.toLowerCase().includes(value.toLowerCase()) || u.email.toLowerCase().includes(value.toLowerCase()));
    this.setState({searchText: value});
    if (value.length == 0) {
      this._loadUser(users,filter,sort,1);
    } else {
      this._loadUser(users,{},sort,1);
    }
  }

  _onMore () {
    console.log('_onMore');
    const {users,filter,sort} = this.props.user;
    this._loadUser(users,filter,sort,this.state.page+1);
  }

  _onCloseLayer (layer) {
    console.log('_onCloseLayer');
    if( layer == 'add') {
      this.props.dispatch({type: u.USER_ADD_FORM_TOGGLE, payload: {adding: false}});
    }else if (layer == 'view') {
      this.setState({viewing: false, user: {}});
    }else if (layer == 'edit') {
      this.props.dispatch({type: u.USER_EDIT_FORM_TOGGLE, payload: {editing: false}});
    }

  }

  _renderLayerView () {
    const {user} = this.state;
    return (
      <Layer hidden={!this.state.viewing}  onClose={this._onCloseLayer.bind(this, 'view')}  closer={true} align="center">
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
  }

  _renderLayerAdd () {
    const {user,roles} = this.state;
    const {errors,busy,adding} = this.props.user;
    const busyIcon = busy ? <Spinning /> : null;
    return (
      <Layer hidden={!adding} onClose={this._onCloseLayer.bind(this, 'add')}  closer={true} align="center">
        <Form autoComplete="off">
          <Header><Heading tag="h3" strong={true}>Add New User</Heading></Header>
          <FormFields>
            <FormField label="User Name" error={errors.name}>
              <input type="text" name="name" value={user.name == undefined ? '' : user.name} onChange={this._onChangeInput.bind(this)} />
            </FormField>
            <FormField label="Email" error={errors.email}>
              <input type="email" name="email" value={user.email == undefined ? '' : user.email} onChange={this._onChangeInput.bind(this)} />
            </FormField>
            <FormField label="Mobile Number" error={errors.mobile}>
              <input type="text" name="mobile" value={user.mobile == undefined ? '': user.mobile} onChange={this._onChangeInput.bind(this)} />
            </FormField>
            <FormField error={errors.role}>
              <Select options={roles} value={user.role} onChange={this._onRoleFilter.bind(this)}/>
            </FormField>
          </FormFields>
          <Footer pad={{"vertical": "medium"}} >
            <Button icon={busyIcon} label="Add" primary={true}  onClick={this._addUser.bind(this)} />
          </Footer>
        </Form>
      </Layer>
    );
  }

  _renderLayerEdit () {
    const {user,roles} = this.state;
    const {errors,busy,editing} = this.props.user;
    const busyIcon = busy ? <Spinning /> : null;
    return (
      <Layer hidden={!editing} onClose={this._onCloseLayer.bind(this, 'edit')}  closer={true} align="center">
        <Form>
          <Header><Heading tag="h3" strong={true}>Update User Details</Heading></Header>
          <FormFields >
            <FormField label="User Name" error={errors.name}>
              <input type="text" name="name" value={user.name} onChange={this._onChangeInput.bind(this)} />
            </FormField>
            <FormField label="Email" error={errors.email}>
              <input type="email" name="email" value={user.email} onChange={this._onChangeInput.bind(this)} />
            </FormField>
            <FormField label="Mobile Number" error={errors.mobile}>
              <input type="text" name="mobile" value={user.mobile} onChange={this._onChangeInput.bind(this)} />
            </FormField>
            <FormField error={errors.role}>
              <Select options={roles} name="role" value={user.role} onChange={this._onRoleFilter.bind(this)}/>
            </FormField>
          </FormFields>
          <Footer pad={{"vertical": "medium"}} >
            <Button icon={busyIcon} label="Update" primary={true}  onClick={this._updateUser.bind(this)} />
          </Footer>
        </Form>
      </Layer>
    );
  }

  render() {
    let {users,initializing,searchText,filteredCount,unfilteredCount,filterActive} = this.state;

    if (initializing) {
      return (
        <Box pad={{vertical: 'large'}}>
          <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
            <Spinning /> Initializing Application ...
          </Box>
        </Box>
      );
    }

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
    let onMore;
    if (users.length > 0 && users.length < filteredCount) {
      onMore = this._onMore;
    }
    const layerFilter = filterActive ? <UserFilter onClose={this._onFilterDeactivate.bind(this)}/> : null;
    const layerAdd = this._renderLayerView();
    const layerView = this._renderLayerAdd();
    const layerEdit = this._renderLayerEdit();

    return (
      <Box>
        <AppHeader/>
        <Header fixed={true} size='large' pad={{ horizontal: 'medium' }}>
          <Title responsive={false}>
            <span>{this.localeData.label_user}</span>
          </Title>
          <Search inline={true} fill={true} size='medium' placeHolder='Search'
            value={searchText} onDOMChange={this._onSearch.bind(this)} />
          <Button icon={<Add />} onClick={this._onAddClick.bind(this)}/>
          <FilterControl filteredTotal={filteredCount}
            unfilteredTotal={unfilteredCount}
            onClick={this._onFilterActivate.bind(this)} />
          <Button icon={<HelpIcon />} onClick={this._onHelpClick.bind(this)}/>
        </Header>
        <Section direction="column" size="xxlarge" pad={{vertical: 'large', horizontal:'small'}}>
          <Box size="large" alignSelf="center" >
            <Table onMore={onMore}>
              <thead>
                <tr>
                  <th>User Name</th>
                  <th>Role</th>
                  <th style={{textAlign: 'right'}}>Action</th>
                </tr>
              </thead>
              <tbody>
                {userItems}
              </tbody>
            </Table>
          </Box>
        </Section>
        {layerAdd}
        {layerView}
        {layerEdit}
        {layerFilter}
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
