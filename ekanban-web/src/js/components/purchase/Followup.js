import React, { Component } from 'react';
import { localeData } from '../../reducers/localization';
import { connect } from 'react-redux';
import {initialize} from '../../actions/misc';
//import {syncInventory} from '../../actions/inventory';
import {followupOrders} from '../../actions/order';
import {getNoOfBins} from '../../utils/miscUtil';
import moment from 'moment';

import AppHeader from '../AppHeader';
import Box from 'grommet/components/Box';
import Button from 'grommet/components/Button';
import CheckBox from 'grommet/components/CheckBox';
import Section from 'grommet/components/Section';
import Spinning from 'grommet/components/icons/Spinning';
import Table from 'grommet/components/Table';
import FilterControl from 'grommet-addons/components/FilterControl';
import TableRow from 'grommet/components/TableRow';
import Header from 'grommet/components/Header';
import Heading from 'grommet/components/Heading';
import HelpIcon from 'grommet/components/icons/base/Help';
import Search from 'grommet/components/Search';
import Title from 'grommet/components/Title';
import Layer from 'grommet/components/Layer';
import Select from 'grommet/components/Select';
import Sidebar from 'grommet/components/Sidebar';
import CloseIcon from 'grommet/components/icons/base/Close';


class Followup extends Component {

  constructor () {
    super();
    this.state = {
      initializing: false,
      searching: false,
      checkedAll: false,
      checked: [],
      searchText: '',
      page: 1, //Page Pending
      orderedInv: [],
      dateFilterItems: [],
      filter: {},
      filterActive: false,
      filteredCount: 0,
      unfilteredCount: 0
    };
    this.localeData = localeData();
    this._loadOrder = this._loadOrder.bind(this);
    this._renderOrdered = this._renderOrdered.bind(this);
    this._getOpenOrders = this._getOpenOrders.bind(this);
    this._renderFilter = this._renderFilter.bind(this);
    this._onMore = this._onMore.bind(this);
  }

  componentWillMount () {
    console.log('componentWillMount');
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }else{
      this._loadOrder(this._getOpenOrders(),this.state.filter,this.state.page,true);
    }
  }

  componentWillReceiveProps (nextProps) {
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');
    }
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({initializing: false});
      this._loadOrder(this._getOpenOrders(),this.state.filter,this.state.page,true);
    }
    if (this.props.order.toggleStatus != nextProps.order.toggleStatus) {
      this._loadOrder(this._getOpenOrders(),this.state.filter,this.state.page,false);
    }
    
  }

  _getOpenOrders () {
    const {category: {products}, order: {orders}} = this.props;
    let orderedInv = [];
    let orderedOrders = orders.filter(o => o.orderState == 'ORDERED' && !o.followedUp);

    let todayMillis = new Date().getTime();
    let oneDayMillis = 24*60*60*1000;
    orderedOrders.forEach(o => {
      const p = products.find(prod => prod.id == o.productId);
      if (p != undefined && o.orderedAt+ p.timeProduction*oneDayMillis <= todayMillis) {
        orderedInv.push({orderId: o.id, productId: p.productId,itemCode: p.itemCode,prodId: p.id, productName: p.name, binSize: p.binQty + ' ' + p.uomPurchase,
          bins: o.bins, orderedAt: o.orderedAt, timeProduction: p.timeProduction, timeTransportation: p.timeTransportation, followedUp: o.followedUp,
          followupDate: moment(new Date(o.orderedAt)).add(p.timeProduction,'days').utcOffset('+05:30').format('DD MMM, YY')});
      }
    });
    return orderedInv;
  }

  _loadOrder (orderedInv,filter,page,isFirstLoad) {
    /////// calculate followup date filter items desc sorted///////////
    let dateSet = new Set();
    orderedInv.forEach(o => dateSet.add(o.followupDate));
    let dateFilterItems = [];
    dateFilterItems.push({label: 'All', value: undefined});
    dateSet.forEach(date => dateFilterItems.push({label: date, value: date}));
    dateFilterItems.sort((date1,date2) => {
      return moment(date2.label, 'DD MMM, YY').toDate().getTime() - moment(date1.label, 'DD MMM, YY').toDate().getTime();
    });
    if (isFirstLoad && dateFilterItems.length > 1) {
      filter.followupDate = [dateFilterItems[1].label];
    }
    ///////////////////////////////////////////////////////
    let unfilteredCount = orderedInv.length;
    if ('followupDate' in filter) {
      const followupDateFilter = filter.followupDate;
      orderedInv = orderedInv.filter(o => followupDateFilter.includes(o.followupDate));
    }
    //////////////////////////////////////////////////////
    let checked = new Array(orderedInv.length).fill(false);
    let filteredCount = orderedInv.length;
    orderedInv.sort((o1,o2) => {
      return moment(o2.followupDate, 'DD MMM, YY').toDate().getTime() - moment(o1.followupDate, 'DD MMM, YY').toDate().getTime();
    });
    orderedInv = orderedInv.slice(0, 15*page);
    this.setState({orderedInv,checked, page,unfilteredCount, filteredCount, dateFilterItems, filter});
  }

  _onMore () {
    this._loadOrder(this._getOpenOrders(),this.state.filter,this.state.page+1, false);
  }

  _onSearch (event) {
    const {filter,page} = this.state;
    let value = event.target.value;
    let orderedInv = this._getOpenOrders();
    orderedInv = orderedInv.filter(i => i.productName.toLowerCase().includes(value.toLowerCase()) || i.productId.toLowerCase().includes(value.toLowerCase()));
    this.setState({searchText: value});
    if (value.length == 0) {
      this._loadOrder(orderedInv, filter,page,false);
    }else {
      this._loadOrder(orderedInv,{},page,false);
    }
  }

  _onSyncClick () {
    // this.props.dispatch(syncInventory());
    // this.props.dispatch(syncOrder());
  }

  _onHelpClick () {
    
  }

  _onFollowUp () {
    let {checked,filter} = this.state;
    let orderedInv = this._getOpenOrders();
    if ('followupDate' in filter) {
      const followupDateFilter = filter.followupDate;
      orderedInv = orderedInv.filter(o => followupDateFilter.includes(o.followupDate));
    }
    const orders = orderedInv.map((order,i) => {
      return {id: order.orderId, followedUp : checked[i]};
    });
    this.props.dispatch(followupOrders(orders));
  }

  _onChangeAll (event) {
    let {checkedAll, checked} = this.state;
    checkedAll = !checkedAll;
    if (checkedAll) {
      checked = checked.map(c => true);
    } else {
      checked = checked.map(c => false);
    }
    this.setState({checkedAll,checked});
  }

  _onChange (index,event) {
    let checked = this.state.checked;
    checked[index] = !checked[index];
    let checkedAll = true;
    for (var i = 0; i < checked.length; i++) {
      if (!checked[i]) {
        checkedAll = false;
        break;
      }
    }
    this.setState({checked,checkedAll});
  }

  _onChangeFilter (name,event) {
    let filter = this.state.filter;

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
    this._loadOrder(this._getOpenOrders(),filter,this.state.page, false);
  }

  _onFilterOpen () {
    this.setState({filterActive: true});
  }

  _onCloseFilter () {
    this.setState({filterActive: false});
  }

  _renderFilter () {
    const {filter,filterActive} = this.state;

   
    if (!filterActive) return null;
    return (
      <Layer align='right' flush={true} closer={false}
        a11yTitle='Followup Filter'>
        <Sidebar size='large'>
          <div>
            <Header size='large' justify='between' align='center'
              pad={{ horizontal: 'medium', vertical: 'medium' }}>
              <Heading tag='h2' margin='none'>Filter</Heading>
              <Button icon={<CloseIcon />} plain={true}
                onClick={this._onCloseFilter.bind(this)} />
            </Header>
            <Section pad={{ horizontal: 'large', vertical: 'small' }}>
              <Heading tag='h3'>Followup Dates</Heading>
              <Select inline={true} multiple={true} options={this.state.dateFilterItems} value={filter.followupDate} onChange={this._onChangeFilter.bind(this,'followupDate')} />
            </Section>
          </div>
        </Sidebar>
      </Layer>
    );
  }

  _renderOrdered () {
    let {orderedInv,checked,filteredCount} = this.state;

    let onMore;
    if (orderedInv.length > 0 && orderedInv.length < filteredCount) {
      onMore = this._onMore;
    }

    if (orderedInv.length > 0) {
      let items = orderedInv.map((inv,i) => {
        return (
          <TableRow key={i}  >
            <td>{inv.itemCode}</td>
            <td>{inv.productName.length > 25 ? inv.productName.substr(0,25) + ' ...': inv.productName}</td>
            <td>{inv.binSize}</td>
            <td>{getNoOfBins(inv.bins)}</td>
            <td>{moment(new Date(inv.orderedAt)).add(inv.timeProduction,'days').utcOffset('+05:30').format('DD MMM, YY')}</td>
            <td>{moment(new Date(inv.orderedAt)).add(inv.timeProduction+inv.timeTransportation,'days').utcOffset('+05:30').format('DD MMM, YY')}</td>
            <td ><CheckBox label='followup' checked={checked[i]} onChange={this._onChange.bind(this,i)}/></td>
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
                <th>Bin Size</th>
                <th>Bins</th>
                <th>Followup date</th>
                <th>Exp. Arrival</th>
                <th><CheckBox label='Check All' checked={this.state.checkedAll} onChange={this._onChangeAll.bind(this)}/></th>
              </tr>
            </thead>
            <tbody>{items}</tbody>
          </Table>
        </Box>
      );
    } else {
      return (
        <Box size="medium" alignSelf="center" pad={{horizontal:'medium'}}>
          <h3>No Orders to follow up</h3>
        </Box>
      );
    }
  }

  render () {
    const {searchText,filteredCount,unfilteredCount,initializing} = this.state;
    const ordered = this._renderOrdered();
    const layerFilter = this._renderFilter();

    if (initializing) {
      return (
        <Box pad={{vertical: 'large'}}>
          <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
            <Spinning /> Initializing Application ...
          </Box>
        </Box>
      );
    }

    const busyIcon = this.props.order.busy ? <Spinning /> : null;

    return (
      <Box>
        <AppHeader/>
        <Header size='large' pad={{ horizontal: 'medium' }}>
          <Title responsive={false}>
            <span>Followup</span>
          </Title>
          <Search inline={true} fill={true} size='medium' placeHolder='search'
            value={searchText} onDOMChange={this._onSearch.bind(this)} />
          <FilterControl filteredTotal={filteredCount}
            unfilteredTotal={unfilteredCount}
            onClick={this._onFilterOpen.bind(this)} />
          <Button icon={<HelpIcon />} onClick={this._onHelpClick.bind(this)}/>
        </Header>
        <Section>
          <Box pad={{vertical: 'small'}}>
            <Box size="meduim" alignSelf="end" pad={{horizontal: 'large'}}>
              <Button icon={busyIcon} label="Save FollowUp" onClick={this._onFollowUp.bind(this)} />
            </Box>
            <Box pad={{vertical: 'small'}}>
              {ordered}
            </Box>
          </Box>
        </Section>
        {layerFilter}
      </Box>

    );
  }
}

Followup.contextTypes = {
  router: React.PropTypes.object
};

let select = (store) => {
  return {misc: store.misc,category: store.category, order: store.order, user: store.user};
};

export default connect(select)(Followup);
