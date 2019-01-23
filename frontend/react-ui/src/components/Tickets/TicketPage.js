import React from 'react';
import TicketForm from './TicketForm';
import {countTickets, createTicket, deleteTicket, getTickets, isFlightExists, pingTickets} from '../../actions/TicketsActions';
import PropTypes from "prop-types";
import connect from "react-redux/es/connect/connect";

class TicketPage extends React.Component {
    render() {
        const {pingTickets, countTickets, deleteTicket, getTickets} = this.props;
        return (
            <div>
                <TicketForm
                    pingTickets={pingTickets}
                    countTickets={countTickets}
                    deleteTicket={deleteTicket}
                    getTickets={getTickets}
                />
                {this.props.children}
            </div>
        );
    }
}

TicketPage.propTypes = {
    pingTickets: PropTypes.func.isRequired,
    countTickets: PropTypes.func.isRequired,
    deleteTicket: PropTypes.func.isRequired,
    getTickets: PropTypes.func.isRequired,
    createTicket: PropTypes.func.isRequired,
    isFlightExists: PropTypes.func.isRequired
};

export default connect(null, { countTickets, createTicket, deleteTicket, getTickets, isFlightExists, pingTickets })(TicketPage);