import axios from 'axios';
import {API_BASE_URL, SERVICE_UUID} from '../config'

export function pingRoutes() {
    return dispatch => {
        return axios.get(`${API_BASE_URL}/pingRoutes`,
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

export function countRoutes() {
    return dispatch => {
        return axios.get(`${API_BASE_URL}/countRoutes`,
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

export function getRoutes(size, page) {
    return dispatch => {
        return axios.get(`${API_BASE_URL}/routes?size=` + size + `&page=` + page,
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

export function getTicketsAndFlights(route) {
    return dispatch => {
        return axios.get(`${API_BASE_URL}/flightsAndTicketsByRoute?uidRoute=` + route,
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

export function createRoute(data) {
    return dispatch => {
        return axios.put(`${API_BASE_URL}/route`, data,
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

export function deleteRoute(route) {
    return dispatch => {
        return axios.delete(`${API_BASE_URL}/route`,
            {
                data: route,
                headers:
                    {
                        'Content-Type': 'application/json',
                        'User': localStorage.getItem('userUuid'),
                        'Service': `${SERVICE_UUID}`
                    }
            });
    }
}