import axios from 'axios';

export function userSignUpRequest(userData) {
    return dispatch => {
        return axios.post(`http://localhost:8090/api/auth/signin`, userData,
            {
                headers: {'Content-Type': 'application/json'}
            })
            .then((result) => {
                if (result.status === 200) {
                    console.info('status = 200');
                    this.props.userInfo(result.data);
                } else {
                    console.info('status = ' + result.status);
                    alert('Проверьте правильность введенных данных!');
                }
            })
            .catch(error => {
                console.info(error);
                alert('Произошла ошибка при авторизации!');
            });
    }
}