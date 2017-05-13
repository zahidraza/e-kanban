import React, { Component } from 'react';
import { connect } from 'react-redux';
//import { localeData } from '../reducers/localization';
import {SUPPLIER_CONSTANTS as c}  from '../../../utils/constants';

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

class SupplierFilter extends Component {
  
  constructor () {
    super();
    this.state = {
      sTypes: []
    };
  }

  componentWillMount () {
    let sTypeSet = new Set();
    this.props.supplier.suppliers.forEach(s => sTypeSet.add(s.supplierType));

    let list = [];
    list.push({label: 'All', value: undefined});
    sTypeSet.forEach(st => list.push({label: st, value: st}));
    this.setState({sTypes: list});
  }

  _onChange (name,event) {
    let filter = this.props.supplier.filter;

    if (!event.option.value) {
      // user selected the 'All' option, which has no value, clear filter
      delete filter[name];
    } else {
      // we get the new option passed back as an object,
      // normalize it to just a value
      let selectedFilter = event.value.map(value => (
        typeof value === 'object' ? value.value : value)
      );
      console.log(selectedFilter);
      filter[name] = selectedFilter;
      if (filter[name].length === 0) {
        delete filter[name];
      }
    }
    this.props.dispatch({type:c.SUPPLIER_FILTER, payload: {filter: filter}});
  }

  _onChangeSort (sort) {
    let sortString = `${sort.value}:${sort.direction}`;
    console.log(sortString);
    this.props.dispatch({type:c.SUPPLIER_SORT, payload: {sort: sortString}});
  }


  render() {
    const {filter,sort} = this.props.supplier;

    const [sortProperty, sortDirection] = sort.split(':');

    return (
      <Layer align='right' flush={true} closer={false}
        a11yTitle='Supplier Filter'>
        <Sidebar size='large'>
          <div>
            <Header size='large' justify='between' align='center'
              pad={{ horizontal: 'medium', vertical: 'medium' }}>
              <Heading tag='h2' margin='none'>Filter</Heading>
              <Button icon={<CloseIcon />} plain={true}
                onClick={this.props.onClose} />
            </Header>
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h3'>Supplier Type</Heading>
              <Select inline={true} multiple={true} options={this.state.sTypes} value={filter.supplierType} onChange={this._onChange.bind(this,'supplierType')} />
            </Section>
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h2'>Sort</Heading>
              <Sort options={[
                { label: 'Supplier Name', value: 'name', direction: 'asc' }
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
  return { supplier: store.supplier, misc: store.misc};
};

export default connect(select)(SupplierFilter);
