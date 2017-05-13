import React, { Component, PropTypes } from 'react';
import Footer from 'grommet/components/Footer';
import Label from 'grommet/components/Label';
import Notification from 'grommet/components/Notification';

export default class CustomNotification extends Component {
  constructor() {
    super();
    this._onClose = this._onClose.bind(this);
  }

  componentWillReceiveProps (nextProps) {
    this.setState({hidden:true});
  }

  _onClose() {
    if(this.props.onClose) {
      this.props.onClose();
    }
  }

  render() {
    if (!this.props.hidden) {
      return (
        <Footer margin="none" align="end" justify="start" full="horizontal" float={true}>
          {this.props.message?<Notification closer={true} id="ntfMessage" pad={{horizontal: "large", vertical:"small", between:"medium"}} status={this.props.status}  message={<Label truncate={true}><strong>{this.props.message}</strong></Label>} onClose={this._onClose}/> : ''}
        </Footer>);
    } else {
      return null;
    }
  }
}

CustomNotification.propTypes = {
  status:PropTypes.oneOf(['ok', 'critical', 'warning']).isRequired,
  onClose: PropTypes.func,
  message: PropTypes.string,
  hidden: PropTypes.bool.isRequired
};
