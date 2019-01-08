import React from 'react';
import {Table, Alert, TabContent, TabPane, Nav, Pagination, PaginationItem, PaginationLink, NavItem, NavLink, Button, Row, Col, Label, Form, Input, FormGroup} from 'reactstrap'
import classnames from 'classnames';
import {pingRoutes, countRoutes, getRoutes, createRoute, deleteRoute, getTicketsAndFlights} from '../../actions/RoutesActions';
import PropTypes from "prop-types";
import connect from "react-redux/es/connect/connect";

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
            routeAggregation: [],
            userInfo:[]
        }
    }

    componentDidMount() {
        this.props.pingRoutes()
            .then((result) => {
                if (result.status === 200) {
                    this.props.countRoutes()
                        .then(result => {
                            this.setState({
                                nnRoutes: result.data,
                                nnPages: result.data / this.state.pageSize
                            });
                            this.props.getRoutes(this.state.pageSize, this.state.currentPage)
                                .then(
                                    response => {
                                        console.log(response);
                                        this.setState({
                                                routes: response.data,
                                                serviceAvailable: true
                                            }
                                        );
                                    })
                                .catch((error) => {
                                    console.error(error);
                                })
                        })
                } else {
                    console.log('Ошибка при получении маршрутов (status = ' + result.status + ')');
                }
            })
            .catch(error => {
                console.error(error);
                this.setState({
                    serviceAvailable: false
                })
            });
        console.log(this.state.routes);
    }

    handleCurrentPageChange (e, index) {
        if (index >= 0 && index<this.state.nnPages && index!==this.state.currentPage) {
            e.preventDefault();
            this.setState({currentPage: index});
            this.props.getRoutes(this.state.pageSize, this.state.currentPage + 1)
                .then(
                    response => this.setState({
                            routes: response.data
                        }
                    ))
                .catch((error) => {
                    console.error(error);
                });
            this.render();
        }
    }

    getTicketsAndFlights = event =>  {
        event.preventDefault();
        this.props.getTicketsAndFlights(this.state.uidRoute)
            .then(result => {
                this.setState({
                    routeAggregation: result.data
                })
            });
        this.createAggregatedReport();
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
    };

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
    };

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
    };

    handleRouteUidChange = event => {
        this.setState({ uidRoute: event.target.value })
    };

    handleSubmitRoute = event => {
        event.preventDefault();
        const requestData = {
            routeName: this.state.routeName
        };
        this.props.createRoute(requestData)
            .then(result => {
                if (result.status === 200) {
                    console.info('status = 200');
                    alert('Маршрут успешно создан!');
                } else {
                    console.info('status = ' + result.status);
                    alert('Ошибка при создании маршрута!');
                }
            });
    };

    handleDeleteRoute(route) {
        return event => {
            event.preventDefault();
            this.props.deleteRoute(route.uid)
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

RouteForm.propTypes = {
    pingRoutes: PropTypes.func.isRequired,
    countRoutes: PropTypes.func.isRequired,
    getRoutes: PropTypes.func.isRequired,
    createRoute: PropTypes.func.isRequired,
    deleteRoute: PropTypes.func.isRequired,
    getTicketsAndFlights: PropTypes.func.isRequired
};

RouteForm.contextTypes = {
    router: PropTypes.object.isRequired
};

export default connect(null, { pingRoutes, countRoutes, getRoutes, createRoute, deleteRoute, getTicketsAndFlights})(RouteForm);