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
//import Tab from 'grommet/components/Tab';
//import Tabs from 'grommet/components/Tabs';
import Table from 'grommet/components/Table';
import TableHeader from 'grommet/components/TableHeader';
import TableRow from 'grommet/components/TableRow';
import Header from 'grommet/components/Header';
//import Heading from 'grommet/components/Heading';
import HelpIcon from 'grommet/components/icons/base/Help';
import Search from 'grommet/components/Search';
import Title from 'grommet/components/Title';
import SyncIcon from 'grommet/components/icons/base/Sync';
import PrintIcon from 'grommet/components/icons/base/Print';
//import Layer from 'grommet/components/Layer';
//import Form from 'grommet/components/Form';
//import FormField from 'grommet/components/FormField';
//import FormFields from 'grommet/components/FormFields';
//import Footer from 'grommet/components/Footer';

class AwaitingOrder extends Component {

  constructor () {
    super();
    this.state = {
      initializing: false,
      searching: false,
      searchText: '',
      page: 1, //Page Pending
      awaitingInv: []
    };
    this.localeData = localeData();
    this._loadInventory = this._loadInventory.bind(this);
    this._renderAwaiting = this._renderAwaiting.bind(this);
    this._getAwaitingInv = this._getAwaitingInv.bind(this);
  }

  componentWillMount () {
    console.log('componentWillMount');
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }else{
      this._loadInventory(this.state.page);
    }
  }

  componentWillReceiveProps (nextProps) {
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');
    }
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({initializing: false});
      this._loadInventory(this.state.page);
    }
    this._loadInventory(this.state.page);
  }

  _getAwaitingInv () {
    const {category: {products}, order: {orders}} = this.props;
    let awaitingInv = [];
    let awaitingOrders = orders.filter(o => o.orderState == 'ORDERED');
    awaitingOrders.forEach(o => {
      const p = products.find(prod => prod.id == o.productId);
      if (p != undefined) {
        awaitingInv.push({productId: p.productId,itemCode: p.itemCode,prodId: p.id, productName: p.name, binSize: p.binQty + ' ' + p.uomPurchase, bins: o.bins, orderedAt: o.orderedAt,
          createdAt: o.createdAt, tat: p.timeOrdering + p.timeProcurement + p.timeTransportation + p.timeBuffer});
      }
    });
    return awaitingInv;
  }

  _loadInventory (page) {
    let awaitingInv = this._getAwaitingInv();
    awaitingInv = awaitingInv.slice(0, 15*page);
    this.setState({awaitingInv, page});
  }

  _onMoreOrders () {
    const {page, searching} = this.state;
    if (!searching) {
      this._loadInventory(page+1);
    }
  }

  _onPrint (index) {
    const {awaitingInv} = this.state;
    let inv = awaitingInv[index];
    window.open(window.serviceHost + "/products/" + inv.prodId + "?bins=" + inv.bins,"_blank","fullscreen=yes");
  }

  _onSearch (event) {
    console.log('_onSearch');
    let value = event.target.value;
    if (value.length > 0) {
      let awaitingInv = this._getAwaitingInv();
      awaitingInv = awaitingInv.filter(i => i.productName.toLowerCase().includes(value.toLowerCase()) || i.productId.toLowerCase().includes(value.toLowerCase()));
      this.setState({awaitingInv, searchText: value, searching: true});
    }else {
      this.setState({searchText: value, searching: false});
      this._loadInventory(1);
    }
  }

  _onSyncClick () {
    this.props.dispatch(syncInventory());
    this.props.dispatch(syncOrder());
  }

  _onHelpClick () {
    console.log('_onHelpClick');
  }

  _renderAwaiting () {
    let {awaitingInv} = this.state;
    console.log(awaitingInv[0]);

    if (awaitingInv.length > 0) {
      let items = awaitingInv.map((inv,i) => {
        return (
          <TableRow key={i}  >
            <td>{inv.itemCode}</td>
            <td>{inv.productName.length > 25 ? inv.productName.substr(0,25) + ' ...': inv.productName}</td>
            <td>{inv.binSize}</td>
            <td>{getNoOfBins(inv.bins)}</td>
            <td>{moment(new Date(inv.orderedAt)).add(inv.tat,'days').utcOffset('+05:30').format('DD MMM, YY')}</td>
            <td ><Button icon={<PrintIcon />} onClick={this._onPrint.bind(this,i)} /></td>
          </TableRow>
        );
      });
      return (
        <Box pad={{horizontal: 'medium', vertical: 'medium'}}>
          <Table scrollable={true} onMore={this._onMoreOrders.bind(this)}>
            <TableHeader labels={['Item Code','Product','Bin Size','Bins','Exp. Arrival','Print']} />

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

    const syncingIcon = this.props.order.syncing ? <Spinning /> : null;

    const awating = this._renderAwaiting();

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

            {awating}
          </Box>
        </Section>
      </Box>
    );
  }
}

AwaitingOrder.contextTypes = {
  router: React.PropTypes.object
};

let select = (store) => {
  return {misc: store.misc, category: store.category, awaitingInv: store.awaitingInv, order: store.order, user: store.user};
};

export default connect(select)(AwaitingOrder);
