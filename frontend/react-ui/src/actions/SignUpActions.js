import axios from 'axios';
import {API_BASE_URL} from '../config'

export function userSignUpRequest(userData) {
    return dispatch => {
        return axios.post(`${API_BASE_URL}/api/auth/signup`, userData,
            {
                headers: {'Content-Type': 'application/json'}
            })
            .then((result) => {
                if (result.status === 201) {
                    console.info('status = 201');
                } else {
                    console.info('status = ' + result.status);
                }
            })
            .catch(error => {
                console.info(error);
            });
    }
}

export function isUserExists(identifier) {
    return dispatch => {
        return axios.get(`${API_BASE_URL}/api/${identifier}`);
    }
}