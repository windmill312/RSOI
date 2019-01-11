import React from 'react';
import FlightFormNew from './FlightFormNew';
import {pingFlights, countFlights, deleteFlight, getFlights, createFlight, isRouteExists} from '../../actions/FlightsActions';
import PropTypes from "prop-types";
import connect from "react-redux/es/connect/connect";

class FlightPage extends React.Component {
    render() {
        const {pingFlights, countFlights, deleteFlight, getFlights} = this.props;
        return (
            <div>
                <FlightFormNew
                    pingFlights={pingFlights}
                    countFlights={countFlights}
                    deleteFlight={deleteFlight}
                    getFlights={getFlights}
                />
                {this.props.children}
            </div>
        );
    }
}

FlightPage.propTypes = {
    pingFlights: PropTypes.func.isRequired,
    countFlights: PropTypes.func.isRequired,
    deleteFlight: PropTypes.func.isRequired,
    getFlights: PropTypes.func.isRequired,
    createFlight: PropTypes.func.isRequired
};

export default connect(null, { pingFlights, countFlights, deleteFlight, getFlights, createFlight, isRouteExists})(FlightPage);