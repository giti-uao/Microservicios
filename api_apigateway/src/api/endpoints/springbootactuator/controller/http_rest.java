package api.endpoints.springbootactuator.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import api.endpoints.springbootactuator.models.requestModel;
import api.endpoints.springbootactuator.models.responseModel;

import java.time.Duration;
import java.util.concurrent.*;


@RestController 
public class http_rest {
    private static final String API_SERVICE_CIRCUIT = "restServiceCircuit";

    TimeLimiterRegistry timeLimiterRegistry = TimeLimiterRegistry.ofDefaults();

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @PostMapping("/process")
    @CircuitBreaker(name = API_SERVICE_CIRCUIT, fallbackMethod = "microserviceFallback")
    public ResponseEntity<responseModel> test(@RequestBody requestModel obj){
        try{
            TimeLimiter timeLimiter = TimeLimiter.of(TimeLimiterConfig.custom()
                    .timeoutDuration(Duration.ofSeconds(20))
                    .build());
            Callable<responseModel> wait = TimeLimiter.decorateFutureSupplier(timeLimiter, () -> test_function(obj));

            Try<responseModel> response = Try.ofCallable(wait);

            if (!response.isSuccess()) {
                Throwable cause = response.getCause();
                if (cause instanceof TimeoutException) {
                    throw (TimeoutException) cause;
                }
                if (cause instanceof NullPointerException) {
                    throw (NullPointerException) cause;
                }
            }

            responseModel response_obj = response.getOrNull();
            return new ResponseEntity<responseModel>(response_obj, HttpStatus.resolve(response_obj.getCode()));
        }
        catch(TimeoutException e){
            System.out.println(e);
            responseModel response_obj = new responseModel("Tiempo Limite superado", 408, "Request Timeout");
            return new ResponseEntity<responseModel>(response_obj, HttpStatus.REQUEST_TIMEOUT);
        }
    }


    private Future<responseModel> test_function(requestModel img_obj) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                responseModel response_obj = restTemplate.postForObject("http://localhost:9080/img", img_obj, responseModel.class);
                Thread.sleep(1);
                return response_obj;
            } catch (InterruptedException e) {
                responseModel response_obj = new responseModel("Tiempo Limite superado", 408, "Request Timeout");
                return response_obj;
            }
        });
    }

    public ResponseEntity<responseModel> microserviceFallback(NullPointerException e) {
        System.out.println(e);
        responseModel response_obj = new responseModel("Servicio Object Detection No disponible", 500, "Internal Fail");
        return new ResponseEntity<responseModel>(response_obj, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}




