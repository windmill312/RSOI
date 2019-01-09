import axios from 'axios';
import setAuthorizationToken from '../utils/SetAuthorizationToken';
import jwtDecode from 'jwt-decode';
import { SET_CURRENT_USER } from './types';

export function setCurrentUser(user) {
    return {
        type: SET_CURRENT_USER,
        user
    };
}

export function logout() {
    return dispatch => {
        localStorage.removeItem('jwtAccessToken');
        localStorage.removeItem('jwtRefreshToken');
        localStorage.removeItem('jwtExpirationInMs');
        localStorage.removeItem('userUuid');
        setAuthorizationToken(false);
        dispatch(setCurrentUser({}));
    }
}

export function login(data) {
    return dispatch => {
        return axios.post('http://localhost:8090/api/auth/signin', data).then(res => {
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