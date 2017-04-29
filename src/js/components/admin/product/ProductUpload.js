import React, { Component } from 'react';
import { localeData } from '../../../reducers/localization';
import { connect } from 'react-redux';
import {initialize}  from '../../../actions/misc';
import {uploadProducts}  from '../../../actions/product';
import {PRODUCT_CONSTANTS as c}  from '../../../utils/constants';

import AppHeader from '../../AppHeader';
import Article from 'grommet/components/Article';
import Anchor from 'grommet/components/Anchor';
import Box from 'grommet/components/Box';
import Button from 'grommet/components/Button';
import Section from 'grommet/components/Section';
import Dropzone from 'react-dropzone';
import Footer from 'grommet/components/Footer';
import Form from 'grommet/components/Form';
import FormField from 'grommet/components/FormField';
import FormFields from 'grommet/components/FormFields';
import Header from 'grommet/components/Header';
import Heading from 'grommet/components/Heading';
import Spinning from 'grommet/components/icons/Spinning';
import CloseIcon from 'grommet/components/icons/base/Close';
import Table from 'grommet/components/Table';
import TableHeader from 'grommet/components/TableHeader';
import TableRow from 'grommet/components/TableRow';

class ProductUpload extends Component {
  
  constructor () {
    super();
    this.state = {
      initializing: false,
      files: [],
      page: 1
    };
    this.localeData = localeData();
    this._renderErrors = this._renderErrors.bind(this);
  }

  componentWillMount () {
    console.log('componentWillMount upload');
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }
  }

  componentWillReceiveProps (nextProps) {
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({initializing: false});
    }
    if (!nextProps.category.uploading) {
      this.context.router.push('/product');
    }
    if (sessionStorage.session == undefined) {
      this.context.router.push('/');
    }
  }

  _onSubmit (e) {
    e.preventDefault();
    if (this.state.files.length == 0) {
      alert("Select file first.");
      return;
    }
    this.props.dispatch(uploadProducts(this.state.files[0]));
  }

  _onMoreErrors () {
    let page = this.state.page;
    this.setState({page: (page+1)});
  }

  _onDrop (files) {
    if (files.length > 1) {
      alert("Select Only 1 File.");
      this.setState({files: []});
      return;
    }
    this.setState({files: files});
  }

  _onClose (event) {  //Main form close
    this.props.dispatch({type: c.PRODUCT_UPLOAD_FORM_TOGGLE, payload: {uploading: false}});
  }

  _renderErrors () {
    let {errorsProduct: errors} = this.props.category;
    errors = errors.slice(0,15*this.state.page);
    if (errors.length > 0) {
      const items = errors.map((e,i) => {
        return (
          <TableRow key={i} >
            <td>{e.row}</td>
            <td>{e.column}</td>
            <td>{e.errorMessage}</td>
          </TableRow>
        );
      });
      return (
        <Box size='xxlarge' alignSelf='center'>
          <Box>
            <h3 style={{color: 'red'}}>Resolve Following errors and try uploading again.</h3> 
          </Box>
          <Box>
            <Table scrollable={true} onMore={this._onMoreErrors.bind(this)}>
              <TableHeader labels={['Row','Column','Error']} />
              <tbody>{items}</tbody>
            </Table>
          </Box>
        </Box>
      );

    } else {
      return null;
    }
  }

  render() {
    const {initializing, files} = this.state;
    const {busy: uploading} = this.props.category;

    if (initializing) {
      return (
        <Box pad={{vertical: 'large'}}>
          <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
            <Spinning /> Initializing Application ...
          </Box>
        </Box>
      );
    }
    
    const errors = this._renderErrors();

    const busy = uploading ? <Spinning /> : null;
    const content = files.length != 0 ? (<div>{files[0].name}</div>) : (<div>Drop file here or Click to open file browser</div>);
    const style = {
      width: 450,
      height: 100,
      borderWidth: 2,
      borderColor: '#666',
      borderStyle: 'dashed',
      borderRadius: 5,
      textAlign: 'center',
      paddingTop: 35,
      margin: 'auto'
    };

    return (
      <Box>
        <AppHeader page={this.localeData.label_test}/>
        <Section>
          <Article align="center" pad={{horizontal: 'medium'}} primary={true}>
            <Form onSubmit={this._onSubmit.bind(this)}>

              <Header size="large" justify="between" pad="none">
                <Heading tag="h2" margin="none" strong={true}>{this.localeData.label_product_upload}</Heading>
                <Anchor icon={<CloseIcon />} path="/product" a11yTitle='Close Upload Product Form' onClick={this._onClose.bind(this)} />
              </Header>

              <FormFields>
                <FormField label="Excel File containing Product data" error="" >
                  <Dropzone style={style} onDrop={this._onDrop.bind(this)} accept='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel' >
                    {content}
                  </Dropzone>
                </FormField>
              </FormFields>

              <Footer pad={{vertical: 'medium'}}>
                <span />
                <Button icon={busy} type="submit" primary={true} label={this.localeData.product_upload_btn}
                  onClick={this._onSubmit.bind(this)} />
              </Footer>
            </Form>
          </Article>
          {/*<Box size='xlarge' alignSelf='center'>
            <Box><h3>Resolve Following errors and try uploading again.</h3> </Box>
            <Box>
              <Table>
                <TableHeader labels={['Row number','Column','Error']} />
                <tbody>
                  <TableRow><td>Test</td><td>Test</td><td>Test</td></TableRow>
                </tbody>
              </Table>
            </Box>
          </Box>*/}
          {errors}
        </Section>
      </Box>
    );
  }
}

ProductUpload.contextTypes = {
  router: React.PropTypes.object
};

let select = (store) => {
  return {misc: store.misc, category: store.category};
};

export default connect(select)(ProductUpload);
