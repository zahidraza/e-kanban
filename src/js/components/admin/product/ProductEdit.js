import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { localeData } from '../../../reducers/localization';
import {updateProduct}  from '../../../actions/product';
import {PRODUCT_CONSTANTS as c}  from '../../../utils/constants';

import AddIcon from "grommet/components/icons/base/Add";
import AppHeader from '../../AppHeader';
import Article from 'grommet/components/Article';
import Header from 'grommet/components/Header';
import Heading from 'grommet/components/Heading';
import Form from 'grommet/components/Form';
import Footer from 'grommet/components/Footer';
import FormFields from 'grommet/components/FormFields';
import FormField from 'grommet/components/FormField';
import Box from 'grommet/components/Box';
import Section from 'grommet/components/Section';
import Select from 'grommet/components/Select';
import Button from 'grommet/components/Button';
import CloseIcon from 'grommet/components/icons/base/Close';
import TrashIcon from 'grommet/components/icons/base/Trash';
import Anchor from 'grommet/components/Anchor';
import Layer from 'grommet/components/Layer';
import List from 'grommet/components/List';
import ListItem from 'grommet/components/ListItem';
import Spinning from 'grommet/components/icons/Spinning';


class ProductEdit extends Component {

  constructor () {
    super();

    this.state = {
      initializing: false,
      product: {},
      errors: [],
      layer: {
        name: '',                             // name of layer under operation [section|supplier]
        "section": {
          title: 'Add Section',
          label: 'Sections',
          filterValue: 'Select Section',
          filterItems: [],                    //Available items for selection
          selectedItems: []                   //Selected Items
        },
        "supplier": {
          title: 'Add Supplier',
          label: 'Suppliers',
          filterValue: 'Select Supplier',
          filterItems: [],                     //Available items for selection
          selectedItems: []                     //Selected Items
        }
      }
      
    };

    this.localeData = localeData();
  }

  componentWillMount () {
    if (!this.props.misc.initialized) {
      this.context.router.push('/product');
    }

    const {section: {sections},supplier: {suppliers},category: {categories, product}} = this.props;
    let {layer,category,subCategory} = this.state;

    const list = categories.map(c => c.name);
    layer.section.filterItems = sections.filter(s => !product.sectionList.map(p => p.name).includes(s.name)).map(sec => sec.name);
    layer.supplier.filterItems = suppliers.filter(s => !product.supplierList.map(p => p.name).includes(s.name)).map(sup => sup.name);

    layer.section.selectedItems = sections.filter(s => product.sectionList.map(p => p.name).includes(s.name));
    layer.supplier.selectedItems = suppliers.filter(s => product.supplierList.map(p => p.name).includes(s.name));

    this.setState({categories: list, category, subCategory, layer, product});
  }

  componentWillReceiveProps (nextProps) {
    if (!nextProps.category.editingProduct) {
      this.context.router.push('/product');
    }
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');
    }
  }

  _onSubmit (event) {     //Main Form Submit
    event.preventDefault();
    let {product,layer} = this.state;

    let url = window.serviceHost + '/categories/' + product.category.id + '/subCategories/' + product.subCategory.id + '/products/' + product.id;
    
    product.sections = layer.section.selectedItems.map(s => s.links[0].href);
    product.suppliers = layer.supplier.selectedItems.map(s => s.links[0].href);

    if (Object.getOwnPropertyNames(product.sections).length === 0) {
      delete product.sections;
    }
    if (Object.getOwnPropertyNames(product.suppliers).length === 0) {
      delete product.suppliers;
    }
    delete product.category;
    delete product.subCategory;
    delete product.sectionList;
    delete product.supplierList;

    console.log(url);
    console.log(product);

    this.props.dispatch(updateProduct(url,product));
  }

  _onInputChange (event) {
    let product = this.state.product;
    product[event.target.getAttribute('name')] = event.target.value;
    this.setState({product: product});
  }

  _cFilter (event) {  //Category Filter
    let {category} = this.state;
    category = event.value;
    //Set subcategory items for the selected category
    const {categories} = this.props.category;
    let i = categories.findIndex(c=> c.name === category);
    const subCategories = categories[i].subCategoryList.map(sc => sc.name);
    this.setState({category: category, subCategories: subCategories, subCategory: 'Select Sub Category'});
  }

  _scFilter (event) { //Sub Category Filter
    let {subCategory} = this.state;
    subCategory = event.value;
    this.setState({subCategory: subCategory});
  }

  _onClose (event) {  //Main form close
    this.props.dispatch({type: c.PRODUCT_EDIT_FORM_TOGGLE, payload: {adding: false}});
  }

  _onLayerSubmit (event) {
    event.preventDefault();
    let {layer} = this.state;
    const {section: {sections}, supplier: {suppliers}} = this.props;

    if (! layer.section.filterValue.includes('Select')) {   
      layer.section.filterItems = layer.section.filterItems.filter(s => s != layer.section.filterValue);
      layer.section.selectedItems = sections.filter(s => !layer.section.filterItems.includes(s.name));
    }
    if (! layer.supplier.filterValue.includes('Select')) {
      layer.supplier.filterItems = layer.supplier.filterItems.filter(s => s != layer.supplier.filterValue);
      layer.supplier.selectedItems = suppliers.filter(s => !layer.supplier.filterItems.includes(s.name));
    }

    layer.section.filterValue = 'Select Section';
    layer.supplier.filterValue = 'Select Supplier';
    layer.name = '';
    this.setState({layer: layer});
  }

  _onLayerClose () {
    let {layer} = this.state;
    layer.name = '';
    layer.section.filterValue = 'Select Section';
    layer.supplier.filterValue = 'Select Supplier';
    this.setState({layer: layer});
  }

  _onLayerSelect (name,event) {
    let {layer} = this.state;
    layer[name].filterValue = event.value;
    this.setState({layer: layer});
  }

  _onLayerSearch () {
    console.log('_onLayerSearch');
  }

  _onRemove (name,index,event) {
    let {layer} = this.state;
    let rItem = layer[name].selectedItems[index];
    layer[name].selectedItems = layer[name].selectedItems.filter(s => s.id != rItem.id);
    layer[name].filterItems.push(rItem.name);
    this.setState({layer: layer});
  }

  _onAdd (name,event) {   // On Section/Supplier Add Click. Show corresponding layer for adding.
    console.log('_onAdd');
    let {layer} = this.state;
    layer.name = name;
    layer.adding = true;
    this.setState({layer: layer});
  }

  _renderLayer (name) {
    const {layer} = this.state;

    let result;
    if (name !== '') {
      result = (
        <Layer align="right" closer={true} onClose={this._onLayerClose.bind(this)}
          a11yTitle={layer[name].title}>
          
          <Form onSubmit={this._onLayerSubmit.bind(this)} compact={false}>
            <Header>
              <Heading tag="h2" margin='none'>{layer[name].title}</Heading>
            </Header>
            <Box pad={{vertical: 'medium'}}/>
            <FormFields>
              <fieldset>
                <FormField htmlFor="name" label={layer[name].label} error=''>
                  <Select id="name" name="name"
                    value={layer[name].filterValue}
                    options={layer[name].filterItems}
                    onChange={this._onLayerSelect.bind(this,name)}
                    onSearch={this._onLayerSearch.bind(this,name)} />
                </FormField>
              </fieldset>
            </FormFields>
            <Footer pad={{vertical: 'medium'}}>
              <Button type="submit" primary={true} label="OK"
                onClick={this._onLayerSubmit.bind(this)} />
            </Footer>
          </Form>
        </Layer>
      );
    }
    return result;
  }

  _renderFields (name) {
    const {layer} = this.state;
    let selected = layer[name].selectedItems;

    const selectedFields = selected.map((s, index) => {
      return (
        <ListItem key={index} justify="between" pad="none"
          separator={index === 0 ? 'horizontal' : 'bottom'}
          responsive={false}>
          <span>{s.name}</span>
          <Button icon={<TrashIcon />}
            onClick={this._onRemove.bind(this,name,index)}
            a11yTitle={`Remove Section`} />
        </ListItem>
      );
    });

    return (
      <fieldset>
        <Header size="small" justify="between">
          <Heading tag="h3">{layer[name].label}</Heading>
          <Button icon={<AddIcon />} onClick={this._onAdd.bind(this,name)}
            a11yTitle={layer[name].title} />
        </Header>
        <List>
          {selectedFields}
        </List>
      </fieldset>
    );
  }

  render () {
    const {product,layer} = this.state;
    const {errorProduct: errors, busy} = this.props.category;

    const layerControl = this._renderLayer(layer.name);
    const sectionFields = this._renderFields('section');
    const supplierFields = this._renderFields('supplier');

    const busyIcon = busy ? <Spinning /> : null;

    return (
      <Box>
        <AppHeader/>
        <Section>
          <Article align="center" pad={{horizontal: 'medium'}} primary={true}>
            <Form onSubmit={this._onSubmit}>

              <Header size="large" justify="between" pad="none">
                <Heading tag="h2" margin="none" strong={true}>{this.localeData.label_product_edit}</Heading>
                <Anchor icon={<CloseIcon />} path="/product" a11yTitle='Close Add Product Form' onClick={this._onClose.bind(this)} />
              </Header>

              <FormFields>

                <fieldset>

                  <FormField label="Product Name" error={errors.name}>
                    <input type="text" name="name" value={product.name} onChange={this._onInputChange.bind(this)} />
                  </FormField>
                  <FormField label="Product Description" error={errors.description}>
                    <input type="text" name="description" value={product.description} onChange={this._onInputChange.bind(this)} />
                  </FormField>
                  <FormField label="Item Code" error={errors.itemCode}>
                    <input type="text" name="itemCode" value={product.itemCode} onChange={this._onInputChange.bind(this)} />
                  </FormField>
                  <FormField label="Price" error={errors.price}>
                    <input type="text" name="price" value={product.price} onChange={this._onInputChange.bind(this)} />
                  </FormField>
                  <FormField label="Minimum Order Quantity" error={errors.minOrderQty}>
                    <input type="text" name="minOrderQty" value={product.minOrderQty} onChange={this._onInputChange.bind(this)} />
                  </FormField>
                  <FormField label="Packet Size" error={errors.packetSize}>
                    <input type="text" name="packetSize" value={product.packetSize} onChange={this._onInputChange.bind(this)} />
                  </FormField>
                  
                </fieldset>

                <fieldset>
                  <Box direction="row" justify="between">
                    <Heading tag="h3">Lead Times</Heading>
                  </Box>
                  <FormField label="Ordering time" error={errors.timeOrdering}>
                    <input type="text" name="timeOrdering" value={product.timeOrdering} onChange={this._onInputChange.bind(this)} />
                  </FormField>
                  <FormField label="Procurement Time" error={errors.timeProcurement}>
                    <input type="text" name="timeProcurement" value={product.timeProcurement} onChange={this._onInputChange.bind(this)} />
                  </FormField>
                  <FormField label="Transportation Time" error={errors.timeTransporation}>
                    <input type="text" name="timeTransporation" value={product.timeTransporation} onChange={this._onInputChange.bind(this)} />
                  </FormField>
                  <FormField label="Buffer Time" error={errors.timeBuffer}>
                    <input type="text" name="timeBuffer" value={product.timeBuffer} onChange={this._onInputChange.bind(this)} />
                  </FormField>
                </fieldset>

                <fieldset>
                  <Box direction="row" justify="between">
                    <Heading tag="h3">Units of Measurement</Heading>
                  </Box>
                  <FormField label="Purchase" error={errors.uomPurchase}>
                    <input type="text" name="uomPurchase" value={product.uomPurchase} onChange={this._onInputChange.bind(this)} />
                  </FormField>
                  <FormField label="Consumption" error={errors.uomConsumption}>
                    <input type="text" name="uomConsumption" value={product.uomConsumption} onChange={this._onInputChange.bind(this)} />
                  </FormField>
                  <FormField label="Conversion Factor" error={errors.conversionFactor}>
                    <input type="text" name="conversionFactor" value={product.conversionFactor} onChange={this._onInputChange.bind(this)} />
                  </FormField>
                </fieldset>

                {sectionFields}
                {supplierFields}

              </FormFields>

              <Footer pad={{vertical: 'medium'}}>
                <span />
                <Button icon={busyIcon} type="submit" primary={true} label={this.localeData.product_edit_btn}
                  onClick={this._onSubmit.bind(this)} />
              </Footer>
            </Form>
          </Article>
          {layerControl}
        </Section>
      </Box>
      
    );
  }
}

ProductEdit.contextTypes = {
  router: PropTypes.object
};

let select = (store) => {
  return {category: store.category, section: store.section, supplier: store.supplier, misc: store.misc};
};

export default connect(select)(ProductEdit);

/*result = (
  <LayerForm compact={true} align="top" title={layer[name].title} submitLabel="OK"
    onClose={this._onLayerClose.bind(this)} onSubmit={this._onLayerSubmit.bind(this)}
    secondaryControl={removeControl}>
    <fieldset>
      <FormField htmlFor="name" label={layer[name].label} error=''>
        <Select id="name" name="name"
          value={layer[name].filterValue}
          options={layer[name].filterItems}
          onChange={this._onLayerSelect.bind(this,name)}
          onSearch={this._onLayerSearch.bind(this,name)} />
      </FormField>
    </fieldset>
  </LayerForm>
);*/

