import axios from 'axios';

export function pingFlights() {
    return dispatch => {
        return axios.get(`http://localhost:8090/pingFlights`,
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

export function countFlights() {
    return dispatch => {
        return axios.get(`http://localhost:8090/countFlights`,
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

export function getFlights(size, page) {
    return dispatch => {
        return axios.get('http://localhost:8090/flights?size=' + size + '&page=' + page,
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

export function createFlight(data) {
    return dispatch => {
        return axios.put(`http://localhost:8090/flight`, data,
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

export function deleteFlight(flight) {
    return dispatch => {
        return axios.delete(`http://localhost:8090/flight`,
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

//todo возможно надо перенести в RouteActions
export function isRouteExists(identifier) {
    return dispatch => {
        return axios.get(`http://localhost:8090/route?uidRoute=${identifier}`,
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