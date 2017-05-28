import React, { Component } from 'react';
import { connect } from 'react-redux';
import { localeData } from '../../../reducers/localization';

import {addCategory,removeCategory,updateCategory}  from '../../../actions/category';
import {initialize}  from '../../../actions/misc';
import {CATEGORY_CONSTANTS as c}  from '../../../utils/constants';

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
import HelpIcon from 'grommet/components/icons/base/Help';
import Layer from 'grommet/components/Layer';
import List from 'grommet/components/List';
import ListItem from 'grommet/components/ListItem';
import ListPlaceholder from 'grommet-addons/components/ListPlaceholder';
import Section from 'grommet/components/Section';
import Spinning from 'grommet/components/icons/Spinning';
import Trash from "grommet/components/icons/base/Trash";
import Title from 'grommet/components/Title';
import Search from 'grommet/components/Search';

class Category extends Component {

  constructor () {
    super();
    this.state = {
      initializing: false,
      categories: [],
      category: {},
      searchText: ''
    };
    this.localeData = localeData();
  }

  componentWillMount () {
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }else {
      this.setState({categories: this.props.category.categories});
    }
  }

  componentWillReceiveProps (nextProps) {
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');

    }
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({initializing: false});
    }
    if (nextProps.misc.initialized) {
      this.setState({categories: nextProps.category.categories});
    }
  }

  _addCategory () {
    this.props.dispatch(addCategory(this.state.category));
  }

  _updateCategory () {
    this.props.dispatch(updateCategory(this.state.category));
  }

  _onSearch (event) {
    const value = event.target.value;
    const categories = this.props.category.categories.filter(c => c.name.toLowerCase().includes(value.toLowerCase()));
    this.setState({searchText: value, categories});
  }

  _onChangeInput ( event ) {
    var category = this.state.category;
    category[event.target.getAttribute('name')] = event.target.value;
    this.setState({category: category});
  }

  _onAddClick () {
    this.setState({category: {}});
    this.props.dispatch({type: c.CATEGORY_ADD_FORM_TOGGLE, payload: {adding: true}});
  }

  _onRemoveClick (index) {
    this.props.dispatch(removeCategory(this.state.categories[index]));
    this.setState({searchText: ''});
  }

  _onEditClick (index) {
    const tmp = this.props.category.categories[index];
    const category = JSON.parse(JSON.stringify({name: tmp.name, _links: tmp._links}));
    this.setState({category});
    this.props.dispatch({type: c.CATEGORY_EDIT_FORM_TOGGLE, payload: {editing: true}});
  }

  _onHelpClick () {
    const helpUrl = window.baseUrl + "/help/category";
    window.open(helpUrl);
  }

  _onCloseLayer (layer) {
    if ( layer == 'add') {
      this.props.dispatch({type: c.CATEGORY_ADD_FORM_TOGGLE, payload: {adding: false}});
    } else if (layer == 'edit') {
      this.props.dispatch({type: c.CATEGORY_EDIT_FORM_TOGGLE, payload: {editing: false}});
    }
  }

  render() {
    const { addingCategory: adding, editingCategory: editing,  errorCategory: errors, busy } = this.props.category;
    const { category, categories, searchText, initializing } = this.state;

    if (initializing) {
      return (
        <Box pad={{vertical: 'large'}}>
          <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
            <Spinning /> Initializing Application ...
          </Box>
        </Box>
      );
    }

    const count = initializing ? 100 : categories.length;
    const busyIcon = busy ? <Spinning /> : null;

    let items = categories.map((c,index) => {
      return (
        <ListItem key={index} justify="between" pad={{vertical:'none',horizontal:'small'}} >
          <span> {c.name} </span>
            <span className="secondary">
              <Button icon={<Edit />} onClick={this._onEditClick.bind(this, index)} />
              <Button icon={<Trash />} onClick={this._onRemoveClick.bind(this, index)} />
            </span>
        </ListItem>
      );
    });

    const layerAdd = (
      <Layer hidden={!adding} onClose={this._onCloseLayer.bind(this, 'add')}  closer={true} align="center">
        <Form>
          <Header><Heading tag="h3" strong={true}>Add New Category</Heading></Header>
          <FormFields>
            <FormField label="Category Name" error={errors.name}>
              <input type="text" name="name" value={category.name == undefined ? '' : category.name} onChange={this._onChangeInput.bind(this)} />
            </FormField>
          </FormFields>
          <Footer pad={{"vertical": "medium"}} >
            <Button icon={busyIcon} label="Add" primary={true}  onClick={this._addCategory.bind(this)} />
          </Footer>
        </Form>
      </Layer>
    );

    const layerEdit = (
      <Layer hidden={!editing} onClose={this._onCloseLayer.bind(this, 'edit')}  closer={true} align="center">
        <Form>
          <Header><Heading tag="h3" strong={true}>Update Category Details</Heading></Header>
          <FormFields >
            <FormField label="Category Name" error={errors.name}>
              <input type="text" name="name" value={category.name} onChange={this._onChangeInput.bind(this)} />
            </FormField>
          </FormFields>
          <Footer pad={{"vertical": "medium"}} >
            <Button icon={busyIcon} label="Update" primary={true}  onClick={this._updateCategory.bind(this)} />
          </Footer>
        </Form>
      </Layer>
    );

    return (
      <Box>
        <AppHeader/>
        <Header size='large' pad={{ horizontal: 'medium' }}>
          <Title responsive={false}>
            <span>{this.localeData.label_category}</span>
          </Title>
          <Search inline={true} fill={true} size='medium' placeHolder='Search'
            value={searchText} onDOMChange={this._onSearch.bind(this)} />
          <Button icon={<Add />} onClick={this._onAddClick.bind(this)}/>
          <Button icon={<HelpIcon />} onClick={this._onHelpClick.bind(this)}/>
        </Header>
        <Section direction="column" pad={{vertical: 'large', horizontal:'small'}}>
          <Box size="large" alignSelf="center" >
            <List > {items} </List>
            <ListPlaceholder unfilteredTotal={count} filteredTotal={count} emptyMessage={this.localeData.category_empty_message} />
          </Box>
        </Section>
        {layerAdd}
        {layerEdit}
      </Box>
    );
  }
}

Category.contextTypes = {
  router: React.PropTypes.object.isRequired
};

let select = (store) => {
  return { category: store.category, misc: store.misc};
};

export default connect(select)(Category);
