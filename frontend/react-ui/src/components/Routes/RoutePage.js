import React from 'react';
import RouteForm from './RouteForm';
import {pingRoutes, countRoutes, getRoutes, createRoute, deleteRoute, getTicketsAndFlights} from '../../actions/RoutesActions';
import PropTypes from "prop-types";
import connect from "react-redux/es/connect/connect";

class RoutePage extends React.Component {
    render() {
        const {pingRoutes, countRoutes, getRoutes, createRoute, deleteRoute, getTicketsAndFlights} = this.props;
        return (
            <div>
                <RouteForm
                    pingRoutes={pingRoutes}
                    countRoutes={countRoutes}
                    createRoute={createRoute}
                    deleteRoute={deleteRoute}
                    getRoutes={getRoutes}
                    getTicketsAndFlights={getTicketsAndFlights}
                />
            </div>
        );
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

export default connect(null, { pingRoutes, countRoutes, getRoutes, createRoute, deleteRoute, getTicketsAndFlights})(RoutePage);
