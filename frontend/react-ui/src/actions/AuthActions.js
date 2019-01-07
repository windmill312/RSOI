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
        localStorage.removeItem('jwtToken');
        setAuthorizationToken(false);
        dispatch(setCurrentUser({}));
    }
}

export function login(data) {
    return dispatch => {
        return axios.post('http://localhost:8090/api/auth/signin', data).then(res => {
            const accessToken = res.data.accessToken;
            localStorage.setItem('jwtAccessToken', accessToken);
            setAuthorizationToken(accessToken);
            dispatch(setCurrentUser(jwtDecode(accessToken)));
        });
    }
}