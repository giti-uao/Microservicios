package api.endpoints.springbootactuator.models;
import java.util.List;

public class requestModel {
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
	public requestModel() {
		super();
	}
	public requestModel(List<objProperties> objetos, String archivo) {
		this.objetos = objetos;
		this.archivo = archivo;
	}
	@Override
	public String toString() {
		return "responseModel [objetos=" + objetos + ", archivo=" + archivo + "]";
	}
	
}
