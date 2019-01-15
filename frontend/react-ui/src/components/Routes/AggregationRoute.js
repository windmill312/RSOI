import React from 'react';
import {FormGroup, ControlLabel, FormControl, Button} from 'react-bootstrap';
import PropTypes from "prop-types";
import {getTicketsAndFlights} from '../../actions/RoutesActions';
import connect from "react-redux/es/connect/connect";
import '../../styles/Routes/Aggregation.css';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import ExpandRow from '../common/ExpandRow';

class AddRoute extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            routeAggregation: [[]],
            uidRoute: '',
            disableButton: true,
            tableHidden: false
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.getTicketsAndFlights = this.getTicketsAndFlights.bind(this);
        this.createTable = this.createTable.bind(this);
    }

    handleChange(e) {
        this.setState({ uidRoute: e.target.value });
        if (e.target.value !== '')
            this.setState({disableButton: false});
        else
            this.setState({disableButton: true});
    }

    handleSubmit() {
        this.getTicketsAndFlights();
    };

    getTicketsAndFlights = event => {
        event.preventDefault();
        this.props.getTicketsAndFlights(this.state.uidRoute)
            .then(result => {
                this.setState({
                    routeAggregation: result.data
                })
            })
            .catch(error => {
                console.log(error);
            });
    };

    createTable() {

        if (this.state.routeAggregation.length > 0)
            return (
                <ExpandRow data={this.state.routeAggregation}/>
            );
    }


    render() {
        return (
            <form onSubmit={this.getTicketsAndFlights} className="form">
                <FormGroup
                    controlId="formBasicText"
                >
                    <ControlLabel>Уникальный номер маршрута</ControlLabel>
                    <FormControl
                        className="input"
                        type="text"
                        value={this.state.uidRoute}
                        placeholder="Введите текст"
                        onChange={this.handleChange}
                    />

                    <Button className="button" bsStyle="danger" type="submit" disabled={this.state.disableButton} >Создать отчет</Button>
                </FormGroup>
                <FormGroup>
                    {this.createTable()}
                </FormGroup>

            </form>
        );
    }
}

AddRoute.propTypes = {
    getTicketsAndFlights: PropTypes.func.isRequired
};

export default connect(null, { getTicketsAndFlights }) (AddRoute);