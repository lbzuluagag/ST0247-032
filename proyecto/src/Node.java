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
	protected Station closestStation;
	protected double distanceToStation;
	
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
	
	// Plans the fasted return to base from this node. Iterates over the set of nodes and always adds the station closest to the base that can be reached
	// with the current energy level. Once the depot itself has been added to the shortest return route, the function finishes by updating the worst case time
	// it takes to return to the depot from this node.
	public void planFastestRouteToBase(ArrayList<ArrayList<Double>> distances, ArrayList<Station> sortedStations, double r, double speed) {
	}
	public double getTimeToBaseWithCharging() {
		return timeToBaseWithCharging;
	}
	public void setTimeToBaseWithCharging(double timeToBaseWithCharging) {
		this.timeToBaseWithCharging = timeToBaseWithCharging;
	}
	public void setClosestStation(ArrayList<ArrayList<Double>> distances, ArrayList<Node> nodes) {
		this.closestStation = new Station(-1, "tmp", -1.0, -1.0, -1);
		this.distanceToStation = (double) Integer.MAX_VALUE;
		ArrayList<Double> currentDistances = distances.get(this.getNumber());
		
		for (Node node : nodes) {
			 if (node instanceof Station && currentDistances.get(node.getNumber()) < this.distanceToStation) {
				 this.closestStation = (Station) node;
				 this.distanceToStation= currentDistances.get(node.getNumber());
			 }
		}
	}

	public Station getClosestStation() {
		return closestStation;
	}
	
	public double getDistanceToStation() {
		return distanceToStation;
	}

	
	
}
