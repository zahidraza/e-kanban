import React, { Component } from 'react';
import { localeData } from '../../reducers/localization';
import { connect } from 'react-redux';
import {initialize} from '../../actions/misc';
import {syncOrder} from '../../actions/order';
import {syncInventory} from '../../actions/inventory';
import {getNoOfBins} from '../../utils/miscUtil';
import moment from 'moment';


import AppHeader from '../AppHeader';
import Box from 'grommet/components/Box';
import Button from 'grommet/components/Button';
import Section from 'grommet/components/Section';
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
import PrintIcon from 'grommet/components/icons/base/Print';
import Layer from 'grommet/components/Layer';
import Sidebar from 'grommet/components/Sidebar';
import FilterControl from 'grommet-addons/components/FilterControl';
import CloseIcon from 'grommet/components/icons/base/Close';
import ViewIcon from "grommet/components/icons/base/View";
import List from 'grommet/components/List';
import ListItem from 'grommet/components/ListItem';
import Select from 'grommet/components/Select';

class AwaitingOrder extends Component {

  constructor () {
    super();
    this.state = {
      initializing: false,
      searching: false,
      viewing: false,
      filterActive: false,
      searchText: '',
      page1: 1, //page1 Pending
      page2: 1,
      activeTab: 0,
      awaitingOrder: [],
      arrTomOrder: [],   //orders arriving tommorrow
      supplierFilterItems: [],
      categoryFilterItems: [],
      classFilterItems: [],
      unfilteredCount: 0,
      filteredCount: 0,
      filter: {},
      order: {}  //Order being viewed
    };
    this.localeData = localeData();
    this._loadOrder = this._loadOrder.bind(this);
    this._loadFilterItems = this._loadFilterItems.bind(this);
    this._getAwaitingOrder = this._getAwaitingOrder.bind(this);
    this._getArrTomOrder = this._getArrTomOrder.bind(this);
    this._renderAwaiting = this._renderAwaiting.bind(this);
    this._renderArrTom = this._renderArrTom.bind(this);
    this._renderFilter = this._renderFilter.bind(this);
    this._renderViewLayer = this._renderViewLayer.bind(this);
    this._onTabChange = this._onTabChange.bind(this);
    this._onMore = this._onMore.bind(this);
  }

  componentWillMount () {
    console.log('componentWillMount');
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }else{
      this._loadFilterItems(this.props);
      this._onTabChange(0);
    }
  }

  componentWillReceiveProps (nextProps) {
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');
    }
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({initializing: false});
      this._loadFilterItems(nextProps);
      const {page1, page2, filter, activeTab} = this.state;
      const awaitingOrder = this._getAwaitingOrder(nextProps);
      const arrTomOrder = this._getArrTomOrder(nextProps);
      this._loadOrder(awaitingOrder,arrTomOrder,filter,page1,page2,activeTab);
    }
    if (this.props.inventory.toggleStatus != nextProps.inventory.toggleStatus) {
      const {page1, page2, filter, activeTab} = this.state;
      const awaitingOrder = this._getAwaitingOrder(nextProps);
      const arrTomOrder = this._getArrTomOrder(nextProps);
      this._loadOrder(awaitingOrder,arrTomOrder,filter,page1,page2,activeTab);
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

  _getAwaitingOrder (props) {
    const {category: {products}, supplier: {suppliers}, inventory: {orders}} = props;
    let awaitingOrder = [];
    let awaitingOrders = orders.filter(o => o.orderState == 'ORDERED');
    awaitingOrders.forEach(o => {
      const p = products.find(prod => prod.id == o.productId);
      const s = suppliers.find(s => s.id == o.supplierId);
      const supplierName = (s == undefined) ? 'Blank' : s.name;
      if (p != undefined) {
        awaitingOrder.push({productId: p.productId,itemCode: p.itemCode,prodId: p.id, productName: p.name, binSize: p.binQty + ' ' + p.uomPurchase,
          bins: o.bins,binsScanned: o.binsScanned, supplierName, orderedAt: o.orderedAt, createdAt: o.createdAt, tat: p.timeProduction + p.timeTransportation,
          classType: p.classType, category: p.category.name});
      }
    });
    return awaitingOrder;
  }

  _getArrTomOrder (props) {
    const {category: {products}, supplier: {suppliers}, inventory: {orders}} = props;
    let arrTomOrder = [];
    let arrTomOrders = orders.filter(o => o.orderState == 'ORDERED');
    arrTomOrders.forEach(o => {
      const p = products.find(prod => prod.id == o.productId);
      const s = suppliers.find(s => s.id == o.supplierId);
      const supplierName = (s == undefined) ? 'Blank' : s.name;
      let diff = moment(new Date().getTime()).diff(o.orderedAt, 'days');

      if (p != undefined && (p.timeProduction + p.timeTransportation - 1) == diff) {
        arrTomOrder.push({productId: p.productId,itemCode: p.itemCode,prodId: p.id, productName: p.name, binSize: p.binQty + ' ' + p.uomPurchase,
          bins: o.bins,binsScanned: o.binsScanned, supplierName, orderedAt: o.orderedAt, createdAt: o.createdAt, tat: p.timeProduction + p.timeTransportation,
          classType: p.classType, category: p.category.name});
      }
    });
    return arrTomOrder;
  }

  _loadOrder (awaitingOrder,arrTomOrder, filter, page1, page2, activeTab) {
    let unfilteredCount;
    let filteredCount;
    if (activeTab == 0) {
      unfilteredCount = awaitingOrder.length;
      if ('supplier' in filter) {
        const supplierFilter = filter.supplier;
        awaitingOrder = awaitingOrder.filter(o => supplierFilter.includes(o.supplierName));
      }
      if ('classType' in filter) {
        const classTypeFilter = filter.classType;
        awaitingOrder = awaitingOrder.filter(o => classTypeFilter.includes(o.classType));
      }
      if ('category' in filter) {
        const categoryFilter = filter.category;
        awaitingOrder = awaitingOrder.filter(o => categoryFilter.includes(o.category));
      }
      awaitingOrder.sort((o1,o2) => {
        return moment(new Date(o1.orderedAt)).add(o1.tat,'days').toDate().getTime() - moment(new Date(o2.orderedAt)).add(o2.tat,'days').toDate().getTime();
      });
      filteredCount = awaitingOrder.length;
    } else if (activeTab == 1) {
      unfilteredCount = arrTomOrder.length;
      if ('supplier' in filter) {
        const supplierFilter = filter.supplier;
        arrTomOrder = arrTomOrder.filter(o => supplierFilter.includes(o.supplierName));
      }
      if ('classType' in filter) {
        const classTypeFilter = filter.classType;
        arrTomOrder = arrTomOrder.filter(o => classTypeFilter.includes(o.classType));
      }
      if ('category' in filter) {
        const categoryFilter = filter.category;
        arrTomOrder = arrTomOrder.filter(o => categoryFilter.includes(o.category));
      }
      // arrTomOrder.sort((o1,o2) => {
      //   return o1.orderedAt - o2.orderedAt;
      // });
      filteredCount = arrTomOrder.length;
    }
    awaitingOrder = awaitingOrder.slice(0, 15*page1);
    arrTomOrder = arrTomOrder.slice(0, 15*page2);
    this.setState({awaitingOrder, arrTomOrder, page1, page2, activeTab, filteredCount,unfilteredCount});
  }

  _onMore () {
    let {filter,page1,page2,activeTab} = this.state;
    if (activeTab == 0) page1++;
    else if (activeTab == 1) page2++;
    const awaitingOrder = this._getAwaitingOrder(this.props);
    const arrTomOrder = this._getArrTomOrder(this.props);
    this._loadOrder(awaitingOrder, arrTomOrder, filter, page1, page2, activeTab);
  }

  _onPrint (index) {
    const {awaitingOrder} = this.state;
    let inv = awaitingOrder[index];
    window.open(window.serviceHost + "/products/prints/" + inv.prodId + "?bins=" + inv.bins,"_blank","fullscreen=yes");
  }

  _onPrintAll () {
    const {arrTomOrder} = this.state;
    if (arrTomOrder.length == 0) {
      alert('No orders to print');
      return;
    }
    let productIds = "";
    arrTomOrder.forEach(o => productIds = productIds + o.prodId + ',');
    productIds = productIds.substr(0,productIds.length-1);
    window.open(window.serviceHost + "/products/prints?productIds=" + productIds,"_blank","fullscreen=yes");
  }

  _onSearch (event) {
    let value = event.target.value;
    let awaitingOrder = this._getAwaitingOrder(this.props);
    let arrTomOrder = this._getArrTomOrder(this.props);
    const activeTab = this.state.activeTab;
    if (activeTab == 0) {
      awaitingOrder = awaitingOrder.filter(i => i.productName.toLowerCase().includes(value.toLowerCase()) || i.itemCode.toLowerCase().includes(value.toLowerCase()));
    } else if (activeTab == 1) {
      arrTomOrder = arrTomOrder.filter(i => i.productName.toLowerCase().includes(value.toLowerCase()) || i.itemCode.toLowerCase().includes(value.toLowerCase()));
    }
    this.setState({searchText: value});
    if (value.length == 0) {
      this._loadOrder(awaitingOrder, arrTomOrder, this.state.filter, 1, 1, activeTab);
    }else {
      this._loadOrder(awaitingOrder, arrTomOrder, {}, 1, 1, activeTab);
    }
  }

  _onSyncClick () {
    this.props.dispatch(syncInventory());
    this.props.dispatch(syncOrder());
  }

  _onHelpClick () {
    console.log('_onHelpClick');
  }

  _onViewClick (index) {
    const {awaitingOrder, arrTomOrder, activeTab} = this.state;
    let order;
    if (activeTab == 0) {
      order = {...awaitingOrder[index]};
    } else if (activeTab == 1) {
      order = {...arrTomOrder[index]};
    }
    this.setState({viewing: true, order});
  }

  _onChangeFilter (name,event) {
    let {page1,page2,filter,activeTab} = this.state;

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
    
    const awaitingOrder = this._getAwaitingOrder(this.props);
    const arrTomOrder = this._getArrTomOrder(this.props);
    this._loadOrder(awaitingOrder,arrTomOrder,filter,page1,page2,activeTab);
    this.setState({filter});
  }

  _onFilterOpen () {
    this.setState({filterActive: true});
  }

  _onCloseFilter () {
    this.setState({filterActive: false});
  }

  _onCloseLayer () {
    this.setState({viewing: false});
  }

  _onTabChange(index) {
    const {page1,page2} = this.state;
    const awaitingOrder = this._getAwaitingOrder(this.props);
    const arrTomOrder = this._getArrTomOrder(this.props);
    //load supplier filter items here
    let supplierFilterItems = [{label: 'All', value: undefined}];
    let supplierSet = new Set();
    if (index == 0) {
      awaitingOrder.forEach(inv => supplierSet.add(inv.supplierName));
    } else if (index == 1) {
      arrTomOrder.forEach(inv => supplierSet.add(inv.supplierName));
    }
    supplierSet.forEach(s => supplierFilterItems.push({label: s, value: s}));
    this._loadOrder(awaitingOrder, arrTomOrder, {}, page1, page2,index);
    this.setState({supplierFilterItems, filter: {}, searchText: ''});
  }

  _renderFilter () {
    const {filter,filterActive} = this.state;

    if (!filterActive) return null;

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
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h3'>Supplier</Heading>
              <Select inline={true} multiple={true} options={this.state.supplierFilterItems} value={filter.supplier} onChange={this._onChangeFilter.bind(this,'supplier')} />
            </Section>
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

  _renderViewLayer () {
    const {order,viewing} = this.state;
    if (!viewing) return null;
    return (
      <Layer hidden={!viewing}  onClose={this._onCloseLayer.bind(this)}  closer={true} align="center">
        <Box size="large"  pad={{vertical: 'none', horizontal:'small'}}>
          <Header><Heading tag="h3" strong={true} >Order Details</Heading></Header>
          <List>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Product Name: </span>
              <span className="secondary">{order.productName}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Item Code: </span>
              <span className="secondary">{order.itemCode}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Category: </span>
              <span className="secondary">{order.category}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Class: </span>
              <span className="secondary">{order.classType}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> No of Bins: </span>
              <span className="secondary">{getNoOfBins(order.bins)}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Cards: </span>
              <span className="secondary">{order.bins}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> No of Bins Scanned: </span>
              <span className="secondary">{order.binsScanned == null ? 0 : getNoOfBins(order.binsScanned)}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Cards Scanned: </span>
              <span className="secondary">{order.binsScanned == null ? '-' : order.binsScanned}</span>
            </ListItem>
          </List>
        </Box>
        <Box pad={{vertical: 'medium', horizontal:'small'}} />
      </Layer>
    );
  }

  _renderAwaiting () {
    let {awaitingOrder, filteredCount} = this.state;
    if (awaitingOrder.length > 0) {
      let onMore = awaitingOrder.length < filteredCount ? this._onMore : null;

      let items = awaitingOrder.map((inv,i) => {
        let binsScanned = (inv.binsScanned == null) ? 0 : getNoOfBins(inv.binsScanned);
        return (
          <TableRow key={i}  >
            <td>{inv.itemCode}</td>
            <td>{inv.productName.length > 25 ? inv.productName.substr(0,25) + ' ...': inv.productName}</td>
            <td>{inv.supplierName.length > 25 ? inv.supplierName.substr(0,25) + ' ...': inv.supplierName}</td>
            <td>{inv.binSize}</td>
            <td>{getNoOfBins(inv.bins)}</td>
            <td>{binsScanned}</td>
            <td>{moment(new Date(inv.orderedAt)).add(inv.tat,'days').utcOffset('+05:30').format('DD MMM, YY')}</td>
            <td>
              <Button icon={<PrintIcon />} onClick={this._onPrint.bind(this,i)} />
              <Button icon={<ViewIcon />} onClick={this._onViewClick.bind(this,i)} />
            </td>
          </TableRow>
        );
      });
      return (
        <Box pad={{horizontal: 'medium', vertical: 'medium'}}>
          <Table onMore={onMore}>
            <TableHeader labels={['Item Code','Product','Supplier','Bin Size','Bins','Bins Scanned','Exp. Arrival','Action']} />
            <tbody>{items}</tbody>
          </Table>
        </Box>
      );
    } else {
      return (
        <Box size="medium" alignSelf="center" pad={{horizontal:'medium'}}>
          <h3>No Awaiting Orders available</h3>
        </Box>
      );
    }
  }

  _renderArrTom () {
    let {arrTomOrder, filteredCount} = this.state;

    if (arrTomOrder.length > 0) {
      const onMore = arrTomOrder.length < filteredCount ? this._onMore : null;
      let items = arrTomOrder.map((inv,i) => {
        let binsScanned = (inv.binsScanned == null) ? 0 : getNoOfBins(inv.binsScanned);
        return (
          <TableRow key={i}  >
            <td>{inv.itemCode}</td>
            <td>{inv.productName.length > 25 ? inv.productName.substr(0,25) + ' ...': inv.productName}</td>
            <td>{inv.supplierName.length > 25 ? inv.supplierName.substr(0,25) + ' ...': inv.supplierName}</td>
            <td>{inv.binSize}</td>
            <td>{getNoOfBins(inv.bins)}</td>
            <td>{binsScanned}</td>
            <td>{moment(new Date(inv.orderedAt)).add(inv.tat,'days').utcOffset('+05:30').format('DD MMM, YY')}</td>
            <td>
              <Button icon={<PrintIcon />} onClick={this._onPrint.bind(this,i)} />
              <Button icon={<ViewIcon />} onClick={this._onViewClick.bind(this,i)} />
            </td>
          </TableRow>
        );
      });
      return (
        <Box pad={{horizontal: 'medium', vertical: 'medium'}}>
          <Table onMore={onMore}>
            <thead>
              <tr>
                <th>Item Code</th>
                <th>Product</th>
                <th>Supplier</th>
                <th>Bin Size</th>
                <th>Bins</th>
                <th>Bins Scanned</th>
                <th>Exp. Arrival</th>
                <th><Button icon={<PrintIcon />} onClick={this._onPrintAll.bind(this)} /></th>
              </tr>
            </thead>
            <tbody>{items}</tbody>
          </Table>
        </Box>
      );
    } else {
      return (
        <Box size="medium" alignSelf="center" pad={{horizontal:'medium'}}>
          <h3>No Orders arriving tommorrow</h3>
        </Box>
      );
    }
  }

  render() {
    const {initializing, searchText, filteredCount, unfilteredCount, activeTab} = this.state;

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

    const awating = this._renderAwaiting();
    const arrTom = this._renderArrTom();
    const layerFilter = this._renderFilter();
    const layerView = this._renderViewLayer();

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
              <Tab title="Orders Awaiting">
                {awating}
              </Tab>
              <Tab title="Orders Arriving Tomorrow">
                {arrTom}
              </Tab>
            </Tabs>
          </Box>
        </Section>
        {layerFilter}
        {layerView}
      </Box>
    );
  }
}

AwaitingOrder.contextTypes = {
  router: React.PropTypes.object
};

let select = (store) => {
  return {misc: store.misc, category: store.category,inventory: store.inventory, supplier: store.supplier, user: store.user};
};

export default connect(select)(AwaitingOrder);
