import React from 'react'
import ReactDOM from 'react-dom';
import * as serviceWorker from './serviceWorker';
import {Provider} from 'react-redux';
import thunk from 'redux-thunk';
import { createStore, applyMiddleware, compose } from 'redux';
import rootReducer from './rootReducer';
import setAuthorizationToken from './utils/SetAuthorizationToken';
import {refreshToken} from "./actions/AuthActions";

import { Router, browserHistory } from 'react-router';
import { setCurrentUser } from './actions/AuthActions';
import routes from './routes';
import jwtDecode from 'jwt-decode';

const store = createStore(
    rootReducer,
    compose(
        applyMiddleware(thunk),
        window.devToolsExtension ? window.devToolsExtension() : f => f
    )
);

if (localStorage.jwtAccessToken) {
    setAuthorizationToken(localStorage.jwtAccessToken);
    store.dispatch(setCurrentUser(jwtDecode(localStorage.jwtAccessToken)));
    store.dispatch(refreshToken());
}

ReactDOM.render(
    <Provider store={store}>
        <Router history={browserHistory} routes={routes} />
    </Provider>,
    document.getElementById('app'));

serviceWorker.unregister();
