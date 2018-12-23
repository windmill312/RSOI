import React from 'react'
import {Row, Button, Jumbotron, Input, Label} from 'reactstrap'
import "react-datepicker/dist/react-datepicker.css";
import 'bootstrap/dist/css/bootstrap.css';
import axios from "axios";
import "../styles/Login.css";
import { Router, Redirect, Route, Switch } from 'react-router'

class RegForm extends React.Component {

    /*authorize(login, password) {
        return event => {
                    event.preventDefault();
                    axios.get(`http://localhost:8090/user/`, {data: flight.uid})
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


        //this.props.transition;
    }

    renderRedirect = () => {
            return <Redirect to='/target' />
    }*/

    render() {
        return (<h1>123</h1>
            /*<Jumbotron className="jumbo">
                <h1 className="display-3">Привет, Пользователь!</h1>
                <p className="lead">Пришла пора авторизовываться!</p>
                <Row className="text_row">
                    <Label>Логин</Label>
                    <Input></Input>
                    <Label>Пароль</Label>
                    <Input></Input>
                </Row>
                <hr className="my-2" />
                <p className="lead">
                    <Button color="primary" onClick={() => { this.authorize()}}>Вход</Button>
                </p>
                <p className="lead">

                    <Router>
                        <div>
                            <Switch>
                                <Route exact path="/" component={RegForm} />
                            </Switch>
                        </div>
                    </Router>


                </p>
            </Jumbotron>*/
        )
    }
}

export default RegForm;