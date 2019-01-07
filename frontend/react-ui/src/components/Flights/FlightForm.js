import React from 'react';
import {Table, Alert, Pagination, PaginationItem, PaginationLink, TabContent, TabPane, Nav, NavItem, NavLink, Button, Row, Col, Label, Form, Input, FormGroup} from 'reactstrap';
import classnames from 'classnames';
import axios from "axios";

class FlightForm extends React.Component {

    constructor() {
        super()
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
        axios.get(`http://localhost:8090/pingFlights`)
            .then((result) => {
                if (result.status === 200) {
                    axios.get(`http://localhost:8090/countFlights`)
                        .then(result => {
                            this.setState({
                                nnRoutes: result.data,
                                nnPages : result.data/this.state.pageSize
                            });
                            axios.get('http://localhost:8090/flights?size=' + this.state.pageSize + '&page=' + this.state.currentPage)
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

    handleCurrentPageChange (e, index) {
        if (index >= 0 && index<this.state.nnPages && index!==this.state.currentPage) {
            e.preventDefault();
            this.setState({currentPage: index});
            axios.get('http://localhost:8090/flights?size=' + this.state.pageSize + '&page=' + (index+1))
                .then(
                    response => this.setState({
                            flights: response.data
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

    toggle1(tab) {
        this.componentDidMount();
        if (this.state.activeSubTab !== tab) {
            this.setState({
                activeSubTab: tab
            });
        }
    }

    handleFlightRouteChange = event => {
        this.setState({ uidRoute: event.target.value });
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
            uidRoute: this.state.uidRoute,
            dtFlight: this.state.dtFlight + ' ' + this.state.tmFlight,
            maxTickets: this.state.maxTickets
        };
        axios.put(`http://localhost:8090/flight`, requestData)
            .then(result => {
                if (result.status === 200) {
                    console.info('status = 200');
                    alert('Рейс успешно создан!');
                } else {
                    console.info('status = ' + result.status);
                    alert('Произошла ошибка при создании рейса!');
                }
            })
            .catch(error => {
                console.info(error);
                alert('Произошла ошибка при создании рейса! Проверьте правильность кода маршрута');
            });
    }

    handleDeleteFlight(flight) {
        return event => {
            event.preventDefault();
            axios.delete(`http://localhost:8090/flight`, {data: flight.uid})
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
                                    {this.createPagination()}
                                </Col>
                            </Row>
                        </TabPane>
                        <TabPane tabId="1">
                            <Row>
                                <Col sm="6">
                                    <Form onSubmit={this.handleSubmitFlight}>
                                        <FormGroup row>
                                            <Label id="lblUidRoute" for="inputUidRoute" sm="5">Код маршрута</Label>
                                            <Col>
                                                <Input name="inputUidRoute" id="inputUidRoute" placeholder="Введите код маршрута" onChange={this.handleFlightRouteChange}/>
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

export default FlightForm;