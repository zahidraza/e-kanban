import React, { Component } from 'react';
// import { localeData } from '../../reducers/localization';
import { connect } from 'react-redux';
// import {initialize} from '../../actions/misc';
// import {syncInventory} from '../../actions/inventory';
// import {getNoOfBins} from '../../utils/miscUtil';
//import moment from 'moment';

import AppHeader from '../AppHeader';
import Box from 'grommet/components/Box';
// import Button from 'grommet/components/Button';
import Section from 'grommet/components/Section';
//import Spinning from 'grommet/components/icons/Spinning';
// import Table from 'grommet/components/Table';
// import TableHeader from 'grommet/components/TableHeader';
// import TableRow from 'grommet/components/TableRow';
import Header from 'grommet/components/Header';
//import Heading from 'grommet/components/Heading';
// import HelpIcon from 'grommet/components/icons/base/Help';
// import Search from 'grommet/components/Search';
import Title from 'grommet/components/Title';
// import SyncIcon from 'grommet/components/icons/base/Sync';

class Stock extends Component {
  //
  // constructor () {
  //   super();
  //   this.state = {
  //     initializing: false,
  //     searching: false,
  //     searchText: '',
  //     page: 1,
  //     stockInv: []
  //   };
  //   this.localeData = localeData();
  //   this._loadInventory = this._loadInventory.bind(this);
  //   this._renderStock = this._renderStock.bind(this);
  //   this._getStockInv = this._getStockInv.bind(this);
  // }
  //
  // componentWillMount () {
  //   console.log('componentWillMount');
  //   if (!this.props.misc.initialized) {
  //     this.setState({initializing: true});
  //     this.props.dispatch(initialize());
  //   }else{
  //     this._loadInventory(this.state.page);
  //   }
  // }
  //
  // componentWillReceiveProps (nextProps) {
  //   if (sessionStorage.session == undefined) {
  //     this.context.router.push('/');
  //   }
  //   if (!this.props.misc.initialized && nextProps.misc.initialized) {
  //     this.setState({initializing: false});
  //     this._loadInventory(this.state.page);
  //   }
  //   this._loadInventory(this.state.page);
  // }
  //
  // _getStockInv () {
  //   const {inventory: {stockMap}, category: {products}} = this.props;
  //   let stockInv = [];
  //   for (var [key, value] of stockMap) {
  //     const p = products.find(prod => prod.productId == key);
  //     if (p != undefined) {
  //       stockInv.push({productId: key,prodId: p.id, productName: p.name, binSize: p.binQty + ' ' + p.uomPurchase, bins: value, noOfBins: getNoOfBins(value)});
  //     }
  //   }
  //   return stockInv;
  // }
  //
  // _loadInventory (page) {
  //   let stockInv = this._getStockInv();
  //   stockInv = stockInv.slice(0, 15*page);
  //   this.setState({stockInv, page});
  // }
  //
  // _onMoreOrders () {
  //   const {page, searching} = this.state;
  //   if (!searching) {
  //     this._loadInventory(page+1);
  //   }
  // }
  //
  // _onSearch (event) {
  //   console.log('_onSearch');
  //   let value = event.target.value;
  //   if (value.length > 0) {
  //     let stockInv = this._getStockInv();
  //     stockInv = stockInv.filter(i => i.productName.toLowerCase().includes(value.toLowerCase()) || i.productId.toLowerCase().includes(value.toLowerCase()));
  //     this.setState({stockInv, searchText: value, searching: true});
  //   }else {
  //     this.setState({searchText: value, searching: false});
  //     this._loadInventory(1);
  //   }
  // }
  //
  // _onSyncClick () {
  //   this.props.dispatch(syncInventory());
  // }
  //
  // _onHelpClick () {
  //   console.log('_onHelpClick');
  // }
  //
  // _renderStock () {
  //   let {stockInv} = this.state;
  //
  //   if (stockInv.length > 0) {
  //     let items = stockInv.map((inv,i) => {
  //       return (
  //         <TableRow key={i}  >
  //           <td>{inv.productId}</td>
  //           <td>{inv.productName}</td>
  //           <td>{inv.binSize}</td>
  //           <td>{inv.bins}</td>
  //           <td>{inv.noOfBins}</td>
  //         </TableRow>
  //       );
  //     });
  //     return (
  //       <Box pad={{horizontal: 'medium', vertical: 'medium'}}>
  //         <Table scrollable={true} onMore={this._onMoreOrders.bind(this)}>
  //           <TableHeader labels={['Product Id','Product','Bin Size','Cards','No of Bins']} />
  //
  //           <tbody>{items}</tbody>
  //         </Table>
  //       </Box>
  //     );
  //   } else {
  //     return (
  //       <Box size="medium" alignSelf="center" pad={{horizontal:'medium'}}>
  //         <h3>No Products available in Stock</h3>
  //       </Box>
  //     );
  //   }
  // }
  //
  // render() {
  //   const {initializing, searchText} = this.state;
  //
  //   if (initializing) {
  //     return (
  //       <Box pad={{vertical: 'large'}}>
  //         <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
  //           <Spinning /> Initializing Application ...
  //         </Box>
  //       </Box>
  //     );
  //   }
  //
  //   const syncingIcon = this.props.inventory.syncing ? <Spinning /> : null;
  //
  //   const stock = this._renderStock();
  //
  //   return (
  //     <Box>
  //       <AppHeader/>
  //       <Header size='large' pad={{ horizontal: 'medium' }}>
  //         <Title responsive={false}>
  //           <span>{this.localeData.label_stock}</span>
  //         </Title>
  //         <Search inline={true} fill={true} size='medium' placeHolder='Search'
  //           value={searchText} onDOMChange={this._onSearch.bind(this)} />
  //         <Button icon={<SyncIcon />}  onClick={this._onSyncClick.bind(this)}/>
  //         <Button icon={<HelpIcon />} onClick={this._onHelpClick.bind(this)}/>
  //       </Header>
  //       <Section>
  //         <Box pad={{horizontal: 'medium', vertical: 'medium'}}>
  //           <Box size="small" alignSelf="center">{syncingIcon}</Box>
  //           {stock}
  //         </Box>
  //       </Section>
  //     </Box>
  //   );
  // }
  render () {
    return (
      <Box>
        <AppHeader/>
        <Header size='large' pad={{ horizontal: 'medium' }}>
          <Title responsive={false}>
            <span>Stock</span>
          </Title>

        </Header>
        <Section>
          <Box pad={{vertical: 'large'}}>
            <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
              Coming soon ...
            </Box>
          </Box>
        </Section>
      </Box>

    );
  }
}

Stock.contextTypes = {
  router: React.PropTypes.object
};

let select = (store) => {
  return {misc: store.misc, category: store.category, inventory: store.inventory, user: store.user};
};

export default connect(select)(Stock);
