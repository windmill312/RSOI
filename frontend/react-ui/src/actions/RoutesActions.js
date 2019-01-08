import axios from 'axios';

export function pingRoutes() {
    return dispatch => {
        return axios.get(`http://localhost:8090/pingRoutes`,
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

export function countRoutes() {
    return dispatch => {
        return axios.get(`http://localhost:8090/countRoutes`,
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

export function getRoutes(size, page) {
    return dispatch => {
        return axios.get('http://localhost:8090/routes?size=' + size + '&page=' + page,
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

export function getTicketsAndFlights(route) {
    return dispatch => {
        return axios.get(`http://localhost:8090/flightsAndTicketsByRoute?uidRoute=` + route,
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

export function createRoute(data) {
    return dispatch => {
        return axios.put(`http://localhost:8090/route`, data,
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

export function deleteRoute(route) {
    return dispatch => {
        return axios.delete(`http://localhost:8090/route`,
            {
                data: route,
                headers:
                    {
                        'Content-Type': 'application/json',
                        'User': localStorage.getItem('userUuid'),
                        'Service': 'ede4bfb8-2acb-441e-9b00-4b786309fcd2'
                    }
            });
    }
}