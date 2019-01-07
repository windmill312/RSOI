import Validator from 'validator';
import isEmpty from 'lodash/isEmpty';

export default function validateInput(data) {
    let errors = {};

    if (Validator.isEmpty(data.identifier)) {
        errors.identifier = '*обязательное поле';
    }

    if (Validator.isEmpty(data.password)) {
        errors.password = '*обязательное поле';
    }

    if (data.password.length < 6) {
        errors.password = 'Пароль не может быть менее 6 символов';
    }

    return {
        errors,
        isValid: isEmpty(errors)
    };
}