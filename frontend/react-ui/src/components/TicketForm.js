import React from 'react';
import {Table, TabContent, TabPane, Nav, NavItem, NavLink, Button, Row, Col, Label, Form, Input, FormGroup} from 'reactstrap'
import classnames from 'classnames';
import axios from "axios";

class TicketForm extends React.Component {

    constructor() {
        super()
        this.state = {
            activeSubTab: '0',
            tickets: []
        }
    }

    componentDidMount() {
        axios.get('http://localhost:8090/tickets?size=50')
            .then(
                response => this.setState({
                        tickets: response.data
                    }
                ))
            .catch((error) => {
                console.error(error);
            });
    }

    toggle1(tab) {
        if (this.state.activeSubTab !== tab) {
            this.setState({
                activeSubTab: tab
            });
        }
    }

    handleClassChange = event => {
        this.setState({ classType: event.target.value });
    }

    handlePassengerChange = event => {
        this.setState({ idPassenger: event.target.value });
    }

    handleFlightChange = event => {
        this.setState({ idFlight: event.target.value });
    }

    handleSubmitTicket = event => {
        event.preventDefault();
        const requestData = {
            idFlight: this.state.idFlight,
            idPassenger: this.state.idPassenger,
            classType: this.state.classType
        };
        axios.put(`http://localhost:8090/ticket`, requestData)
            .then(result => {
                console.info(result);
                console.info(result.data);
            })
    }

    handleDeleteTicket(ticket) {
        return event => {
            event.preventDefault();
            axios.delete(`http://localhost:8090/ticket`, {params: {idTicket: ticket.idTicket}})
                .then(result => {
                    console.log(result);
                })
                .catch((error) => {
                    console.log(error);
                });
        }
    }

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
    }

    render() {
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
                                            <th> Ticket ID</th>
                                            <th> Flight ID</th>
                                            <th> Passenger ID</th>
                                            <th> Class</th>
                                            <th> UUID</th>
                                            <th></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {this.state.tickets.map((ticket, i) =>
                                                <tr key={i}>
                                                    <td key={ticket.idTicket} id="thIdTicket" name="thIdTicket" value={ticket.idTicket} key={ticket.idTicket}>{ticket.idTicket}</td>
                                                    <td key={i}> {ticket.idFlight}</td>
                                                    <td key={i}> {ticket.idPassenger}</td>
                                                    <td key={i} contentEditable={true}
                                                        onBlur={event => {console.log(event);
                                                            this.handleChangeTicket(ticket)}}> {ticket.classType}</td>
                                                    <td key={i}> {ticket.uid}</td>
                                                    <td key={ticket.idTicket}><Button color="danger" onClick={this.handleDeleteTicket(ticket)}>Удалить</Button></td>
                                                </tr>
                                        )}
                                        </tbody>
                                    </Table>
                                </Col>
                            </Row>
                        </Form>
                    </TabPane>
                    <TabPane tabId="1">
                        <Row>
                            <Col sm="6">
                                <Form onSubmit={this.handleSubmitTicket}>
                                    <FormGroup row>
                                        <Label id="lblIdPassenger" for="inputIdPassenger" sm="5">№ пассажира</Label>
                                        <Col>
                                            <Input type="number" name="inputIdPassenger" id="inputIdPassenger"
                                                   placeholder="Введите ID пассажира"
                                                   onChange={this.handlePassengerChange}/>
                                        </Col>
                                    </FormGroup>
                                    <FormGroup row>
                                        <Label id="lbl" for="inputIdFlight" sm="5">№ рейса</Label>
                                        <Col>
                                            <Input type="number" name="inputIdFlight" id="inputIdFlight"
                                                   placeholder="Введите ID рейса"
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
        )
    }
}

export default TicketForm;