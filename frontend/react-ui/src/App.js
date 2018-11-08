import React from 'react'
import './App.css'
import axios from 'axios'
import {Table, TabContent, TabPane, Nav, NavItem, NavLink, Card, Button, CardTitle, CardText, Row, Col, Jumbotron} from 'reactstrap'
import classnames from 'classnames';
import 'bootstrap/dist/css/bootstrap.css'

class App extends React.Component {

    constructor() {
        super()
        this.state = {
            tickets: [],
            activeTab: '0',
            disabled : false
        }
    }

    componentDidMount() {
        axios.get('http://localhost:8090/tickets')
            .then(
                response => this.setState({
                        tickets: response.data
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

    onOpen() {
        this.setState({
            activeTab: 1,
            disabled: true
        })
    }

    changeTicket(idTicket, classType) {
        console.log("changed")
    }

    deleteTicket(idTicket) {
        console.log("deleted")
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
                        <Row>
                            <Col sm="12">
                                <div className="content_container">
                                    <Table id = "tableId" className = "table">
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
                                        <tbody>
                                        {this.state.tickets.map(function (ticket) {
                                            return (
                                                <tr>
                                                    <th key = {ticket.idTicket}> {ticket.idTicket}</th>
                                                    <td> {ticket.idFlight}</td>
                                                    <td contentEditable={true} onChange={() => this.changeTicket()}> {ticket.idPassenger}</td>
                                                    <td> {ticket.classType}</td>
                                                    <td> {ticket.uid}</td>
                                                    <td><Button color="danger" onClick={() => this.deleteTicket(ticket.idTicket)}>Удалить</Button></td>
                                                </tr>
                                            )
                                        })}
                                        </tbody>
                                    </Table>
                                </div>
                            </Col>
                        </Row>
                    </TabPane>

                    <TabPane tabId="2">
                        <Row>
                            <Col sm="6">
                                <Card body>
                                    <CardTitle>Special Title Treatment</CardTitle>
                                    <CardText>With supporting text below as a natural lead-in to additional content.</CardText>
                                    <Button>Go somewhere</Button>
                                </Card>
                            </Col>
                            <Col sm="6">
                                <Card body>
                                    <CardTitle>Special Title Treatment</CardTitle>
                                    <CardText>With supporting text below as a natural lead-in to additional content.</CardText>
                                    <Button>Go somewhere</Button>
                                </Card>
                            </Col>
                        </Row>
                    </TabPane>

                    <TabPane tabId="3">
                        <Row>
                            <Col sm="6">
                                <Card body>
                                    <CardTitle>Special Title Treatment</CardTitle>
                                    <CardText>With supporting text below as a natural lead-in to additional content.</CardText>
                                    <Button>Go somewhere</Button>
                                </Card>
                            </Col>
                            <Col sm="6">
                                <Card body>
                                    <CardTitle>Special Title Treatment</CardTitle>
                                    <CardText>With supporting text below as a natural lead-in to additional content.</CardText>
                                    <Button>Go somewhere</Button>
                                </Card>
                            </Col>
                        </Row>
                    </TabPane>
                </TabContent>
            </div>
        )
    }
}

export default App