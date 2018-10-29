/*package rsoi.lab2.gateway;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GatewayApplicationTests {

    private final Gson gson = new Gson();

    @Autowired
    private GatewayApplication application;

    private MockMvc mvc;


    private void checkAvailability(String url) throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        int httpStatusCode = mvcResult.getResponse().getStatus();
        assertEquals(httpStatusCode,200);
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.length()>0);
    }

    //Flight Service
    @Test
    public void myRoutesTest1() throws Exception {
        checkAvailability("/pingFlight");
    }

    @Test
    public void myRoutesTest2() throws Exception {
        String url = "http://localhost:8083/flights/add?idRoute=1&dtFlight=01.01.2018 15:00:00&&maxTickets=50";
        checkAvailability(url);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString() == "Done");
    }

    @Test
    public void myRoutesTest3() throws Exception {
        String url = "http://localhost:8083/flights/edit?idFlight=1&nnTickets=2";
        checkAvailability(url);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString() == "true");
    }

    @Test
    public void myRoutesTest4() throws Exception {
        String url = "http://localhost:8083/flights/show?idFlight=1";
        checkAvailability(url);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FlightInfo flightJson = gson.fromJson(mvcResult
                .getResponse().getContentAsString(), FlightInfo.class);
        assertTrue(flightJson.getNnTickets() == 2);
    }

    @Test
    public void myRoutesTest5() throws Exception {
        String url = "http://localhost:8083/flights";
        checkAvailability(url);
    }

    //Routes Service
    @Test
    public void myRoutesTest6() throws Exception {
        checkAvailability("http://localhost:8082/routes/ping");
    }

    @Test
    public void myRoutesTest7() throws Exception {
        String url = "http://localhost:8082/routes/add?routeName=Berlin/Pariss";
        checkAvailability(url);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString() == "Done");
    }

    @Test
    public void myRoutesTest8() throws Exception {
        String url = "http://localhost:8082/routes/edit?idRoute=Berlin/Paris";
        checkAvailability(url);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString() == "Done");
    }

    @Test
    public void myRoutesTest9() throws Exception {
        String url = "http://localhost:8082/routes/show?idRoute=1";
        checkAvailability(url);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        RouteInfo routeJson = gson.fromJson(mvcResult
                .getResponse().getContentAsString(), RouteInfo.class);
        assertTrue(routeJson.getRouteName() == "Berlin/Paris");
    }

    @Test
    public void myRoutesTest10() throws Exception {
        String url = "http://localhost:8082/routes";
        checkAvailability(url);
    }

    //Tickets Service
    @Test
    public void myRoutesTest11() throws Exception {
        checkAvailability("http://localhost:8081/tickets/ping");
    }

    @Test
    public void myRoutesTest12() throws Exception {
        String url = "http://localhost:8081/tickets/edit?idTicket=1&classType=ECONOMIC";
        checkAvailability(url);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString() == "true");
    }

    @Test
    public void myRoutesTest14() throws Exception {
        String url = "http://localhost:8081/tickets/show?idTicket=1";
        checkAvailability(url);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        TicketInfo ticketJson = gson.fromJson(mvcResult
                .getResponse().getContentAsString(), TicketInfo.class);
        assertTrue(ticketJson.getClassType() == "ECONOMIC");
    }

    @Test
    public void myRoutesTest15() throws Exception {
        String url = "http://localhost:8081/tickets";
        checkAvailability(url);
    }

    @Test
    public void myRoutesTest16() throws Exception {
        String url = "http://localhost:8081/tickets/deleteFlightTickets?idFlight=1";
        checkAvailability(url);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString() == "true");
    }

    //Complex requests
    @Test
    public void findFlightsAndTicketsTest() throws Exception {
        String url ="http://localhost:8083/flights/routeFlights?idRoute=1";
        checkAvailability(url);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        JSONArray jsonFlightArray = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertTrue(jsonFlightArray.length() == 1);
        assertTrue(jsonFlightArray.getJSONObject(0).get("nnTickets") == "1");

        url = "http://localhost:8081/tickets/flightTickets?idFlight="
                + jsonFlightArray.getJSONObject(0).get("idFlight");
        checkAvailability(url);
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        JSONArray jsonTicketArray = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertTrue(jsonTicketArray.length() == 1);
        assertTrue(jsonTicketArray.getJSONObject(0).get("classType") == "ECONOMIC");
    }

    @Test
    public void addTicketTest() throws Exception {
        String url = "http://localhost:8081/tickets/add?classType=ECONOMIC&idFlight=1&idPassenger=5&nnMaxTickets=2";
        checkAvailability(url);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        TicketInfo ticketJson = gson.fromJson(mvcResult
                .getResponse().getContentAsString(), TicketInfo.class);
        assertTrue(ticketJson.getClassType() == "ECONOMIC");

        url ="http://localhost:8083/flights/show?idFlight=1";
        checkAvailability(url);
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        JSONArray jsonFlightArray = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertTrue(jsonFlightArray.length() == 1);
        assertTrue(jsonFlightArray.getJSONObject(0).get("nnTickets") == "2");
    }

    @Test
    public void deleteTicketTest() throws Exception {
        String url = "http://localhost:8081/tickets/add?classType=ECONOMIC&idFlight=1&idPassenger=5&nnMaxTickets=2";
        checkAvailability(url);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        String ticketJson = mvcResult
                .getResponse().getContentAsString();
        assertTrue(ticketJson == "true");
    }

}*/
