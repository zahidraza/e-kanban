import React, { Component } from 'react';
import { localeData } from '../reducers/localization';
import { connect } from 'react-redux';
import {initialize} from '../actions/misc';
import moment from 'moment';

import AppHeader from './AppHeader';
import Box from 'grommet/components/Box';
import Section from 'grommet/components/Section';
import Spinning from 'grommet/components/icons/Spinning';

class Test extends Component {

  constructor () {
    super();
    this.state = {
      initializing: false
    };
    this.localeData = localeData();
  }

  componentWillMount () {
    console.log('componentWillMount');
    if (!this.props.misc.initialized) {
      this.setState({initializing: true});
      this.props.dispatch(initialize());
    }
    let x = moment('26 May, 17','DD MMM, YY');

    console.log(x.toDate().getTime());
  }

  componentWillReceiveProps (nextProps) {
    if (!this.props.misc.initialized && nextProps.misc.initialized) {
      this.setState({initializing: false});
    }
  }

  render() {
    const {initializing} = this.state;

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
        <AppHeader page={this.localeData.label_test}/>
        <Section>
          <h1>Test Navigation page1</h1>
          {moment(new Date()).add(3,'days').utcOffset('+05:30').format('DD MMM, YY')}

        </Section>
      </Box>
    );
  }
}

Test.contextTypes = {
  router: React.PropTypes.object
};

let select = (store) => {
  return {misc: store.misc};
};

export default connect(select)(Test);
