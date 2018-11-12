import React from 'react'
import './App.css'
import axios from 'axios'
import {Table, TabContent, TabPane, Nav, NavItem, NavLink, Card, Button, CardTitle, CardText, Row, Col, Jumbotron, Label, Form, Input, FormGroup} from 'reactstrap'
import DateTimePicker from 'react-datetime-picker';
import "react-datepicker/dist/react-datepicker.css";
import classnames from 'classnames';
import 'bootstrap/dist/css/bootstrap.css'

class App extends React.Component {

    constructor() {
        super()
        this.state = {
            tickets: [],
            flights: [],
            routes: [],

            activeTab: '0',
            activeSubTab: '0',
            disabled : false,

            dtFlight: '',
            tmFlight: '',
            routeName: ''
        }

        this.toggle = this.toggle.bind(this);
        this.toggle1 = this.toggle1.bind(this);
    }

    componentDidMount() {
        axios.get('http://localhost:8090/tickets?size=10')
            .then(
                response => this.setState({
                        tickets: response.data
                    }
                ))
            .catch((error) => {
                console.error(error);
            });

        axios.get('http://localhost:8090/flights?size=50')
            .then(
                response => this.setState({
                        flights: response.data
                    }
                ))
            .catch((error) => {
                console.error(error);
            });

        axios.get('http://localhost:8090/routes?size=50')
            .then(
                response => this.setState({
                        routes: response.data
                    }
                ))
            .catch((error) => {
                console.error(error);
            });
    }

    toggle(tab) {
        if (this.state.activeTab !== tab) {
            this.setState({
                activeTab: tab
            });
        }
    }

    toggle1(tab) {
        if (this.state.activeSubTab !== tab) {
            this.setState({
                activeSubTab: tab
            });
        }
    }

    onOpen() {
        this.setState({
            activeTab: 1,
            disabled: true
        })
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

    handleRouteNmChange = event => {
        this.setState({ routeName: event.target.value })
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

    handleSubmitRoute = event => {
        event.preventDefault();
        const requestData = {
            routeName: this.state.routeName
        };
        axios.put(`http://localhost:8090/route`, requestData)
            .then(result => {
                console.info(result);
                console.info(result.data);
            })
    }

    handleDeleteTicket = event => {
        event.preventDefault();

        axios.delete(`http://localhost:8090/ticket`, event.target.value).then(result => {
            console.log(result)
        });
    }

    render() {
        return (

            <div>
                <Nav tabs>
                    <NavItem>
                        <NavLink disabled={this.state.disabled} id="authTab"
                                 className={classnames({ active: this.state.activeTab === '0' })}
                        >
                            Авторизация
                        </NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink  id="ticketTab"
                            className={classnames({ active: this.state.activeTab === '1' })}
                                onClick={() => {this.toggle('1')}}
                        >
                            Билеты
                        </NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink
                            className={classnames({ active: this.state.activeTab === '2' })}
                                 onClick={() => { this.toggle('2')}}
                        >
                            Рейсы
                        </NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink
                            className={classnames({ active: this.state.activeTab === '3' })}
                            onClick={() => { this.toggle('3')}}
                        >
                            Маршруты
                        </NavLink>
                    </NavItem>
                </Nav>
                <TabContent activeTab={this.state.activeTab}>

                    <TabPane tabId="0">
                        <Jumbotron>
                            <h1 className="display-3">Привет, Пользователь!</h1>
                            <p className="lead">Это приветственное окно. В ближайшем будущем здесь будет авторизация</p>
                            <hr className="my-2" />
                            <p>Для продолжения нажми кнопку Далее</p>
                            <p className="lead">
                                <Button color="primary" onClick={() => {this.onOpen()}}>Далее</Button>
                            </p>
                        </Jumbotron>
                    </TabPane>

                    <TabPane tabId="1">
                        <div className="content_container">
                            <Nav subtabs>
                                <NavItem>
                                    <NavLink
                                        className={classnames({ active: this.state.activeSubTab === '0' })}
                                        onClick={() => { this.toggle1('0'); }}
                                    >
                                        Билеты
                                    </NavLink>
                                </NavItem>
                                <NavItem>
                                    <NavLink
                                        className={classnames({ active: this.state.activeSubTab === '1' })}
                                        onClick={() => { this.toggle1('1'); }}
                                    >
                                        Добавить билет
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
                                                    <th> Ticket ID </th>
                                                    <th> Flight ID </th>
                                                    <th> Passenger ID </th>
                                                    <th> Class </th>
                                                    <th> UUID </th>
                                                    <th></th>
                                                </tr>
                                                </thead>
                                                <tbody onSubmit={this.handleDeleteTicket}>
                                                {this.state.tickets.map(function (ticket) {
                                                    return (
                                                        <tr>
                                                            <th id="thIdTicket" key = {ticket.idTicket}> {ticket.idTicket}</th>
                                                            <td> {ticket.idFlight}</td>
                                                            <td contentEditable={true} onChange={() => this.changeTicket}> {ticket.idPassenger}</td>
                                                            <td> {ticket.classType}</td>
                                                            <td> {ticket.uid}</td>
                                                            <td><Button color="danger" type="submit">Удалить</Button></td>
                                                        </tr>
                                                    )
                                                })}
                                                </tbody>
                                            </Table>
                                        </Col>
                                    </Row>
                                </TabPane>
                                <TabPane tabId="1">
                                    <Row>
                                        <Col sm="6">
                                            <Form onSubmit={this.handleSubmitTicket}>
                                                <FormGroup row>
                                                    <Label id="lblIdPassenger" for="inputIdPassenger" sm="5">№ пассажира</Label>
                                                    <Col>
                                                    <Input type="number" name="inputIdPassenger" id="inputIdPassenger" placeholder="Введите ID пассажира" onChange={this.handlePassengerChange}/>
                                                    </Col>
                                                </FormGroup>
                                                <FormGroup row>
                                                    <Label id lbl for="inputIdFlight" sm="5">№ рейса</Label>
                                                    <Col>
                                                    <Input type="number" name="inputIdFlight" id="inputIdFlight" placeholder="Введите ID рейса" onChange={this.handleFlightChange}/>
                                                    </Col>
                                                </FormGroup>
                                                <FormGroup row>
                                                    <Label for="exampleSelect" sm="5">Выберите класс:</Label>
                                                    <Col>
                                                        <Input type="select" name="select" id="exampleSelect"  onChange={this.handleClassChange}>
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
                    </TabPane>

                    <TabPane tabId="2">
                        <div className="content_container">
                            <Nav subtabs>
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
                                                <tbody onSubmit={this.handleDeleteFlight}>
                                                {this.state.flights.map(function (flight) {
                                                    return (
                                                        <tr>
                                                            <th id="thIdFlight" key = {flight.idFlight}> {flight.idFlight}</th>
                                                            <td> {flight.idRoute}</td>
                                                            <td contentEditable={true}> {flight.dtFlight}</td>
                                                            <td> {flight.nnTickets}</td>
                                                            <td> {flight.maxTickets}</td>
                                                            <td> {flight.uid}</td>
                                                            <td><Button color="danger" type="submit">Удалить</Button></td>
                                                        </tr>
                                                    )
                                                })}
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
                    </TabPane>

                    <TabPane tabId="3">
                        <TabPane tabId="1">
                            <div className="content_container">
                                <Nav subtabs>
                                    <NavItem>
                                        <NavLink
                                            className={classnames({ active: this.state.activeSubTab === '0' })}
                                            onClick={() => { this.toggle1('0'); }}
                                        >
                                            Маршруты
                                        </NavLink>
                                    </NavItem>
                                    <NavItem>
                                        <NavLink
                                            className={classnames({ active: this.state.activeSubTab === '1' })}
                                            onClick={() => { this.toggle1('1'); }}
                                        >
                                            Добавить маршрут
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
                                                        <th> № маршрута </th>
                                                        <th> Направление </th>
                                                        <th> Уникальный номер </th>
                                                        <th></th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    {this.state.routes.map(function (route) {
                                                        return (
                                                            <tr>
                                                                <th id="thIdRoute" key = {route.idRoute}> {route.idRoute}</th>
                                                                <td contentEditable={true}> {route.routeName} </td>
                                                                <td> {route.uid}</td>
                                                                <td><Button color="danger" type="submit">Удалить</Button></td>
                                                            </tr>
                                                        )
                                                    })}
                                                    </tbody>
                                                </Table>
                                            </Col>
                                        </Row>
                                    </TabPane>
                                    <TabPane tabId="1">
                                        <Row>
                                            <Col sm="6">
                                                <Form onSubmit={this.handleSubmitRoute}>
                                                    <FormGroup row>
                                                        <Label id="lblNmRoute" for="inputNmRoute" sm="5">№ пассажира</Label>
                                                        <Col>
                                                            <Input name="inputNmRoute" id="inputNmRoute" placeholder="Введите направление маршрута" onChange={this.handleRouteNmChange}/>
                                                        </Col>
                                                    </FormGroup>
                                                    <FormGroup row>
                                                        <Button id="btnCreateRoute" type="submit">Добавить</Button>
                                                    </FormGroup>
                                                </Form>
                                            </Col>
                                        </Row>
                                    </TabPane>
                                </TabContent>
                            </div>
                        </TabPane>
                    </TabPane>
                </TabContent>
            </div>
        )
    }
}

export default App