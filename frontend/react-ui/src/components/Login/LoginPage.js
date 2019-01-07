import React from 'react';
import LoginForm from './LoginForm';
import PropTypes from 'prop-types';
import { addFlashMessage } from '../../actions/FlashMessages.js';
import connect from "react-redux/es/connect/connect";

class LoginPage extends React.Component {
    render() {
        const {addFlashMessage} = this.props;
        return (
            <div className="row">
                <div className="col-md-4 col-md-offset-4">
                    <LoginForm addFlashMessage={addFlashMessage}/>
                </div>
            </div>
        );
    }
}

LoginPage.propTypes = {
    addFlashMessage: PropTypes.func.isRequired
};

export default connect( null, {addFlashMessage})(LoginPage);