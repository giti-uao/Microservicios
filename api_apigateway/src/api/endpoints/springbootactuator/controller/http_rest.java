package api.endpoints.springbootactuator.controller;

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

import api.endpoints.springbootactuator.models.requestModel;
import api.endpoints.springbootactuator.models.responseModel;


@RestController 
public class http_rest {
	
	@Autowired
    private RestTemplate restTemplate;
	
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @PostMapping("/process_img")
    @ResponseBody
    public ResponseEntity<responseModel> process_img(@RequestBody requestModel img_obj){
    	try {
			responseModel response_obj = restTemplate.postForObject("http://apiservice:9080/img",img_obj, responseModel.class);
			if(response_obj.getCode()==500){
			    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response_obj);
            }
            return ResponseEntity.status(HttpStatus.OK).body(response_obj);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseModel response_obj = new responseModel("Error al detectar objetos",500,"Internal Fail");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response_obj);
		}
    }
    
}


