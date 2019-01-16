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
import OAuthForm from "./components/Login/OAuthForm";

export default (
    <Route path='/' component={App}>
        <IndexRoute component={Greetings}/>
        <Route path="/signup" component={SignUp}/>
        <Route path="/login" component={Login}/>
        <Route path="/routes">
            <IndexRoute component={Routes}/>
            <Route path="/routes/create" component={requireAuth(AddRoute)}/>
            <Route path="/routes/aggregation" component={AggregationRoute}/>
        </Route>
        <Route path="/flights" >
            <IndexRoute component={Flights}/>
            <Route path="/flights/create" component={requireAuth(AddFlight)}/>
        </Route>
        <Route path="/tickets" >
            <IndexRoute component={requireAuth(Tickets)}/>
            <Route path="/tickets/create" component={requireAuth(AddTicket)}/>
        </Route>
        <Route path="/oauth" component={requireAuth(OAuthForm)}/>
    </Route>
)