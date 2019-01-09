import React from 'react';
import {FormGroup, ControlLabel, FormControl, Button} from 'react-bootstrap';
import PropTypes from "prop-types";
import {createRoute} from '../../actions/RoutesActions';
import connect from "react-redux/es/connect/connect";

class AddRoute extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            value: '',
            disableButton: true
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(e) {
        this.setState({ value: e.target.value });
        if (e.target.value.length > 0)
            this.setState({disableButton: false});
        else
            this.setState({disableButton: true});
    }

    handleSubmit = event => {
        event.preventDefault();
        const requestData = {
            routeName: this.state.routeName
        };
        this.props.createRoute(requestData)
            .then(result => {
                if (result.status === 200) {
                    console.info('status = 200');
                    alert('Маршрут успешно создан!');
                } else {
                    console.info('status = ' + result.status);
                    alert('Ошибка при создании маршрута!');
                }
            });
    };

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <FormGroup
                    controlId="formBasicText"
                >
                    <ControlLabel>Название маршрута</ControlLabel>
                    <FormControl
                        type="text"
                        value={this.state.value}
                        placeholder="Введите текст"
                        onChange={this.handleChange}
                    />

                    <Button className="button" bsStyle="danger" type="submit" disabled={this.state.disableButton} >Добавить</Button>
                </FormGroup>
            </form>
        );
    }
}

AddRoute.propTypes = {
    createRoute: PropTypes.func.isRequired
};

export default connect(null, { createRoute }) (AddRoute);