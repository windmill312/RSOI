import React, { Component } from "react";
import "../../styles/Login.css";
import PropTypes from 'prop-types';
import TextFieldGroup from '../common/TextFieldGroup';
import { login } from '../../actions/AuthActions';
import {connect} from "react-redux";
import validateInput from '../../validations/Login';
import {addFlashMessage} from "../../actions/FlashMessages";
import {SERVICE_UUID} from '../../config';

class LoginForm extends Component {
    constructor(props) {
        super(props);

        this.state = {
            identifier: "",
            password: "",
            serviceUuid: `${SERVICE_UUID}`,
            errors: {},
            isLoading: false
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    };

    handleChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    isValid() {
        const { errors, isValid } = validateInput(this.state);

        if (!isValid) {
            this.setState({ errors });
        }

        return isValid;
    }

    handleSubmit(e) {
        e.preventDefault();
        if (this.isValid()) {
            this.setState({ errors: {}, isLoading: true });
            this.props.login(this.state)
                .then(
                    () => {
                        localStorage.setItem('identifier', this.state.identifier);
                        sessionStorage.setItem('password', this.state.password);
                        const url = localStorage.getItem('url');
                        if (url !== null) {
                            this.context.router.push(`${url}`);
                        } else
                            this.context.router.push(`/tickets`);
                    }
                )
                .catch(
                    (err) => {
                        if (err.response) {
                            this.setState({errors: err.response.data.message, isLoading: false});
                            this.props.addFlashMessage({
                                type: 'error',
                                text: 'Проверьте правильность введенных данных!'
                            })
                        }
                        else {
                            this.setState({isLoading: false});
                            this.props.addFlashMessage({
                                type: 'error',
                                text: 'Сервис временно недоступен!'
                            })
                        }
                    }

                );
        }
    }

    render() {
        const { errors, identifier, password, isLoading } = this.state;

        return (
            <div className="Login">
                <form onSubmit={this.handleSubmit}>
                    { errors.form && <div className="alert alert-danger">{errors.form}</div> }

                    <TextFieldGroup
                        field="identifier"
                        label="Логин / Email"
                        value={identifier}
                        error={errors.identifier}
                        onChange={this.handleChange}
                    />

                    <TextFieldGroup
                        field="password"
                        label="Пароль"
                        value={password}
                        error={errors.password}
                        onChange={this.handleChange}
                        type="password"
                    />

                    <div className="form-group"><button className="btn btn-primary btn-lg" disabled={isLoading}>Войти</button></div>
                </form>
            </div>
        );
    };
}

LoginForm.propTypes = {
    login: PropTypes.func.isRequired,
    addFlashMessage: PropTypes.func.isRequired
};

LoginForm.contextTypes = {
    router: PropTypes.object.isRequired
};

export default connect(null, { login, addFlashMessage })(LoginForm);