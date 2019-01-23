import React from 'react';
import RouteForm from './RouteForm';
import {pingRoutes, countRoutes, getRoutes, deleteRoute} from '../../actions/RoutesActions';
import PropTypes from "prop-types";
import connect from "react-redux/es/connect/connect";

class RoutePage extends React.Component {
    render() {
        const {pingRoutes, countRoutes, getRoutes, deleteRoute} = this.props;
        return (
            <div>
                <RouteForm
                    pingRoutes={pingRoutes}
                    countRoutes={countRoutes}
                    deleteRoute={deleteRoute}
                    getRoutes={getRoutes}
                />
                {this.props.children}
            </div>
        );
    }
}

RouteForm.propTypes = {
    pingRoutes: PropTypes.func.isRequired,
    countRoutes: PropTypes.func.isRequired,
    getRoutes: PropTypes.func.isRequired,
    deleteRoute: PropTypes.func.isRequired
};

export default connect(null, { pingRoutes, countRoutes, getRoutes, deleteRoute})(RoutePage);