import java.util.ArrayList;
import java.util.LinkedList;

abstract class Node {
	protected int number;
	protected String name;
	protected double xpos;
	protected double ypos;
	private double distanceToBase;
	LinkedList<Node> fastestRouteToBase = new LinkedList<Node>();
	private double timeToBaseWithCharging;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getXpos() {
		return xpos;
	}
	public void setXpos(double xpos) {
		this.xpos = xpos;
	}
	public double getYpos() {
		return ypos;
	}
	public void setYpos(double ypos) {
		this.ypos = ypos;
	}
	public boolean equals(Object comp) {
		if (comp == null) {return false;}
		if (!(comp instanceof Node)) {return false;}
		return (this.number == ((Node) comp).getNumber());
	}
	public int hashCode() {
		int out = 17;
		out = 31 * out + number;
		return out;
	}
	public double getDistanceToBase() {
		return distanceToBase;
	}

	public void setDistanceToBase(ArrayList<ArrayList<Double>> distances) {
		this.distanceToBase = distances.get(this.number).get(0);
	}
	
	public void planFastestRouteToBase(ArrayList<ArrayList<Double>> distances, ArrayList<Station> sortedStations, double r, double speed) {
	}
	public double getTimeToBaseWithCharging() {
		return timeToBaseWithCharging;
	}
	public void setTimeToBaseWithCharging(double timeToBaseWithCharging) {
		this.timeToBaseWithCharging = timeToBaseWithCharging;
	}
	
}
