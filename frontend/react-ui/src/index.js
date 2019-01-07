import React from 'react'
import ReactDOM from 'react-dom';
import NavigationBar from './components/common/NavigationBar';
import * as serviceWorker from './serviceWorker';
import {Provider} from 'react-redux'
import thunk from 'redux-thunk'
import { createStore, applyMiddleware } from 'redux'

import { Router, browserHistory } from 'react-router';
import routes from './routes'

const store = createStore (
    (state = {}) => state,
    applyMiddleware(thunk)
);

ReactDOM.render(
    <Provider store={store}>
        <Router history={browserHistory} routes={routes}>
            <NavigationBar />
        </Router>
    </Provider>,
    document.getElementById('app'));

serviceWorker.unregister();
