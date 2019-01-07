import { combineReducers } from 'redux';

import flashMessages from './reducers/FlashMessagesReducer';
import auth from './reducers/AuthReducer';

export default combineReducers({
    flashMessages,
    auth
});