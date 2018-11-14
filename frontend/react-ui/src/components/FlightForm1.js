import React from 'react';
import {Table, TabContent, TabPane, Nav, NavItem, NavLink, Button, Row, Col, Label, Form, Input, FormGroup} from 'reactstrap';
import classnames from 'classnames';
import axios from "axios";

class FlightForm extends React.Component {

    constructor() {
        super()
        this.state = {
            activeSubTab: '0',
            flights: []
       }
    }

    componentDidMount() {
        axios.get('http://localhost:8090/flights?size=50')
            .then(
                response => this.setState({
                        flights: response.data
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

    handleFlightRouteChange = event => {
        this.setState({ idRoute: event.target.value });
    }

    handleFlightDateChange = event => {
        this.setState({ dtFlight: event.target.value });
    }

    handleFlightTimeChange = event => {
        this.setState({ tmFlight: event.target.value });
    }

    handleFlightMaxTicketsChange = event => {
        this.setState({ maxTickets: event.target.value });
    }

    handleSubmitFlight = event => {
        event.preventDefault();
        const requestData = {
            idRoute: this.state.idRoute,
            dtFlight: this.state.dtFlight + ' ' + this.state.tmFlight,
            maxTickets: this.state.maxTickets
        };
        axios.put(`http://localhost:8090/flight`, requestData)
            .then(result => {
                console.info(result);
                console.info(result.data);
            })
    }

    handleDeleteFlight(flight) {
        return event => {
            event.preventDefault();
            axios.delete(`http://localhost:8090/flight`, {params: {idFlight: flight.idFlight}})
                .then(result => {
                    console.log(result);
                })
                .catch((error) => {
                    console.log(error);
                });
        }
    }

    render() {
            return (
                <div>
                    <Nav>
                        <NavItem>
                            <NavLink
                                className={classnames({ active: this.state.activeSubTab === '0' })}
                                onClick={() => { this.toggle1('0'); }}
                            >
                                Рейсы
                            </NavLink>
                        </NavItem>
                        <NavItem>
                            <NavLink
                                className={classnames({ active: this.state.activeSubTab === '1' })}
                                onClick={() => { this.toggle1('1'); }}
                            >
                                Добавить рейс
                            </NavLink>
                        </NavItem>
                    </Nav>
                    <TabContent activeTab={this.state.activeSubTab}>
                        <TabPane tabId="0">
                            <Row>
                                <Col sm="12">
                                    <Table id = "tableId" className = "table" size="sm">
                                        <thead>
                                        <tr>
                                            <th> № рейса  </th>
                                            <th> № маршрута  </th>
                                            <th> Дата и время </th>
                                            <th> Количество купленных билетов </th>
                                            <th> Максимальное количество билетов </th>
                                            <th> Уникальный номер рейса </th>
                                            <th></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {this.state.flights.map((flight, i) =>
                                            <tr>
                                                <th id="thIdFlight" key = {flight.idFlight}> {flight.idFlight}</th>
                                                <td> {flight.idRoute}</td>
                                                <td> {flight.dtFlight}</td>
                                                <td> {flight.nnTickets}</td>
                                                <td> {flight.maxTickets}</td>
                                                <td> {flight.uid}</td>
                                                <td><Button color="danger" onClick={this.handleDeleteFlight(flight)}>Удалить</Button></td>
                                            </tr>
                                        )}
                                        </tbody>
                                    </Table>
                                </Col>
                            </Row>
                        </TabPane>
                        <TabPane tabId="1">
                            <Row>
                                <Col sm="6">
                                    <Form onSubmit={this.handleSubmitFlight}>
                                        <FormGroup row>
                                            <Label id="lblIdRoute" for="inputIdRoute" sm="5">№ маршрута</Label>
                                            <Col>
                                                <Input type="number" name="inputIdRoute" id="inputIdRoute" placeholder="Введите ID маршрута" onChange={this.handleFlightRouteChange}/>
                                            </Col>
                                        </FormGroup>
                                        <FormGroup row>
                                            <Label id="lblDtFlight" for="inputDtFlight" sm="5">Дата и время рейса</Label>
                                            <Col>
                                                <Input type="date"  name="inputDtFlight" id="inputDtFlight" placeholder="Введите дату рейса" onChange={this.handleFlightDateChange}/>
                                                <Input type="time" name="inputTmFlight" id="inputTmFlight" placeholder="Введите время рейса" onChange={this.handleFlightTimeChange}/>
                                            </Col>
                                        </FormGroup>
                                        <FormGroup row>
                                            <Label id="lblMaxTickets" for="inputMaxTickets" sm="5">Максимальное количество билетов</Label>
                                            <Col>
                                                <Input type="number" name="inputMaxTickets" id="inputMaxTickets" placeholder="Введите максимальное количество билетов" onChange={this.handleFlightMaxTicketsChange}/>
                                            </Col>
                                        </FormGroup>
                                        <FormGroup row>
                                            <Button id="btnCreateFlight" type="submit">Добавить</Button>
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

export default FlightForm;