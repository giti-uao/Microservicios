package api.endpoints.springbootactuator.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import api.endpoints.springbootactuator.models.*;

import java.sql.*;

@RestController
public class http_rest {
	
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @PostMapping("/data")
    @ResponseBody
	@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<responseModel> postController(@RequestBody requestModel request_model) throws SQLException {
	    try {
			Connection connection = null;
			// Database connect
			// Conectamos con la base de datos
			connection = DriverManager.getConnection(
					"jdbc:mysql://msmicroservice:3306/rescity?autoReconnect=true&useSSL=false",
					"root", "rescity");
			boolean valid = connection.isValid(50000);

			if(valid){
				String query = "INSERT INTO rescity.imagenes (archivo) VALUES(?);";
				PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, request_model.getArchivo());

				int affectedRows = statement.executeUpdate();

				if (affectedRows == 0) {
					throw new SQLException("Creating image failed, no rows affected.");
				}

				try{
					ResultSet generatedKeys = statement.getGeneratedKeys();
					if (generatedKeys.next()) {
						long id = generatedKeys.getLong(1);
						for (objProperties objs: request_model.getObjetos()){
							String query2 = "INSERT INTO rescity.objetos (id_img, confidence, label, x, y, w, h) VALUES(?,?,?,?,?,?,?);";
							PreparedStatement statement2 = connection.prepareStatement(query2);
							statement2.setString(1, String.valueOf(id));
							statement2.setString(2, String.valueOf(objs.getConfidence()));
							statement2.setString(3, objs.getLabel());
							statement2.setString(4, String.valueOf(objs.getX()));
							statement2.setString(5, String.valueOf(objs.getY()));
							statement2.setString(6, String.valueOf(objs.getW()));
							statement2.setString(7, String.valueOf(objs.getH()));
							statement2.executeUpdate();
						}
					}
					else {
						throw new SQLException("Creating user failed, no ID obtained.");
					}
				}catch (Exception e) {
					System.out.print(e);
					responseModel response_obj = new responseModel("Error al guardar los datos",500,"FAIL");

					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response_obj);
				}
			}
			responseModel response_obj = new responseModel("Datos Almacenados correctamente",200,"OK");
			return ResponseEntity.status(HttpStatus.OK).body(response_obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseModel response_obj = new responseModel("Error al guardar los datos",500,"Internal Fail");
			return ResponseEntity.status(HttpStatus.OK).body(response_obj);
		}
	}
    
}


