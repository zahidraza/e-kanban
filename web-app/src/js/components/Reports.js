import React, { Component } from 'react';
// import { localeData } from '../../reducers/localization';
import { connect } from 'react-redux';
// import {initialize} from '../../actions/misc';
// import {syncInventory} from '../../actions/inventory';
// import {getNoOfBins} from '../../utils/miscUtil';
//import moment from 'moment';

import AppHeader from './AppHeader';
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


class Report extends Component {
  render () {
    return (
      <Box>
        <AppHeader/>
        <Header size='large' pad={{ horizontal: 'medium' }}>
          <Title responsive={false}>
            <span>Report</span>
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

Report.contextTypes = {
  router: React.PropTypes.object
};

let select = (store) => {
  return {misc: store.misc, category: store.category, inventory: store.inventory, order: store.order, user: store.user};
};

export default connect(select)(Report);
