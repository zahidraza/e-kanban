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

class ProductFilter extends Component {
  
  constructor () {
    super();
    this.state = {
      category: [],
      subCategory: [],
      section: [],
      supplier: [],
      classType: []
    };
  }

  componentWillMount () {
    const {category: {categories}, section: {sections}, supplier: {suppliers}} = this.props;
    let category = [], subCategory = [], section = [], supplier = [];
    let classType = [
      {label: 'All', value: undefined},
      {label: 'CLASS_A', value: 'CLASS_A'},
      {label: 'CLASS_B', value: 'CLASS_B'},
      {label: 'CLASS_C', value: 'CLASS_C'}
    ];

    category.push({label: 'All', value: undefined});
    categories.forEach(c => category.push({label: c.name, value: c.name}));

    subCategory.push({label: 'All', value: 'All'});
    categories.forEach(c => {
      c.subCategoryList.forEach(sc => {
        subCategory.push({label: sc.name, value: sc.name});
      });
    });

    section.push({label: 'All', value: 'All'});
    sections.forEach(s => section.push({label: s.name, value: s.name}));

    supplier.push({label: 'All', value: 'All'});
    suppliers.forEach(s => supplier.push({label: s.name, value: s.name}));

    this.setState({category, classType, section, subCategory, supplier});
  }

  _onChange (name,event) {
    let filter = this.props.category.filter;
    if (!event.option.value || event.option.value == 'All') {
      // user selected the 'All' option, which has no value, clear filter
      delete filter[name];
      if (name == 'subCategory' || name == 'section' || name == 'supplier') {
        filter[name] = ['All'];
      }
    } else {
      // we get the new option passed back as an object,
      // normalize it to just a value
      let selectedFilter = event.value.map(value => (
        typeof value === 'object' ? value.value : value)
      );
      selectedFilter = selectedFilter.filter(v => v != 'All');
      filter[name] = selectedFilter;
      if (filter[name].length === 0) {
        delete filter[name];
        if (name == 'subCategory' || name == 'section' || name == 'supplier') {
          filter[name] = ['All'];
        }
      }
    }
    this.props.dispatch({type:c.CATEGORY_FILTER, payload: {filter: filter}});
  }

  _onChangeSort (sort) {
    let sortString = `${sort.value}:${sort.direction}`;
    this.props.dispatch({type:c.CATEGORY_SORT, payload: {sort: sortString}});
  }


  render() {
    const {filter,sort} = this.props.category;

    const [sortProperty, sortDirection] = sort.split(':');

    return (
      <Layer align='right' flush={true} closer={false}
        a11yTitle='Product Filter'>
        <Sidebar size='large'>
          <div>
            <Header size='large' justify='between' align='center'
              pad={{ horizontal: 'medium', vertical: 'medium' }}>
              <Heading tag='h2' margin='none'>Filter</Heading>
              <Button icon={<CloseIcon />} plain={true}
                onClick={this.props.onClose} />
            </Header>
            
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h3'>Class</Heading>
              <Select inline={true}  multiple={true} options={this.state.classType} value={filter.class} onChange={this._onChange.bind(this,'class')} />
            </Section>
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h3'>Category</Heading>
              <Select inline={true} multiple={true} options={this.state.category} value={filter.category} onChange={this._onChange.bind(this,'category')} />
            </Section>
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h3'>Sub Category</Heading>
              <Select  multiple={true} inline={false} options={this.state.subCategory} value={filter.subCategory} onChange={this._onChange.bind(this,'subCategory')} />
            </Section>
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h3'>Section</Heading>
              <Select  multiple={true} inline={false} options={this.state.section} value={filter.section} onChange={this._onChange.bind(this,'section')} />
            </Section>
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h3'>Supplier</Heading>
              <Select inline={false} multiple={true} options={this.state.supplier} value={filter.supplier} onChange={this._onChange.bind(this,'supplier')} />
            </Section>
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h2'>Sort</Heading>
              <Sort options={[
                { label: 'Product Name', value: 'name', direction: 'asc' }
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
  return { category: store.category, section: store.section, supplier: store.supplier};
};

export default connect(select)(ProductFilter);
