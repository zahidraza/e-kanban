import React, { Component } from 'react';
import { localeData } from '../../reducers/localization';
import { connect } from 'react-redux';
import {initialize} from '../../actions/misc';
import {generateOrder,syncOrder} from '../../actions/order';
import {syncInventory} from '../../actions/inventory';
import {getNoOfBins} from '../../utils/miscUtil';
import {ORDER_CONSTANTS as c} from '../../utils/constants';
import moment from 'moment';


import AppHeader from '../AppHeader';
import Box from 'grommet/components/Box';
import Button from 'grommet/components/Button';
import Section from 'grommet/components/Section';
import Select from 'grommet/components/Select';
import Spinning from 'grommet/components/icons/Spinning';
import Tab from 'grommet/components/Tab';
import Tabs from 'grommet/components/Tabs';
import Table from 'grommet/components/Table';
import TableHeader from 'grommet/components/TableHeader';
import TableRow from 'grommet/components/TableRow';
import Header from 'grommet/components/Header';
import Heading from 'grommet/components/Heading';
import HelpIcon from 'grommet/components/icons/base/Help';
import Search from 'grommet/components/Search';
import Title from 'grommet/components/Title';
import SyncIcon from 'grommet/components/icons/base/Sync';
import Layer from 'grommet/components/Layer';
import Form from 'grommet/components/Form';
import FormField from 'grommet/components/FormField';
import FormFields from 'grommet/components/FormFields';
import Footer from 'grommet/components/Footer';
import Sidebar from 'grommet/components/Sidebar';
import FilterControl from 'grommet-addons/components/FilterControl';
import CloseIcon from 'grommet/components/icons/base/Close';
import CartIcon from 'grommet/components/icons/base/Cart';
import ViewIcon from "grommet/components/icons/base/View";
import List from 'grommet/components/List';
import ListItem from 'grommet/components/ListItem';

class Tracking extends Component {

  constructor () {
    super();
    this.state = {
      initializing: false,
      searching: false,
      viewing: false,
      searchText: '',
      activeTab: 0,
      page1: 1, //Page Pending
      page2: 1, //Page Ordered
      page3: 1, //Page Delayed
      inventory: [],
      noOfBins: '',
      errors: {},
      supplier: '',  //selected supplier for order
      inv: {}, // selected inventory/order for viewing
      orderProduct: {},    //Product for Ordering
      filter: {},
      filterActive: false,
      unfilteredCount: 0,
      filteredCount: 0,
      supplierFilterItems: [],
      classFilterItems: [],
      categoryFilterItems: []
    };
    this.localeData = localeData();
    this._loadInventory = this._loadInventory.bind(this);
    this._loadFilterItems = this._loadFilterItems.bind(this);
    this._getPendingInv = this._getPendingInv.bind(this);
    this._getOrderedInv = this._getOrderedInv.bind(this);
    this._getDelayedInv = this._getDelayedInv.bind(this);
    this._onMore = this._onMore.bind(this);

    this._renderPending = this._renderPending.bind(this);
    this._renderOrdered = this._renderOrdered.bind(this);
    this._renderDelayed = this._renderDelayed.bind(this);
    this._renderOrderLayer = this._renderOrderLayer.bind(this);
    this._renderViewLayer = this._renderViewLayer.bind(this);
    this._renderFilter = this._renderFilter.bind(this);
  }

  componentWillMount () {
    console.log('componentWillMount');
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }else{
      this._loadFilterItems(this.props);
      const {page1,page2,page3,filter} = this.state;
      const pendingInv = this._getPendingInv(this.props);
      const orderedInv = this._getOrderedInv(this.props);
      const delayedInv = this._getDelayedInv(this.props);
      this._loadInventory(pendingInv, orderedInv, delayedInv, filter, page1, page2, page3,0);
    }
  }

  componentWillReceiveProps (nextProps) {
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');
    }
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({initializing: false});
      this._loadFilterItems(nextProps);
      const {page1,page2,page3,filter} = this.state;
      const pendingInv = this._getPendingInv(nextProps);
      const orderedInv = this._getOrderedInv(nextProps);
      const delayedInv = this._getDelayedInv(nextProps);
      this._loadInventory(pendingInv, orderedInv, delayedInv, filter, page1, page2, page3,0);
    }
    if (this.props.inventory.toggleStatus != nextProps.inventory.toggleStatus) {
      const {page1,page2,page3,filter} = this.state;
      const pendingInv = this._getPendingInv(nextProps);
      const orderedInv = this._getOrderedInv(nextProps);
      const delayedInv = this._getDelayedInv(nextProps);
      this._loadInventory(pendingInv, orderedInv, delayedInv, filter, page1, page2, page3,0);
    }
  }

  _loadFilterItems (props) {
    const {category: {categories}} = props;
    let classFilterItems = [
      {label: 'All', value: undefined},
      {label: 'CLASS_A', value: 'CLASS_A'},
      {label: 'CLASS_B', value: 'CLASS_B'},
      {label: 'CLASS_C', value: 'CLASS_C'}
    ];
    let categoryFilterItems = [ {label: 'All', value: undefined}];
    categories.forEach(c => categoryFilterItems.push({label: c.name, value: c.name}));
    this.setState({classFilterItems, categoryFilterItems}); 
  }

  _getPendingInv (props) {
    const {inventory: {pendingMap}, category: {products}} = props;
    let pendingInv = [];
    for (var [key, value] of pendingMap) {
      const p = products.find(prod => prod.productId == key);
      const noOfBins = value.length;
      const ageing = moment(new Date().getTime()).diff(value[0].createdAt, 'days');
      let bins;
      value.forEach(inv => {
        if (bins == undefined) bins = String(inv.binNo) + ',';
        else bins = bins + String(inv.binNo) + ',';
      });
      bins = bins.substr(0,bins.length-1);
      if (p != undefined) {
        pendingInv.push({productId: key, prodId: p.id, itemCode: p.itemCode, productName: p.name, suppliers: p.supplierList, binSize: p.binQty + ' ' + p.uomPurchase, 
          bins, noOfBins,binQty: p.binQty, uom: p.uomPurchase, createdAt: value[noOfBins-1].createdAt, ageing, classType: p.classType, category: p.category.name});
      }
    }
    return pendingInv;
  }

  _getOrderedInv (props) {
    let {category: {products}, supplier: {suppliers}, inventory: {orders}, user: {users}} = props;
    let orderedInv = [];
    orders = orders.filter(o => o.orderState == 'ORDERED');
    orders.forEach(o => {
      const p = products.find(prod => prod.id == o.productId);
      const s = suppliers.find(s => s.id == o.supplierId);
      const supplierName = (s == undefined) ? 'Blank' : s.name;
      const u = users.find(user => user.id == o.orderedBy);
      let orderedBy = (u == undefined) ? '' : u.name;

      if (p != undefined) {
        orderedInv.push({productId: p.productId, itemCode: p.itemCode, productName: p.name, supplierName, binSize: p.binQty + ' ' + p.uomPurchase,
          bins: o.bins, noOfBins: getNoOfBins(o.bins), orderedAt: o.orderedAt,orderedBy,tat: p.timeProduction + p.timeTransportation, 
          classType: p.classType, category: p.category.name,binQty: p.binQty, uom: p.uomPurchase});
      }
    });
    return orderedInv;
  }

  _getDelayedInv (props) {
    let {category: {products}, supplier: {suppliers}, inventory: {orders}, user: {users}} = props;
    let delayedInv = [];
    let todayMillis = new Date().getTime();
    let oneDayMillis = 24*60*60*1000;
    orders.forEach(o => {
      const p = products.find(prod => prod.id == o.productId);
      const s = suppliers.find(s => s.id == o.supplierId);
      const supplierName = (s == undefined) ? 'Blank' : s.name;
      const u = users.find(user => user.id == o.orderedBy);
      let orderedBy = (u == undefined) ? '' : u.name;
      let delay = moment(todayMillis).diff(o.orderedAt + (p.timeProduction + p.timeTransportation)*oneDayMillis, 'days');
      if (p != undefined && delay > 0) {
        delayedInv.push({productId: p.productId, itemCode: p.itemCode, productName: p.name, supplierName, binSize: p.binQty + ' ' + p.uomPurchase,
          bins: o.bins, noOfBins: getNoOfBins(o.bins), orderedAt: o.orderedAt,orderedBy, tat: p.timeProduction + p.timeTransportation, delay, classType: p.classType, 
          category: p.category.name,binQty: p.binQty, uom: p.uomPurchase});
      }
    });
    delayedInv.sort((inv1,inv2) => inv2.delay - inv1.delay);
    return delayedInv;
  }

  _loadInventory (pendingInv,orderedInv,delayedInv,filter,page1, page2, page3,activeTab) {
    let unfilteredCount;
    let filteredCount;
    if (activeTab == 0) {
      unfilteredCount = pendingInv.length;
      if ('classType' in filter) {
        const classTypeFilter = filter.classType;
        pendingInv = pendingInv.filter(o => classTypeFilter.includes(o.classType));
      }
      if ('category' in filter) {
        const categoryFilter = filter.category;
        pendingInv = pendingInv.filter(o => categoryFilter.includes(o.category));
      }
      pendingInv.sort((o1,o2) => {
        return o2.ageing - o1.ageing;
      });
      filteredCount = pendingInv.length;
    } else if (activeTab == 1) {
      unfilteredCount = orderedInv.length;
      if ('supplier' in filter) {
        const supplierFilter = filter.supplier;
        orderedInv = orderedInv.filter(o => supplierFilter.includes(o.supplierName));
      }
      if ('classType' in filter) {
        const classTypeFilter = filter.classType;
        orderedInv = orderedInv.filter(o => classTypeFilter.includes(o.classType));
      }
      if ('category' in filter) {
        const categoryFilter = filter.category;
        orderedInv = orderedInv.filter(o => categoryFilter.includes(o.category));
      }
      orderedInv.sort((o1,o2) => {
        return o2.orderedAt - o1.orderedAt;
      });
      filteredCount = orderedInv.length;
    } else if (activeTab == 2) {
      unfilteredCount = delayedInv.length;
      if ('supplier' in filter) {
        const supplierFilter = filter.supplier;
        delayedInv = delayedInv.filter(o => supplierFilter.includes(o.supplierName));
      }
      if ('classType' in filter) {
        const classTypeFilter = filter.classType;
        delayedInv = delayedInv.filter(o => classTypeFilter.includes(o.classType));
      }
      if ('category' in filter) {
        const categoryFilter = filter.category;
        delayedInv = delayedInv.filter(o => categoryFilter.includes(o.category));
      }
      delayedInv.sort((o1,o2) => {
        return o2.delay - o1.delay;
      });
      filteredCount = delayedInv.length;
    }
    pendingInv = pendingInv.slice(0, 15*page1);
    orderedInv = orderedInv.slice(0, 15*page2);
    delayedInv = delayedInv.slice(0, 15*page3);
    this.setState({pendingInv, orderedInv, delayedInv, page1, page2, page3, activeTab, filteredCount,unfilteredCount});
  }

  _onMore () {
    let {filter,page1,page2,page3,activeTab} = this.state;
    if (activeTab == 0) page1++;
    else if (activeTab == 1) page2++;
    else if (activeTab == 2) page3++;
    const pendingInv = this._getPendingInv(this.props);
    const orderedInv = this._getOrderedInv(this.props);
    const delayedInv = this._getDelayedInv(this.props);
    this._loadInventory(pendingInv, orderedInv, delayedInv, filter, page1, page2, page3,activeTab);
  }

  _order () {
    let {pendingMap} = this.props.inventory;
    const {orderProduct,noOfBins,supplier} = this.state;
    //Validate Supplier
    if (supplier == '') {
      if (orderProduct.suppliers.length != 0) {
        alert('Select Supplier');
        return;
      } else {
        alert('No supplier added for this product. consult admininstrator for adding supplier.');
      }
    }
    const regex = /^\d+$/;
    let errors = {};
    if (noOfBins.length > 0 && !regex.test(noOfBins)) {
      errors.noOfBins = 'Not a valid number.';
    } else if (noOfBins == 0) {
      errors.noOfBins = 'Number of bins should be greater than zero.';
    } else if (noOfBins > orderProduct.noOfBins) {
      errors.noOfBins = 'Number of bins cannot be greater than ' + orderProduct.noOfBins;
    }
    if ('noOfBins' in errors) {
      this.setState({errors});
      return;
    }
    const sup = orderProduct.suppliers.find(s => s.name == supplier);
    const supId = sup == undefined ? null : sup.id;
    let order = {productId: orderProduct.prodId,binQty: orderProduct.binQty, prodId: orderProduct.productId, orderedBy: sessionStorage.userId, supplierId: supId};
    if (noOfBins == orderProduct.noOfBins) {
      order.bins = orderProduct.bins;
      pendingMap.delete(orderProduct.productId);
    }else {
      let bins = orderProduct.bins.substr(0,2*noOfBins);
      bins = bins.substr(0,2*noOfBins-1);
      order.bins = bins;
      let value = pendingMap.get(orderProduct.productId);
      for (let i = 0; i < noOfBins; i++) value.shift();
      pendingMap.set(orderProduct,value);
    }
    this.props.dispatch(generateOrder(order));
  }

  _onOrderClick (index) {
    const {pendingInv} = this.state;
    const inv = pendingInv[index];
    console.log(inv);
    this.setState({orderProduct: {...inv}});
    this.props.dispatch({type: c.ORDER_ADD_FORM_TOGGLE, payload: {ordering: true}});
  }

  _onSearch (event) {
    let value = event.target.value;
    let pendingInv = this._getPendingInv(this.props);
    let orderedInv = this._getOrderedInv(this.props);
    let delayedInv = this._getDelayedInv(this.props);
    const activeTab = this.state.activeTab;
    if (activeTab == 0) {
      pendingInv = pendingInv.filter(i => i.productName.toLowerCase().includes(value.toLowerCase()) || i.itemCode.toLowerCase().includes(value.toLowerCase()));
    } else if (activeTab == 1) {
      orderedInv = orderedInv.filter(i => i.productName.toLowerCase().includes(value.toLowerCase()) || i.itemCode.toLowerCase().includes(value.toLowerCase()));
    } else if (activeTab == 2) {
      delayedInv = delayedInv.filter(i => i.productName.toLowerCase().includes(value.toLowerCase()) || i.itemCode.toLowerCase().includes(value.toLowerCase()));
    }
    this.setState({searchText: value});
    if (value.length == 0) {
      this._loadInventory(pendingInv, orderedInv, delayedInv, this.state.filter, 1, 1, 1, activeTab);
    }else {
      this._loadInventory(pendingInv, orderedInv, delayedInv, {}, 1, 1, 1, activeTab);
    }
  }

  _onSupplierFilter (event) {
    let supplier = event.value;
    this.setState({supplier});
  }

  _onSyncClick () {
    this.props.dispatch(syncInventory());
    this.props.dispatch(syncOrder());
  }

  _onHelpClick () {
    console.log('_onHelpClick');
  }

  _onViewClick (index) {
    const {pendingInv, orderedInv, delayedInv, activeTab} = this.state;
    let inv;
    if (activeTab == 0) {
      inv = {...pendingInv[index]};
    } else if (activeTab == 1) {
      inv = {...orderedInv[index]};
    } else if (activeTab == 2) {
      inv = {...delayedInv[index]};
    }
    this.setState({viewing: true, inv});
  }

  _onChangeInput (event) {
    this.setState({noOfBins: event.target.value, errors: {}});
  }

  _onChangeFilter (name,event) {
    let {page1,page2,page3,filter,activeTab} = this.state;

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
    this.setState({filter});

    const pendingInv = this._getPendingInv(this.props);
    const orderedInv = this._getOrderedInv(this.props);
    const delayedInv = this._getDelayedInv(this.props);
    this._loadInventory(pendingInv, orderedInv, delayedInv, filter, page1, page2, page3, activeTab);
  }

  _onFilterOpen () {
    this.setState({filterActive: true});
  }

  _onCloseFilter () {
    this.setState({filterActive: false});
  }

  _onCloseLayer (layer) {
    if (layer == 'view') {
      this.setState({viewing: false});
    } else if (layer == 'order') {
      this.setState({orderProduct: {}});
      this.props.dispatch({type: c.ORDER_ADD_FORM_TOGGLE, payload: {ordering: false}});
    }
  }

  _onTabChange(index) {
    const {page1,page2,page3} = this.state;
    const pendingInv = this._getPendingInv(this.props);
    const orderedInv = this._getOrderedInv(this.props);
    const delayedInv = this._getDelayedInv(this.props);
    //load supplier filter items here
    let supplierFilterItems = [{label: 'All', value: undefined}];
    let supplierSet = new Set();
    if (index == 1) {
      orderedInv.forEach(inv => supplierSet.add(inv.supplierName));
    } else if (index == 2) {
      delayedInv.forEach(inv => supplierSet.add(inv.supplierName));
    }
    supplierSet.forEach(s => supplierFilterItems.push({label: s, value: s}));
    this._loadInventory(pendingInv, orderedInv, delayedInv, {}, page1, page2, page3,index);
    this.setState({supplierFilterItems, filter: {}, searchText: ''});
  }

  _renderFilter () {
    const {filter,filterActive, activeTab} = this.state;

    if (!filterActive) return null;

    let supFilter;
    if (activeTab != 0) {
      supFilter = (
        <Section pad={{ horizontal: 'large', vertical: 'small' }}>
          <Heading tag='h3'>Supplier</Heading>
          <Select inline={true} multiple={true} options={this.state.supplierFilterItems} value={filter.supplier} onChange={this._onChangeFilter.bind(this,'supplier')} />
        </Section>
      );
    } 
    return (
      <Layer align='right' flush={true} closer={false} a11yTitle='order Filter'>
        <Sidebar size='large'>
          <div>
            <Header size='large' justify='between' align='center'
              pad={{ horizontal: 'medium', vertical: 'medium' }}>
              <Heading tag='h2' margin='none'>Filter</Heading>
              <Button icon={<CloseIcon />} plain={true}
                onClick={this._onCloseFilter.bind(this)} />
            </Header>
            {supFilter}
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h3'>Class</Heading>
              <Select inline={true} multiple={true} options={this.state.classFilterItems} value={filter.classType} onChange={this._onChangeFilter.bind(this,'classType')} />
            </Section>
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h3'>Category</Heading>
              <Select inline={true} multiple={true} options={this.state.categoryFilterItems} value={filter.category} onChange={this._onChangeFilter.bind(this,'category')} />
            </Section>
          </div>
        </Sidebar>
      </Layer>
    );
  }

  _renderOrderLayer () {
    let {orderProduct, supplier} = this.state;
    let {busy, ordering} = this.props.inventory;

    if (!ordering) return null;

    const busyIcon = busy ? <Spinning /> : null;
    if (supplier == '') {
      supplier = orderProduct.suppliers.length == 0 ? 'No Supplier Exist' : 'Select Supplier';
    }
    const suppliers = orderProduct.suppliers.map(s => s.name);
    return (
      <Layer hidden={!ordering} onClose={this._onCloseLayer.bind(this, 'order')}  closer={true} align="center">
        <Form>
          <Header><Heading tag="h3" strong={true}>Create Order</Heading></Header>
          <FormFields>
            <FormField label="Supplier" htmlFor="sType" >
              <Select id="supplier" name="supplier" options={suppliers}
                value={supplier}  onChange={this._onSupplierFilter.bind(this)} />
            </FormField>
            <FormField label="No of Bins" error={this.state.errors.noOfBins}>
              <input type="text" name="noOfBins" value={this.state.noOfBins} placeholder="Enter no. of bins to order" onChange={this._onChangeInput.bind(this)} />
            </FormField>
          </FormFields>
          <Footer pad={{"vertical": "medium"}} >
            <Button icon={busyIcon} label="Order" primary={true}  onClick={this._order.bind(this)} />
          </Footer>
        </Form>
      </Layer>
    );
  }

  _renderViewLayer () {
    const {inv,viewing,activeTab} = this.state;
    if (!viewing) return null;

    let ageing,orderedBy,orderedAt,expArrival,delayedBy,orderedQty;
    if (activeTab == 0) {
      ageing = (
        <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
          <span> Ageing: </span>
          <span className="secondary">{inv.ageing + ' day(s)'}</span>
        </ListItem>
      );
    }else if (activeTab == 1 || activeTab == 2) {
      orderedBy = (
        <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
          <span> OrderedBy: </span>
          <span className="secondary">{inv.orderedBy}</span>
        </ListItem>
      );
      orderedQty = (
        <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
          <span> Order Quantity: </span>
          <span className="secondary">{inv.noOfBins*inv.binQty + " " + inv.uom}</span>
        </ListItem>
      );
      orderedAt = (
        <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
          <span> Ordered On: </span>
          <span className="secondary">{moment(new Date(inv.orderedAt)).utcOffset('+05:30').format('DD MMM, YY hh:mm A')}</span>
        </ListItem>
      );
      expArrival = (
        <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
          <span> Exp. Arrival: </span>
          <span className="secondary">{moment(new Date(inv.orderedAt)).add(inv.tat,'days').utcOffset('+05:30').format('DD MMM, YY')}</span>
        </ListItem>
      );
    }
    if (activeTab == 2) {
      delayedBy = (
        <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
          <span> Delayed by: </span>
          <span className="secondary">{inv.delay + ' day(s)'}</span>
        </ListItem>
      );
    }

    return (
      <Layer hidden={!viewing}  onClose={this._onCloseLayer.bind(this, 'view')}  closer={true} align="center">
        <Box size="large"  pad={{vertical: 'none', horizontal:'small'}}>
          <Header><Heading tag="h3" strong={true} >Order Details</Heading></Header>
          <List>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Product Name: </span>
              <span className="secondary">{inv.productName}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Item Code: </span>
              <span className="secondary">{inv.itemCode}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Category: </span>
              <span className="secondary">{inv.category}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Class: </span>
              <span className="secondary">{inv.classType}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> No of Bins: </span>
              <span className="secondary">{inv.noOfBins}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Cards: </span>
              <span className="secondary">{inv.bins}</span>
            </ListItem>
            {ageing}
            {orderedQty}
            {orderedBy}
            {orderedAt}
            {expArrival}
            {delayedBy}
          </List>
        </Box>
        <Box pad={{vertical: 'medium', horizontal:'small'}} />
      </Layer>
    );
  }

  _renderPending () {
    let {pendingInv, filteredCount} = this.state;
    if (pendingInv.length > 0) {
      let onMore = (pendingInv.length < filteredCount) ? this._onMore : null;
      let items = pendingInv.map((inv,i) => {
        return (
          <TableRow key={i}  >
            <td>{inv.itemCode}</td>
            <td>{inv.productName.length > 25 ? inv.productName.substr(0,25) + ' ...': inv.productName}</td>
            <td>{inv.binSize}</td>
            <td>{inv.noOfBins}</td>
            <td>{moment(new Date(inv.createdAt)).utcOffset('+05:30').format('DD/MM/YYYY, hh:mm A')}</td>
            <td>{inv.ageing}</td>
            <td>
              <Button icon={<ViewIcon />} onClick={this._onViewClick.bind(this,i)} />
              <Button icon={<CartIcon />} onClick={this._onOrderClick.bind(this,i)} />
            </td>
          </TableRow>
        );
      });
      return (
        <Box pad={{horizontal: 'medium', vertical: 'medium'}}>
          <Table onMore={onMore}>
            <TableHeader labels={['Item Code','Product','Bin Size','Bins','Created At','Ageing','ACTION']} />

            <tbody>{items}</tbody>
          </Table>
        </Box>
      );
    } else {
      return (
        <Box size="medium" alignSelf="center" pad={{horizontal:'medium'}}>
          <h3>No Pending Orders available</h3>
        </Box>
      );
    }
  }

  _renderOrdered () {
    let {orderedInv, filteredCount} = this.state;
    if (orderedInv.length > 0) {
      let onMore = (orderedInv.length < filteredCount) ? this._onMore : null;
      let items = orderedInv.map((inv,i) => {
        return (
          <TableRow key={i}  >
            <td>{inv.itemCode}</td>
            <td>{inv.productName.length > 25 ? inv.productName.substr(0,25) + ' ...': inv.productName}</td>
            <td>{inv.supplierName.length > 25 ? inv.supplierName.substr(0,25) + ' ...': inv.supplierName}</td>
            <td>{inv.binSize}</td>
            <td>{inv.noOfBins}</td>
            <td>{inv.orderedBy}</td>
            <td>{moment(new Date(inv.orderedAt)).utcOffset('+05:30').format('DD MMM, YY hh:mm A')}</td>
            <td>{moment(new Date(inv.orderedAt)).add(inv.tat,'days').utcOffset('+05:30').format('DD MMM, YY')}</td>
            <td><Button icon={<ViewIcon />} onClick={this._onViewClick.bind(this,i)} /></td>
          </TableRow>
        );
      });
      return (
        <Box pad={{horizontal: 'medium', vertical: 'medium'}}>
          <Table onMore={onMore}>
            <TableHeader labels={['Item Code','Product','Supplier','Bin Size','Bins','Ordered By','Ordered At','Exp. Arrival','View']} />

            <tbody>{items}</tbody>
          </Table>
        </Box>
      );
    } else {
      return (
        <Box size="medium" alignSelf="center" pad={{horizontal:'medium'}}>
          <h3>No Ordered Orders available</h3>
        </Box>
      );
    }
  }

  _renderDelayed () {
    let {delayedInv, filteredCount} = this.state;
    if (delayedInv.length > 0) {
      let onMore = (delayedInv.length < filteredCount) ? this._onMore : null;
      let items = delayedInv.map((inv,i) => {
        return (
          <TableRow key={i}  >
            <td>{inv.itemCode}</td>
            <td>{inv.productName.length > 25 ? inv.productName.substr(0,25) + ' ...': inv.productName}</td>
            <td>{inv.supplierName.length > 25 ? inv.supplierName.substr(0,25) + ' ...': inv.supplierName}</td>
            <td>{inv.binSize}</td>
            <td>{inv.noOfBins}</td>
            <td>{inv.orderedBy}</td>
            <td>{moment(new Date(inv.orderedAt)).utcOffset('+05:30').format('DD MMM, YY hh:mm A')}</td>
            <td>{moment(new Date(inv.orderedAt)).add(inv.tat,'days').utcOffset('+05:30').format('DD MMM, YY')}</td>
            <td><Button icon={<ViewIcon />} onClick={this._onViewClick.bind(this,i)} /></td>
          </TableRow>
        );
      });
      return (
        <Box pad={{horizontal: 'medium', vertical: 'medium'}}>
          <Table onMore={onMore}>
            <TableHeader labels={['Item Code','Product','Supplier','Bin Size','Bins','Ordered By','Ordered At','Exp. Arrival','View']} />

            <tbody>{items}</tbody>
          </Table>
        </Box>
      );
    } else {
      return (
        <Box size="medium" alignSelf="center" pad={{horizontal:'medium'}}>
          <h3>No Delayed Orders available</h3>
        </Box>
      );
    }
  }

  render() {
    const {initializing, searchText, activeTab, filteredCount, unfilteredCount} = this.state;

    if (initializing) {
      return (
        <Box pad={{vertical: 'large'}}>
          <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
            <Spinning /> Initializing Application ...
          </Box>
        </Box>
      );
    }

    const syncingIcon = this.props.inventory.syncing ? <Spinning /> : null;

    const pending = this._renderPending();
    const ordered = this._renderOrdered();
    const delayed = this._renderDelayed();
    const layerOrdering = this._renderOrderLayer();
    const layerView = this._renderViewLayer();
    const layerFilter = this._renderFilter();

    

    return (
      <Box>
        <AppHeader/>
        <Header size='large' pad={{ horizontal: 'medium' }}>
          <Title responsive={false}>
            <span>{this.localeData.label_tracking}</span>
          </Title>
          <Search inline={true} fill={true} size='medium' placeHolder='search product name, item code'
            value={searchText} onDOMChange={this._onSearch.bind(this)} />
          <Button icon={<SyncIcon />}  onClick={this._onSyncClick.bind(this)}/>
          <FilterControl filteredTotal={filteredCount}
            unfilteredTotal={unfilteredCount}
            onClick={this._onFilterOpen.bind(this)} />
          <Button icon={<HelpIcon />} onClick={this._onHelpClick.bind(this)}/>
        </Header>
        <Section>
          <Box pad={{horizontal: 'medium', vertical: 'medium'}}>
            <Box size="small" alignSelf="center">{syncingIcon}</Box>
            <Tabs justify="center" activeIndex={activeTab} onActive={this._onTabChange.bind(this)} >
              <Tab title="Pending Orders">
                {pending}
              </Tab>
              <Tab title="Ordered">
                {ordered}
              </Tab>
              <Tab title="Delayed Orders">
                {delayed}
              </Tab>
            </Tabs>
          </Box>
        </Section>
        {layerOrdering}
        {layerView}
        {layerFilter}
      </Box>
    );
  }
}

Tracking.contextTypes = {
  router: React.PropTypes.object
};

let select = (store) => {
  return {misc: store.misc, category: store.category, inventory: store.inventory, user: store.user, supplier: store.supplier};
};

export default connect(select)(Tracking);
