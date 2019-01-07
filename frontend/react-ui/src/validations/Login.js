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

    return {
        errors,
        isValid: isEmpty(errors)
    };
}