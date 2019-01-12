import React from 'react';
import { Link } from 'react-router';
import { connect } from 'react-redux';
import { logout } from '../../actions/AuthActions';
import PropTypes from 'prop-types';

class NavigationBar extends React.Component {
    logout(e) {
        e.preventDefault();
        this.props.logout();
    }

    render() {
        const {isAuthenticated} = this.props.auth;

        const userLinks = (
            <ul className="nav navbar-nav navbar-right">
                <li><a href="#" onClick={this.logout.bind(this)}>Выйти</a></li>
            </ul>
        );

        const guestLinks = (
            <ul className="nav navbar-nav navbar-right">
                <li><Link to="/signup">Зарегистрироваться</Link></li>
                <li><Link to="/login">Войти</Link></li>
            </ul>
        );

        return (
            <nav className="navbar navbar-default">
                <div className="container-fluid">
                    <div className="navbar-header">
                        <a className="navbar-brand" href="/routes">Маршруты</a>
                        <a className="navbar-brand" href="/flights">Рейсы</a>
                        <a className="navbar-brand" href="/tickets">Мои билеты</a>
                    </div>

                    <div className="collapse navbar-collapse">
                        { isAuthenticated ? userLinks : guestLinks }
                    </div>
                </div>
            </nav>
        );
    }
}

NavigationBar.propTypes = {
    auth: PropTypes.object.isRequired
};

function mapStateToProps(state) {
    return {
        auth: state.auth
    };
}

export default connect(mapStateToProps, { logout })(NavigationBar);