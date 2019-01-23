import React from 'react'
import ReactDOM from 'react-dom';
import * as serviceWorker from './serviceWorker';
import {Provider} from 'react-redux';
import thunk from 'redux-thunk';
import {applyMiddleware, compose, createStore} from 'redux';
import rootReducer from './rootReducer';
import setAuthorizationToken from './utils/SetAuthorizationToken';

import {browserHistory, Router} from 'react-router';
import {refreshToken, setCurrentUser} from './actions/AuthActions';
import routes from './routes';
import jwtDecode from 'jwt-decode';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';

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
        <Router history={browserHistory} routes={routes}/>
    </Provider>,
    document.getElementById('app'));

serviceWorker.unregister();
