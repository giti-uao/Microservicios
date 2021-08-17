package api.endpoints.springbootactuator.models;
import java.util.List;

public class requestDataModel {
	private List<objProperties> objetos;
	private String archivo;
	public List<objProperties> getObjetos() {
		return objetos;
	}
	public void setObjetos(List<objProperties> objetos) {
		this.objetos = objetos;
	}
	public String getArchivo() {
		return archivo;
	}
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	public requestDataModel(List<objProperties> objetos, String archivo) {
		super();
		this.objetos = objetos;
		this.archivo = archivo;
	}
	
}
