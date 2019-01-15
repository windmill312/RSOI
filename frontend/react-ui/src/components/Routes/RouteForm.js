import React from 'react';
import { Button, DropdownButton, MenuItem, Pagination, Alert, Table} from 'react-bootstrap';
import {pingRoutes, countRoutes, getRoutes, deleteRoute, getTicketsAndFlights} from '../../actions/RoutesActions';
import PropTypes from "prop-types";
import connect from "react-redux/es/connect/connect";
import '../../styles/Routes/Routes.css'

class RouteForm extends React.Component {

    constructor() {
        super();
        this.state = {
            routes: [],
            serviceAvailable: true,
            nnRoutes: 0,
            currentPage: 1,
            pageSize: 5,
            nnPages: 0
        };

        this.handleCurrentPageChange = this.handleCurrentPageChange.bind(this);
    }

    componentDidMount() {
        this.props.pingRoutes()
            .then((result) => {
                if (result.status === 200) {
                    this.props.countRoutes()
                    .then(result => {
                        console.log(Math.ceil(result.data / this.state.pageSize));
                        this.setState({
                            nnRoutes: result.data,
                            nnPages: Math.ceil(result.data / this.state.pageSize)
                        });
                        this.props.getRoutes(this.state.pageSize, this.state.currentPage)
                            .then(
                                response => {
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

    handleCurrentPageChange (index) {
        if (index >= 1 && index<=this.state.nnPages && index!==this.state.currentPage) {
            this.props.getRoutes(this.state.pageSize, index)
                .then(
                    response => this.setState({
                        routes: response.data,
                        currentPage: index
                        }
                    ))
                .catch((error) => {
                    console.error(error);
                });
            this.render();
        }
    }

    createPagination() {
        const items = [];
        for (let number = 1; number <= this.state.nnPages; number++) {
            items.push(
                <Pagination.Item active={number === this.state.currentPage} key={number} onClick={() => {
                    this.handleCurrentPageChange(number);
                }}>{number}</Pagination.Item>
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
                        <th> Уникальный номер маршрута</th>
                        <th> Направление</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.state.routes.map((route) =>
                        <tr key={route.uid}>
                            <td> {route.uid}</td>
                            <td> {route.routeName} </td>
                            <td><Button bsStyle="danger"
                                onClick={this.handleDeleteRoute(route)}
                                        disabled={localStorage.getItem('isAdmin') === 'false'}
                            >Удалить</Button>
                            </td>
                        </tr>
                    )}
                    </tbody>
                </Table>
                <br/>
            </div>
        )
    }

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
                    <div>
                        <DropdownButton className='dropdown'
                            bsStyle="info"
                            title="Действия"
                            id={`dropdown`} >

                            <MenuItem eventKey="1" href="/routes/create" disabled={localStorage.getItem('isAdmin') === 'false'} >Добавить</MenuItem>
                            <MenuItem eventKey="2" href="/routes/aggregation">Отчет по маршруту</MenuItem>

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
        } else {
            return (
                <div>
                    <Alert bsStyle="danger">
                        Сервис временно недоступен
                    </Alert>
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
    deleteRoute: PropTypes.func.isRequired,
    getTicketsAndFlights: PropTypes.func.isRequired
};

RouteForm.contextTypes = {
    router: PropTypes.object.isRequired
};

export default connect(null, { pingRoutes, getRoutes,countRoutes, deleteRoute, getTicketsAndFlights})(RouteForm);