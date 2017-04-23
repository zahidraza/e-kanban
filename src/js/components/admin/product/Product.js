import React, { Component} from 'react';
import { connect } from 'react-redux';
import { localeData } from '../../../reducers/localization';
//import {getCategories} from '../../../actions/category';
import {removeProduct,syncProduct}  from '../../../actions/product';
import {initialize}  from '../../../actions/misc';
import {PRODUCT_CONSTANTS as c}  from '../../../utils/constants';

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
import ProductFilter from './ProductFilter';
import Table from 'grommet/components/Table';
import TableHeader from 'grommet/components/TableHeader';
import TableRow from 'grommet/components/TableRow';
import Trash from "grommet/components/icons/base/Trash";
//import Tiles from 'grommet/components/Tiles';
import Title from 'grommet/components/Title';
//import ProductTile from './ProductTile';
import UploadIcon from 'grommet/components/icons/base/Upload';
import SyncIcon from 'grommet/components/icons/base/Sync';

class Product extends Component {
  
  constructor () {
    super();
    this.state = {
      initializing: false,
      errors: [],
      products: [],
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
  }

  componentWillMount () {
    console.log('componentWillMount');
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }else{
      const {categories,filter,sort} = this.props.category;
      this._loadProduct(categories,filter,sort,this.state.page);
    }
  }

  componentWillReceiveProps (nextProps) {
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({initializing: false});
      const {categories,filter,sort} = nextProps.category;
      this._loadProduct(categories,filter,sort,this.state.page);
    }
    if (this.props.misc.initialized && !nextProps.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }
    if (this.props.category.toggleStatus != nextProps.category.toggleStatus) {
      const {categories,filter,sort} = nextProps.category;
      this._loadProduct(categories,filter,sort,this.state.page);
    }
  }

  _onMoreProducts () {
    const {categories,filter,sort} = this.props.category;
    let page = this.state.page;
    page = page+1;
    this._loadProduct(categories,filter,sort,page);
  }

  _loadProduct (categories,filter,sort,page) {
    console.log("_loadProduct()");
    let products = this.props.category.products;
    const unfilteredCount = products.length;
    console.log(filter);
    console.log('unfiltered =' + unfilteredCount);
    if ('class' in filter) {
      const classFilter = filter.class;
      console.log(classFilter);
      products = products.filter(p => classFilter.includes(p.classType));  
    }

    if ('category' in filter) {
      const categoryFilter = filter.category;
      products = products.filter(p => categoryFilter.includes(p.category.name));  
    }

    // const subCategoryFilter = filter.subCategory;
    // if (!subCategoryFilter.includes('All')) {
    //   products = products.filter(p => subCategoryFilter.includes(p.subCategory.name));
    // }

    // const sectionFilter = filter.section;
    // if (!sectionFilter.includes('All')) {
    //   products = products.filter(p => sectionFilter.includes(p.subCategory.name));
    // }

    const filteredCount = products.length;
    let productNotAvailable = false;
    if (filteredCount == 0) {
      productNotAvailable = true;
    }
    products = products.slice(0,20*page);
    //products = this._productSort(products,sort);
    this.setState({products, filteredCount, unfilteredCount, page, productNotAvailable}); 
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

  _onSearch () {
    console.log('_onSearch');
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
    console.log('_onAddClick');
    this.props.dispatch({type: c.PRODUCT_ADD_FORM_TOGGLE,payload: {adding: true}});
  }

  _onUploadClick () {
    console.log('_onUploadClick');
  }

  _onRemoveClick (index) {
    console.log('_onRemoveClick');
    const {products} = this.state;

    this.props.dispatch(removeProduct(products[index]));
  }

  _onEditClick (index) {
    console.log('_onEditClick');
    const {products} = this.state;
    this.props.dispatch({type: c.PRODUCT_EDIT_FORM_TOGGLE, payload:{editing: true, product: products[index]}});
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
    window.open("http://localhost:8080/help/product");
  }

  /*_renderProducts (products) {
    const items = products.map((p, index)=>{
      let sections = '  ';
      p.sectionList.forEach(s => sections += s.name + ', ');
      let suppliers = '  ';
      p.supplierList.forEach(s => suppliers += s.name + ', ');
      return (
        <ProductTile key={index} name={p.name} desc={p.description} category={p.category.name} subCategory={p.subCategory.name} 
          sections={sections.substring(0,sections.length-2).trim()} suppliers={suppliers.substring(0,suppliers.length-2).trim()} 
          id={p.id} itemCode={p.itemCode} price={p.price} classType={p.classType} 
          demand={p.demand} t1={p.timeOrdering} t2={p.timeProcurement} t3={p.timeTransporation} t4={p.timeBuffer} 
          uomP={p.uomPurchase} uomC={p.uomConsumption} cFactor={p.conversionFactor} kanbanType={p.kanbanType} 
          noOfBin={p.noOfBins} binQty={p.binQty} pktSize={p.packetSize} />

      );
    });
    return items;
  }*/

  _renderProducts (products) {
    const items = products.map((p, index)=>{
      let sections = '  ';
      p.sectionList.forEach(s => sections += s.name + ', ');
      // let suppliers = '  ';
      // p.supplierList.forEach(s => suppliers += s.name + ', ');
      return (
        <TableRow key={index}  >
          <td >{p.productId}</td>
          <td>{p.itemCode}</td>
          <td >{p.name}</td>
          <td >{p.category.name}</td>
          <td >{p.subCategory.name}</td>
          <td >{sections.substring(0,sections.length-2).trim()}</td>
          {/*<td >{suppliers.substring(0,suppliers.length-2).trim()}</td>*/}
          <td >{p.noOfBins}</td>
          <td>{p.price}</td>
          <td>{p.classType}</td>
          <td style={{textAlign: 'right', padding: 0}}>
            <Button icon={<Edit />} onClick={this._onEditClick.bind(this,index)} />
            <Button icon={<Trash />} onClick={this._onRemoveClick.bind(this,index)} />
          </td>
        </TableRow>

      );
    });
    return items;
  }

  render() {
    const {refreshing} = this.props.category;
    const { products, searchText, filterActive, filteredCount, unfilteredCount, initializing, productNotAvailable } = this.state;

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

    const items = this._renderProducts(products);

    const layerFilter = filterActive ? <ProductFilter onClose={this._onFilterDeactivate.bind(this)}/> : null;

    let productItem = productNotAvailable ? <Box size="medium" alignSelf="center" pad={{horizontal:'medium'}}><h3>No Product available</h3></Box>: (
      <Table scrollable={true} onMore={this._onMoreProducts.bind(this)}>
        <TableHeader labels={['Product Id','ItemCode','Product Name','Category','Sub Category','Section','No. of Bins', 'Price', 'Class Type','ACTION']} />
        
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
    let helpControl = (<Button icon={<HelpIcon />}  onClick={this._onHelpClick.bind(this)}/>);
    let syncControl = (<Button icon={<SyncIcon />}  onClick={this._onSyncClick.bind(this)}/>);


    return (
      <Box >
        <AppHeader/>

        <Header size='large' pad={{ horizontal: 'medium' }}>
          <Title>
            <span>{this.localeData.label_product}</span>
          </Title>
          <Search inline={true} fill={true} size='medium' placeHolder='Search'
            value={searchText} onDOMChange={this._onSearch.bind(this)} />
          {uploadControl}
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
            {/*<Tiles fill={true} flush={true}>
              {items}
            </Tiles>*/}
            {productItem}
          </Box>
        </Section>
        {layerFilter}
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
