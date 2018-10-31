package rsoi.lab2.gateway.controllers;

import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rsoi.lab2.gateway.model.FlightInfo;
import rsoi.lab2.gateway.model.RouteInfo;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GatewayControllerTest {

    GatewayController controller = new GatewayController();

    int idFlight;
    int idRoute;
    int idTicket;

    @Test
    public void addRoute() {

        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setRouteName("TEST");
        routeInfo.setIdRoute(89);

        assertTrue(controller.addRoute(routeInfo).contains("Route created with id"));
    }

    @Test
    public void addFlight() {

        String route = controller.getRouteByNm("TEST");
        JSONArray array = new JSONArray(route);
        idRoute = array.getJSONObject(0).getInt("idRoute");
        FlightInfo flightInfo1 = new FlightInfo();
        flightInfo1.setIdFlight(89);
        flightInfo1.setNnTickets(0);
        flightInfo1.setMaxTickets(5);
        flightInfo1.setIdRoute(idRoute);
        flightInfo1.setDtFlight("01.05.2018 12:10:55");

        FlightInfo flightInfo2 = new FlightInfo();
        flightInfo2.setIdFlight(88);
        flightInfo2.setNnTickets(0);
        flightInfo2.setMaxTickets(5);
        flightInfo2.setIdRoute(idRoute);
        flightInfo2.setDtFlight("01.05.2018 12:10:55");

        assertTrue(controller.addFlight(flightInfo1).contains("Flight created with id"));
        assertTrue(controller.addFlight(flightInfo2).contains("Flight created with id"));
    }

    @Test
    public void deleteFlight() {

        String flights = controller.getFlightsByRoute(idRoute, 1, 5);
        JSONArray array = new JSONArray(flights);
        int idRemovingFlight = array.getJSONObject(0).getInt("idFlight");
        assertTrue(controller.deleteFlight(idRemovingFlight).contains("Flight successfully removed"));
        idFlight = array.getJSONObject(1).getInt("idFlight");

    }

    /*@Test
    public void addTicket() {

        TicketInfo ticket1 = new TicketInfo();
        ticket1.setIdTicket(89);
        ticket1.setIdFlight(idFlight);
        ticket1.setIdPassenger(0);
        ticket1.setClassType("ECONOMIC");

        TicketInfo ticket2 = new TicketInfo();
        ticket2.setIdTicket(89);
        ticket2.setIdFlight(idFlight);
        ticket2.setIdPassenger(1);
        ticket2.setClassType("ECONOMIC");

        String oldFlight = controller.getFlight(idFlight);

        assertTrue(controller.addTicket(ticket1).contains("Ticket created with id"));
        assertTrue(controller.addTicket(ticket2).contains("Ticket created with id"));

        String newFlight = controller.getFlight(idFlight);

        assertTrue(oldFlight != newFlight);
    }

    @Test
    public void deleteTicket() {

        String flight = controller.getFlight(idFlight);

        String tickets = controller.getTickets(idFlight, 1, 5);
        JSONArray array = new JSONArray(tickets);
        int idRemovingTicket = array.getJSONObject(0).getInt("idTicket");
        idTicket = array.getJSONObject(1).getInt("idTicket");


        assertTrue(controller.deleteTicket(idRemovingTicket).equals("Ticket removed"));

        String newFlight = controller.getFlight(idFlight);

        assertTrue(flight != newFlight);

    }

    @Test
    public void findFlightsAndTickets() {

        assertTrue(!controller.findFlightsAndTickets(idRoute, 1 ,5).equals(""));

    }*/

    @Test
    public void deleteRoute() {

        assertTrue(controller.deleteRoute(idRoute).contains("Route successfully removed"));

        assertTrue(controller.getFlight(idFlight).equals("[]"));
        assertTrue(controller.getTicket(idTicket).equals("[]"));

    }
}
