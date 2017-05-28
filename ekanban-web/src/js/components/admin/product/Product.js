import React, { Component} from 'react';
import { connect } from 'react-redux';
import { localeData } from '../../../reducers/localization';
import {removeProduct,syncProduct,syncOneProduct}  from '../../../actions/product';
import {initialize}  from '../../../actions/misc';
import {getMonth,getItemMasterHeader,getItemMasterBody}  from '../../../utils/miscUtil';
import {PRODUCT_CONSTANTS as c}  from '../../../utils/constants';
import {CSVLink} from 'react-csv';

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
import ProductFilter from './ProductFilter';
import Table from 'grommet/components/Table';
import TableHeader from 'grommet/components/TableHeader';
import TableRow from 'grommet/components/TableRow';
// import Trash from "grommet/components/icons/base/Trash";
import Title from 'grommet/components/Title';
import UploadIcon from 'grommet/components/icons/base/Upload';
import SyncIcon from 'grommet/components/icons/base/Sync';
import DownloadIcon from 'grommet/components/icons/base/Download';
import ViewIcon from "grommet/components/icons/base/View";
import List from 'grommet/components/List';
import ListItem from 'grommet/components/ListItem';
import Layer from 'grommet/components/Layer';

class Product extends Component {

  constructor () {
    super();
    this.state = {
      initializing: false,
      searching: false,
      syncing: false,
      viewing: false,
      errors: [],
      products: [],
      productsDownload:[],
      product: {},
      searchText: '',
      filterActive: false,
      filteredCount: 0,
      unfilteredCount: 0,
      page: 1,
      productNotAvailable: false  //Whether product is available for select filter
    };
    this.localeData = localeData();
    this._loadProduct = this._loadProduct.bind(this);
    this._productSort = this._productSort.bind(this);
    this._renderProducts = this._renderProducts.bind(this);
    this._renderLayerView = this._renderLayerView.bind(this);
    this._onMore = this._onMore.bind(this);
  }

  componentWillMount () {
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }else{
      const {products,filter,sort} = this.props.category;
      this._loadProduct(products,filter,sort,this.state.page);
    }
  }

  componentWillReceiveProps (nextProps) {
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');
    }
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({initializing: false});
      const {products,filter,sort} = nextProps.category;
      this._loadProduct(products,filter,sort,this.state.page);
    }
    if (this.props.misc.initialized && !nextProps.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }
    if (this.props.category.toggleStatus != nextProps.category.toggleStatus) {
      const {products,filter,sort} = nextProps.category;
      this._loadProduct(products,filter,sort,this.state.page);
    }
    if (!this.state.syncing && nextProps.category.uploaded) {
      this.setState({syncing: true});
      this.props.dispatch(syncProduct());
    }
    if (nextProps.category.productAdded != -1) {
      this.props.dispatch(syncOneProduct(nextProps.category.productAdded));
    }
  }

  _onMore () {
    const {products,filter,sort} = this.props.category;
    this._loadProduct(products,filter,sort,this.state.page+1);
  }

  _loadProduct (products,filter,sort,page) {
    const unfilteredCount = products.length;
    //Filter non discontinued product
    products = products.filter(p => !p.freezed);

    if ('class' in filter) {
      const classFilter = filter.class;
      products = products.filter(p => classFilter.includes(p.classType));
    }
    if ('category' in filter) {
      const categoryFilter = filter.category;
      products = products.filter(p => categoryFilter.includes(p.category.name));
    }

    if ('subCategory' in filter) {
      const subCategoryFilter = filter.subCategory;
      if (!subCategoryFilter.includes('All'))
        products = products.filter(p => subCategoryFilter.includes(p.subCategory.name));
    }

    if ('section' in filter) {
      const sectionFilter = filter.section;
      if (!sectionFilter.includes('All'))
        products = products.filter(p => {
          for (let i = 0; i < p.sectionList.length; i++) {
            if (sectionFilter.includes(p.sectionList[i].name)) return true;
          }
          return false;
        });
    }

    if ('supplier' in filter) {
      const supplierFilter = filter.supplier;
      if (!supplierFilter.includes('All'))
        products = products.filter(p => {
          for (let i = 0; i < p.supplierList.length; i++) {
            if (supplierFilter.includes(p.supplierList[i].name)) return true;
          }
          return false;
        });
    }

    const filteredCount = products.length;
    let productNotAvailable = false;
    if (filteredCount == 0) {
      productNotAvailable = true;
    }
    let productsDownload = [getItemMasterHeader()];
    products.forEach(p => {
      p.consumptions.forEach(c => {
        p[getMonth(c.month)] = c.value;
      });
      productsDownload.push(getItemMasterBody(p));
    });

    products = products.slice(0,20*page);
    this.setState({products,productsDownload, filteredCount, unfilteredCount, page, productNotAvailable});
  }

  _productSort (products,sort) {
    const [sortProperty,sortDirection] = sort.split(':');
    let result = products.sort((a,b) => {
      if (sortProperty == 'name' && sortDirection == 'asc') {
        return (a.name < b.name) ? -1 : 1;
      } else if (sortProperty == 'name' && sortDirection == 'desc') {
        return (a.name > b.name) ? -1 : 1;
      }
    });
    return result;
  }

  _onSearch (event) {
    let {products,filter,sort} = this.props.category;
    let value = event.target.value;
    products = products.filter(p => p.name.toLowerCase().includes(value.toLowerCase()) || p.itemCode.toLowerCase().includes(value.toLowerCase()));
    this.setState({searchText: value});
    if (value.length > 0) {
      this._loadProduct(products,filter,sort,1);
    }else{
      this._loadProduct(products,{},sort,1);
    }
  }

  _onFilterActivate () {
    this.setState({filterActive: true});
  }

  _onFilterDeactivate () {
    this.setState({filterActive: false});
  }

  _onChangeInput ( event ) {
    var product = this.state.product;
    product[event.target.getAttribute('name')] = event.target.value;
    this.setState({product: product});
  }

  _onAddClick () {
    this.props.dispatch({type: c.PRODUCT_ADD_FORM_TOGGLE,payload: {adding: true}});
  }

  _onViewClick (index) {
    const {products} = this.state;
    let product = {...products[index]};
    this.setState({product,viewing: true});
  }

  _onUploadClick () {

    this.props.dispatch({type: c.PRODUCT_UPLOAD_FORM_TOGGLE,payload: {uploading: true}});
  }

  _onRemoveClick (index) {
    const {products} = this.state;
    this.props.dispatch(removeProduct(products[index]));
  }

  _onEditClick (index) {
    console.log('_onEditClick');
    const {products} = this.state;
    this.props.dispatch({type: c.PRODUCT_EDIT_FORM_TOGGLE, payload:{editing: true, product: {...products[index]}}});
    this.context.router.push('/product/edit');
  }

  _onSyncClick () {
    const value = confirm('Are you sure you want to re-assign dynamically calculated value for products?');
    if (!value) {
      return;
    }
    this.props.dispatch(syncProduct());
  }

  _onHelpClick () {
    const helpUrl = window.baseUrl + "/help/product";
    window.open(helpUrl);
  }

  _onCloseLayer () {
    this.setState({viewing: false});
  }

  _renderLayerView () {
    const {product,viewing} = this.state;
    console.log(product);
    if (!viewing) return null;

    const suppliers = product.supplierList.map((s,i) => {
      return (
        <ListItem  key={i} justify="end" pad={{vertical:'small',horizontal:'small'}} >
          {s.name}
        </ListItem>
      );
    });
    let sections = '  ';
    product.sectionList.forEach(s => sections += s.name + ', ');
    sections = sections.substring(0,sections.length-2).trim();
    

    return (
      <Layer hidden={!viewing}  onClose={this._onCloseLayer.bind(this)}  closer={true} align="center">
        <Box size="large"  pad={{vertical: 'none', horizontal:'small'}}>
          <Header><Heading tag="h3" strong={true} >Product Details</Heading></Header>
          <List>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> product Name: </span>
              <span className="secondary">{product.name}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Item Code: </span>
              <span className="secondary">{product.itemCode}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Category: </span>
              <span className="secondary">{product.category.name}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Sub Category: </span>
              <span className="secondary">{product.subCategory.name}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Price: </span>
              <span className="secondary">{"Rs "+product.price}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Class: </span>
              <span className="secondary">{product.classType}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Kanban Type: </span>
              <span className="secondary">{product.kanbanType}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Bin Quantity: </span>
              <span className="secondary">{product.binQty + " " + product.uomPurchase}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> No. of Bins: </span>
              <span className="secondary">{product.noOfBins}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Minimum Order Quantity: </span>
              <span className="secondary">{product.minOrderQty + " " + product.uomPurchase}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Packet Size: </span>
              <span className="secondary">{product.packetSize + " " + product.uomPurchase}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Demand: </span>
              <span className="secondary">{product.demand + " " + product.uomPurchase + "/day"}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Stock on Floor: </span>
              <span className="secondary">{product.stkOnFloor + " " + product.uomPurchase}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Ordered Qty: </span>
              <span className="secondary">{product.orderedQty + " " + product.uomPurchase}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Is Locked? </span>
              <span className="secondary">{product.ignoreSync ? 'YES' : 'NO'}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Is Discontinued? </span>
              <span className="secondary">{product.freezed ? 'YES' : 'NO'}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Ordering time: </span>
              <span className="secondary">{product.timeOrdering + " days"}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Production time: </span>
              <span className="secondary">{product.timeProduction + " days"}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Transportation time: </span>
              <span className="secondary">{product.timeTransportation + " days"}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Buffer time: </span>
              <span className="secondary">{product.timeBuffer + " days"}</span>
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> Sections: </span>
              <span className="secondary"/>
            </ListItem>
            <ListItem justify="end" pad={{vertical:'small',horizontal:'small'}} >
              {sections}
            </ListItem>
            <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
              <span> suppliers: </span>
              <span className="secondary"/>
            </ListItem>
            {suppliers}
          </List>
        </Box>
        <Box pad={{vertical: 'medium', horizontal:'small'}} />
      </Layer>
    );
  }

  _renderProducts (products) {
    const items = products.map((p, index)=>{
      let sections = '  ';
      p.sectionList.forEach(s => sections += s.name + ', ');
      sections = sections.substring(0,sections.length-2).trim();
      return (
        <TableRow key={index}  >
          <td>{p.itemCode}</td>
          <td >{p.name.length > 21 ? p.name.substr(0,21) + ' ...' : p.name}</td>
          <td >{p.category.name}</td>
          <td >{p.subCategory.name}</td>
          <td >{sections.length > 10 ? sections.substr(0,10) + ' ...' : sections}</td>
          <td >{p.noOfBins}</td>
          <td >{p.binQty + ' ' + p.uomConsumption}</td>
          <td>{p.price}</td>
          <td>{p.classType}</td>
          <td style={{textAlign: 'right', padding: 0}}>
            <Button icon={<ViewIcon />} onClick={this._onViewClick.bind(this,index)} />
            <Button icon={<Edit />} onClick={this._onEditClick.bind(this,index)} />
          </td>
        </TableRow>

      );
    });
    return items;
  }

  render() {
    const {refreshing} = this.props.category;
    const { products,productsDownload, searchText, filterActive, filteredCount, unfilteredCount, initializing, productNotAvailable } = this.state;

    if (initializing) {
      return (
        <Box pad={{vertical: 'large'}}>
          <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
            <Spinning /> Initializing Application ...
          </Box>
        </Box>
      );
    }

    const loading = refreshing ? (<Spinning />) : null;
    const layerFilter = filterActive ? <ProductFilter onClose={this._onFilterDeactivate.bind(this)}/> : null;
    const layerView = this._renderLayerView();

    const items = this._renderProducts(products);
    let onMore;
    if (products.length > 0 && products.length < filteredCount) {
      onMore = this._onMore;
    }

    let productItem = productNotAvailable ? <Box size="medium" alignSelf="center" pad={{horizontal:'medium'}}><h3>No Product available</h3></Box>: (
      <Table onMore={onMore}>
        <TableHeader labels={['ItemCode','Product Name','Category','Sub Category','Section','No. of Bins', 'Bin Size', 'Price', 'Class Type','ACTION']} />

        <tbody>{items}</tbody>
      </Table>
    );

    /*let addControl;
    if ('read only' !== role) {
      addControl = (
        <Anchor icon={<AddIcon />} path='/product/add'
          a11yTitle={`Add Product`} />
      );
    }*/
    let addControl = (<Anchor icon={<Add />} path='/product/add' a11yTitle={`Add Product`} onClick={this._onAddClick.bind(this)}/>);
    let uploadControl = (<Anchor icon={<UploadIcon />} path='/product/upload' a11yTitle={`Upload Product`} onClick={this._onUploadClick.bind(this)}/>);
    let downloadControl = (<CSVLink data={productsDownload} filename={'Products.csv'}  ><DownloadIcon /></CSVLink>);
    let helpControl = (<Button icon={<HelpIcon />}  onClick={this._onHelpClick.bind(this)}/>);
    let syncControl = (<Button icon={<SyncIcon />}  onClick={this._onSyncClick.bind(this)}/>);


    return (
      <Box full='horizontal'>
        <AppHeader/>

        <Header size='large' pad={{ horizontal: 'medium' }}>
          <Title>
            <span>{this.localeData.label_product}</span>
          </Title>
          <Search inline={true} fill={true} size='medium' placeHolder='Search'
            value={searchText} onDOMChange={this._onSearch.bind(this)} />
          {uploadControl}
          {downloadControl}
          {addControl}
          {syncControl}
          <FilterControl filteredTotal={filteredCount}
            unfilteredTotal={unfilteredCount}
            onClick={this._onFilterActivate.bind(this)} />
            {helpControl}
        </Header>

        <Section direction="column" pad={{vertical: 'large', horizontal:'small'}}>
          <Box size="xsmall" alignSelf="center" pad={{horizontal:'medium'}}>{loading}</Box>
          <Box full="horizontal" wrap={true} size='full'>
            {productItem}
          </Box>
        </Section>
        {layerFilter}
        {layerView}
      </Box>
    );
  }
}

Product.contextTypes = {
  router: React.PropTypes.object.isRequired
};

let select = (store) => {
  return { category: store.category, misc: store.misc};
};

export default connect(select)(Product);
