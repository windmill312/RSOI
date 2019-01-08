import React from 'react';
import {Route, IndexRoute} from 'react-router';

import App from './components/App';
import Greetings from './components/common/Greetings';
import Login from './components/Login/LoginPage';
import SignUp from './components/SignUp/SignUpPage';
import Routes from './components/Routes/RoutePage';
import Flights from './components/Flights/FlightForm';
import Tickets from './components/Tickets/TicketForm';
import requireAuth from './utils/requireAuth';

export default (
    <Route path='/' component={App}>
        <IndexRoute component={Greetings}/>
        <Route path="/signup" component={SignUp}/>
        <Route path="/login" component={Login}/>
        <Route path="/routes" component={requireAuth(Routes)}/>
        <Route path="/flights" component={requireAuth(Flights)}/>
        <Route path="/tickets" component={requireAuth(Tickets)}/>
    </Route>
)