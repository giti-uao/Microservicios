package api.endpoints.springbootactuator.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import api.endpoints.springbootactuator.detection.yolo;
import api.endpoints.springbootactuator.models.objProperties;
import api.endpoints.springbootactuator.models.requestModel;
import api.endpoints.springbootactuator.models.responseModel;
import api.endpoints.springbootactuator.models.requestDataModel;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;


@RestController
public class http_rest {
	private static final String CIRCUIT_SERVICE = "CircuitService";
	private static final String LIMITER_SERVICE = "RateLimiterService";
	@Autowired
    private RestTemplate restTemplate;
	
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

	@PostMapping("/img")
	@CircuitBreaker(name = CIRCUIT_SERVICE, fallbackMethod = "microserviceFallback")
	public ResponseEntity<responseModel> test(@RequestBody requestModel obj){
		try{
			RateLimiterConfig config = RateLimiterConfig.custom()
					.timeoutDuration(Duration.ofMillis(0))
					.limitRefreshPeriod(Duration.ofSeconds(5))
					.limitForPeriod(10)
					.build();

			RateLimiter rateLimiter = RateLimiter.of("yolo", config);

			Supplier<responseModel> wait = RateLimiter.decorateSupplier(rateLimiter, test_function(obj));

			Try<responseModel> response = Try.ofSupplier(wait);
			System.out.println("salida" + response.getOrNull());
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

	private Supplier<responseModel> test_function(requestModel img_obj) {
		yolo yolo_obj = new yolo();
		List<objProperties> resp = yolo_obj.detect_objs(0.4,img_obj.getUrl());
		if(resp.size() == 0){
			responseModel response_obj2 = new responseModel("Error al detectar objetos",500,"Internal Fail");
			System.out.println(response_obj2);
			return (Supplier<responseModel>) response_obj2;
		}
		requestDataModel request_data = new requestDataModel(resp,img_obj.getArchivo());
		responseModel response_obj = restTemplate.postForObject("http://localhost:7000/data",request_data, responseModel.class);
		System.out.println(response_obj);

		return (Supplier<responseModel>) response_obj;
	}



    @PostMapping("/process")
    @ResponseBody
	@CircuitBreaker(name=CIRCUIT_SERVICE, fallbackMethod = "microserviceFallback")
    public ResponseEntity<responseModel> process_img(@RequestBody requestModel img_obj) throws InterruptedException {
    	System.out.println(img_obj.toString());
		yolo yolo_obj = new yolo();
		List<objProperties> resp = yolo_obj.detect_objs(0.4,img_obj.getUrl());
		if(resp.size() == 0){
			responseModel response_obj2 = new responseModel("Error al detectar objetos",500,"Internal Fail");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response_obj2);
		}
		requestDataModel request_data = new requestDataModel(resp,img_obj.getArchivo());
		responseModel response_obj = restTemplate.postForObject("http://localhost:7000/data",request_data, responseModel.class);
		if(response_obj.getCode()==500){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response_obj);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response_obj);

    }
	public ResponseEntity<responseModel> microserviceFallback(Exception e){
		responseModel response_obj = new responseModel("Servicio DB no disponible",500,"Internal Fail");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response_obj);
	}
	public ResponseEntity<responseModel> rateLimiterFallback(Exception e){
		System.out.println(e);
		responseModel response_obj = new responseModel("El servicio OD no admite tantas llamadas",429,"Too Many Request");
		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response_obj);
	}
}


