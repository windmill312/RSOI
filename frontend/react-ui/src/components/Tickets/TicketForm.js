import React from 'react';
import {Table, Alert, TabContent, Pagination, PaginationItem, PaginationLink, TabPane, Nav, NavItem, NavLink, Button, Row, Col, Label, Form, Input, FormGroup} from 'reactstrap'
import classnames from 'classnames';
import axios from "axios";

class TicketForm extends React.Component {

    constructor() {
        super()
        this.state = {
            activeSubTab: '0',
            nnTickets: 0,
            tickets: [],
            serviceAvailable: true,
            currentPage: 1,
            pageSize: 5,
            nnPages: 0
        }
    }

    componentDidMount() {
        axios.get(`http://localhost:8090/pingTickets`)
            .then((result) => {
                if (result.status === 200) {
                    axios.get(`http://localhost:8090/countTickets`)
                        .then(result => {
                            this.setState({
                                nnTickets: result.data,
                                nnPages : result.data/this.state.pageSize
                            });
                            axios.get('http://localhost:8090/tickets?size=' + this.state.pageSize + '&page=' + this.state.currentPage)
                                .then(
                                    response => this.setState({
                                            tickets: response.data,
                                            serviceAvailable: true
                                        }
                                    ))
                                .catch((error) => {
                                    console.error(error);
                                })
                        })
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

    toggle1(tab) {
        this.componentDidMount();
        if (this.state.activeSubTab !== tab) {
            this.setState({
                activeSubTab: tab
            });
        }
    }

    handleCurrentPageChange (e, index) {
        if (index >= 0 && index<this.state.nnPages && index!==this.state.currentPage) {
            e.preventDefault();
            this.setState({currentPage: index});
            axios.get('http://localhost:8090/tickets?size=' + this.state.pageSize + '&page=' + (index+1))
                .then(
                    response => this.setState({
                            tickets: response.data
                        }
                    ))
                .catch((error) => {
                    console.error(error);
                });
            this.render();
        } else {
            return;
        }
    }

    handleClassChange = event => {
        this.setState({ classType: event.target.value });
    }

    handlePassengerChange = event => {
        this.setState({ idPassenger: event.target.value });
    }

    handleFlightChange = event => {
        this.setState({ uidFlight: event.target.value });
    }

    handleSubmitTicket = event => {
        event.preventDefault();
        const requestData = {
            uidFlight: this.state.uidFlight,
            idPassenger: this.state.idPassenger,
            classType: this.state.classType
        };
        axios.put(`http://localhost:8090/ticket`, requestData)
            .then((result) => {
                if (result.status === 200) {
                    console.info('status = 200');
                    alert('Билет успешно создан!');
                } else {
                    console.info('status = ' + result.status);
                    alert('Произошла ошибка при создании билета!');
                }
            })
            .catch(error => {
                console.info(error);
                alert('Произошла ошибка при создании билета! Проверьте правильность кода рейса');
            });
    };

    handleDeleteTicket(ticket) {
        return event => {
            event.preventDefault();
            axios.delete(`http://localhost:8090/ticket`, {data: ticket.uid})
                .then(result => {
                    if (result.status === 200) {
                        console.info('status = 200');
                        alert('Билет успешно удален!');
                    } else {
                        console.info('status = ' + result.status);
                        alert('Произошла ошибка при удалении билета!');
                    }
                })
        }
    };

    handleChangeTicket(ticket) {
        return event => {
            console.log(ticket, event.target.value)
            /*event.preventDefault();
            axios.delete(`http://localhost:8090/ticket`, {params: {idTicket: ticket.idTicket}})
                .then(result => {
                    console.log(result);
                })
                .catch((error) => {
                    console.log(error);
                });*/
        }
    };

    createPagination = () => {
        const pages = [];
        for (let i = 0; i < this.state.nnPages; i++) {
            pages.push(
                <PaginationItem key={i} >
                    <PaginationLink onClick={e => {this.handleCurrentPageChange(e,i)}} key={i} >
                        {i+1}
                    </PaginationLink>
                </PaginationItem>);
        }
        return <Pagination aria-label="Page navigation example">
            <PaginationItem>
                <PaginationLink previous onClick={e => {this.handleCurrentPageChange(e, this.state.currentPage - 1)}} href="#"/>
            </PaginationItem>
            {pages}
            <PaginationItem>
                <PaginationLink next onClick={e => {this.handleCurrentPageChange(e, this.state.currentPage + 1)}} href="#" />
            </PaginationItem>
        </Pagination>;
    }

    render() {
        if (this.state.serviceAvailable) {
            return (
                <div>
                    <Nav>
                        <NavItem>
                            <NavLink
                                className={classnames({active: this.state.activeSubTab === '0'})}
                                onClick={() => {
                                    this.toggle1('0');
                                }}
                            >
                                Билеты
                            </NavLink>
                        </NavItem>
                        <NavItem>
                            <NavLink
                                className={classnames({active: this.state.activeSubTab === '1'})}
                                onClick={() => {
                                    this.toggle1('1');
                                }}
                            >
                                Добавить билет
                            </NavLink>
                        </NavItem>
                    </Nav>
                    <TabContent activeTab={this.state.activeSubTab}>
                        <TabPane tabId="0">
                            <Form>
                                <Row>
                                    <Col sm="12">
                                        <Table id="tableId" className="table" size="sm">
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
                                                        <td key={i} contentEditable={true}
                                                            onBlur={event => {console.log(event);
                                                                this.handleChangeTicket(ticket)}}> {ticket.classType}</td>
                                                        <td key={ticket.idTicket}><Button color="danger" onClick={this.handleDeleteTicket(ticket)}>Удалить</Button></td>
                                                    </tr>
                                            )}
                                            </tbody>
                                        </Table>
                                        {this.createPagination()}
                                    </Col>
                                </Row>
                            </Form>
                        </TabPane>
                        <TabPane tabId="1">
                            <Row>
                                <Col sm="6">
                                    <Form onSubmit={this.handleSubmitTicket}>
                                        <FormGroup row>
                                            <Label id="lblIdPassenger" for="inputIdPassenger" sm="5">Код пассажира</Label>
                                            <Col>
                                                <Input type="number" name="inputIdPassenger" id="inputIdPassenger"
                                                       placeholder="Введите код пассажира"
                                                       onChange={this.handlePassengerChange}/>
                                            </Col>
                                        </FormGroup>
                                        <FormGroup row>
                                            <Label id="lbl" for="inputUidFlight" sm="5">Код рейса</Label>
                                            <Col>
                                                <Input name="inputUidFlight" id="inputUidFlight"
                                                       placeholder="Введите код рейса"
                                                       onChange={this.handleFlightChange}/>
                                            </Col>
                                        </FormGroup>
                                        <FormGroup row>
                                            <Label for="exampleSelect" sm="5">Выберите класс:</Label>
                                            <Col>
                                                <Input type="select" name="select" id="exampleSelect"
                                                       onChange={this.handleClassChange}>
                                                    <option>Выберите класс</option>
                                                    <option>Эконом</option>
                                                    <option>Бизнес</option>
                                                    <option>Первый</option>
                                                </Input>
                                            </Col>
                                        </FormGroup>
                                        <FormGroup row>
                                            <Button id="btnCreateTicket" type="submit">Добавить</Button>
                                        </FormGroup>
                                    </Form>
                                </Col>
                            </Row>
                        </TabPane>
                    </TabContent>
                </div>
            )} else {
            return (
                <div>
                    <Alert color="danger">Сервис временно недоступен</Alert>
                    <Button outline onClick={()=> {this.componentDidMount(); this.render();}}>Обновить</Button>
                </div>
            )
        }
    }
}

export default TicketForm;