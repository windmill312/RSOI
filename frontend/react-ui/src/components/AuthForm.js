import React from 'react'
import {Button, Jumbotron} from 'reactstrap'
import "react-datepicker/dist/react-datepicker.css";
import 'bootstrap/dist/css/bootstrap.css'

class AuthForm extends React.Component {

    render() {
        return (
            <Jumbotron>
                <h1 className="display-3">Привет, Пользователь!</h1>
                <p className="lead">Это приветственное окно. В ближайшем будущем здесь будет авторизация</p>
                <hr className="my-2" />
                <p>Для продолжения нажми кнопку Далее</p>
                <p className="lead">
                    <Button color="primary" onClick={this.props.transition}>Далее</Button>
                </p>
            </Jumbotron>
        )
    }
}

export default AuthForm;