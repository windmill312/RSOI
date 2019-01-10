import React from 'react';
import {pingFlights, countFlights, deleteFlight, getFlights} from '../../actions/FlightsActions';
import PropTypes from "prop-types";
import connect from "react-redux/es/connect/connect";
import { Button, DropdownButton, MenuItem, Pagination, Alert, Table} from 'react-bootstrap';

class FlightFormNew extends React.Component {

    constructor() {
        super();
        this.state = {
            activeSubTab: '0',
            flights: [],
            serviceAvailable: true,
            nnFlights: 0,
            currentPage: 1,
            pageSize: 5,
            nnPages: 0
        }
    }

    componentDidMount() {
        this.props.pingFlights()
            .then((result) => {
                if (result.status === 200) {
                    this.props.countFlights()
                        .then(result => {
                            this.setState({
                                nnRoutes: result.data,
                                nnPages : result.data/this.state.pageSize
                            });
                            this.props.getFlights(this.state.pageSize, this.state.currentPage)
                                .then(
                                    response => this.setState({
                                            flights: response.data,
                                            serviceAvailable: true
                                        }
                                    ))
                        })
                        .catch((error) => {
                            console.error(error);
                        });
                } else {
                    console.log('Ошибка при получении рейсов (status = ' + result.status + ')');
                }
            })
            .catch(error => {
                console.error(error);
                this.setState({
                    serviceAvailable: false
                })
            })
    }

    handleCurrentPageChange (index) {
        if (index >= 1 && index<=this.state.nnPages && index!==this.state.currentPage) {
            this.setState({currentPage: index});
            this.props.getFlights(this.state.pageSize, this.state.currentPage)
                .then(
                    response => this.setState({
                            flights: response.data
                        }
                    ))
                .catch((error) => {
                    console.error(error);
                });
            this.render();
        }
    }

    handleDeleteFlight(flight) {
        return event => {
            event.preventDefault();
            this.props.deleteFlight(flight.uid)
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
    }

    createPagination() {
        const items = [];
        for (let number = 1; number <= this.state.nnPages; number++) {
            items.push(
                <Pagination.Item active={number === this.state.currentPage} key={number} onClick={this.handleCurrentPageChange(number)}>{number}</Pagination.Item>
            );
        }

        const paginationBasic = (
            <div className="pagination">
                <Pagination bsSize="medium" >{items}</Pagination>
            </div>
        );

        return (paginationBasic);
    };

    createTable() {
        return (
            <div>
                <Table responsive id="tableId" className="table">
                    <thead>
                    <tr>
                        <th> Код рейса </th>
                        <th> Код маршрута  </th>
                        <th> Дата и время </th>
                        <th> Количество купленных билетов </th>
                        <th> Максимальное количество билетов </th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.state.flights.map((flight) =>
                        <tr>
                            <td> {flight.uid}</td>
                            <td> {flight.uidRoute}</td>
                            <td> {flight.dtFlight}</td>
                            <td> {flight.nnTickets}</td>
                            <td> {flight.maxTickets}</td>
                            <td><Button color="danger" onClick={this.handleDeleteFlight(flight)}>Удалить</Button></td>
                        </tr>
                    )}
                    </tbody>
                </Table>
                <br/>
            </div>
        )
    }

    render() {
        //if (this.state.serviceAvailable) {
            return (
                <div>
                    <div>
                        <DropdownButton className='dropdown'
                                        bsStyle="info"
                                        title="Действия"
                                        id={`dropdown`} >

                            <MenuItem eventKey="1" href="/flights/create">Добавить</MenuItem>

                        </DropdownButton>
                    </div>
                    <div>
                        {this.createPagination()}
                    </div>
                    <div>
                        {this.createTable()}
                    </div>
                </div>
            )
        /*} else {
            return (
                <div>
                    <Alert bsStyle="danger">
                        Сервис временно недоступен
                    </Alert>
                    <Button outline onClick={()=> {this.componentDidMount(); this.render();}}>Обновить</Button>
                </div>
            )
        }*/
    }
}

FlightFormNew.propTypes = {
    pingFlights: PropTypes.func.isRequired,
    countFlights: PropTypes.func.isRequired,
    deleteFlight: PropTypes.func.isRequired,
    getFlights: PropTypes.func.isRequired
};

export default connect(null, { pingFlights, countFlights, deleteFlight, getFlights })(FlightFormNew);