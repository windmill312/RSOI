import axios from 'axios';
import {API_BASE_URL} from '../config'

export function checkService(serviceUuid) {
    return dispatch => {
            return axios.get(`${API_BASE_URL}/oauth?serviceUuid=${serviceUuid}`);
    }
}

