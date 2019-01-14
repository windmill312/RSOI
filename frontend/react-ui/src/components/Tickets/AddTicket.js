import React from 'react';
import {FormGroup, ControlLabel, FormControl, Button, Alert, DropdownButton, MenuItem} from 'react-bootstrap';
import PropTypes from "prop-types";
import {createTicket, isFlightExists} from '../../actions/TicketsActions';
import connect from "react-redux/es/connect/connect";
import { addFlashMessage } from '../../actions/FlashMessages.js';
import TextFieldGroup from "../common/TextFieldGroup";

class AddRoute extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            errors: {},
            invalid_uid: true,
            invalid_type: true
        };

        this.handleClassChange = this.handleClassChange.bind(this);
        this.handlePassengerChange = this.handlePassengerChange.bind(this);
        this.handleFlightChange = this.handleFlightChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.checkFlightExists = this.checkFlightExists.bind(this);
    }

    handleClassChange(event) {
        console.log("!!!!");
        this.setState({
            classType: event.target.value,
            invalid_type: false
        });
    }

    handlePassengerChange(event) {
        this.setState({ idPassenger: event.target.value });
    }

    handleFlightChange(event) {
        this.setState({ uidFlight: event.target.value });
    }

    checkFlightExists(e) {
        const field = e.target.name;
        const val = e.target.value;
        if (val !== '') {
            let errors = this.state.errors;
            let invalid_uid;
            this.props.isFlightExists(val)
                .then(res => {
                    if (res.status === 200) {
                        errors[field] = '';
                        invalid_uid = false;
                        this.setState({ errors, invalid_uid });
                    }
                })
                .catch(err => {
                        console.log('status = ' + err.status);
                        errors[field] = 'Такого рейса не существует';
                        invalid_uid = true;
                        this.setState({ errors, invalid_uid });
                    }
                );

        }
    }

    handleSubmit = event => {
        event.preventDefault();

        const requestData = {
            uidFlight: this.state.uidFlight,
            idPassenger: localStorage.getItem('userUuid'),
            classType: this.state.classType
        };
        this.props.createTicket(requestData)
            .then(result => {
                if (result.status === 200) {
                    console.info('status = 200');
                    this.render(
                        <Alert bsStyle="success">Билет успешно создан!</Alert>
                    );
                } else {
                    console.info('status = ' + result.status);
                    return (
                        <Alert bsStyle="success">Произошла ошибка при создании рейса!</Alert>
                    );
                }
            })
            .catch(error => {
                console.info(error);
                return (
                    <Alert bsStyle="success">Произошла ошибка при создании рейса! Проверьте правильность кода
                        маршрута!</Alert>
                );
            });
    }

    render() {
        const { errors } = this.state;
        return (
            <form onSubmit={this.handleSubmit}>
                <FormGroup
                    controlId="formBasicText"
                >
                    <TextFieldGroup
                        error={errors.uidFlight}
                        label="Уникальный номер рейса"
                        onChange={this.handleFlightChange}
                        check={this.checkFlightExists}
                        value={this.state.uidFlight}
                        field="uidFlight"
                    />
                </FormGroup>

                <FormGroup controlId="formControlsSelect">
                    <ControlLabel>Класс</ControlLabel>
                    <FormControl componentClass="select" onChange={this.handleClassChange}>
                        <option value="Выберите" disabled={true} selected={true}>Выберите:</option>
                        <option value="Эконом">Эконом</option>
                        <option value="Бизнес">Бизнес</option>
                        <option value="Первый">Первый</option>
                    </FormControl>
                </FormGroup>

                <FormGroup controlId="buttonGroup">
                    <Button className="button" bsStyle="danger" type="submit" disabled={this.state.invalid_uid || this.state.invalid_type} >Купить</Button>
                </FormGroup>
            </form>
        )
    }
}

AddRoute.propTypes = {
    createTicket: PropTypes.func.isRequired,
    addFlashMessage: PropTypes.func.isRequired,
    isFlightExists: PropTypes.func.isRequired
};

export default connect(null, { createTicket, isFlightExists, addFlashMessage }) (AddRoute);