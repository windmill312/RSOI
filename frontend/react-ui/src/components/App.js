import React from 'react'
import '../styles/App.css'
import "react-datepicker/dist/react-datepicker.css";
import NavigationBar from './common/NavigationBar';
import FlashMessagesList from '../components/flash/FlashMessagesList';
import {refreshToken} from "../actions/AuthActions";
import {connect} from 'react-redux';

class App extends React.Component {

    componentDidUpdate(prevProps) {
        if (!prevProps.props.auth && this.props.auth) {
            // When we are logged in, refresh the auth token periodically
            this.startPeriodicRefresh();
        } else if (prevProps.props.auth && !this.props.auth) {
            // Stop refreshing when we log out
            this.stopPeriodicRefresh();
        }
    }

    componentWillUnmount() {
        this.stopPeriodicRefresh();
    }

    startPeriodicRefresh() {
        this.refreshInterval = setInterval(
            () => this.props.dispatch(refreshToken()),
            0.5 * 60 * 1000 // Half a minute
        );
    }

    stopPeriodicRefresh() {
        if (!this.refreshInterval) {
            return;
        }

        clearInterval(this.refreshInterval);
    }


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

    render() {
        return (
            <div className="container">
                <NavigationBar />
                <FlashMessagesList />
                {this.props.children}
            </div>
        )
    }
}

function mapStateToProps(state) {
    return {
        auth: state.auth
    };
}

export default connect(mapStateToProps)(App);