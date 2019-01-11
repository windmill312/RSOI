import React from 'react';
import {FormGroup, ControlLabel, FormControl, Button} from 'react-bootstrap';
import PropTypes from "prop-types";
import {createFlight, isRouteExists} from '../../actions/FlightsActions';
import connect from "react-redux/es/connect/connect";
import { addFlashMessage } from '../../actions/FlashMessages.js';
import TextFieldGroup from "../common/TextFieldGroup";

class AddRoute extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            uidRoute: '',
            dtFlight: '',
            tmFlight: '',
            maxTickets: '',
            errors: {},
            invalid: true
        };

        this.handleFlightRouteChange = this.handleFlightRouteChange.bind(this);
        this.handleFlightDateChange = this.handleFlightDateChange.bind(this);
        this.handleFlightMaxTicketsChange = this.handleFlightMaxTicketsChange.bind(this);
        this.handleFlightTimeChange = this.handleFlightTimeChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.checkRouteExists = this.checkRouteExists.bind(this);
    }

    handleFlightRouteChange(event) {
        this.setState({ [event.target.name]: event.target.value });
    }

    handleFlightDateChange(event) {
        this.setState({ dtFlight: event.target.value });
    };

    handleFlightTimeChange(event) {
        this.setState({ tmFlight: event.target.value });
    };

    handleFlightMaxTicketsChange(event) {
        this.setState({ maxTickets: event.target.value });
    };

    checkRouteExists(e) {
        const field = e.target.name;
        const val = e.target.value;
        if (val !== '') {
            let errors = this.state.errors;
            let invalid;
            this.props.isRouteExists(val)
                .then(res => {
                    if (res.status === 200) {
                        errors[field] = '';
                        invalid = false;
                        this.setState({ errors, invalid });
                    }
                })
                .catch(err => {
                        console.log('status = ' + err.status);
                        errors[field] = 'Такого маршрута не существует';
                        invalid = true;
                        this.setState({ errors, invalid });
                    }
                );

        }
    }

    handleSubmit = event => {
        event.preventDefault();

        const requestData = {
            uidRoute: this.state.uidRoute,
            dtFlight: this.state.dtFlight + ' ' + this.state.tmFlight,
            maxTickets: this.state.maxTickets
        };
        this.props.createFlight(requestData)
            .then(result => {
                if (result.status === 200) {
                    console.info('status = 200');
                    alert('Рейс успешно создан!');
                } else {
                    console.info('status = ' + result.status);
                    alert('Произошла ошибка при создании рейса!');
                }
            })
            .catch(error => {
                console.info(error);
                alert('Произошла ошибка при создании рейса! Проверьте правильность кода маршрута');
            });
    };

    render() {
        const { errors } = this.state;
        return (
            <form onSubmit={this.handleSubmit}>
                <FormGroup
                    controlId="formBasicText"
                >
                    <TextFieldGroup
                        error={errors.uidRoute}
                        label="Уникальный номер маршрута"
                        onChange={this.handleFlightRouteChange}
                        checkUserExists={this.checkRouteExists}
                        value={this.state.uidRoute}
                        field="uidRoute"
                    />

                    <ControlLabel>Дата рейса</ControlLabel>
                    <FormControl
                        type="date"
                        value={this.state.dtFlight}
                        placeholder="Введите текст"
                        onChange={this.handleFlightDateChange}
                    />

                    <ControlLabel>Время рейса</ControlLabel>
                    <FormControl
                        type="time"
                        value={this.state.tmFlight}
                        placeholder="Введите текст"
                        onChange={this.handleFlightTimeChange}
                    />

                    <ControlLabel>Максимальное количество билетов</ControlLabel>
                    <FormControl
                        type="number"
                        value={this.state.maxTickets}
                        placeholder="Введите текст"
                        onChange={this.handleFlightMaxTicketsChange}
                    />

                    <Button className="button" bsStyle="danger" type="submit" disabled={this.state.invalid} >Добавить</Button>
                </FormGroup>
            </form>
        )
    }
}

AddRoute.propTypes = {
    createFlight: PropTypes.func.isRequired,
    addFlashMessage: PropTypes.func.isRequired,
    isRouteExists: PropTypes.func.isRequired
};

export default connect(null, { createFlight, addFlashMessage, isRouteExists }) (AddRoute);