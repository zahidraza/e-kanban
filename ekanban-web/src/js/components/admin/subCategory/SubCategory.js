import React, { Component } from 'react';
import { connect } from 'react-redux';
import { localeData } from '../../../reducers/localization';
import {initialize}  from '../../../actions/misc';
import {addSubCategory,removeSubCategory,updateSubCategory}  from '../../../actions/subCategory';
import {SUB_CATEGORY_CONSTANTS as c}  from '../../../utils/constants';

import AppHeader from '../../AppHeader';
import Add from "grommet/components/icons/base/Add";
import Box from 'grommet/components/Box';
import Button from 'grommet/components/Button';
import Edit from "grommet/components/icons/base/Edit";
import FilterControl from 'grommet-addons/components/FilterControl';
import Footer from 'grommet/components/Footer';
import Form from 'grommet/components/Form';
import FormField from 'grommet/components/FormField';
import FormFields from 'grommet/components/FormFields';
import Header from 'grommet/components/Header';
import Heading from 'grommet/components/Heading';
import HelpIcon from 'grommet/components/icons/base/Help';
import Layer from 'grommet/components/Layer';
import Search from 'grommet/components/Search';
import Section from 'grommet/components/Section';
import Select from 'grommet/components/Select';
import Spinning from 'grommet/components/icons/Spinning';
import SubCategoryFilter from './SubCategoryFilter';
import Table from 'grommet/components/Table';
import TableRow from 'grommet/components/TableRow';
import Trash from "grommet/components/icons/base/Trash";
import Title from 'grommet/components/Title';

class SubCategory extends Component {

  constructor () {
    super();
    this.state = {
      initializing: false,
      subCategories: [],
      subCategory: {},
      searchText: '',
      filterActive: false,
      filteredCount: 0,
      unfilteredCount: 0,
      page: 1,
      cFilter: undefined  //Category Filter value in add and edit layer
    };
    this.localeData = localeData();
    this._loadSubCategory = this._loadSubCategory.bind(this);
    this._subCategorySort = this._subCategorySort.bind(this);
    this._getSubCategories = this._getSubCategories.bind(this);
    this._onMore = this._onMore.bind(this);
  }

  componentWillMount () {
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    } else {
      const {categories,filter,sort} = this.props.category;
      if (categories.length == 0) {
        alert('You need to add Category first. No SubCategory Available.');
        this.context.router.push('/category');
      }else{
        this._loadSubCategory(this._getSubCategories(categories),filter,sort,1);
        let cFilter = categories[0].name;
        this.setState({cFilter});
      }

    }
  }

  componentWillReceiveProps (nextProps) {
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');
    }
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({cFilter: nextProps.category.categories[0].name});
      this.setState({initializing: false});
    }
    if (this.props.category.toggleStatus != nextProps.category.toggleStatus) {
      const {categories,filter,sort} = nextProps.category;
      this._loadSubCategory(this._getSubCategories(categories),filter,sort,1);
    }

  }

  _getSubCategories (categories) {
    let list = [] ;
    categories.forEach(c => {
      c.subCategoryList.forEach(sc => {
        list.push({category:{name: c.name, id: c.id}, subCategory:{name: sc.name, id: sc.id}});
      });
    });
    return list;
  }

  _loadSubCategory (subCategories,filter,sort,page) {
    let unfilteredCount = subCategories.length;
    if ('category' in filter) {
      const categoryFilter = filter.category;
      subCategories = subCategories.filter(sc => categoryFilter.includes(sc.category.name));
    }
    subCategories = this._subCategorySort(subCategories,sort);
    let filteredCount = subCategories.length;
    subCategories = subCategories.slice(0,15*page);
    this.setState({subCategories, page, filteredCount, unfilteredCount});
  }

  _subCategorySort (subCategories,sort) {
    const [sortProperty,sortDirection] = sort.split(':');
    let result = subCategories.sort((a,b) => {
      if (sortProperty == 'category' && sortDirection == 'asc') {
        return (a.category.name.toLowerCase() < b.category.name.toLowerCase()) ? -1 : 1;
      } else if (sortProperty == 'category' && sortDirection == 'desc') {
        return (a.category.name.toLowerCase() > b.category.name.toLowerCase()) ? -1 : 1;
      } else if (sortProperty == 'subCategory' && sortDirection == 'asc') {
        return (a.subCategory.name.toLowerCase() < b.subCategory.name.toLowerCase()) ? -1 : 1;
      } else if (sortProperty == 'subCategory' && sortDirection == 'desc') {
        return (a.subCategory.name.toLowerCase() > b.subCategory.name.toLowerCase()) ? -1 : 1;
      }
    });
    return result;
  }

  _addSubCategory () {
    const category = this.props.category.categories.find(c => c.name === this.state.cFilter);
    this.props.dispatch(addSubCategory(category._links.subCategoryList.href,this.state.subCategory));
  }

  _updateSubCategory () {
    const category = this.props.category.categories.find(c => c.name === this.state.cFilter);
    this.props.dispatch(updateSubCategory(category._links.subCategoryList.href + '/' + this.state.subCategory.id ,this.state.subCategory));
  }

  _onSearch (event) {
    const {categories,filter,sort} = this.props.category;
    const value = event.target.value;
    let subCategories = this._getSubCategories(categories);
    subCategories = subCategories.filter(sc => sc.subCategory.name.toLowerCase().includes(value.toLowerCase()));
    this.setState({searchText: value});
    if (value.length == 0) {
      this._loadSubCategory(subCategories,filter,sort,1);
    } else {
      this._loadSubCategory(subCategories,{},sort,1);
    }
  }

  _onMore () {
    const {categories,filter,sort} = this.props.category;
    this._loadSubCategory(this._getSubCategories(categories),filter,sort,this.state.page+1);
  }

  _onFilter (event) {
    this.setState({cFilter: event.value});
  }

  _onFilterActivate () {
    this.setState({filterActive: true});
  }

  _onFilterDeactivate () {
    this.setState({filterActive: false});
  }

  _onChangeInput ( event ) {
    var subCategory = this.state.subCategory;
    subCategory[event.target.getAttribute('name')] = event.target.value;
    this.setState({subCategory: subCategory});
  }

  _onAddClick () {
    this.setState({subCategory: {}});
    this.props.dispatch({type: c.SUB_CATEGORY_ADD_FORM_TOGGLE,payload: {adding: true}});
  }

  _onRemoveClick (scId,scName, cName) {
    const category = this.props.category.categories.find(c => c.name === cName);
    let subCategory = {
      id: scId,
      name: scName,
      _links: {
        self: {href: category._links.subCategoryList.href + '/' + scId},
        category: {href: category._links.self.href}
      }
    };
    this.props.dispatch(removeSubCategory(subCategory));
  }

  _onEditClick (scId,scName, cName) {
    this.setState({subCategory: {id: scId, name: scName}, cFilter: cName});
    this.props.dispatch({type: c.SUB_CATEGORY_EDIT_FORM_TOGGLE, payload:{editing: true}});
  }

  _onHelpClick () {
    const helpUrl = window.baseUrl + "/help/subCategory";
    window.open(helpUrl);
  }

  _onCloseLayer (layer) {
    if ( layer == 'add') {
      this.props.dispatch({type: c.SUB_CATEGORY_ADD_FORM_TOGGLE,payload: {adding: false}});
    } else if (layer == 'edit') {
      this.props.dispatch({type: c.SUB_CATEGORY_EDIT_FORM_TOGGLE,payload: {editing: false}});
    }
  }

  render() {
    const {addingSubCategory:adding, editingSubCategory: editing,categories, busy, errorSubCategory: errors} = this.props.category;
    const { subCategories, subCategory, searchText, filterActive,filteredCount,unfilteredCount, cFilter,initializing  } = this.state;

    if (initializing) {
      return (
        <Box pad={{vertical: 'large'}}>
          <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
            <Spinning /> Initializing Application ...
          </Box>
        </Box>
      );
    }

    const busyIcon = busy ? <Spinning /> : null;

    const items = subCategories.map((sc, index)=>{
      return (
        <TableRow key={index}  >
          <td >{sc.category.name}</td>
          <td >{sc.subCategory.name}</td>
          <td style={{textAlign: 'right', padding: 0}}>
            <Button icon={<Edit />} onClick={this._onEditClick.bind(this,sc.subCategory.id,sc.subCategory.name,sc.category.name)} />
            <Button icon={<Trash />} onClick={this._onRemoveClick.bind(this,sc.subCategory.id,sc.subCategory.name,sc.category.name)} />
          </td>
        </TableRow>
      );
    });
    let onMore;
    if (subCategories.length > 0 && subCategories.length < filteredCount) {
      onMore = this._onMore;
    }

    const cItems = categories.map(c => c.name);

    const layerFilter = filterActive ? <SubCategoryFilter onClose={this._onFilterDeactivate.bind(this)}/> : null;

    const layerAdd = (
      <Layer hidden={!adding} onClose={this._onCloseLayer.bind(this, 'add')}  closer={true} align="center">
        <Form>
          <Header><Heading tag="h3" strong={true}>Add New SubCategory</Heading></Header>
          <FormFields>
            <FormField>
              <Select options={cItems} value={cFilter} onChange={this._onFilter.bind(this)}/>
            </FormField>
            <FormField label="SubCategory Name" error={errors.name}>
              <input type="text" name="name" value={subCategory.name == undefined ? '' : subCategory.name} onChange={this._onChangeInput.bind(this)} />
            </FormField>
          </FormFields>
          <Footer pad={{"vertical": "medium"}} >
            <Button icon={busyIcon} label="Add" primary={true}  onClick={this._addSubCategory.bind(this)} />
          </Footer>
        </Form>
      </Layer>
    );

    const layerEdit = (
      <Layer hidden={!editing} onClose={this._onCloseLayer.bind(this, 'edit')}  closer={true} align="center">
        <Form>
          <Header><Heading tag="h3" strong={true}>Update SubCategory Details</Heading></Header>
          <FormFields >
            <FormField label="SubCategory Name" error={errors.name}>
              <input type="text" name="name" value={subCategory.name} onChange={this._onChangeInput.bind(this)} />
            </FormField>
          </FormFields>
          <Footer pad={{"vertical": "medium"}} >
            <Button icon={busyIcon} label="Update" primary={true}  onClick={this._updateSubCategory.bind(this)} />
          </Footer>
        </Form>
      </Layer>
    );

    return (
      <Box>
        <AppHeader/>

        <Header fixed={true} size='large' pad={{ horizontal: 'medium' }}>
          <Title responsive={false}>
            <span>{this.localeData.label_sub_category}</span>
          </Title>
          <Search inline={true} fill={true} size='medium' placeHolder='search sub category name'
            value={searchText} onDOMChange={this._onSearch.bind(this)} />
          <Button icon={<Add />} onClick={this._onAddClick.bind(this)}/>
          <FilterControl filteredTotal={filteredCount}
            unfilteredTotal={unfilteredCount}
            onClick={this._onFilterActivate.bind(this)} />
          <Button icon={<HelpIcon />} onClick={this._onHelpClick.bind(this)}/>
        </Header>

        <Section direction="column" pad={{vertical: 'large', horizontal:'small'}}>
          <Box size="large" alignSelf="center" >
            <Table onMore={onMore}>
              <thead>
                <tr>
                  <th>Category</th>
                  <th>Sub Category</th>
                  <th style={{textAlign: 'right'}}>ACTION</th>
                </tr>
              </thead>
              <tbody>{items}</tbody>
            </Table>
          </Box>
        </Section>
        {layerAdd}
        {layerEdit}
        {layerFilter}
      </Box>
    );
  }
}
SubCategory.contextTypes = {
  router: React.PropTypes.object.isRequired
};

let select = (store) => {
  return { category: store.category, subCategory: store.subCategory, misc: store.misc};
};

export default connect(select)(SubCategory);
