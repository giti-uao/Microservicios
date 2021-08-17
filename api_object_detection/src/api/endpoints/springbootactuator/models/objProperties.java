package api.endpoints.springbootactuator.models;

public class objProperties {
	private String label;
	private int x;
	private int y;
	private int w;
	private int h;
	private double confidence;
	
	public objProperties(String label, int x, int y, int w, int h, double confidence) {
		super();
		this.label = label;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.confidence = confidence;
	}
	
	public String getLabel() {
		return label;
	}
	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	
	
}
