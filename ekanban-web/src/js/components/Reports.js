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
// import Header from 'grommet/components/Header';
//import Heading from 'grommet/components/Heading';
// import HelpIcon from 'grommet/components/icons/base/Help';
// import Search from 'grommet/components/Search';
// import Title from 'grommet/components/Title';
// import Menu from 'grommet/components/Menu';
// import Anchor from 'grommet/components/Anchor';
// import Form from 'grommet/components/Form';
// //import Footer from 'grommet/components/Footer';
// import FormFields from 'grommet/components/FormFields';
// import FormField from 'grommet/components/FormField';


class Report extends Component {
  render () {

    const items = [];
    for (var i = 0; i < 100; i++) {
      var e = (<tr><td>Hello</td></tr>);
      items.push(e);
    }
    return (
      <Box>
        <AppHeader />

        <Section>
          <Box pad={{vertical: 'large'}}>
            <Box align='center' alignSelf='center' pad={{vertical: 'large'}}>
              Coming soon ...
            </Box>
            <table>
              {items}
            </table>
            {/*<Box size='medium' >
              <Form>
                <table style={{align: 'center'}}>
                  <tbody>
                  <tr >
                    <td style={{paddingRight: 20, paddingBottom: 10}}>
                      <FormField label="Product Name*" error='Product name cannot be blank'>
                        <input type="text" name="name" />
                      </FormField>
                    </td>
                    <td style={{paddingLeft: 20, paddingBottom: 10}}>
                      <FormField label="Product Name*">
                        <input type="text" name="name" />
                      </FormField>
                    </td>
                  </tr>
                  <tr>
                    <td style={{paddingRight: 20, paddingBottom: 10}}>
                      <FormField label="Product Name*">
                        <input type="text" name="name" />
                      </FormField>
                    </td>
                    <td style={{paddingLeft: 20, paddingBottom: 10}}>
                      <FormField label="Product Name*">
                        <input type="text" name="name" />
                      </FormField>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </Form>
            </Box>*/}
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
