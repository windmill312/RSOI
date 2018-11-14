import React from 'react';
import {Table, TabContent, TabPane, Nav, NavItem, NavLink, Button, Row, Col, Label, Form, Input, FormGroup} from 'reactstrap'
import classnames from 'classnames';
import axios from "axios";

class RouteForm extends React.Component {

    constructor() {
        super()
        this.state = {
            activeSubTab: '0',
            routes: []
        }
    }

    componentDidMount() {
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

    toggle1(tab) {
        if (this.state.activeSubTab !== tab) {
            this.setState({
                activeSubTab: tab
            });
        }
    }

    handleRouteNmChange = event => {
        this.setState({ routeName: event.target.value })
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

    handleDeleteRoute(route) {
        return event => {
            event.preventDefault();
            axios.delete(`http://localhost:8090/route`, {params: {idRoute: route.idRoute}})
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
                                    {this.state.routes.map((route) =>
                                            <tr>
                                                <th id="thIdRoute" key = {route.idRoute}> {route.idRoute}</th>
                                                <td> {route.routeName} </td>
                                                <td> {route.uid}</td>
                                                <td><Button color="danger" onClick={this.handleDeleteRoute(route)}>Удалить</Button></td>
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
        )
    }
}

export default RouteForm;