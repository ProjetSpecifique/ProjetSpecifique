package DataBase;

public class DistanceMatrixDataBaseElement {

	private String id1;
	private String id2;
	private double distance;
		
	public DistanceMatrixDataBaseElement(String id1, String id2, double distance) {
		super();
		this.id1 = id1;
		this.id2 = id2;
		this.distance = distance;
	}
	
	public String getId1() {
		return id1;
	}
	public void setId1(String id1) {
		this.id1 = id1;
	}
	public String getId2() {
		return id2;
	}
	public void setId2(String id2) {
		this.id2 = id2;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
		
}
