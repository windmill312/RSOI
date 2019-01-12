import axios from 'axios';
import {API_BASE_URL} from '../config'

export function pingTickets() {
    return dispatch => {
        return axios.get(API_BASE_URL + `/pingTickets`,
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
        return axios.get(API_BASE_URL + `/countTickets`,
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
        return axios.get(API_BASE_URL + `/tickets?size=` + size + `&page=` + page,
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
        return axios.put(API_BASE_URL + `/ticket`, data,
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
        return axios.delete(API_BASE_URL + `/ticket`,
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
        return axios.get(API_BASE_URL + `/flight?uidFlight=${identifier}`,
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