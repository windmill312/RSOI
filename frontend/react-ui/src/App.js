import React from 'react'
import './App.css'
import axios from 'axios'
import {Button, Table} from 'reactstrap'
import 'bootstrap/dist/css/bootstrap.css'
import 'react-bootstrap-table/css/react-bootstrap-table.css'

class App extends React.Component {

    constructor() {
        super()
        this.state = {
            tickets: []
        }
        this.handleClick = this.handleClick.bind(this)

    }

    dropdown() {
        document.getElementById("tableId").style.display = "inline-block";
    }

    handleClick() {
        axios.get('http://localhost:8090/tickets')
            .then(
                response => this.setState({tickets: response.data}
                ))
            .catch((error) => {
                console.error(error);
            });

        this.dropdown();
    }

    deleteTicket() {
        console.log("deleted")
    }

    render() {
        return (
            < div
        className = "main" >
            < div
        className = 'button__container' >
            < Button
        className = 'button'
        onClick = {this.handleClick
    }>
        Get
        Tickets
        < /Button>
        < /div>

        < div
        className = "content_container" >
            < Table
        id = "tableId"
        className = "table" >
            < thead >
            < tr >
            < th > Ticket
        ID < /th>
        < th > Flight
        ID < /th>
        < th > Passenger
        ID < /th>
        < th > Class < /th>
        < th > UUID < /th>
        < /tr>
        < /thead>
        < tbody >
        {this.state.tickets.map(function (ticket) {
            return ( < tr >
                < th
            key = {ticket.idTicket
        }>
            {
                ticket.idTicket
            }
        <
            /th>
            < td > {ticket.idFlight
        }<
            /td>
            < td > {ticket.idPassenger
        }<
            /td>
            < td > {ticket.classType
        }<
            /td>
            < td > {ticket.uid
        }<
            /td>
            < Button
            className = "deleteButton"
            onClick = {this.deleteClick.bind(this, ticket.idTicket)
        }>
            Delete
            < /Button>
            < /tr>
        )
        })
    }
    <
        /tbody>
        < /Table>
        < /div>
        < /div>
    )
    }
}

export default App