import React from 'react';
import {countTickets, deleteTicket, getTickets, pingTickets} from '../../actions/TicketsActions';
import PropTypes from "prop-types";
import connect from "react-redux/es/connect/connect";
import { Button, DropdownButton, MenuItem, Pagination, Alert, Table} from 'react-bootstrap';

class TicketForm extends React.Component {

    constructor() {
        super();
        this.state = {
            tickets: [],
            serviceAvailable: true,
            nnTickets: 0,
            currentPage: 1,
            pageSize: 5,
            nnPages: 0
        };

        this.handleCurrentPageChange = this.handleCurrentPageChange.bind(this);
        this.handleDeleteTicket = this.handleDeleteTicket.bind(this);
    }

    componentDidMount() {
        this.props.pingTickets()
            .then((result) => {
                if (result.status === 200) {
                    this.props.countTickets()
                        .then(result => {
                            this.setState({
                                nnTickets: result.data,
                                nnPages : result.data/this.state.pageSize
                            });
                            this.props.getTickets(this.state.pageSize, this.state.currentPage)
                                .then(
                                    response => this.setState({
                                            tickets: response.data,
                                            serviceAvailable: true
                                        }
                                    ))
                        })
                        .catch((error) => {
                            console.error(error);
                        });
                } else {
                    console.log('Ошибка при получении билетов (status = ' + result.status + ')');
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
            this.props.getTickets(this.state.pageSize, this.state.currentPage)
                .then(
                    response => this.setState({
                            tickets: response.data
                        }
                    ))
                .catch((error) => {
                    console.error(error);
                });
            this.render();
        }
    }

    handleDeleteTicket(flight) {
        return event => {
            event.preventDefault();
            this.props.deleteTicket(flight.uid)
                .then(result => {
                    if (result.status === 200) {
                        console.info('status = 200');
                        alert('Вы вернули билет!');
                    } else {
                        console.info('status = ' + result.status);
                        alert('Произошла ошибка при возврате билета!');
                    }
                });
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
                        <th> Код билета</th>
                        <th> Код рейса</th>
                        <th> Пассажир</th>
                        <th> Класс</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.state.tickets.map((ticket, i) =>
                        <tr key={i}>
                            <td key={i}> {ticket.uid}</td>
                            <td key={i}> {ticket.uidFlight}</td>
                            <td key={i}> {ticket.idPassenger}</td>
                            <td key={i}> {ticket.classType}</td>
                            <td key={i} contentEditable={true}
                                onBlur={event => {console.log(event);
                                    this.handleChangeTicket(ticket)}}> {ticket.classType}</td>
                            <td key={ticket.idTicket}><Button color="danger" onClick={this.handleDeleteTicket(ticket)}>Удалить</Button></td>
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

                            <MenuItem eventKey="1" href="/tickets/create">Купить</MenuItem>

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

TicketForm.propTypes = {
    pingTickets: PropTypes.func.isRequired,
    countTickets: PropTypes.func.isRequired,
    deleteTicket: PropTypes.func.isRequired,
    getTickets: PropTypes.func.isRequired
};

export default connect(null, { countTickets, deleteTicket, getTickets, pingTickets })(TicketForm);