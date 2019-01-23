import React from 'react'
import '../styles/App.css'
import "react-datepicker/dist/react-datepicker.css";
import NavigationBar from './common/NavigationBar';
import FlashMessagesList from '../components/flash/FlashMessagesList';
import {refreshToken} from "../actions/AuthActions";
import {connect} from 'react-redux';
import PropTypes from "prop-types";

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

    }

    componentDidUpdate(prevProps) {
        if (!prevProps.auth && this.props.auth) {
            this.startPeriodicRefresh();
        } else if (prevProps.auth && !this.props.auth) {
            this.stopPeriodicRefresh();
        }
    }

    componentWillUnmount() {
        this.stopPeriodicRefresh();
    }

    startPeriodicRefresh() {
        this.refreshInterval = setInterval(
            () => refreshToken(),
            1
            //parseInt(localStorage.getItem('jwtExpirationInMs')) /3600
        );
    }

    stopPeriodicRefresh() {
        if (!this.refreshInterval) {
            return;
        }

        clearInterval(this.refreshInterval);
    }

    render() {
        return (
            <div className="container">
                <NavigationBar/>
                <FlashMessagesList/>
                {this.props.children}
            </div>
        )
    }
}

App.propTypes = {
    auth: PropTypes.object.isRequired
};

function mapStateToProps(state) {
    return {
        auth: state.auth
    };
}

export default connect(mapStateToProps)(App);