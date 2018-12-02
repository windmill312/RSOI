import React from 'react'
import {Button, Jumbotron, Input, Label} from 'reactstrap'
import "react-datepicker/dist/react-datepicker.css";
import 'bootstrap/dist/css/bootstrap.css';
import axios from "axios";

class AuthForm extends React.Component {

    authorize(login, password) {
        return event => {
                    event.preventDefault();
                    axios.get(`http://localhost:8090/flight`, {data: flight.uid})
                        .then(result => {
                            if (result.status === 200) {
                                console.info('status = 200');
                                alert('Билет успешно удален!');
                            } else {
                                console.info('status = ' + result.status);
                                alert('Произошла ошибка при удалении рейса!');
                            }
                        })
                }


        this.props.transition;
    }

    render() {
        return (
            <Jumbotron>
                <h1 className="display-3">Привет, Пользователь!</h1>
                <p className="lead">Пришла пора авторизовываться!</p>
                <Row>
                    <Label>Логин</Label>
                    <Input></Input>
                    <Label>Пароль</Label>
                    <Input></Input>
                </Row>
                <hr className="my-2" />
                <p className="lead">
                    <Button color="primary" onClick={() -> { this.authorize; }}>Вход</Button>
                </p>
            </Jumbotron>
        )
    }
}

export default AuthForm;