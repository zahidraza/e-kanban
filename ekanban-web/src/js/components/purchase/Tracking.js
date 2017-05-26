import React, { Component } from 'react';
import { localeData } from '../../reducers/localization';
import { connect } from 'react-redux';
import {initialize} from '../../actions/misc';
import {generateOrder,syncOrder} from '../../actions/order';
import {syncInventory} from '../../actions/inventory';
import {getNoOfBins, getAgeing} from '../../utils/miscUtil';
import {ORDER_CONSTANTS as c} from '../../utils/constants';
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
import Layer from 'grommet/components/Layer';
import Form from 'grommet/components/Form';
import FormField from 'grommet/components/FormField';
import FormFields from 'grommet/components/FormFields';
import Footer from 'grommet/components/Footer';

class Tracking extends Component {

  constructor () {
    super();
    this.state = {
      initializing: false,
      searching: false,
      searchText: '',
      page1: 1, //Page Pending
      page2: 1, //Page Ordered
      inventory: [],
      noOfBins: '',
      errors: {},
      orderProduct: {}    //Product for Ordering
    };
    this.localeData = localeData();
    this._loadInventory = this._loadInventory.bind(this);
    this._renderPending = this._renderPending.bind(this);
    this._renderOrdered = this._renderOrdered.bind(this);
    this._renderOrderLayer = this._renderOrderLayer.bind(this);
    this._getPendingInv = this._getPendingInv.bind(this);
    this._getOrderedInv = this._getOrderedInv.bind(this);
  }

  componentWillMount () {
    console.log('componentWillMount');
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }else{
      this._loadInventory(this.state.page1, this.state.page2);
    }
  }

  componentWillReceiveProps (nextProps) {
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');
    }
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({initializing: false});
      this._loadInventory(this.state.page1, this.state.page2);
    }
    this._loadInventory(this.state.page1, this.state.page2);
  }

  _getPendingInv () {
    const {inventory: {pendingMap}, category: {products}} = this.props;
    let pendingInv = [];
    for (var [key, value] of pendingMap) {
      const p = products.find(prod => prod.productId == key);
      if (p != undefined) {
        pendingInv.push({productId: key,prodId: p.id,itemCode: p.itemCode, productName: p.name, binSize: p.binQty + ' ' + p.uomPurchase, bins: value.bins, noOfBins: getNoOfBins(value.bins),createdAt: value.createdAt});
      }
    }
    return pendingInv;
  }

  _getOrderedInv () {
    const {category: {products}, order: {orders}, user: {users}} = this.props;
    let orderedInv = [];
    orders.forEach(o => {
      const p = products.find(prod => prod.id == o.productId);
      const u = users.find(user => user.id == o.orderedBy);
      let orderedBy = u == undefined ? '' : u.name;

      if (p != undefined) {
        orderedInv.push({productId: p.productId, itemCode: p.itemCode, productName: p.name, binSize: p.binQty + ' ' + p.uomPurchase, bins: o.bins, orderedAt: o.orderedAt,orderedBy,
          tat: p.timeProduction + p.timeTransportation});
      }
    });
    return orderedInv;
  }

  _loadInventory (page1, page2) {
    let pendingInv = this._getPendingInv();
    let orderedInv = this._getOrderedInv();

    pendingInv = pendingInv.slice(0, 15*page1);
    orderedInv = orderedInv.slice(0, 15*page2);
    this.setState({pendingInv, orderedInv, page1, page2});
  }

  _onMoreOrders () {
    const {page1, page2, searching} = this.state;
    if (!searching) {
      this._loadInventory(page1+1, page2+1);
    }
  }

  _order () {
    const {orderProduct,noOfBins} = this.state;
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
    let order = {productId: orderProduct.prodId, prodId: orderProduct.productId, orderedBy: sessionStorage.userId};
    if (noOfBins == orderProduct.noOfBins) {
      order.bins = orderProduct.bins;
    }else {
      let bins = orderProduct.bins.substr(0,2*noOfBins);
      bins = bins.substr(0,2*noOfBins-1);
      order.bins = bins;
    }
    this.props.dispatch(generateOrder(order));
  }

  _onOrderClick (index) {
    const {pendingInv} = this.state;
    const inv = pendingInv[index];
    this.setState({orderProduct: {...inv}});
    this.props.dispatch({type: c.ORDER_ADD_FORM_TOGGLE, payload: {ordering: true}});
  }

  _onSearch (event) {
    console.log('_onSearch');
    let value = event.target.value;
    if (value.length > 0) {
      let pendingInv = this._getPendingInv();
      let orderedInv = this._getOrderedInv();
      pendingInv = pendingInv.filter(i => i.productName.toLowerCase().includes(value.toLowerCase()) || i.productId.toLowerCase().includes(value.toLowerCase()));
      orderedInv = orderedInv.filter(i => i.productName.toLowerCase().includes(value.toLowerCase()) || i.productId.toLowerCase().includes(value.toLowerCase()));
      this.setState({pendingInv, orderedInv, searchText: value, searching: true});
    }else {
      this.setState({searchText: value, searching: false});
      this._loadInventory(1,1);
    }
  }

  _onSyncClick () {
    this.props.dispatch(syncInventory());
    this.props.dispatch(syncOrder());
  }

  _onHelpClick () {
    console.log('_onHelpClick');
  }

  _onChangeInput (event) {
    this.setState({noOfBins: event.target.value, errors: {}});
  }

  _onCloseLayer () {
    this.setState({orderProduct: {}});
    this.props.dispatch({type: c.ORDER_ADD_FORM_TOGGLE, payload: {ordering: false}});
  }

  _renderOrderLayer () {
    const {busy, ordering} = this.props.order;
    const busyIcon = busy ? <Spinning /> : null;
    return (
      <Layer hidden={!ordering} onClose={this._onCloseLayer.bind(this)}  closer={true} align="center">
        <Form>
          <Header><Heading tag="h3" strong={true}>Create Order</Heading></Header>
          <FormFields>
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

  _renderPending () {
    let {pendingInv} = this.state;
    if (pendingInv.length > 0) {
      let items = pendingInv.map((inv,i) => {
        return (
          <TableRow key={i}  >
            <td>{inv.itemCode}</td>
            <td>{inv.productName.length > 25 ? inv.productName.substr(0,25) + ' ...': inv.productName}</td>
            <td>{inv.binSize}</td>
            <td>{inv.noOfBins}</td>
            <td>{moment(new Date(inv.createdAt)).utcOffset('+05:30').format('DD/MM/YYYY, hh:mm A')}</td>
            <td>{getAgeing(inv.createdAt)}</td>
            <td><Button label='Order' onClick={this._onOrderClick.bind(this,i)} /></td>
          </TableRow>
        );
      });
      return (
        <Box pad={{horizontal: 'medium', vertical: 'medium'}}>
          <Table scrollable={true} onMore={this._onMoreOrders.bind(this)}>
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
    let {orderedInv} = this.state;


    if (orderedInv.length > 0) {
      let items = orderedInv.map((inv,i) => {
        return (
          <TableRow key={i}  >
            <td>{inv.itemCode}</td>
            <td>{inv.productName.length > 25 ? inv.productName.substr(0,25) + ' ...': inv.productName}</td>
            <td>{inv.binSize}</td>
            <td>{getNoOfBins(inv.bins)}</td>
            <td>{inv.orderedBy}</td>
            <td>{moment(new Date(inv.orderedAt)).utcOffset('+05:30').format('DD MMM, YY hh:mm A')}</td>
            <td>{moment(new Date(inv.orderedAt)).add(inv.tat,'days').utcOffset('+05:30').format('DD MMM, YY')}</td>
          </TableRow>
        );
      });
      return (
        <Box pad={{horizontal: 'medium', vertical: 'medium'}}>
          <Table scrollable={true} onMore={this._onMoreOrders.bind(this)}>
            <TableHeader labels={['Item Code','Product','Bin Size','Bins','Ordered By','Ordered At','Exp. Arrival']} />

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

  render() {
    const {initializing, searchText} = this.state;

    if (initializing) {
      return (
        <Box pad={{vertical: 'large'}}>
          <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
            <Spinning /> Initializing Application ...
          </Box>
        </Box>
      );
    }

    const syncingIcon = this.props.inventory.syncing || this.props.order.syncing ? <Spinning /> : null;

    const pending = this._renderPending();
    const ordered = this._renderOrdered();
    const layerOrdering = this._renderOrderLayer();

    return (
      <Box>
        <AppHeader/>
        <Header size='large' pad={{ horizontal: 'medium' }}>
          <Title responsive={false}>
            <span>{this.localeData.label_tracking}</span>
          </Title>
          <Search inline={true} fill={true} size='medium' placeHolder='Search'
            value={searchText} onDOMChange={this._onSearch.bind(this)} />
          <Button icon={<SyncIcon />}  onClick={this._onSyncClick.bind(this)}/>
          <Button icon={<HelpIcon />} onClick={this._onHelpClick.bind(this)}/>
        </Header>
        <Section>
          <Box pad={{horizontal: 'medium', vertical: 'medium'}}>
            <Box size="small" alignSelf="center">{syncingIcon}</Box>
            <Tabs justify="center">
              <Tab title="Pending Orders">
                {pending}
              </Tab>
              <Tab title="Ordered">
                {ordered}
              </Tab>
            </Tabs>
          </Box>
        </Section>
        {layerOrdering}
      </Box>
    );
  }
}

Tracking.contextTypes = {
  router: React.PropTypes.object
};

let select = (store) => {
  return {misc: store.misc, category: store.category, inventory: store.inventory, order: store.order, user: store.user};
};

export default connect(select)(Tracking);
