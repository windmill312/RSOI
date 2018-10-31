package rsoi.lab2.gateway.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rsoi.lab2.gateway.model.FlightInfo;
import rsoi.lab2.gateway.model.RouteInfo;
import rsoi.lab2.gateway.model.TicketInfo;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GatewayControllerTest {

    GatewayController controller = new GatewayController();

    @Test
    public void addRoute() {

        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setRouteName("TEST");

        assertTrue(controller.addRoute(routeInfo).contains("Route created with id"));

    }

    @Test
    public void addFlight() {

        FlightInfo flightInfo1 = new FlightInfo();
        flightInfo1.setNnTickets(0);
        flightInfo1.setMaxTickets(5);
        flightInfo1.setIdRoute(1);
        flightInfo1.setDtFlight("01.05.2018 12:10:55");

        assertTrue(controller.addFlight(flightInfo1).contains("Flight created with id"));
    }

    @Test
    public void deleteFlight() {

        FlightInfo flightInfo2 = new FlightInfo();
        flightInfo2.setNnTickets(0);
        flightInfo2.setMaxTickets(5);
        flightInfo2.setIdRoute(1);
        flightInfo2.setDtFlight("01.05.2018 12:10:55");

        String flight = controller.addFlight(flightInfo2);
        int idFlight = Integer.parseInt(flight.replaceAll("\\D+", ""));

        assertTrue(controller.deleteFlight(idFlight).contains("Flight successfully removed"));

    }

    @Test
    public void addTicket() {

        FlightInfo flightInfo2 = new FlightInfo();
        flightInfo2.setNnTickets(0);
        flightInfo2.setMaxTickets(5);
        flightInfo2.setIdRoute(1);
        flightInfo2.setDtFlight("01.05.2018 12:10:55");

        String flight = controller.addFlight(flightInfo2);

        int idFlight = Integer.parseInt(flight.replaceAll("\\D+", ""));

        TicketInfo ticket1 = new TicketInfo();
        ticket1.setIdFlight(idFlight);
        ticket1.setIdPassenger(0);
        ticket1.setClassType("ECONOMIC");

        String oldFlight = controller.getFlight(idFlight);

        assertTrue(controller.addTicket(ticket1).contains("Ticket created with id"));

        String newFlight = controller.getFlight(idFlight);

        assertTrue(oldFlight != newFlight);

    }

    @Test
    public void deleteTicket() {

        FlightInfo flightInfo2 = new FlightInfo();
        flightInfo2.setNnTickets(0);
        flightInfo2.setMaxTickets(5);
        flightInfo2.setIdRoute(1);
        flightInfo2.setDtFlight("01.05.2018 12:10:55");

        int idFlight = Integer.parseInt(controller.addFlight(flightInfo2).replaceAll("\\D+", ""));

        TicketInfo ticket1 = new TicketInfo();
        ticket1.setIdFlight(idFlight);
        ticket1.setIdPassenger(0);
        ticket1.setClassType("ECONOMIC");

        assertTrue(controller.addTicket(ticket1).contains("Ticket created with id"));

        int idTicket = Integer.parseInt(controller.addTicket(ticket1).replaceAll("\\D+", ""));

        String oldFlight = controller.getFlight(idFlight);

        assertTrue(controller.deleteTicket(idTicket).equals("Ticket removed"));

        String newFlight = controller.getFlight(idFlight);

        assertTrue(oldFlight != newFlight);

    }

    @Test
    public void findFlightsAndTickets() {

        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setRouteName("TEST");
        routeInfo.setIdRoute(89);

        int idRoute = Integer.parseInt(controller.addRoute(routeInfo).replaceAll("\\D+", ""));

        FlightInfo flightInfo2 = new FlightInfo();
        flightInfo2.setNnTickets(0);
        flightInfo2.setMaxTickets(5);
        flightInfo2.setIdRoute(idRoute);
        flightInfo2.setDtFlight("01.05.2018 12:10:55");

        int idFlight = Integer.parseInt(controller.addFlight(flightInfo2).replaceAll("\\D+", ""));

        TicketInfo ticket1 = new TicketInfo();
        ticket1.setIdFlight(idFlight);
        ticket1.setIdPassenger(0);
        ticket1.setClassType("ECONOMIC");

        int idTicket = Integer.parseInt(controller.addTicket(ticket1).replaceAll("\\D+", ""));

        assertTrue(controller.findFlightsAndTickets(idRoute, 1, 5).contains(String.valueOf(idTicket)));

    }

    @Test
    public void deleteRoute() {

        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setRouteName("TEST");

        int idRoute = Integer.parseInt(controller.addRoute(routeInfo).replaceAll("\\D+", ""));

        assertTrue(controller.deleteRoute(idRoute).contains("Route successfully removed"));

    }
}
