import React from 'react';
import SignUpForm from "../SignUp/SignUpForm";
import {connect} from 'react-redux';
import {userSignUpRequest, isUserExists} from '../../actions/SignUpActions';
import PropTypes from 'prop-types';
import { addFlashMessage } from '../../actions/FlashMessages.js';


class SignUpPage extends React.Component {

    render() {
        const {userSignUpRequest, addFlashMessage} = this.props;
        return (
            <div className="row">
                <div className="col-md-4 col-md-offset-4">
                    <SignUpForm userSignUpRequest={userSignUpRequest} addFlashMessage={addFlashMessage} isUserExists={isUserExists}/>
                </div>
            </div>
        )
    }
}

SignUpPage.propTypes = {
    userSignUpRequest: PropTypes.func.isRequired,
    addFlashMessage: PropTypes.func.isRequired,
    isUserExists: PropTypes.func.isRequired
};

export default connect( null, {userSignUpRequest, addFlashMessage, isUserExists})(SignUpPage);