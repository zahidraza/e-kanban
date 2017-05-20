import React, { Component } from 'react';
import { connect } from 'react-redux';
//import { localeData } from '../reducers/localization';
import {USER_CONSTANTS as c, USER_ROLES as ur}  from '../../../utils/constants';

//import Box from 'grommet/components/Box';
import Button from 'grommet/components/Button';
import CloseIcon from 'grommet/components/icons/base/Close';
import Header from 'grommet/components/Header';
import Heading from 'grommet/components/Heading';
import Layer from 'grommet/components/Layer';
import Section from 'grommet/components/Section';
import Select from 'grommet/components/Select';
import Sidebar from 'grommet/components/Sidebar';
import Sort from 'grommet-addons/components/Sort';

class UserFilter extends Component {

  constructor () {
    super();
    this.state = {
      roles: [
        {label: 'All', value: undefined},
        {label: ur.ROLE_ADMIN, value: ur.ROLE_ADMIN},
        {label: ur.ROLE_STORE, value: ur.ROLE_STORE},
        {label: ur.ROLE_PURCHASE, value: ur.ROLE_PURCHASE},
        {label: ur.ROLE_USER, value: ur.ROLE_USER}
      ]
    };
  }

  componentWillMount () {

  }

  _onChange (name,event) {
    let filter = this.props.user.filter;

    if (!event.option.value) {
      delete filter[name];
    } else {
      let x = event.value.map(value => (
        typeof value === 'object' ? value.value : value)
      );
      filter[name] = x;
      if (filter[name].length === 0) {
        delete filter[name];
      }
    }
    this.props.dispatch({type:c.USER_FILTER, payload: {filter}});
  }

  _onChangeSort (sort) {
    let sortString = `${sort.value}:${sort.direction}`;
    this.props.dispatch({type:c.USER_SORT, payload: {sort: sortString}});
  }


  render() {
    const {filter,sort} = this.props.user;

    const [sortProperty, sortDirection] = sort.split(':');

    return (
      <Layer align='right' flush={true} closer={false}
        a11yTitle='User Filter'>
        <Sidebar size='large'>
          <div>
            <Header size='large' justify='between' align='center'
              pad={{ horizontal: 'medium', vertical: 'medium' }}>
              <Heading tag='h2' margin='none'>Filter</Heading>
              <Button icon={<CloseIcon />} plain={true}
                onClick={this.props.onClose} />
            </Header>
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h3'>Role</Heading>
              <Select inline={true} multiple={true} options={this.state.roles} value={filter.role} onChange={this._onChange.bind(this,'role')} />
            </Section>
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h2'>Sort</Heading>
              <Sort options={[
                {label: 'User Name', value: 'name', direction: 'asc' }
              ]} value={sortProperty} direction={sortDirection}
              onChange={this._onChangeSort.bind(this)} />
            </Section>
          </div>
        </Sidebar>
      </Layer>
    );
  }
}

let select = (store) => {
  return { user: store.user, misc: store.misc};
};

export default connect(select)(UserFilter);
