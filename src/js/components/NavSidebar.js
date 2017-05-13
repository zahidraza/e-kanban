import React, { Component } from "react";
import { connect } from 'react-redux';
import { localeData } from '../reducers/localization';
import { USER_ROLES as ur} from '../utils/constants';

import Sidebar from "grommet/components/Sidebar";
import Header from "grommet/components/Header";
import Title from "grommet/components/Title";
import Button from "grommet/components/Button";
import Menu from "grommet/components/Menu";
import Close from "grommet/components/icons/base/Close";
import Anchor from 'grommet/components/Anchor';
import Footer from "grommet/components/Footer";

import { navActivate } from '../actions/misc';

class NavSidebar extends Component {

  constructor () {
    super();
    this._onClose = this._onClose.bind(this);
    this.localeData = localeData();
  }

  componentWillMount () {
    this.setState({localeData: localeData()});
  }

  _onClose () {
    console.log('Close Button clicked');
    this.props.dispatch(navActivate(false));
  }

  render () {
    const { itemsAdmin, itemsStore, itemsPurchase, itemsUser} = this.props.nav;
    const { role } = window.sessionStorage;
    let items;
    if (role == ur.ROLE_ADMIN) {
      items = itemsAdmin;
    } else if (role == ur.ROLE_PURCHASE) {
      items = itemsPurchase;
    } else if (role == ur.ROLE_STORE) {
      items = itemsStore;
    } else if (role == ur.ROLE_USER) {
      items = itemsUser;
    }

    var links = items.map( (page, index) => {
      return (
        <Anchor  key={page.label} path={page.path} label={page.label} />
      );
    });
    return (
      <Sidebar colorIndex="neutral-1" size="small">
        <Header pad="medium" justify="between" >
          <Title>{this.localeData.app_name_short}</Title>
          <Button icon={<Close />} onClick={this._onClose} />
        </Header>
        <Menu fill={true} primary={true}>
          {links}
        </Menu>
        <Footer pad={{horizontal: 'medium', vertical: 'small'}}>
          <h5> (c)2017 {this.localeData.company_name}</h5>
        </Footer>
      </Sidebar>
    );
  }
}

let select = (store) => {
  return { nav : store.nav};
};

export default connect(select)(NavSidebar);
