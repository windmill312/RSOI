import React from 'react';
import {Route, IndexRoute} from 'react-router';

import App from './components/App';
import Greetings from './components/common/Greetings';
import Login from './components/Login/LoginPage';
import SignUp from './components/SignUp/SignUpPage';
import Routes from './components/Routes/RouteForm';
import Flights from './components/Flights/FlightForm';
import Tickets from './components/Tickets/TicketForm';

export default (
    <Route path='/' component={App}>
        <IndexRoute component={Greetings}/>
        <Route path="/signup" component={SignUp}/>
        <Route path="/login" component={Login}/>
        <Route path="/routes" component={Routes}/>
        <Route path="/flights" component={Flights}/>
        <Route path="/tickets" component={Tickets}/>
    </Route>
)