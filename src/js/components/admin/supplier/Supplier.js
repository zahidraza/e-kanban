import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { localeData } from '../../../reducers/localization';
import {initialize}  from '../../../actions/misc';
import {removeSupplier}  from '../../../actions/supplier';
import {SUPPLIER_CONSTANTS as c}  from '../../../utils/constants';

import AppHeader from '../../AppHeader';
import Add from "grommet/components/icons/base/Add";
import Anchor from 'grommet/components/Anchor';
import Box from 'grommet/components/Box';
import Button from 'grommet/components/Button';
import Edit from "grommet/components/icons/base/Edit";
import FilterControl from 'grommet-addons/components/FilterControl';
import Header from 'grommet/components/Header';
import HelpIcon from 'grommet/components/icons/base/Help';
import Search from 'grommet/components/Search';
import Section from 'grommet/components/Section';
import Spinning from 'grommet/components/icons/Spinning';
import SupplierFilter from './SupplierFilter';
import Table from 'grommet/components/Table';
import TableHeader from 'grommet/components/TableHeader';
import TableRow from 'grommet/components/TableRow';
import Trash from "grommet/components/icons/base/Trash";
import Title from 'grommet/components/Title';

class Supplier extends Component {
  
  constructor () {
    super();
    this.state = {
      initializing: false,
      errors: [],
      suppliers: [],
      supplier: {},
      searchText: '',
      filterActive: false,
      filteredCount: 0,
      unfilteredCount: 0
    };
    this.localeData = localeData();
    this._loadSupplier = this._loadSupplier.bind(this);
    this._supplierSort = this._supplierSort.bind(this);
  }

  componentWillMount () {
    console.log('componentWillMount');
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    } else {
      const {suppliers,filter,sort} = this.props.supplier;
      this._loadSupplier(suppliers,filter,sort);
    }
  }

  componentWillReceiveProps (nextProps) {
    console.log('componentWillReceiveProps');
    if ( (this.props.supplier.toggleStatus != nextProps.supplier.toggleStatus) || (!this.props.misc.initialized && nextProps.misc.initialized) ) {
      const {suppliers,filter,sort} = nextProps.supplier;
      this._loadSupplier(suppliers,filter,sort);
      this.setState({initializing: false});
    }
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');
    }
  }

  _loadSupplier (suppliers,filter,sort) {
    console.log("_loadSupplier()");
    if ('supplierType' in filter) {
      const supplierTypeFilter = filter.supplierType;
      let list1 = suppliers.filter(s => supplierTypeFilter.includes(s.supplierType));
      list1 = this._supplierSort(list1,sort);
      this.setState({suppliers: list1, filteredCount: list1.length, unfilteredCount: suppliers.length});    
    } else {
      suppliers = this._supplierSort(suppliers,sort);
      this.setState({suppliers: suppliers, filteredCount: suppliers.length, unfilteredCount: suppliers.length}); 
    }
  }

  _supplierSort (suppliers,sort) {
    const [sortProperty,sortDirection] = sort.split(':');
    let result = suppliers.sort((a,b) => {
      if (sortProperty == 'name' && sortDirection == 'asc') {
        return (a.name < b.name) ? -1 : 1;
      } else if (sortProperty == 'name' && sortDirection == 'desc') {
        return (a.name > b.name) ? -1 : 1;
      }
    });
    return result;
  }

  _onSearch () {
    console.log('_onSearch');
  }

  _onFilterActivate () {
    console.log(this.props.supplier.filter);
    console.log(this.props.supplier.sort);
    this.setState({filterActive: true});
  }

  _onFilterDeactivate () {
    this.setState({filterActive: false});
  }

  _onChangeInput ( event ) {
    var supplier = this.state.supplier;
    supplier[event.target.getAttribute('name')] = event.target.value;
    this.setState({supplier: supplier});
  }

  _onAddClick () {
    console.log('_onAddClick');
    this.props.dispatch({type: c.SUPPLIER_ADD_FORM_TOGGLE,payload: {adding: true}});
  }

  _onRemoveClick (index) {
    console.log('_onRemoveClick');
    const {suppliers} = this.state;

    this.props.dispatch(removeSupplier(suppliers[index]));
  }

  _onEditClick (index) {
    console.log('_onEditClick');
    const {suppliers} = this.state;
    this.props.dispatch({type: c.SUPPLIER_EDIT_FORM_TOGGLE, payload:{editing: true,supplier: suppliers[index]}});
    this.context.router.push('/supplier/edit');
  }

  _onHelpClick () {
    const helpUrl = window.baseUrl + "/help/supplier";
    window.open(helpUrl);
  }

  render() {
    const {fetching} = this.props.supplier;
    const { suppliers, searchText, filterActive,filteredCount,unfilteredCount,initializing } = this.state;

    if (initializing) {
      return (
        <Box pad={{vertical: 'large'}}>
          <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
            <Spinning /> Initializing Application ...
          </Box>
        </Box>
      );
    }

    const loading = fetching ? (<Spinning />) : null;

    const items = suppliers.map((s, index)=>{
      let addr;
      if (s.address != null) {
        const addr2 = s.address;
        addr = addr2.street + ', ' + ((addr2.landmark == null) ? "" : addr2.landmark + ", ") + addr2.city + ", " + addr2.state + ", " + addr2.country + ", " + addr2.zip;
      } else {
        addr = 'Not Available';
      }
      return (
        <TableRow key={index}  >
          <td >{s.name}</td>
          <td >{s.contactPerson}</td>
          <td >{s.supplierType}</td>
          <td >{addr}</td>
          <td style={{textAlign: 'right', padding: 0}}>
            <Button icon={<Edit />} onClick={this._onEditClick.bind(this,index)} />
            <Button icon={<Trash />} onClick={this._onRemoveClick.bind(this,index)} />
          </td>
        </TableRow>
      );
    });

    const layerFilter = filterActive ? <SupplierFilter onClose={this._onFilterDeactivate.bind(this)}/> : null;

    /*let addControl;
    if ('read only' !== role) {
      addControl = (
        <Anchor icon={<AddIcon />} path='/supplier/add'
          a11yTitle={`Add Supplier`} />
      );
    }*/
    let addControl = (<Anchor icon={<Add />} path='/supplier/add' a11yTitle={`Add Supplier`} onClick={this._onAddClick.bind(this)}/>);
    //let editControl = (<Anchor icon={<Add />} path='/supplier/add' a11yTitle={`Add Supplier`} onClick={this._onAddClick.bind(this)}/>);


    return (
      <Box full='horizontal'>
        <AppHeader/>

        <Header size='large' pad={{ horizontal: 'medium' }}>
          <Title responsive={false}>
            <span>{this.localeData.label_supplier}</span>
          </Title>
          <Search inline={true} fill={true} size='medium' placeHolder='Search'
            value={searchText} onDOMChange={this._onSearch.bind(this)} />
          {addControl}
          <FilterControl filteredTotal={filteredCount}
            unfilteredTotal={unfilteredCount}
            onClick={this._onFilterActivate.bind(this)} />
          <Button icon={<HelpIcon />} onClick={this._onHelpClick.bind(this)}/>
        </Header>

        <Section direction="column" pad={{vertical: 'large', horizontal:'small'}}>
          <Box size="xsmall" alignSelf="center" pad={{horizontal:'medium'}}>{loading}</Box>
          <Box >
            <Table>
              <TableHeader labels={['Supplier','Contact Person','Supplier Type','Address','ACTION']} />

              <tbody>{items}</tbody>
            </Table>
          </Box>
        </Section>
        {layerFilter}
      </Box>
    );
  }
}

Supplier.contextTypes = {
  router: PropTypes.object
};

let select = (store) => {
  return { supplier: store.supplier, misc: store.misc};
};

export default connect(select)(Supplier);
