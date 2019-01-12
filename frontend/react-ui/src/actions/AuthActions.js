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
            localStorage.setItem('jwtAccessToken', accessToken);
            localStorage.setItem('jwtRefreshToken', refreshToken);
            localStorage.setItem('jwtExpirationInMs', jwtExpirationInMs);
            setAuthorizationToken(accessToken);
            const decodedToken = jwtDecode(accessToken);
            localStorage.setItem('userUuid', decodedToken.jti);
            dispatch(setCurrentUser(decodedToken));

        });
    }
}

export function refreshToken() {
    return dispatch => {
        const authToken = localStorage.getItem('accessToken');
        return fetch(`${API_BASE_URL}/api/auth/refresh-token`, {
            method: 'POST',
            headers: {
                // Provide our existing token as credentials to get a new one
                Authorization: `Bearer ${authToken}`
            },
            data: {
                identifier: localStorage.getItem('identifier'),
                serviceUuid: `${SERVICE_UUID}`
            }
        })
            .then(res => res.json())
            .then(res => {
                const accessToken = res.data.accessToken;
                const refreshToken = res.data.refreshToken;
                const jwtExpirationInMs = res.data.jwtExpirationInMs;
                localStorage.setItem('jwtAccessToken', accessToken);
                localStorage.setItem('jwtRefreshToken', refreshToken);
                localStorage.setItem('jwtExpirationInMs', jwtExpirationInMs);
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
            });
    }
}