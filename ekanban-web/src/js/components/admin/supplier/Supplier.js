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
import Heading from 'grommet/components/Heading';
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
import ViewIcon from "grommet/components/icons/base/View";
import List from 'grommet/components/List';
import ListItem from 'grommet/components/ListItem';
import Layer from 'grommet/components/Layer';

class Supplier extends Component {

  constructor () {
    super();
    this.state = {
      initializing: false,
      page: 1,
      suppliers: [],
      supplier: {},
      searchText: '',
      filterActive: false,
      filteredCount: 0,
      unfilteredCount: 0,
      viewing: false
    };
    this.localeData = localeData();
    this._loadSupplier = this._loadSupplier.bind(this);
    this._supplierSort = this._supplierSort.bind(this);
    this._onMore = this._onMore.bind(this);
    this._renderLayerView = this._renderLayerView.bind(this);
  }

  componentWillMount () {
    console.log('componentWillMount');
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    } else {
      const {suppliers,filter,sort} = this.props.supplier;
      this._loadSupplier(suppliers,filter,sort,1);
    }
  }

  componentWillReceiveProps (nextProps) {
    console.log('componentWillReceiveProps');
    if ( (this.props.supplier.toggleStatus != nextProps.supplier.toggleStatus) || (!this.props.misc.initialized && nextProps.misc.initialized) ) {
      const {suppliers,filter,sort} = nextProps.supplier;
      this._loadSupplier(suppliers,filter,sort,1);
      this.setState({initializing: false});
    }
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');
    }
  }

  _loadSupplier (suppliers,filter,sort,page) {
    console.log("_loadSupplier()");
    let unfilteredCount = suppliers.length;

    if ('supplierType' in filter) {
      const supplierTypeFilter = filter.supplierType;
      suppliers = suppliers.filter(s => supplierTypeFilter.includes(s.supplierType));
    }
    suppliers = this._supplierSort(suppliers,sort);
    let filteredCount = suppliers.length;
    suppliers = suppliers.slice(0,15*page);
    this.setState({suppliers,page,filteredCount,unfilteredCount});

  }

  _supplierSort (suppliers,sort) {
    const [sortProperty,sortDirection] = sort.split(':');
    let result = suppliers.sort((a,b) => {
      if (sortProperty == 'name' && sortDirection == 'asc') {
        return (a.name.toLowerCase() < b.name.toLowerCase()) ? -1 : 1;
      } else if (sortProperty == 'name' && sortDirection == 'desc') {
        return (a.name.toLowerCase() > b.name.toLowerCase()) ? -1 : 1;
      }
    });
    return result;
  }

  _onSearch (event) {
    let {suppliers,filter,sort} = this.props.supplier;
    const value = event.target.value;
    suppliers = suppliers.filter(s => s.name.toLowerCase().includes(value.toLowerCase()) || s.products.map(p => p.toLowerCase()).find(p => p.includes(value.toLowerCase())) != undefined);
    this.setState({searchText: value});
    if (value.length == 0) {
      this._loadSupplier(suppliers,filter,sort,1);
    } else {
      this._loadSupplier(suppliers,{},sort,1);
    }
  }

  _onMore () {
    const {suppliers,filter,sort} = this.props.supplier;
    this._loadSupplier(suppliers,filter,sort,this.state.page+1);
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

  _onViewClick (index) {
    let supplier = this.state.suppliers[index];
    this.setState({supplier,viewing: true});
  }

  _onEditClick (index) {
    console.log('_onEditClick');
    const {suppliers} = this.state;
    const supplier = JSON.parse(JSON.stringify(suppliers[index]));
    this.props.dispatch({type: c.SUPPLIER_EDIT_FORM_TOGGLE, payload:{editing: true,supplier}});
    this.context.router.push('/supplier/edit');
  }

  _onHelpClick () {
    const helpUrl = window.baseUrl + "/help/supplier";
    window.open(helpUrl);
  }

  _onCloseLayer () {
    this.setState({viewing: false});
  }

  _renderLayerView () {
    const {supplier,viewing} = this.state;
    if (!viewing) return null;

    const products = supplier.products.map((p,i) => {
      return (
        <ListItem justify="end" pad={{vertical:'small',horizontal:'small'}} >
          {p}
        </ListItem>
      );
    });
    let addr;
    if (supplier.address != null) {
      const addr2 = supplier.address;
      addr = addr2.street + ', ' + ((addr2.landmark == null) ? "" : addr2.landmark + ", ") + addr2.city + ", " + addr2.state + ", " + addr2.country + ", " + addr2.zip;
    } else {
      addr = 'Not Available';
    }

    return (
      <Layer hidden={!viewing}  onClose={this._onCloseLayer.bind(this)}  closer={true} align="center">
        <Box size="large"  pad={{vertical: 'none', horizontal:'small'}}>
          <Header><Heading tag="h3" strong={true} >Supplier Details</Heading></Header>
          <List>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Supplier Name: </span>
              <span className="secondary">{supplier.name}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Contact Person: </span>
              <span className="secondary">{supplier.contactPerson}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Supplier Type: </span>
              <span className="secondary">{supplier.supplierType}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Address: </span>
              <span className="secondary"/>
            </ListItem>
            <ListItem justify="end" pad={{vertical:'small',horizontal:'small'}} >
              {addr}
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Products Supplied: </span>
              <span className="secondary"/>
            </ListItem>
            {products}
          </List>
        </Box>
        <Box pad={{vertical: 'medium', horizontal:'small'}} />
      </Layer>
    );
  }

  render() {
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
    const items = suppliers.map((s, index)=>{
      let addr;
      if (s.address != null) {
        const addr2 = s.address;
        addr = addr2.street + ', ' + ((addr2.landmark == null) ? "" : addr2.landmark + ", ") + addr2.city + ", " + addr2.state + ", " + addr2.country + ", " + addr2.zip;
      } else {
        addr = 'Not Available';
      }
      let products = '  ';
      s.products.forEach(p => products += p + ', ');
      products = products.substring(0,products.length-2).trim();


      return (
        <TableRow key={index}  >
          <td >{s.name.length > 25 ? s.name.substr(0,25) + ' ...' : s.name}</td>
          <td >{products.length > 25 ? products.substr(0,25) + ' ...' : products}</td>
          <td >{s.contactPerson != null && s.contactPerson.length > 15 ? s.contactPerson.substr(0,15) + ' ...' : s.contactPerson}</td>
          <td >{s.supplierType}</td>
          <td >{addr.length > 25 ? addr.substr(0,25) + ' ...' : addr}</td>
          <td>
            <Button icon={<ViewIcon />} onClick={this._onViewClick.bind(this,index)} />
            <Button icon={<Edit />} onClick={this._onEditClick.bind(this,index)} />
            <Button icon={<Trash />} onClick={this._onRemoveClick.bind(this,index)} />
          </td>
        </TableRow>
      );
    });
    let onMore;
    if (suppliers.length > 0 && suppliers.length < filteredCount) {
      onMore = this._onMore;
    }


    const layerFilter = filterActive ? <SupplierFilter onClose={this._onFilterDeactivate.bind(this)}/> : null;
    const layerView = this._renderLayerView();

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

        <Header fixed={true} size='large' pad={{ horizontal: 'medium' }}>
          <Title responsive={false}>
            <span>{this.localeData.label_supplier}</span>
          </Title>
          <Search inline={true} fill={true} size='medium' placeHolder='search supplier or product'
            value={searchText} onDOMChange={this._onSearch.bind(this)} />
          {addControl}
          <FilterControl filteredTotal={filteredCount}
            unfilteredTotal={unfilteredCount}
            onClick={this._onFilterActivate.bind(this)} />
          <Button icon={<HelpIcon />} onClick={this._onHelpClick.bind(this)}/>
        </Header>

        <Section direction="column" pad={{vertical: 'large', horizontal:'small'}}>
          <Box >
            <Table onMore={onMore}>
              <TableHeader labels={['Supplier','Products Supplied','Contact Person','Supplier Type','Address','ACTION']} />

              <tbody>{items}</tbody>
            </Table>
          </Box>
        </Section>
        {layerFilter}
        {layerView}
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
