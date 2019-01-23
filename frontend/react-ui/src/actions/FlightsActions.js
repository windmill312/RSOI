import axios from 'axios';
import {API_BASE_URL, SERVICE_UUID} from '../config'

export function pingFlights() {
    return dispatch => {
        return axios.get(`${API_BASE_URL}/pingFlights`,
            {
                headers:
                    {
                        'Content-Type': 'application/json',
                        'User': localStorage.getItem('userUuid'),
                        'Service': `${SERVICE_UUID}`
                    }
            });
    }
}

export function countFlights() {
    return dispatch => {
        return axios.get(`${API_BASE_URL}/countFlights`,
            {
                headers:
                    {
                        'Content-Type': 'application/json',
                        'User': localStorage.getItem('userUuid'),
                        'Service': `${SERVICE_UUID}`
                    }
            });
    }
}

export function getFlights(size, page) {
    return dispatch => {
        return axios.get(API_BASE_URL + '/flights?size=' + size + '&page=' + page,
            {
                headers:
                    {
                        'Content-Type': 'application/json',
                        'User': localStorage.getItem('userUuid'),
                        'Service': `${SERVICE_UUID}`
                    }
            });
    }
}

export function createFlight(data) {
    return dispatch => {
        return axios.put(`${API_BASE_URL}/flight`, data,
            {
                headers:
                    {
                        'Content-Type': 'application/json',
                        'User': localStorage.getItem('userUuid'),
                        'Service': `${SERVICE_UUID}`
                    }
            });
    }
}

export function deleteFlight(flight) {
    return dispatch => {
        return axios.delete(`${API_BASE_URL}/flight`,
            {
                data: flight,
                headers:
                    {
                        'Content-Type': 'application/json',
                        'User': localStorage.getItem('userUuid'),
                        'Service': `${SERVICE_UUID}`
                    }
            });
    }
}

//todo возможно надо перенести в RouteActions
export function isRouteExists(identifier) {
    return dispatch => {
        return axios.get(`${API_BASE_URL}/route?uidRoute=${identifier}`,
            {
                headers:
                    {
                        'Content-Type': 'application/json',
                        'User': localStorage.getItem('userUuid'),
                        'Service': `${SERVICE_UUID}`
                    }
            });
    }
}