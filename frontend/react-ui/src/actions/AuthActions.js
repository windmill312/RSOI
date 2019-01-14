import axios from 'axios';
import setAuthorizationToken from '../utils/SetAuthorizationToken';
import jwtDecode from 'jwt-decode';
import { SET_CURRENT_USER } from './types';
import {API_BASE_URL, SERVICE_UUID} from '../config'

export function setCurrentUser(user) {
    return {
        type: SET_CURRENT_USER,
        user
    };
}

export function logout() {
    return dispatch => {
        localStorage.removeItem('jwtAccessToken');
        setAuthorizationToken(false);
        dispatch(setCurrentUser({}));
    }
}

export function login(data) {
    return dispatch => {
        return axios.post(`${API_BASE_URL}/api/auth/signin`, data).then(res => {
            const accessToken = res.data.accessToken;
            const refreshToken = res.data.refreshToken;
            const jwtExpirationInMs = res.data.jwtExpirationInMs;
            const isAdmin = res.data.admin;
            const msFrom = new Date().getTime().toString();
            localStorage.setItem('jwtAccessToken', accessToken);
            localStorage.setItem('jwtRefreshToken', refreshToken);
            localStorage.setItem('jwtExpirationInMs', jwtExpirationInMs);
            localStorage.setItem('isAdmin', isAdmin);
            localStorage.setItem('msFrom', msFrom);
            setAuthorizationToken(accessToken);
            const decodedToken = jwtDecode(accessToken);
            localStorage.setItem('userUuid', decodedToken.jti);
            dispatch(setCurrentUser(decodedToken));

        });
    }
}

export function refreshToken() {
    return dispatch => {
    if ( new Date().getTime() - parseInt(localStorage.getItem('msFrom'))  > parseInt(localStorage.getItem('jwtExpirationInMs'))) {

        const data = {
            identifier: localStorage.getItem('identifier'),
            serviceUuid: `${SERVICE_UUID}`
        };


            const authToken = localStorage.getItem('jwtRefreshToken');
            return axios.post(`${API_BASE_URL}/api/auth/refresh-token`, data, {
                headers: {
                    // Provide our existing token as credentials to get a new one
                    Authorization: `Bearer ${authToken}`
                }
            })
                .then(res => {
                    const accessToken = res.data.accessToken;
                    const refreshToken = res.data.refreshToken;
                    const jwtExpirationInMs = res.data.jwtExpirationInMs;
                    const isAdmin = res.data.admin;
                    const msFrom = new Date().getTime().toString();
                    localStorage.setItem('jwtAccessToken', accessToken);
                    localStorage.setItem('jwtRefreshToken', refreshToken);
                    localStorage.setItem('jwtExpirationInMs', jwtExpirationInMs);
                    localStorage.setItem('isAdmin', isAdmin);
                    localStorage.setItem('msFrom', msFrom);
                    setAuthorizationToken(accessToken);
                })
                .catch(err => {
                    // We couldn't get a refresh token because our current credentials
                    // are invalid or expired, or something else went wrong, so clear
                    // them and sign us out
                    console.log(err);
                    localStorage.removeItem('jwtAccessToken');
                    localStorage.removeItem('jwtRefreshToken');
                    localStorage.removeItem('jwtExpirationInMs');
                    localStorage.removeItem('isAdmin');
                    localStorage.removeItem('msFrom');
                });
        }
    }
}