import axios from 'axios';

export function userSignUpRequest(userData) {
    return dispatch => {
        return axios.post(`http://localhost:8090/api/auth/signup`, userData,
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
        return axios.get(`http://localhost:8090/api/${identifier}`);
    }
}