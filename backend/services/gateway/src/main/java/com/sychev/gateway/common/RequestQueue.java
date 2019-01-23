/*package rsoi.lab2.gateway.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import FlightInfo;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;


public class RequestQueue extends Thread {

    private Logger logger = Logger.getLogger(RequestQueue.class.getName());

    @Value("app.queueCapacity")
    int capacity;

    @Value("${app.gatewayUuid}")
    private String gatewayUuid;

    private static volatile RequestQueue instance;

    @Resource(
            authenticationType = Resource.AuthenticationType.CONTAINER,
            shareable = true
        )
    private BlockingQueue<FlightInfo> requests;

    private static FlightInfo SHUTDOWN_REQ;
    private volatile boolean shuttingDown, executionTerminated;

    public RequestQueue() {
        //requests = new ArrayBlockingQueue<>(capacity);
        //instance = new RequestQueue();
        //SHUTDOWN_REQ = new FlightInfo();
        //SHUTDOWN_REQ.setMaxTickets(-1);
        start();
    }

    public void add(FlightInfo request) {
        if (shuttingDown || executionTerminated) return;
        try {
            requests.put(request);
        } catch (InterruptedException iex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Unexpected interruption");
        }
    }

    public void shutDown() throws InterruptedException {
        shuttingDown = true;
        requests.put(SHUTDOWN_REQ);
    }

    @Override
    public void run() {
        try {
            FlightInfo item;


            while (true) {
                item = requests.take();
                try {
                    RestTemplate restTemplateFlight = new RestTemplate();

                    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                    requestFactory.setConnectTimeout(500);
                    requestFactory.setReadTimeout(500);
                    restTemplateFlight.setRequestFactory(requestFactory);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

                    HttpEntity<FlightInfo> requestFlight = new HttpEntity<>(item, headers);
                    restTemplateFlight.postForObject("http://localhost:8083/flight?_method=patch" + "&gatewayUuid=" + gatewayUuid, requestFlight, ResponseEntity.class);
                }
                catch (ResourceAccessException ex) {
                    logger.info("Service is not available! Waiting 1 sec.");
                    requests.add(item);
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException iex) {
            logger.info(iex.getLocalizedMessage());
            Thread.currentThread().interrupt();
        } finally {
            executionTerminated = true;
        }
    }
}*/
