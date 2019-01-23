package com.sychev.controllers;

import com.google.gson.Gson;
import com.sychev.flight.controllers.FlightController;
import com.sychev.flight.entity.Flight;
import com.sychev.flight.model.FlightInfo;
import com.sychev.flight.services.FlightService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(FlightController.class)
public class FlightControllerTest {

    private final Gson gson = new Gson();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FlightService service;

    @InjectMocks
    private FlightController controller = new FlightController(service);

    @Test
    public void pingTest() {
        assertEquals(controller.ping().getStatusCode(), HttpStatus.OK);
    }


    @Test
    public void listFlights() throws Exception {

        FlightInfo flightInfo = new FlightInfo();
        flightInfo.setIdFlight(10);
        flightInfo.setNnTickets(0);
        flightInfo.setMaxTickets(5);
        flightInfo.setUidRoute(UUID.randomUUID());
        flightInfo.setUid(UUID.randomUUID());
        flightInfo.setDtFlight("2018-07-12 12:10");

        List<FlightInfo> allTickets = Arrays.asList(flightInfo);

        given(service.listAll()).willReturn(allTickets);

        mvc.perform(get("/flights")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idFlight", is(flightInfo.getIdFlight())));
    }

    @Test
    public void getFlight() throws Exception {

        FlightInfo flightInfo = new FlightInfo();
        flightInfo.setNnTickets(0);
        flightInfo.setMaxTickets(5);
        flightInfo.setUidRoute(UUID.randomUUID());
        UUID uidFlight = UUID.randomUUID();
        flightInfo.setUid(uidFlight);
        flightInfo.setDtFlight("2018-07-12 12:10");

        given(service.getFlightInfoByUid(uidFlight)).willReturn(flightInfo);

        MvcResult mvcResult = mvc.perform(get("/flight")
                .contentType(MediaType.APPLICATION_JSON)
                .param("uidFlight", uidFlight.toString()))
                .andExpect(status().isOk())
                .andReturn();

        FlightInfo newTicket = gson.fromJson(mvcResult.getResponse().getContentAsString(), FlightInfo.class);
        assertEquals(flightInfo.getIdFlight(), newTicket.getIdFlight());
    }

    @Test
    public void getRouteFlights() throws Exception {

        FlightInfo flightInfo1 = new FlightInfo();
        flightInfo1.setIdFlight(10);
        flightInfo1.setNnTickets(0);
        flightInfo1.setMaxTickets(5);
        UUID uidRoute = UUID.randomUUID();
        flightInfo1.setUidRoute(uidRoute);
        flightInfo1.setUid(UUID.randomUUID());
        flightInfo1.setDtFlight("2018-07-12 12:10");

        FlightInfo flightInfo2 = new FlightInfo();
        flightInfo2.setIdFlight(11);
        flightInfo2.setNnTickets(0);
        flightInfo2.setMaxTickets(5);
        flightInfo2.setUidRoute(uidRoute);
        flightInfo2.setUid(UUID.randomUUID());
        flightInfo2.setDtFlight("2018-07-12 12:10");

        List<FlightInfo> allTickets = Arrays.asList(flightInfo1, flightInfo2);
        given(service.listRouteFlights(uidRoute)).willReturn(allTickets);

        MvcResult mvcResult = mvc.perform(get("/flights")
                .param("uidRoute", uidRoute.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JSONArray jsonFlightArray = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertTrue(jsonFlightArray.length() == 2);

    }

    @Test
    public void add() throws Exception {

        List<FlightInfo> allTickets = new ArrayList<>();
        given(service.listAll()).willReturn(allTickets);

        FlightInfo flightInfo = new FlightInfo();
        flightInfo.setIdFlight(10);
        flightInfo.setNnTickets(0);
        flightInfo.setMaxTickets(5);
        flightInfo.setUidRoute(UUID.randomUUID());
        UUID uidFlight = UUID.randomUUID();
        flightInfo.setUid(uidFlight);
        flightInfo.setDtFlight("2018-07-12 12:10");

        Flight flight = new Flight();
        flight.setIdFlight(10);
        flight.setNnTickets(0);
        flight.setMaxTickets(5);
        flight.setUidRoute(UUID.randomUUID());
        flight.setUuid(uidFlight);
        flight.setDtFlight("2018-07-12 12:10");

        given(service.saveOrUpdate(flight)).willReturn(flight);

        mvc.perform(put("/flight")
                .content(gson.toJson(flightInfo))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void edit() throws Exception {

        FlightInfo flightInfo = new FlightInfo();
        flightInfo.setIdFlight(10);
        flightInfo.setNnTickets(0);
        flightInfo.setMaxTickets(5);
        flightInfo.setUidRoute(UUID.randomUUID());
        UUID uidFlight = UUID.randomUUID();
        flightInfo.setUid(uidFlight);
        flightInfo.setDtFlight("2018-07-12 12:10");

        Flight flight = new Flight();
        flight.setIdFlight(10);
        flight.setNnTickets(0);
        flight.setMaxTickets(5);
        flight.setUidRoute(UUID.randomUUID());
        flight.setUuid(uidFlight);
        flight.setDtFlight("2018-07-12 12:10");


        given(service.getFlightByUid(uidFlight)).willReturn(flight);
        given(service.saveOrUpdate(flight)).willReturn(flight);

        mvc.perform(patch("/flight")
                .content(gson.toJson(flightInfo))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteFlight() throws Exception {

        UUID uidFlight = UUID.randomUUID();
        doNothing().when(service).delete(uidFlight);
        mvc.perform(delete("/flight")
                .content(uidFlight.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteRouteFlights() throws Exception {

        UUID uidRoute = UUID.randomUUID();
        doNothing().when(service).deleteRouteFlights(uidRoute);
        mvc.perform(delete("/flights")
                .content(uidRoute.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }
}