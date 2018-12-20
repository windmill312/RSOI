import React from 'react';
import {Table, Alert, TabContent, TabPane, Nav, Pagination, PaginationItem, PaginationLink, NavItem, NavLink, Button, Row, Col, Label, Form, Input, FormGroup} from 'reactstrap'
//import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import classnames from 'classnames';
import axios from "axios";

class RouteForm extends React.Component {

    constructor() {
        super();
        this.state = {
            activeSubTab: '0',
            routes: [],
            serviceAvailable: true,
            nnRoutes: 0,
            currentPage: 1,
            pageSize: 5,
            nnPages: 0,
            routeAggregation: []
        }
    }

    componentDidMount() {
        axios.get(`http://localhost:8090/pingRoutes`)
            .then((result) => {
                if (result.status === 200) {
                    axios.get(`http://localhost:8090/countRoutes`)
                        .then(result => {
                            this.setState({
                                nnRoutes: result.data,
                                nnPages : result.data/this.state.pageSize
                            });
                            axios.get('http://localhost:8090/routes?size=' + this.state.pageSize + '&page=' + this.state.currentPage)
                                .then(
                                    response => this.setState({
                                            routes: response.data,
                                            serviceAvailable: true
                                        }
                                    ))
                        })
                        .catch((error) => {
                            console.error(error);
                        });
                } else {
                    console.log('Ошибка при получении маршрутов (status = ' + result.status + ')');
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
                            routes: response.data
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

    getTicketsAndFlights = event =>  {
        event.preventDefault();
        axios.get(`http://localhost:8090/flightsAndTicketsByRoute?uidRoute=` + this.state.uidRoute)
            .then(result => {
                this.setState({
                    routeAggregation: result.data
                })
            });
        this.createAggregatedReport();
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


    /*[
        {
            "idFlight": 12,
            "uid": "0de0139c-b09e-4c0c-be01-854f5b439083",
            "uidRoute": "abb88b8f-1ce0-4c60-b609-b7e1bb48acd6",
            "dtFlight": "2018-08-02 19:45",
            "nnTickets": 1,
            "maxTickets": 50,
            "tickets": [
                {
                    "idTicket": 18,
                    "uidFlight": "0de0139c-b09e-4c0c-be01-854f5b439083",
                    "idPassenger": 45,
                    "classType": "Бизнес",
                    "uid": "55ec141d-65bb-406f-8211-a2f031f78b33"
                }
            ]
        },
{
    "idFlight": 13,
    "uid": "23581242-f010-4148-a3a9-2f18131cad07",
    "uidRoute": "abb88b8f-1ce0-4c60-b609-b7e1bb48acd6",
    "dtFlight": "2015-02-20 15:15",
    "nnTickets": 1,
    "nnTickets": 15,
    "tickets": [
        {
            "idTicket": 19,
            "uidFlight": "23581242-f010-4148-a3a9-2f18131cad07",
            "idPassenger": 1,
            "classType": "Бизнес",
            "uid": "a5b1028d-5812-490d-8935-03ee1ad71b13"
        }
        ]
}
]*/

    createAggregatedReport = () => {
        const aggrArray = [];
        this.state.routeAggregation.map(record => {

            aggrArray.push(
                <tr>
                    <td> {record.uid}</td>
                    <td> {record.routeName} </td>
                    <td> {record.dtFlight} </td>
                    <td> {record.nnTickets} </td>
                    <td> {record.nnTickets} </td>
                </tr>
            );

            return aggrArray;

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

    handleRouteNmChange = event => {
        this.setState({ routeName: event.target.value })
    }

    handleRouteUidChange = event => {
        this.setState({ uidRoute: event.target.value })
    }

    handleSubmitRoute = event => {
        event.preventDefault();
        const requestData = {
            routeName: this.state.routeName
        };
        axios.put(`http://localhost:8090/route`, requestData)
            .then((result) => {
                if (result.status === 200) {
                    console.info('status = 200');
                    alert('Маршрут успешно создан!');
                } else {
                    console.info('status = ' + result.status);
                    alert('Ошибка при создании маршрута!');
                }
            });
    }

    handleDeleteRoute(route) {
        return event => {
            event.preventDefault();
            axios.delete(`http://localhost:8090/route`, {data: route.uid})
                .then(result => {
                    if (result.status === 200) {
                        console.info('status = 200');
                        alert('Маршрут успешно удален!');
                    } else {
                        console.info('status = ' + result.status);
                        alert('Произошла ошибка при удалении маршрута!');
                    }
                })
        }
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
                                Маршруты
                            </NavLink>
                        </NavItem>
                        <NavItem>
                            <NavLink
                                className={classnames({active: this.state.activeSubTab === '1'})}
                                onClick={() => {
                                    this.toggle1('1');
                                }}
                            >
                                Добавить маршрут
                            </NavLink>
                        </NavItem>
                    </Nav>
                    <TabContent activeTab={this.state.activeSubTab}>
                        <TabPane tabId="0">
                            <Row>
                                <Col sm="12">

                                    <Table id="tableId" className="table" size="sm">
                                        <thead>
                                        <tr>
                                            <th> Уникальный номер маршрута</th>
                                            <th> Направление</th>
                                            <th></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {this.state.routes.map((route) =>
                                            <tr>
                                                <td> {route.uid}</td>
                                                <td> {route.routeName} </td>
                                                <td><Button color="danger"
                                                            onClick={this.handleDeleteRoute(route)}>Удалить</Button>
                                                </td>
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
                                    <Form onSubmit={this.handleSubmitRoute}>
                                        <FormGroup row>
                                            <Label id="lblNmRoute" for="inputNmRoute" sm="5">Направление</Label>
                                            <Col>
                                                <Input name="inputNmRoute" id="inputNmRoute"
                                                       placeholder="Введите направление маршрута"
                                                       onChange={this.handleRouteNmChange}/>
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
            )
        } else {
            return (
                <div>
                    <Alert color="danger">Сервис временно недоступен</Alert>
                    <Button outline onClick={()=> {this.componentDidMount(); this.render();}}>Обновить</Button>
                </div>
            )
        }
    }
}

export default RouteForm;