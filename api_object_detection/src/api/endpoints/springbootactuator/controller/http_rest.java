package api.endpoints.springbootactuator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

import java.util.List;



@RestController 
public class http_rest {
	
	@Autowired
    private RestTemplate restTemplate;
	
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @PostMapping("/img")
    @ResponseBody
    public responseModel process_img(@RequestBody requestModel img_obj){
    	try {
			yolo yolo_obj = new yolo();
			List<objProperties> resp = yolo_obj.detect_objs(0.4,img_obj.getUrl());
			requestDataModel request_data = new requestDataModel(resp,img_obj.getArchivo());
			responseModel response_obj = restTemplate.postForObject("http://localhost:7000/data",request_data, responseModel.class);
			return response_obj;
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseModel response_obj = new responseModel("Error al detectar objetos",500,"Internal Fail");
			return response_obj;
		}
    }
    
}


