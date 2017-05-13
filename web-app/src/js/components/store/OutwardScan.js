import React, { Component } from 'react';
import { localeData } from '../../reducers/localization';
import { connect } from 'react-redux';
import {initialize} from '../../actions/misc';
import {updateInventory} from '../../actions/inventory';
import {BIN_STATE as bs} from '../../utils/constants';

import AppHeader from '../AppHeader';
import Box from 'grommet/components/Box';
import Button from 'grommet/components/Button';
import Section from 'grommet/components/Section';
import Spinning from 'grommet/components/icons/Spinning';
// import Header from 'grommet/components/Header';
// import Heading from 'grommet/components/Heading';
import List from 'grommet/components/List';
import ListItem from 'grommet/components/ListItem';
import Toast from 'grommet/components/Toast';

class OutwardScan extends Component {
  
  constructor () {
    super();
    this.state = {
      initializing: false,
      binId: '',
      bin: {},
      available: false,
      notFound: false,
      show: false,
      toast: { status: '', message: ''}
    };
    this.localeData = localeData();
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
    if (this.props.inventory.message == '' && nextProps.inventory.message != '') {
      const message = nextProps.inventory.message;
      let toast = {};
      if (message.includes('error')) {
        toast.status = 'critical';
      } else {
        toast.status = 'ok';
      }
      toast.message = message;
      this.setState({toast, show: true, binId: '', bin: {}, available: false});
    }
  }

  _onChange (event) {
    let binId = event.target.value;
    let bin = {};
    let available = false;
    let notFound = false;

    //Complete Product Id Entered
    if (binId.length == 13) {
      const inventory = this.props.inventory.inventory;
      const i = inventory.findIndex(inv => inv.binId === binId);
      if (i == -1) {
        notFound = true;
      }else {
        const inv = inventory[i];
        //Find Product to update info
        const products = this.props.category.products;
        const j = products.findIndex(p => p.id === inv.productId);
        const product = products[j];
        //Find all bins of this product
        const allBins = inventory.filter(i => i.prodId == inv.prodId);
        const inStock = allBins.filter(b => b.binState == bs.STORE).length;
        const inPurchase = allBins.filter(b => b.binState == bs.PURCHASE).length;
        const inOrder = allBins.filter(b => b.binState == bs.ORDERED).length;

        bin = {...inv, ...product, inStock, inPurchase, inOrder};
        available = true;
        binId = '';
      }
      
    }

    this.setState({binId, bin, available,notFound});
  }

  _onCloseToast () {
    this.setState({show: false});
    document.getElementById("barCodeInput").focus();
  }

  _scan () {
    console.log("_scan()");
    const bin = this.state.bin;
    const inv = {url: bin.links[0].href, binState: 'PURCHASE'};
    this.props.dispatch(updateInventory(inv));
    this.setState({available: false});
    document.getElementById("barCodeInput").focus();
  }

  render() {
    const {initializing, bin,available,notFound,show,toast,binId} = this.state;

    if (initializing) {
      return (
        <Box pad={{vertical: 'large'}}>
          <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
            <Spinning /> Initializing Application ...
          </Box>
        </Box>
      );
    }

    const toastControl = !show ? null : ( 
      <Toast status={toast.status}
        onClose={this._onCloseToast.bind(this)}>
        {toast.message}
      </Toast>
    );

    let scanBtn;
    if (available) {
      scanBtn = bin.binState != bs.STORE ? <h3>Bin is not in Store.</h3> : <Button label="Scan" onClick={this._scan.bind(this)} />;
    }

    const binNotFound = notFound ? <Box size='small' alignSelf='center'  pad={{vertical: 'large', horizontal:'small'}}> <h3>Bin Not Found</h3> </Box> : null;

    const binItem = !available ? null : (
      <Box size='large' alignSelf='center'  pad={{vertical: 'large', horizontal:'small'}}>
        <Box>
        <List>
          <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
            <span> Product </span>
            <span className="secondary">{bin.name}</span>
          </ListItem>
          <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
            <span> Bin Id </span>
            <span className="secondary">{bin.binId}</span>
          </ListItem>
          <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
            <span> Item Code </span>
            <span className="secondary">{bin.itemCode}</span>
          </ListItem>
          <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
            <span> Bins in Stock </span>
            <span className="secondary">{bin.inStock}</span>
          </ListItem>
          <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
            <span> Bins in Purchase </span>
            <span className="secondary">{bin.inPurchase}</span>
          </ListItem>
          <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
            <span> Bins in Order </span>
            <span className="secondary">{bin.inOrder}</span>
          </ListItem>
          <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
            <span> Card </span>
            <span className="secondary">{bin.binNo + '/' + bin.noOfBins}</span>
          </ListItem>
          <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
            <span> Category </span>
            <span className="secondary">{bin.category.name}</span>
          </ListItem>
          <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
            <span> Sub Category </span>
            <span className="secondary">{bin.subCategory.name}</span>
          </ListItem>
          <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
            <span> Class </span>
            <span className="secondary">{bin.classType}</span>
          </ListItem>
          <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
            <span> Minimum Order Quantity </span>
            <span className="secondary">{bin.minOrderQty}</span>
          </ListItem>
          <ListItem justify="between" pad={{vertical:'small',horizontal:'small'}} >
            <span> Packet Size </span>
            <span className="secondary">{bin.packetSize}</span>
          </ListItem>
        </List>
        </Box>
        <Box alignSelf='center' pad={{vertical: 'medium'}}>
          {scanBtn}
        </Box>
        
      </Box>
    );

    return (
      <Box>
        <AppHeader/>
        <Section>
          <Box size='medium' alignSelf='center' pad={{vertical: 'large'}}>
            <input type='text' name='productId' id='barCodeInput' value={binId} placeholder="Enter or Scan Product Id" autoFocus onChange={this._onChange.bind(this)} />
          </Box>
          {binItem}
          {binNotFound}
        </Section>
        {toastControl}
      </Box>
    );
  }
}

OutwardScan.contextTypes = {
  router: React.PropTypes.object
};

let select = (store) => {
  return {misc: store.misc, category: store.category, inventory: store.inventory};
};

export default connect(select)(OutwardScan);
