import React from 'react'
import {Button} from 'react-bootstrap'
import "../../styles/Greetings.css"

class Greetings extends React.Component {
    render() {
        return (
            <div className="jumbotron">
                <h2>Добро пожаловать на ГудБайАмерика.ру!</h2>
                <hr/>
                <Button className="continue-button" bsStyle="primary" href="/login">Нажмите для продолжения</Button>
            </div>
        );
    }
}

export default Greetings;