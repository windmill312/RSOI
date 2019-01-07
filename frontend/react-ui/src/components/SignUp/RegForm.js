import React from 'react'
import "react-datepicker/dist/react-datepicker.css";
import axios from "axios";
import "../../styles/Register.css";
import { Button, FormGroup, FormControl, ControlLabel } from "react-bootstrap";


class RegForm extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            email: "",
            password: "",
            name: "",
            username: ""
        };
    }

    validateForm() {
        return this.state.email.length > 0 && this.state.password.length >= 6;
    };

    handleChange = event => {
        this.setState({
            [event.target.id]: event.target.value
        });
    };

    handleSubmit = async event => {
            event.preventDefault();
            const requestData = {
                name:this.state.name,
                username:this.state.username,
                email:this.state.email,
                password:this.state.password
            };
            axios.post(`http://localhost:8090/api/auth/signup`, requestData,
                {
                    headers: {'Content-Type': 'application/json'}
                })
                .then((result) => {
                    if (result.status === 201) {
                        console.info('status = 201');
                        alert('Регистрация прошла успешно!');
                    }
                })
                .catch(error => {
                    if (error.response.status === 400) {
                        console.info('status = 400');
                        alert('Такой пользователь уже существует!');
                    }
                    else {
                        console.info(error);
                        alert('Произошла ошибка при регистрации!');
                    }
                });
        this.props.authTransition('0');
    };

    render() {
        return (
            <div className="Register">
                <form onSubmit={this.handleSubmit}>
                    <FormGroup controlId="email" bsSize="large">
                        <ControlLabel>Email*</ControlLabel>
                        <FormControl
                            autoFocus
                            type="email"
                            value={this.state.email}
                            onChange={this.handleChange}
                        />
                    </FormGroup>
                    <FormGroup controlId="password" bsSize="large">
                        <ControlLabel>Пароль*(min 6 символов)</ControlLabel>
                        <FormControl
                            value={this.state.password}
                            onChange={this.handleChange}
                            type="password"
                        />
                    </FormGroup>
                    <FormGroup controlId="username" bsSize="large">
                        <ControlLabel>Никнейм</ControlLabel>
                        <FormControl
                            value={this.state.username}
                            onChange={this.handleChange}
                        />
                    </FormGroup>
                    <FormGroup controlId="name" bsSize="large">
                        <ControlLabel>Полное имя</ControlLabel>
                        <FormControl
                            value={this.state.name}
                            onChange={this.handleChange}
                        />
                    </FormGroup>
                    <Button
                        block
                        bsSize="large"
                        bsStyle="danger"
                        disabled={!this.validateForm()}
                        type="submit"
                    >
                        Зарегистрироваться
                    </Button>
                </form>
            </div>
        )
    }
}

export default RegForm;