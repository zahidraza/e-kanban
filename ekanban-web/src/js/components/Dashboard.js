import React, {Component} from 'react';
import { connect } from 'react-redux';
import { localeData } from '../reducers/localization';
import {initialize}  from '../actions/misc';

import AppHeader from './AppHeader';
import Box from 'grommet/components/Box';
import Header from 'grommet/components/Header';
import Heading from 'grommet/components/Heading';
import Section from 'grommet/components/Section';
import Spinning from 'grommet/components/icons/Spinning';
import AnnotatedMeter from 'grommet-addons/components/AnnotatedMeter';
import Tiles from 'grommet/components/Tiles';
import Tile from 'grommet/components/Tile';

class Dashboard extends Component {
  constructor () {
    super();
    this.state = {
      initializing: false,
      classType: {a: 0, b: 0, c : 0},
      classTypeValue: {a: 0, b: 0, c : 0}
    };

    this._loadReport = this._loadReport.bind(this);
  }

  componentWillMount () {
    console.log("componentWillMount");
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    } else {
      this._loadReport(this.props.category.products);
    }
    this.setState({localeData: localeData()});
  }

  componentWillReceiveProps (nextProps) {
    console.log("componentWillReceiveProps");
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({initializing: false});
      this._loadReport(nextProps.category.products);
    }
  }

  _loadReport (products) {
    let classType = {}, classTypeValue = {};
    const a = products.filter(p => p.classType == 'CLASS_A');
    const b = products.filter(p => p.classType == 'CLASS_B');
    const c = products.filter(p => p.classType == 'CLASS_C');

    classType.a = a.length;
    classType.b = b.length;
    classType.c = c.length;

    let tmpSum = 0;
    a.forEach(p => {
      tmpSum += p.price*p.stkOnFloor;
    });
    classTypeValue.a = parseFloat((tmpSum/100000).toFixed(2));
    tmpSum = 0;
    b.forEach(p => {
      tmpSum += p.price*p.stkOnFloor;
    });
    classTypeValue.b = parseFloat((tmpSum/100000).toFixed(2));
    tmpSum = 0;
    c.forEach(p => {
      tmpSum += p.price*p.stkOnFloor;
    });
    classTypeValue.c = parseFloat((tmpSum/100000).toFixed(2));

    console.log(typeof classTypeValue.a);

    this.setState({classType, classTypeValue});
  }


  render () {
    const {initializing,classType,classTypeValue} = this.state;



    if (initializing) {
      return (
        <Box pad={{vertical: 'large'}}>
          <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
            <Spinning /> Initializing Application ...
          </Box>
        </Box>
      );
    }

    return (
      <Box>
        <AppHeader />
        <Section direction="column" pad={{vertical: 'large', horizontal:'small'}}>

        <Tiles fill={true}
          flush={false}>
          <Tile separator='all'>
            <Header align='start' full='horizontal' size='medium' fill={true} pad={{vertical: 'small'}}>
              <Heading tag="h3" strong={true}>Number of Items</Heading>
            </Header>
            <AnnotatedMeter legend={true}
              type='circle'
              size='medium'
              series={[{"label": "Class A", "value": classType.a, "colorIndex": "graph-1"}, {"label": "Class B", "value": classType.b, "colorIndex": "graph-2"},{"label": "Class C", "value": classType.c, "colorIndex": "graph-3"}]} />
          </Tile>
          <Tile separator='all'>
          <Header align='start' full='horizontal' size='medium' fill={true} pad={{vertical: 'small'}}>
            <Heading tag="h3" strong={true}>Current Stock</Heading>
          </Header>
            <AnnotatedMeter legend={true}
              type='circle'
              units='lacs'
              size='medium'
              series={[{"label": "Class A", "value": classTypeValue.a, "colorIndex": "graph-1"}, {"label": "Class B", "value": classTypeValue.b, "colorIndex": "graph-2"}, {"label": "Class C", "value": classTypeValue.c, "colorIndex": "graph-3"}]} />
          </Tile>
        </Tiles>


        </Section>
      </Box>
    );
  }
}

Dashboard.contextTypes = {
  router: React.PropTypes.object.isRequired
};

let select = (store) => {
  return { nav: store.nav, misc: store.misc, inventory: store.inventory, category: store.category};
};

export default connect(select)(Dashboard);
