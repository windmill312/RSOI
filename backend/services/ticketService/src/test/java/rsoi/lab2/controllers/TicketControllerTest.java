package rsoi.lab2.controllers;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import rsoi.lab2.entity.Ticket;
import rsoi.lab2.model.TicketInfo;
import rsoi.lab2.services.TicketService;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    private final Gson gson = new Gson();
    @Autowired
    private MockMvc mvc;
    @MockBean
    private TicketService service;
    @InjectMocks
    private TicketController controller = new TicketController(service);

    @Test
    public void pingTest() {
        assertEquals(controller.ping(null).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getTicketsTest()
            throws Exception {

        TicketInfo ticket = new TicketInfo();
        ticket.setIdTicket(16);
        UUID uidFlight = UUID.randomUUID();
        ticket.setUidFlight(uidFlight);
        ticket.setUidPassenger(UUID.randomUUID());
        ticket.setClassType("ECONOMIC");

        List<TicketInfo> allTickets = Arrays.asList(ticket);

        given(service.listAll()).willReturn(allTickets);

        mvc.perform(get("/tickets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idTicket", is(ticket.getIdTicket())));
    }

    @Test
    public void getTicketTest() throws Exception {
        TicketInfo ticket = new TicketInfo();
        ticket.setIdTicket(16);
        UUID uidFlight = UUID.randomUUID();
        ticket.setUidFlight(uidFlight);
        ticket.setUidPassenger(UUID.randomUUID());
        ticket.setClassType("ECONOMIC");
        UUID uid = UUID.randomUUID();
        ticket.setUid(uid);

        given(service.getTicketInfoByUid(uid)).willReturn(ticket);

        MvcResult mvcResult = mvc.perform(get("/ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .param("uidTicket", ticket.getUid().toString()))
                .andExpect(status().isOk())
                .andReturn();

        TicketInfo newTicket = gson.fromJson(mvcResult.getResponse().getContentAsString(), TicketInfo.class);
        assertEquals(ticket.getIdTicket(), newTicket.getIdTicket());
    }

    @Test
    public void getFlightTicketsTest() throws Exception {
        TicketInfo ticket1 = new TicketInfo();
        ticket1.setIdTicket(16);
        UUID uidFlight = UUID.randomUUID();
        ticket1.setUidFlight(uidFlight);
        ticket1.setUidPassenger(UUID.randomUUID());
        ticket1.setClassType("ECONOMIC");

        TicketInfo ticket2 = new TicketInfo();
        ticket2.setIdTicket(16);
        ticket2.setUidFlight(uidFlight);
        ticket2.setUidPassenger(UUID.randomUUID());
        ticket2.setClassType("LUXURY");

        List<TicketInfo> allTickets = Arrays.asList(ticket1, ticket2);
        given(service.listFlightTickets(uidFlight)).willReturn(allTickets);

        MvcResult mvcResult = mvc.perform(get("/flightTickets")
                .param("uidFlight", uidFlight.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JSONArray jsonFlightArray = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertTrue(jsonFlightArray.length() == 2);
    }

    @Test
    public void getFlightTicketsByClassTypeTest() throws Exception {

        UUID uidFlight = UUID.randomUUID();
        given(service.countTicketsByFlightAndClassType(uidFlight, "ECONOMIC")).willReturn(1);

        MvcResult mvcResult = mvc.perform(get("/countTickets")
                .param("uidFlight", uidFlight.toString())
                .param("classType", "ECONOMIC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        int count = Integer.parseInt(mvcResult.getResponse().getContentAsString());
        assertTrue(count == 1);
    }

    @Test
    public void addTicketTest() throws Exception {

        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setClassType("ECONOMIC");
        UUID uid = UUID.randomUUID();
        ticketInfo.setUidFlight(uid);
        ticketInfo.setUidPassenger(UUID.randomUUID());

        Ticket ticket = new Ticket();
        ticket.setClassType(ticketInfo.getClassType());
        ticket.setUidFlight(ticketInfo.getUidFlight());
        ticket.setUidPassenger(ticketInfo.getUidPassenger());

        given(service.saveOrUpdate(ticket)).willReturn(ticket);

        MvcResult mvcResult = mvc.perform(put("/ticket")
                .content(gson.toJson(ticketInfo))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        UUID uidTicket = UUID.fromString(mvcResult.getResponse().getContentAsString());

        assertNotEquals(uidTicket, "");
    }

    @Test
    public void editTicketTest() throws Exception {

        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setIdTicket(16);
        ticketInfo.setClassType("ECONOMIC");
        UUID uidFlight = UUID.randomUUID();
        ticketInfo.setUidFlight(uidFlight);
        UUID uid = UUID.randomUUID();
        ticketInfo.setUid(uid);
        ticketInfo.setUidPassenger(UUID.randomUUID());

        Ticket ticket = new Ticket();
        ticket.setClassType(ticketInfo.getClassType());
        ticket.setUidFlight(ticketInfo.getUidFlight());
        ticket.setUidPassenger(ticketInfo.getUidPassenger());
        ticket.setUid(uid);

        given(service.getTicketByUid(uid)).willReturn(ticket);
        given(service.saveOrUpdate(ticket)).willReturn(ticket);

        mvc.perform(patch("/ticket")
                .content(gson.toJson(ticketInfo))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTicketTest() throws Exception {

        UUID uid = UUID.randomUUID();
        doNothing().when(service).delete(uid);
        mvc.perform(delete("/ticket")
                .content(uid.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteFlightTicketsTest() throws Exception {

        UUID uid = UUID.randomUUID();
        doNothing().when(service).deleteFlightTickets(uid);
        mvc.perform(delete("/tickets")
                .content(uid.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }
}