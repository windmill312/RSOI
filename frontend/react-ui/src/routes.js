import React from 'react';
import {Route, IndexRoute} from 'react-router';

import App from './components/App';
import Greetings from './components/common/Greetings';
import Login from './components/Login/LoginPage';
import SignUp from './components/SignUp/SignUpPage';
import Routes from './components/Routes/RoutePage';
import AddRoute from './components/Routes/AddRoute';
import AggregationRoute from './components/Routes/AggregationRoute';
import Flights from './components/Flights/FlightPage';
import AddFlight from './components/Flights/AddFlight';
import Tickets from './components/Tickets/TicketPage';
import AddTicket from './components/Tickets/AddTicket';
import requireAuth from './utils/requireAuth';

export default (
    <Route path='/' component={App}>
        <IndexRoute component={Greetings}/>
        <Route path="/signup" component={SignUp}/>
        <Route path="/login" component={Login}/>
        <Route path="/routes">
            <IndexRoute component={requireAuth(Routes)}/>
            <Route path="/routes/create" component={requireAuth(AddRoute)}/>
            <Route path="/routes/aggregation" component={requireAuth(AggregationRoute)}/>
        </Route>
        <Route path="/flights" >
            <IndexRoute component={requireAuth(Flights)}/>
            <Route path="/flights/create" component={AddFlight}/>
        </Route>
        <Route path="/tickets" >
            <IndexRoute component={Tickets}/>
            <Route path="/tickets/create" component={AddTicket}/>
        </Route>
    </Route>
)