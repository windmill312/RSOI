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
import rsoi.lab2.entity.Route;
import rsoi.lab2.model.PingResponse;
import rsoi.lab2.model.RouteInfo;
import rsoi.lab2.services.RouteService;

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
@WebMvcTest(RouteController.class)
public class RouteControllerTest {

    private final Gson gson = new Gson();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RouteService service;

    @InjectMocks
    private RouteController controller = new RouteController(service);

    @Test
    public void pingTest() {
        assertEquals(controller.ping().getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getRoutesTest1() throws Exception {

        RouteInfo route = new RouteInfo();
        route.setIdRoute(1);
        route.setRouteName("Moscow/Paris");

        List<RouteInfo> allRoutes = Arrays.asList(route);

        given(service.listAll()).willReturn(allRoutes);

        mvc.perform(get("/routes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idRoute", is(route.getIdRoute())));
    }

    @Test
    public void getRoutesTest2() throws Exception {
        RouteInfo route = new RouteInfo();
        route.setIdRoute(1);
        route.setRouteName("Moscow/Paris");

        List<RouteInfo> allRoutes = Arrays.asList(route);

        given(service.listAllByNmRoute("Moscow/Paris")).willReturn(allRoutes);

        MvcResult mvcResult = mvc.perform(get("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .param("nmRoute", "Moscow/Paris"))
                .andExpect(status().isOk())
                .andReturn();

        JSONArray array = new JSONArray(mvcResult.getResponse().getContentAsString());

        assertTrue(array.length() == 1);
    }

    @Test
    public void getRouteTest() throws Exception {

        RouteInfo route = new RouteInfo();
        route.setIdRoute(1);
        route.setRouteName("Moscow/Paris");
        UUID uidRoute = UUID.randomUUID();
        route.setUid(uidRoute);

        given(service.getRouteInfoByUid(uidRoute)).willReturn(route);

        MvcResult mvcResult = mvc.perform(get("/route")
                .contentType(MediaType.APPLICATION_JSON)
                .param("uidRoute", uidRoute.toString()))
                .andExpect(status().isOk())
                .andReturn();

        RouteInfo newRoute = gson.fromJson(mvcResult.getResponse().getContentAsString(), RouteInfo.class);
        assertEquals(route.getIdRoute(), newRoute.getIdRoute());
    }

    @Test
    public void addRouteTest() throws Exception {

        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setRouteName("Moscow/Paris");
        routeInfo.setUid(UUID.randomUUID());

        Route route = new Route();
        route.setUid(routeInfo.getUid());
        route.setNmRoute(routeInfo.getRouteName());
        route.setIdRoute(routeInfo.getIdRoute());

        given(service.saveOrUpdate(route)).willReturn(route);

        mvc.perform(put("/route")
                .content(gson.toJson(routeInfo))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void editRouteTest() throws Exception {
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setIdRoute(1);
        UUID uidRoute = UUID.randomUUID();
        routeInfo.setUid(uidRoute);
        routeInfo.setRouteName("Moscow/Paris");

        Route route = new Route();
        route.setIdRoute(routeInfo.getIdRoute());
        route.setNmRoute(routeInfo.getRouteName());
        route.setUid(routeInfo.getUid());


        given(service.getRouteByUid(uidRoute)).willReturn(route);
        given(service.saveOrUpdate(route)).willReturn(route);

        mvc.perform(patch("/route")
                .content(new Gson().toJson(route))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteRouteTest() throws Exception {

        UUID uidRoute = UUID.randomUUID();
        doNothing().when(service).delete(uidRoute);
        mvc.perform(delete("/route")
                .content(uidRoute.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }
}


