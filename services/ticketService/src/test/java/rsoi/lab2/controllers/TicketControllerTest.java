package rsoi.lab2.controllers;

import org.junit.Before;
import org.junit.Test;
import rsoi.lab2.model.PingResponse;

import static org.junit.Assert.*;

public class TicketControllerTest {

    private TicketController app;

    @Before
    public void load() {
        app = new TicketController();
    }

    /*@Test
    public void pingTest(){
        assertSame(app.ping().getResponse(), new PingResponse("ok").getResponse());
    }

    @Test
    public void listTicketsTest() {
        assertEquals(app.listTickets(),null);
    }

    @Test
    public void addTicketTest() {
        assertEquals(app.add("ECONOMIC", 5,1,2), 0);
        assertEquals(app.add("ECONOMIC", 5,2,2),1);
        assertEquals(app.add("ECONOMIC", 5,3,2), -1);
    }

    @Test
    public void getFlightTickets() {
    }

    @Test
    public void getTicket() {
    }

    @Test
    public void add() {
    }

    @Test
    public void countFlightTickets() {
    }

    @Test
    public void countTickets() {
    }

    @Test
    public void edit() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void deleteFlightTickets() {
    }*/
}