import React from 'react';
import validateInput from '../../validations/SignUp';
import TextFieldGroup from '../common/TextFieldGroup';
import PropTypes from 'prop-types';
import { isUserExists, userSignUpRequest } from '../../actions/SignUpActions';
import {connect} from "react-redux";
import { addFlashMessage } from '../../actions/FlashMessages.js';
import {SERVICE_UUID} from "../../config";

class SignUpForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            email: '',
            password: '',
            passwordConfirmation: '',
            errors: {},
            isLoading: false,
            invalid: false,
            serviceUuid: `${SERVICE_UUID}`
        };

        this.onChange = this.onChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.checkUserExists = this.checkUserExists.bind(this);
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    isValid() {
        const { errors, isValid } = validateInput(this.state);

        if (!isValid) {
            this.setState({ errors });
        }

        return isValid;
    }

    checkUserExists(e) {
        console.log(this.props);
        const field = e.target.name;
        const val = e.target.value;
        if (val !== '') {
            let errors = this.state.errors;
            let invalid;
            this.props.isUserExists(val)
                .then(res => {
                    if (res.status === 200) {
                        errors[field] = '';
                        invalid = false;
                        this.setState({ errors, invalid });
                    }
                })
                .catch(err => {
                    console.log('status = ' + err.status);
                    errors[field] = 'Такой пользователь уже существует: ' + val;
                    invalid = true;
                    this.setState({ errors, invalid });
                }
                );

        }
    }

    onSubmit(e) {
        e.preventDefault();

        if (this.isValid()) {
            this.setState({ errors: {}, isLoading: true });
            this.props.userSignUpRequest(this.state)
                .then(
                    res => {
                        this.props.addFlashMessage({
                            type: 'success',
                            text: 'Регистрация прошла успешно!'
                        });
                        this.context.router.push('/login');
                    },
                    (err) => this.setState({ errors: err.response.data, isLoading: false })
                )
                .catch(
                    error => {
                        console.log(error.status);
                        this.props.addFlashMessage({
                            type: 'error',
                            text: 'Произошла ошибка при регистрации!'
                        });
                });
        }
    }

    render() {
        const { errors } = this.state;
        return (
            <form onSubmit={this.onSubmit}>
                <h1>Присоединяйся!</h1>

                <TextFieldGroup
                    error={errors.username}
                    label="Имя"
                    onChange={this.onChange}
                    check={this.checkUserExists}
                    value={this.state.username}
                    field="username"
                />

                <TextFieldGroup
                    error={errors.email}
                    label="Email"
                    onChange={this.onChange}
                    check={this.checkUserExists}
                    value={this.state.email}
                    field="email"
                />

                <TextFieldGroup
                    error={errors.password}
                    label="Пароль"
                    onChange={this.onChange}
                    value={this.state.password}
                    field="password"
                    type="password"
                />

                <TextFieldGroup
                    error={errors.passwordConfirmation}
                    label="Подтверждение пароля"
                    onChange={this.onChange}
                    value={this.state.passwordConfirmation}
                    field="passwordConfirmation"
                    type="password"
                />

                <div className="form-group">
                    <button disabled={this.state.isLoading || this.state.invalid} className="btn btn-primary btn-lg">
                        Зарегистрироваться
                    </button>
                </div>
            </form>
        );
    }
}

SignUpForm.propTypes = {
    userSignUpRequest: PropTypes.func.isRequired,
    addFlashMessage: PropTypes.func.isRequired,
    isUserExists: PropTypes.func.isRequired
};

SignUpForm.contextTypes = {
    router: PropTypes.object.isRequired
};

export default connect(null, { isUserExists, addFlashMessage, userSignUpRequest })(SignUpForm);