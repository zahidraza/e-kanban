import React, { Component } from 'react';
import { localeData } from '../../reducers/localization';
import { connect } from 'react-redux';
import {initialize} from '../../actions/misc';

import AppHeader from '../AppHeader';
import Box from 'grommet/components/Box';
import Button from 'grommet/components/Button';
import Section from 'grommet/components/Section';
import Spinning from 'grommet/components/icons/Spinning';
import Header from 'grommet/components/Header';
import Title from 'grommet/components/Title';
import HelpIcon from 'grommet/components/icons/base/Help';
import Search from 'grommet/components/Search';
import Table from 'grommet/components/Table';
import TableHeader from 'grommet/components/TableHeader';
import TableRow from 'grommet/components/TableRow';
import PrintIcon from 'grommet/components/icons/base/Print';

class BarcodeGenerate extends Component {

  constructor () {
    super();
    this.state = {
      initializing: false,
      searching: false,
      available: false,
      searchText: '',
      products: []
    };
    this.localeData = localeData();
    this._renderProducts = this._renderProducts.bind(this);
  }

  componentWillMount () {
    console.log('componentWillMount');
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }
  }

  componentWillReceiveProps (nextProps) {
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');
    }
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({initializing: false});
    }
  }

  _onSearch (event) {
    let value = event.target.value;
    if (value.length > 1) {
      let products = this.props.category.products.filter(p => p.name.toLowerCase().includes(value.toLowerCase()) || p.productId.toLowerCase().includes(value.toLowerCase()));
      let available = products.length > 0 ? true : false;
      this.setState({searchText: value,products, searching: true, available});
    }else{
      this.setState({searchText: value,searching: false});
    }
  }

  _onHelpClick () {

  }

  _onPrint (productId) {
    window.open(window.serviceHost + "/products/prints/" + productId ,"_blank","fullscreen=yes");
  }

  _renderProducts () {
    const {searching,available,searchText,products} = this.state;

    if (searching) {
      if (available) {
        const items = products.map((p, index)=>{
          return (
            <TableRow key={index}  >
              <td >{p.productId}</td>
              <td >{p.name}</td>
              <td >{p.category.name}</td>
              <td >{p.subCategory.name}</td>
              <td >{p.noOfBins}</td>
              <td >{p.binQty}</td>
              <td>
                <Button icon={<PrintIcon />} onClick={this._onPrint.bind(this,p.id)} />
              </td>
            </TableRow>
          );
        });
        return (
          <Table scrollable={true}>
            <TableHeader labels={['Product Id','Product Name','Category','Sub Category','No. of Bins', 'Bin Size','Print']} />

            <tbody>{items}</tbody>
          </Table>
        );
      } else {
        return (
          <Box size="large" alignSelf="center" pad={{horizontal:'medium'}}><h3>No Product matching '{searchText}' found.</h3></Box>
        );
      }
    } else {
      return (
        <Box size="large" alignSelf="center" pad={{horizontal:'medium'}}><h3>Search products to generate Kanban Card. </h3></Box>
      );
    }
  }

  render() {
    const {initializing,searchText} = this.state;

    if (initializing) {
      return (
        <Box pad={{vertical: 'large'}}>
          <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
            <Spinning /> Initializing Application ...
          </Box>
        </Box>
      );
    }

    const products = this._renderProducts();

    return (
      <Box>
        <AppHeader/>
        <Header size='large' pad={{ horizontal: 'medium' }}>
          <Title responsive={false}>
            <span>{this.localeData.label_generate_barcode}</span>
          </Title>
          <Search inline={true} fill={true} size='medium' placeHolder='Search Products [Enter minimum of 2 characters]'
            value={searchText} onDOMChange={this._onSearch.bind(this)} />
          <Button icon={<HelpIcon />} onClick={this._onHelpClick.bind(this)}/>
        </Header>
        <Section direction="column" pad={{vertical: 'large', horizontal:'small'}}>
          <Box>{products}</Box>
        </Section>
      </Box>
    );
  }
}

BarcodeGenerate.contextTypes = {
  router: React.PropTypes.object
};

let select = (store) => {
  return {misc: store.misc, category: store.category};
};

export default connect(select)(BarcodeGenerate);
