import React, { Component } from 'react';
import { connect } from 'react-redux';
import { localeData } from '../../../reducers/localization';
import {initialize}  from '../../../actions/misc';
import {addSection,removeSection,updateSection}  from '../../../actions/section';
import {SECTION_CONSTANTS as c}  from '../../../utils/constants';

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
import Search from 'grommet/components/Search';
import Section from 'grommet/components/Section';
import Spinning from 'grommet/components/icons/Spinning';
import Trash from "grommet/components/icons/base/Trash";
import Title from 'grommet/components/Title';

class Sections extends Component {

  constructor () {
    super();
    this.state = {
      initializing: false,
      page: 1,
      total: 0,
      section: {},
      sections: [],
      searchText: ''
    };
    this.localeData = localeData();
    this._loadSection = this._loadSection.bind(this);
    this._onMore = this._onMore.bind(this);
  }

  componentWillMount () {
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }else {
      this._loadSection(this.props.section.sections,1);
    }
  }

  componentWillReceiveProps (nextProps) {
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');
    }
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({initializing: false});
      his._loadSection(nextProps.section.sections,1);
    }
    if (nextProps.misc.initialized) {
      this.setState({sections: nextProps.section.sections});
      this._loadSection(nextProps.section.sections,1);
    }
  }

  _loadSection (sections,page) {
    const total = sections.length;
    sections = sections.sort((a,b) => a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1);
    sections = sections.slice(0,15*page);
    this.setState({sections,page,page,total});
  }

  _addSection () {
    this.props.dispatch(addSection(this.state.section));
  }

  _updateSection () {
    this.props.dispatch(updateSection(this.state.section));
  }

  _onSearch (event) {
    const value = event.target.value;
    const sections = this.props.section.sections.filter(s => s.name.toLowerCase().includes(value.toLowerCase()));
    this.setState({searchText: value});
    this._loadSection(sections,1);
  }

  _onMore () {
    this._loadSection(this.props.section.sections,this.state.page+1);
  }

  _onChangeInput ( event ) {
    var section = this.state.section;
    section[event.target.getAttribute('name')] = event.target.value;
    this.setState({section: section});
  }

  _onAddClick () {
    this.setState({section: {}});
    this.props.dispatch({type: c.SECTION_ADD_FORM_TOGGLE, payload: {adding: true}});
  }

  _onRemoveClick (index) {
    this.props.dispatch(removeSection(this.state.sections[index]));
    this.setState({searchText: ''});
  }

  _onEditClick (index) {
    this.setState({section: {...this.state.sections[index]}});
    this.props.dispatch({type: c.SECTION_EDIT_FORM_TOGGLE, payload: {editing: true}});
  }
  _onHelpClick () {
    const helpUrl = window.baseUrl + "/help/section";
    window.open(helpUrl);
  }

  _onCloseLayer (layer) {
    if ( layer == 'add') {
      this.props.dispatch({type: c.SECTION_ADD_FORM_TOGGLE, payload: {adding: false}});
    } else if (layer == 'edit') {
      this.props.dispatch({type: c.SECTION_EDIT_FORM_TOGGLE, payload: {editing: false}});
    }
  }

  render() {
    const { busy, adding, editing, errors } = this.props.section;
    const { section, sections, searchText,initializing,total } = this.state;

    if (initializing) {
      return (
        <Box pad={{vertical: 'large'}}>
          <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
            <Spinning /> Initializing Application ...
          </Box>
        </Box>
      );
    }

    const busyIcon = busy ? (<Spinning />) : null;
    const count = initializing ? 100 : sections.length;

    let items = sections.map((s,index) => {
      return (
        <ListItem key={index} justify="between" pad={{vertical:'none',horizontal:'small'}} >
          <span> {s.name} </span>
            <span className="secondary">
              <Button icon={<Edit />} onClick={this._onEditClick.bind(this, index)} />
              <Button icon={<Trash />} onClick={this._onRemoveClick.bind(this, index)} />
            </span>
        </ListItem>
      );
    });

    let onMore;
    if (sections.length > 0 && sections.length < total) {
      onMore = this._onMore;
    }


    const layerAdd = (
      <Layer hidden={!adding} onClose={this._onCloseLayer.bind(this, 'add')}  closer={true} align="center">
        <Form>
          <Header><Heading tag="h3" strong={true}>Add New Section</Heading></Header>
          <FormFields>
            <FormField label="Section Name" error={errors.name}>
              <input type="text" name="name" value={section.name == undefined ? '' : section.name} onChange={this._onChangeInput.bind(this)} />
            </FormField>
          </FormFields>
          <Footer pad={{"vertical": "medium"}} >
            <Button icon={busyIcon} label="Add" primary={true}  onClick={this._addSection.bind(this)} />
          </Footer>
        </Form>
      </Layer>
    );

    const layerEdit = (
      <Layer hidden={!editing} onClose={this._onCloseLayer.bind(this, 'edit')}  closer={true} align="center">
        <Form>
          <Header><Heading tag="h3" strong={true}>Update Section Details</Heading></Header>
          <FormFields >
            <FormField label="Section Name" error={errors.name}>
              <input type="text" name="name" value={section.name} onChange={this._onChangeInput.bind(this)} />
            </FormField>
          </FormFields>
          <Footer pad={{"vertical": "medium"}} >
            <Button icon={busyIcon} label="Update" primary={true}  onClick={this._updateSection.bind(this)} />
          </Footer>
        </Form>
      </Layer>
    );

    return (
      <Box>
        <AppHeader/>
        <Header fixed={true} size='large' pad={{ horizontal: 'medium' }}>
          <Title responsive={false}>
            <span>{this.localeData.label_section}</span>
          </Title>
          <Search inline={true} fill={true} size='medium' placeHolder='Search'
            value={searchText} onDOMChange={this._onSearch.bind(this)} />
          <Button icon={<Add />} onClick={this._onAddClick.bind(this)}/>
          <Button icon={<HelpIcon />} onClick={this._onHelpClick.bind(this)}/>
        </Header>
        <Section direction="column" pad={{vertical: 'large', horizontal:'small'}}>
          <Box size="large" alignSelf="center" >
            <List onMore={onMore}> {items} </List>
            <ListPlaceholder unfilteredTotal={count} filteredTotal={count} emptyMessage={this.localeData.section_empty_message} />
          </Box>
        </Section>
        {layerAdd}
        {layerEdit}
      </Box>
    );
  }
}

Sections.contextTypes = {
  router: React.PropTypes.object.isRequired
};

let select = (store) => {
  return { section: store.section, misc: store.misc};
};

export default connect(select)(Sections);
