import React from 'react'
import './styles/App.css'
import {TabContent, TabPane, Nav, NavItem, NavLink} from 'reactstrap'
import "react-datepicker/dist/react-datepicker.css";
import classnames from 'classnames';
import 'bootstrap/dist/css/bootstrap.css'
import TicketForm from './components/TicketForm';
import FlightForm from './components/FlightForm';
import RouteForm from './components/RouteForm';
import AuthForm from './components/Login'
import RegForm from "./components/RegForm";
import axios from "axios";

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            activeTab: '0',
            disabled: false,
            userUuid: '',
            userName: '',
            userInfo: []
        }

        this.toggle = this.toggle.bind(this);
    }

    toggle(tab) {
        if (this.state.activeTab !== tab) {
            this.setState({
                activeTab: tab
            });
            if (tab === '0')
                this.setState({
                    disabled: false
                })
        }
    }

    render() {
        return (
            <div id="root">
                <Nav tabs>
                    <NavItem>
                        <NavLink disabled={this.state.disabled} id="authTab"
                                 className={classnames({ active: this.state.activeTab === '0' })}
                        >
                            Авторизация
                        </NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink disabled={!this.state.disabled} id="ticketTab"
                            className={classnames({ active: this.state.activeTab === '1' })}
                                onClick={() => {this.toggle('1')}}
                        >
                            Билеты
                        </NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink disabled={!this.state.disabled}
                            className={classnames({ active: this.state.activeTab === '2' })}
                                 onClick={() => {this.toggle('2')}}
                        >
                            Рейсы
                        </NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink disabled={!this.state.disabled}
                            className={classnames({ active: this.state.activeTab === '3' })}
                            onClick={() => {this.toggle('3')}}
                        >
                            Маршруты
                        </NavLink>
                    </NavItem>
                </Nav>
                <TabContent activeTab={this.state.activeTab}>
                    <TabPane tabId="0">
                        <AuthForm
                            regTransition={() => {this.toggle('4')}}
                            routeTransition={(page) => {this.toggle(page); this.setState({disabled: true}) }}/>
                    </TabPane>
                    <TabPane tabId="1">
                        <div className="content_container">
                            <TicketForm />
                        </div>
                    </TabPane>
                    <TabPane tabId="2">
                        <div className="content_container">
                            <FlightForm />
                        </div>
                    </TabPane>
                    <TabPane tabId="3">
                        <TabPane tabId="1">
                            <div className="content_container">
                                <RouteForm />
                            </div>
                        </TabPane>
                    </TabPane>
                    <TabPane tabId="4">
                        <TabPane tabId="1">
                            <div className="content_container">
                                <RegForm authTransition={this.toggle}/>
                            </div>
                        </TabPane>
                    </TabPane>
                </TabContent>
            </div>
        )
    }
}

export default App