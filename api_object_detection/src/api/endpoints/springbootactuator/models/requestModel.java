package api.endpoints.springbootactuator.models;

public class requestModel {
	private String url;
	private String archivo;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getArchivo() {
		return archivo;
	}

	@Override
	public String toString() {
		return "requestModel{" +
				"url='" + url + '\'' +
				", archivo='" + archivo + '\'' +
				'}';
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	public requestModel(String url, String archivo) {
		super();
		this.url = url;
		this.archivo = archivo;
	}
	public requestModel() {
		super();
	}
	
}
