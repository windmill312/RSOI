import Validator from 'validator';
import isEmpty from 'lodash/isEmpty';

export default function validateInput(data) {
    let errors = {};

    if (Validator.isEmpty(data.username)) {
        errors.username = 'Это обязательное поле';
    }
    if (Validator.isEmpty(data.email)) {
        errors.email = 'Это обязательное поле';
    }
    if (!Validator.isEmail(data.email)) {
        errors.email = 'Неверный Email';
    }
    if (Validator.isEmpty(data.password)) {
        errors.password = 'Это обязательное поле';
    }
    if (data.password.length < 6) {
        errors.password = 'Пароль должен содержать не менее 6 символов';
    }
    if (Validator.isEmpty(data.passwordConfirmation)) {
        errors.passwordConfirmation = 'Это обязательное поле';
    }
    if (!Validator.equals(data.password, data.passwordConfirmation)) {
        errors.passwordConfirmation = 'Пароли должны совпадать';
    }

    return {
        errors,
        isValid: isEmpty(errors)
    }
}