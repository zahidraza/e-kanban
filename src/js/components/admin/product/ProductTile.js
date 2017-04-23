import React, { Component } from 'react';
// import { localeData } from '../reducers/localization';

// import AppHeader from './AppHeader';
import Box from 'grommet/components/Box';
// import Button from 'grommet/components/Button';
// import Section from 'grommet/components/Section';
//import Table from 'grommet/components/Table';
//import TableRow from 'grommet/components/TableRow';
// import Tiles from 'grommet/components/Tiles';
import Tile from 'grommet/components/Tile';

//import * as userAction  from '../actions/user';

class ProductTile extends Component {
  
  constructor () {
    super();
  }

  render() {
    const {name, desc, category, subCategory, sections, suppliers, id, itemCode, price, classType, demand, t1, t2, t3, t4, uomP, uomC, cFactor, kanbanType, noOfBin, binQty, pktSize} = this.props;
    
    const secs = (sections == '') ? "Section Not Found" : sections;
    const sups = (suppliers == '') ? "Supplier Not Found" : suppliers;
    const iCode = (itemCode == null) ? 'ItemCode' : itemCode;
    const description = (desc == null) ? "Description not available" : desc;
    const cType = (classType == null) ? "Class Type" : classType;
    const dmd = (demand == null) ? 'Demand' : demand;
    const kType = (kanbanType == null) ? 'KanbanType' : kanbanType;
    const noOfBins = (noOfBin == null) ? 'No. Of Bins' : noOfBin;
    const bQty = (binQty == null) ? 'Bin Qty' : binQty;
    const packetSize = (pktSize == null) ? 'Packet Size' : pktSize;
    return (
      <Tile >
        <Box direction="column" size="large" colorIndex="light-2" alignSelf="center" pad={{horizontal: 'small', vertical: 'none', between: 'small'}}
          margin={{horizontal: 'small', vertical: 'small'}} >
              
          <Box direction="row" pad={{horizontal: 'small', vertical: 'none', between: 'none'}}>
            <Box basis="full" align="start">{name}</Box>
          </Box>
          <Box direction="row" pad={{horizontal: 'small', vertical: 'none', between: 'none'}}>
            <Box  basis="full" align="start">{description}</Box>
          </Box>
          <Box direction="row" pad={{horizontal: 'small', vertical: 'none', between: 'none'}}>
            <Box basis="1/2" align="start">{category}</Box>
            <Box basis="1/2" align="end">{subCategory}</Box>
          </Box>
          <Box direction="row" pad={{horizontal: 'small', vertical: 'none', between: 'none'}}>
            <Box  basis="full" align="start">{secs}</Box>
          </Box>
          <Box direction="row" pad={{horizontal: 'small', vertical: 'none', between: 'none'}}>
            <Box  basis="full" align="start">{sups}</Box>
          </Box>
          <Box direction="row" pad={{horizontal: 'small', vertical: 'none', between: 'none'}}>
            <Box basis="1/3" align="start">{id}</Box>
            <Box basis="1/3" align="center">{iCode}</Box>
            <Box basis="1/3" align="end">Rs {price}</Box>
          </Box>
          <Box direction="row" pad={{horizontal: 'small', vertical: 'none', between: 'none'}}>
            <Box basis="1/2" align="start">{cType}</Box>
            <Box basis="1/2" align="end">{dmd}</Box>
          </Box>
          <Box direction="row" pad={{horizontal: 'small', vertical: 'none', between: 'none'}}>
            <Box basis="1/4" align="start">{t1} hrs</Box>
            <Box basis="1/4" align="center">{t2} hrs</Box>
            <Box basis="1/4" align="center">{t3} hrs</Box>
            <Box basis="1/4" align="end">{t4} hrs</Box>
          </Box>
          <Box direction="row" pad={{horizontal: 'small', vertical: 'none', between: 'none'}}>
            <Box basis="1/3" align="start">{uomP}</Box>
            <Box basis="1/3" align="center">{uomC}</Box>
            <Box basis="1/3" align="end">{cFactor}</Box>
          </Box>
          <Box direction="row" pad={{horizontal: 'small', vertical: 'none', between: 'none'}}>
            <Box basis="1/4" align="start">{kType}</Box>
            <Box basis="1/4" align="center">{noOfBins}</Box>
            <Box basis="1/4" align="center">{bQty}</Box>
            <Box basis="1/4" align="end">{packetSize}</Box>
          </Box>
        </Box>
      </Tile>
    );
  }
}

export default ProductTile;
