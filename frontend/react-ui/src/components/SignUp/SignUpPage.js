import React from 'react';
import { Button, FormGroup, FormControl, ControlLabel } from "react-bootstrap";
import LoginForm from "../Login/LoginForm";
import {connect} from 'react-redux';
import {userSignUpRequest} from '../../actions/SignUpActions';
import PropTypes from 'prop-types';
import { addFlashMessage } from '../../actions/FlashMessages.js';


class SignUpPage extends React.Component {

    render() {
        const {userSignUpRequest, addFlashMessage} = this.props;
        return (
            <div className="row">
                <div className="col-md-4 col-md-offset-4">
                    <LoginForm userSignUpRequest={userSignUpRequest} addFlashMessage={addFlashMessage}/>
                </div>
                
            </div>
        )
    }
}

SignUpPage.propTypes = {
    userSignUpRequest: PropTypes.func.isRequired,
    addFlashMessage: PropTypes.func.isRequired
};

export default connect( null, {userSignUpRequest, addFlashMessage})(SignUpPage);