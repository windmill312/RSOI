import axios from 'axios';

export function pingTickets() {
    return dispatch => {
        return axios.get(`http://localhost:8090/pingTickets`,
            {
                headers:
                    {
                        'Content-Type':'application/json',
                        'User':localStorage.getItem('userUuid'),
                        'Service':'ede4bfb8-2acb-441e-9b00-4b786309fcd2'
                    }
            });
    }
}

export function countTickets() {
    return dispatch => {
        return axios.get(`http://localhost:8090/countTickets`,
            {
                headers:
                    {
                        'Content-Type': 'application/json',
                        'User': localStorage.getItem('userUuid'),
                        'Service': 'ede4bfb8-2acb-441e-9b00-4b786309fcd2'
                    }
            });
    }
}

export function getTickets(size, page) {
    return dispatch => {
        return axios.get('http://localhost:8090/tickets?size=' + size + '&page=' + page,
            {
                headers:
                    {
                        'Content-Type': 'application/json',
                        'User': localStorage.getItem('userUuid'),
                        'Service': 'ede4bfb8-2acb-441e-9b00-4b786309fcd2'
                    }
            });
    }
}

export function createTicket(data) {
    return dispatch => {
        return axios.put(`http://localhost:8090/ticket`, data,
            {
                headers:
                    {
                        'Content-Type': 'application/json',
                        'User': localStorage.getItem('userUuid'),
                        'Service': 'ede4bfb8-2acb-441e-9b00-4b786309fcd2'
                    }
            });
    }
}

export function deleteTicket(flight) {
    return dispatch => {
        return axios.delete(`http://localhost:8090/ticket`,
            {
                data: flight,
                headers:
                    {
                        'Content-Type': 'application/json',
                        'User': localStorage.getItem('userUuid'),
                        'Service': 'ede4bfb8-2acb-441e-9b00-4b786309fcd2'
                    }
            });
    }
}

//todo возможно надо перенести в FlightActions
export function isFlightExists(identifier) {
    return dispatch => {
        return axios.get(`http://localhost:8090/flight?uidFlight=${identifier}`,
            {
                headers:
                    {
                        'Content-Type': 'application/json',
                        'User': localStorage.getItem('userUuid'),
                        'Service': 'ede4bfb8-2acb-441e-9b00-4b786309fcd2'
                    }
            });
    }
}