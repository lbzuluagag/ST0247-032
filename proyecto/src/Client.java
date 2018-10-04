import java.util.ArrayList;

public class Client extends Node {
	private boolean visited = false;
	private Vehicle assignedVehicle;
	private Station closestStation;
	private double distanceToStation;
	private double distanceToBase;
	
	public Client (int num, String name, double x, double y) {
		this.number = num;
		this.name = name;
		this.xpos = x;
		this.ypos = y;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public Vehicle getAssignedVehicle() {
		return assignedVehicle;
	}

	public void setAssignedVehicle(Vehicle assignedVehicle) {
		this.assignedVehicle = assignedVehicle;
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

	public double getDistanceToBase() {
		return distanceToBase;
	}

	public void setDistanceToBase(ArrayList<ArrayList<Double>> distances) {
		this.distanceToBase = distances.get(this.number).get(0);
	}
}
