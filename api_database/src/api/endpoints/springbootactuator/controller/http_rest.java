package api.endpoints.springbootactuator.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import api.endpoints.springbootactuator.models.*;
@RestController 
public class http_rest {
	
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @PostMapping("/data")
    @ResponseBody
    public responseModel postController(@RequestBody requestModel request_model) {
	    try {
			System.out.print(request_model.getArchivo());
			System.out.print(request_model.getObjetos());
			for (objProperties objs: request_model.getObjetos()){
				System.out.print(objs.getW());
			}
			responseModel response_obj = new responseModel("Datos Almacenados correctamente",200,"OK");
			return response_obj;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseModel response_obj = new responseModel("Error al guardar los datos",500,"Internal Fail");
			return response_obj;
		}
	}
    
}


