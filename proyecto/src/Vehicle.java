import java.util.ArrayList;
import java.util.LinkedList;

public class Vehicle {
	private int number;
	private double r;
	private double speed;
	private double energyLevel;
	private double remTime;
	private boolean outOfTime = false;
	private LinkedList<Node> route = new LinkedList<Node>();
	private Node currentNode;
	private Client closestClient;
	private double distanceToClient;
	
	public Vehicle (int number, double Q, Node d, double r, double speed, double Tmax, ArrayList<ArrayList<Double>> distances, ArrayList<Node> nodes) {
		this.number = number;
		this.energyLevel = Q;
		this.currentNode = d;
		this.r = r;
		this.speed = speed;
		this.setRemTime(Tmax);
		this.findClosestClient(distances, nodes);
		this.addNodeToRoute(nodes.get(0));
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public double getEnergyLevel() {
		return energyLevel;
	}
	public void setEnergyLevel(double energyLevel) {
		this.energyLevel = energyLevel;
	}
	public LinkedList<Node> getRoute() {
		return route;
	}
	public void addNodeToRoute(Node node) {
		this.route.add(node);
	}
	public Node getCurrentNode() {
		return currentNode;
	}
	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}

	public Client getClosestClient() {
		return closestClient;
	}

	public void findClosestClient(ArrayList<ArrayList<Double>> distances, ArrayList<Node> nodes) {
		this.closestClient = new Client(-1, "tmp", -1.0, -1.0);
		this.distanceToClient = (double) Integer.MAX_VALUE;
		ArrayList<Double> currentDistances = distances.get(this.currentNode.getNumber());
		
		for (Node node : nodes) {
			if (node instanceof Client && !((Client) node).isVisited() && currentDistances.get(node.getNumber()) < this.distanceToClient) {
				this.closestClient = (Client) node;
				this.distanceToClient = currentDistances.get(node.getNumber());
			}
		}
	}

	public double getDistanceToClient() {
		return distanceToClient;
	}

	public void setDistanceToClient(double distanceToClient) {
		this.distanceToClient = distanceToClient;
	}

	public void setRoute(LinkedList<Node> route) {
		this.route = route;
	}
	
	public double getRemTime() {
		return remTime;
	}

	public void setRemTime(double remTime) {
		this.remTime = remTime;
	}
	
	// Check if the vehicle can safely go to the proposed node without running out of time
	// i.e. after subtracting the travel time to the client and the visit time at the client from the vehicle's remaining time, it will still have enough time to go back to the base
	// CN = closest neighbour
	public boolean canGoToCNInTime(double timeAtClient) {
		// calculate travel time to client by dividing distance by speed
		double timeToClient = this.distanceToClient / this.speed;
		double timeToBase = this.closestClient.getTimeToBaseWithCharging();
		return (this.remTime - timeToClient - timeAtClient -  timeToBase > 0);
	}
	
	public void returnToBase(ArrayList<ArrayList<Double>> distances) {
		if (this.canReturnToBase()) {
			this.moveTo(this.currentNode.fastestRouteToBase.get(this.currentNode.fastestRouteToBase.size()-1), distances, 0);
		}
		else {
			for (Node n : this.currentNode.fastestRouteToBase) {
				this.moveTo(n, distances, ((Station) n).getS());
			}
		}
	}
	
	// Check if the vehicle can safely go to the proposed node without running out of fuel
	// i.e. subtract fuel consumption per kilometer multiplied by distrance from the remaining energy and check if the vehicle could still reach the next station from the target client
	public boolean canGoToCNWithFuel() {
		double fuelToClient = this.distanceToClient * this.r;
		double fuelToStation = this.closestClient.getDistanceToStation() * this.r;
		
		return (this.energyLevel - fuelToClient - fuelToStation > 0);
	}
	
	// Check if the vehicle could directly return to the base without having to stop for recharging
	public boolean canReturnToBase() {
		double distanceToBase = this.currentNode.getDistanceToBase();
		double consumptionToBase = distanceToBase * this.r;
		return this.energyLevel - consumptionToBase > 0;
	}
	
	public void moveTo(Node node, ArrayList<ArrayList<Double>> distances, double timeAtNode) {
		this.addNodeToRoute(node);																// Add node to vehicle's route
		this.setRemTime(this.remTime - distances.get(this.currentNode.getNumber()).get(node.getNumber()) / this.speed - timeAtNode);
																								// Update vehicle's remaining time
		
		if (node instanceof Client) {															// Check if target node is a client
			this.setEnergyLevel(this.energyLevel - this.distanceToClient * this.r);				// Update vehicle's remaining energy				
			((Client) node).setVisited(true);													// Mark client as visited
		}
		else if (node instanceof Station) {														// Check if target node is a station
			if (node instanceof Depot) {														// If a vehicle returns to the depot, make sure that it will not be used anymore
				this.setOutOfTime(true);														// mark vehicle as "out of time"
			}
			this.setEnergyLevel(((Station) node).getQ());										// Assume full charge (for now)
		}
		this.setCurrentNode(node);																// Update current node of vehicle
	}

	public boolean isOutOfTime() {
		return outOfTime;
	}

	public void setOutOfTime(boolean outOfTime) {
		this.outOfTime = outOfTime;
	}
}
