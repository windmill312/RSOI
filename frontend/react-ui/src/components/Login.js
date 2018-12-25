import React, { Component } from "react";
import { Button, FormGroup, FormControl, ControlLabel } from "react-bootstrap";
import { Link } from 'react-router-dom'
import "../styles/Login.css";
import axios from "axios";

export default class Login extends Component {
    constructor(props) {
        super(props);

        this.state = {
            email: "",
            password: ""
        };
    };

    validateForm() {
        return this.state.email.length > 0 && this.state.password.length > 0;
    };

    handleChange = event => {
        this.setState({
            [event.target.id]: event.target.value
        });
    };

    handleSubmit = async event => {
        event.preventDefault();
        const requestData = {
            usernameOrEmail:this.state.email,
            password:this.state.password,
            serviceUuid: "ede4bfb8-2acb-441e-9b00-4b786309fcd2"
        };
        axios.post(`http://localhost:8090/api/auth/signin`, requestData,
            {
                headers: {'Content-Type': 'application/json'}
            })
            .then((result) => {
                if (result.status === 200) {
                    console.info('status = 200');
                    this.props.userInfo(result.data);
                    this.props.routeTransition('3');
                } else {
                    console.info('status = ' + result.status);
                    alert('Проверьте правильность введенных данных!');
                }
            })
            .catch(error => {
                console.info(error);
                alert('Произошла ошибка при авторизации!');
            });
    };

    render() {
        return (
            <div className="Login">
                <form onSubmit={this.handleSubmit}>
                    <FormGroup controlId="email" bsSize="large">
                        <ControlLabel>Email</ControlLabel>
                        <FormControl
                            autoFocus
                            type="email"
                            value={this.state.email}
                            onChange={this.handleChange}
                        />
                    </FormGroup>
                    <FormGroup controlId="password" bsSize="large">
                        <ControlLabel>Пароль</ControlLabel>
                        <FormControl
                            value={this.state.password}
                            onChange={this.handleChange}
                            type="password"
                        />
                    </FormGroup>
                    <Button
                        block
                        bsSize="large"
                        disabled={!this.validateForm()}
                        type="submit"
                    >
                        Войти
                    </Button>
                    <Link to='/register' onClick={this.props.regTransition}>Зарегистрироваться</Link>
                </form>
            </div>
        );
    };
}