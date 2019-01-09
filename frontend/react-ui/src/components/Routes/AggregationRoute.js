import React from 'react';
import {FormGroup, ControlLabel, FormControl, Button, Table} from 'react-bootstrap';
import PropTypes from "prop-types";
import {getTicketsAndFlights} from '../../actions/RoutesActions';
import connect from "react-redux/es/connect/connect";
import '../../styles/Routes/Aggregation.css';

class AddRoute extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            routeAggregation: [],
            uidRoute: '',
            disableButton: true,
            tableHidden: false
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.getTicketsAndFlights = this.getTicketsAndFlights.bind(this);
        this.createAggregatedReport = this.createAggregatedReport.bind(this);
        this.createTable = this.createTable.bind(this);
    }

    handleChange(e) {
        this.setState({ uidRoute: e.target.value });
        if (e.target.value !== '')
            this.setState({disableButton: false});
        else
            this.setState({disableButton: true});
    }

    handleSubmit(e) {
        e.preventDefault();
        this.getTicketsAndFlights();

    };

    getTicketsAndFlights()  {
        this.props.getTicketsAndFlights(this.state.uidRoute)
            .then(result => {
                this.setState({
                    routeAggregation: result.data
                })
            });
        this.createAggregatedReport();
    };

    createTable() {

        return (
            <div>
                <Table responsive id="tableId" className="table">
                    <thead>
                    <tr>
                        <th>Уникальный номер рейса</th>
                        <th>Дата рейса</th>
                        <th>Количество купленных билетов</th>
                        <th>Общее количество билетов</th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.createAggregatedReport}
                    </tbody>
                </Table>
            </div>
        )
    }

    createAggregatedReport() {
        const aggrArray = [];
        this.state.routeAggregation.map(record => {

            aggrArray.push(
                <tr>
                    <td> {record.uid}</td>
                    <td> {record.dtFlight} </td>
                    <td> {record.nnTickets} </td>
                    <td> {record.maxTickets} </td>
                </tr>
            );
        });

        return aggrArray;
    };


    render() {
        return (
            <form onSubmit={this.handleSubmit} className="form">
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

                {this.createTable()}

            </form>
        );
    }
}

AddRoute.propTypes = {
    getTicketsAndFlights: PropTypes.func.isRequired
};

export default connect(null, { getTicketsAndFlights }) (AddRoute);