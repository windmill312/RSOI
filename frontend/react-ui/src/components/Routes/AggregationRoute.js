import React from 'react';
import {FormGroup, ControlLabel, FormControl, Button, Alert} from 'react-bootstrap';
import PropTypes from "prop-types";
import {getTicketsAndFlights} from '../../actions/RoutesActions';
import {pingTickets} from '../../actions/TicketsActions';
import {pingFlights} from '../../actions/FlightsActions';
import {pingRoutes} from '../../actions/RoutesActions';
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
            tableHidden: false,
            invalid: true,
            expandable: false
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

    componentDidMount() {
        this.props.pingRoutes()
            .then(
                () => {
                    this.props.pingFlights()
                        .then(() => {
                            this.setState({
                                invalid: false
                            });
                            this.props.pingTickets()
                                .then(() => {
                                    this.setState({
                                        expandable: true
                                    });
                                });
                        })
                        .catch(
                            (error) => {
                                console.log(error);
                                this.setState({
                                    invalid: true
                                });
                            }
                        )
                }
            )
            .catch(
                (error) => {
                    console.log(error);
                    this.setState({
                        invalid: true
                    });
                }
            )
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
                <ExpandRow data={this.state.routeAggregation} isExpandable={this.state.expandable}/>
            );
    }


    render() {
        if (this.state.invalid === false)
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
        else
            return (
                <div>
                    <Alert bsStyle="danger">
                        Функция временно недоступна
                    </Alert>
                    <Button outline onClick={()=> {this.componentDidMount(); this.render();}}>Обновить</Button>
                </div>
            );
    }
}

AddRoute.propTypes = {
    getTicketsAndFlights: PropTypes.func.isRequired,
    pingTickets: PropTypes.func.isRequired,
    pingRoutes: PropTypes.func.isRequired,
    pingFlights: PropTypes.func.isRequired
};

export default connect(null, { getTicketsAndFlights, pingTickets, pingFlights, pingRoutes }) (AddRoute);