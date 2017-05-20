import React, { Component } from 'react';
import { connect } from 'react-redux';
//import { localeData } from '../reducers/localization';
import {CATEGORY_CONSTANTS as c}  from '../../../utils/constants';

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

class SubCategoryFilter extends Component {
  
  constructor () {
    super();
    this.state = {
      categories: []
    };
  }

  componentWillMount () {
    const {categories} = this.props.category;
    let list = [];
    list.push({label: 'All', value: undefined});
    categories.forEach(c => {
      list.push({ label: c.name, value: c.name});
    });
    this.setState({categories: list});
  }

  _onChange (name,event) {
    let filter = this.props.category.filter;

    if (!event.option.value) {
      // user selected the 'All' option, which has no value, clear filter
      delete filter[name];
    } else {
      // we get the new option passed back as an object,
      // normalize it to just a value
      let x = event.value.map(value => (
        typeof value === 'object' ? value.value : value)
      );
      console.log(x);
      filter[name] = x;
      if (filter[name].length === 0) {
        delete filter[name];
      }
    }
    this.props.dispatch({type:c.CATEGORY_FILTER, payload: {filter: filter}});
  }

  _onChangeSort (sort) {
    let sortString = `${sort.value}:${sort.direction}`;
    console.log(sortString);
    this.props.dispatch({type:c.CATEGORY_SORT, payload: {sort: sortString}});
  }


  render() {
    const {filter,sort} = this.props.category;

    const [sortProperty, sortDirection] = sort.split(':');

    return (
      <Layer align='right' flush={true} closer={false}
        a11yTitle='Sub Category Filter'>
        <Sidebar size='large'>
          <div>
            <Header size='large' justify='between' align='center'
              pad={{ horizontal: 'medium', vertical: 'medium' }}>
              <Heading tag='h2' margin='none'>Filter</Heading>
              <Button icon={<CloseIcon />} plain={true}
                onClick={this.props.onClose} />
            </Header>
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h3'>Category</Heading>
              <Select inline={true} multiple={true} options={this.state.categories} value={filter.category} onChange={this._onChange.bind(this,'category')} />
            </Section>
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h2'>Sort</Heading>
              <Sort options={[
                { label: 'Category Name', value: 'category', direction: 'asc' },
                { label: 'SubCategory Name', value: 'subCategory', direction: 'asc' }
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
  return { category: store.category, misc: store.misc};
};

export default connect(select)(SubCategoryFilter);
