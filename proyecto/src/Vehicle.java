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
		// fetch distance from client to base and calculate travel time
		double distanceClientToBase = this.getClosestClient().getDistanceToBase();
		double timeClientToBase = distanceClientToBase / this.speed;
		
		return (this.remTime - timeToClient - timeAtClient - timeClientToBase > 0);
	}
	
	// Check if the vehicle can safely go to the proposed node without running out of fuel
	// i.e. subtract fuel consumption per kilometer multiplied by distrance from the remaining energy and check if the vehicle could still reach the next station from the target client
	public boolean canGoToCNWithFuel() {
		double fuelToClient = this.distanceToClient * this.r;
		double fuelToStation = this.closestClient.getDistanceToStation() * this.r;
		
		return (this.energyLevel - fuelToClient - fuelToStation > 0);
	}
	
	// The last way in which a vehicle could get stuck if it does not have enough fuel to get back to base and it runs out of time while recharging its battery.
	// Therefore, we have to check if the vehicle could reach the base with its energy level at the target client.
	// If not, we have to check if it has enough time to recharge and still return to the base without violating the time constraints.
	public boolean canReturnToBase() {
		// This is yet to be implemented!
		return true;
	}
	
	public void moveTo(Node node, double timeAtClient) {
		this.addNodeToRoute(node);																// Add node to vehicle's route
		
		
		if (node instanceof Client) {															// Check if target node is a client
			this.setEnergyLevel(this.energyLevel - this.distanceToClient * this.r);				// Update vehicle's remaining energy
			this.setRemTime(this.remTime - this.distanceToClient / this.speed - timeAtClient);	// Update vehicle's remaining time			
			((Client) node).setVisited(true);													// If yes, mark client as visited
		}
		else if (node instanceof Station) {														// Check if target node is a station
			if (node instanceof Depot) {														// If a vehicle returns to the depot, make sure that it will not be used anymore
				this.setOutOfTime(true);														// mark vehicle as "out of time"
				this.setRemTime(this.remTime - 0.0 / this.speed);
			}
			else {
				this.setRemTime(this.remTime - 0.0 / this.speed - ((Station) node).S);			// Assume full charging time
			}
			this.setEnergyLevel(((Station) node).Q);											// Assume full charge (for now)
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
