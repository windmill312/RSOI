/*package rsoi.lab2.controllers;

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
import User;
import UserInfo;
import UserService;

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
    private UserService service;
    @InjectMocks
    private TicketController controller = new TicketController(service);

    @Test
    public void pingTest() {
        assertEquals(controller.ping().getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getTicketsTest()
            throws Exception {

        UserInfo ticket = new UserInfo();
        ticket.setUuid(16);
        UUID uidFlight = UUID.randomUUID();
        ticket.setUidUser(uidFlight);
        ticket.setIdPassenger(0);
        ticket.setName("ECONOMIC");

        List<UserInfo> allTickets = Arrays.asList(ticket);

        given(service.listAll()).willReturn(allTickets);

        mvc.perform(get("/tickets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idTicket", is(ticket.getUuid())));
    }

    @Test
    public void getTicketTest() throws Exception {
        UserInfo ticket = new UserInfo();
        ticket.setUuid(16);
        UUID uidFlight = UUID.randomUUID();
        ticket.setUidUser(uidFlight);
        ticket.setIdPassenger(0);
        ticket.setName("ECONOMIC");
        UUID uid = UUID.randomUUID();
        ticket.setUid(uid);

        given(service.getUserInfoByUid(uid)).willReturn(ticket);

        MvcResult mvcResult = mvc.perform(get("/ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .param("uidTicket", ticket.getUid().toString()))
                .andExpect(status().isOk())
                .andReturn();

        UserInfo newTicket = gson.fromJson(mvcResult.getResponse().getContentAsString(), UserInfo.class);
        assertEquals(ticket.getUuid(), newTicket.getUuid());
    }

    @Test
    public void getFlightTicketsTest() throws Exception {
        UserInfo ticket1 = new UserInfo();
        ticket1.setUuid(16);
        UUID uidFlight = UUID.randomUUID();
        ticket1.setUidUser(uidFlight);
        ticket1.setIdPassenger(0);
        ticket1.setName("ECONOMIC");

        UserInfo ticket2 = new UserInfo();
        ticket2.setUuid(16);
        ticket2.setUidUser(uidFlight);
        ticket2.setIdPassenger(0);
        ticket2.setName("LUXURY");

        List<UserInfo> allTickets = Arrays.asList(ticket1, ticket2);
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

        UserInfo ticketInfo = new UserInfo();
        ticketInfo.setName("ECONOMIC");
        UUID uid = UUID.randomUUID();
        ticketInfo.setUidUser(uid);
        ticketInfo.setIdPassenger(1);

        User ticket = new User();
        ticket.setName(ticketInfo.getName());
        ticket.setUidUser(ticketInfo.getUidUser());
        ticket.setIdPassenger(ticketInfo.getIdPassenger());

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

        UserInfo ticketInfo = new UserInfo();
        ticketInfo.setUuid(16);
        ticketInfo.setName("ECONOMIC");
        UUID uidFlight = UUID.randomUUID();
        ticketInfo.setUidUser(uidFlight);
        UUID uid = UUID.randomUUID();
        ticketInfo.setUid(uid);
        ticketInfo.setIdPassenger(1);

        User ticket = new User();
        ticket.setName(ticketInfo.getName());
        ticket.setUidUser(ticketInfo.getUidUser());
        ticket.setIdPassenger(ticketInfo.getIdPassenger());
        ticket.setUid(uid);

        given(service.getUserByUid(uid)).willReturn(ticket);
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
}*/