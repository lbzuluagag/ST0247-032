import java.util.ArrayList;

public class VND {
	ArrayList<Node> solution = new ArrayList<Node>();
	ArrayList<Node> nodes;											// all nodes
	ArrayList<ArrayList<Double>> distances;							// distance matrix
	ArrayList<Vehicle> vehicles;									// all vehicles
	double r;														// energy consumption per km
	double speed;													// speed
	double Tmax;													// maximum route duration
	double St_customer;												// customer visit time
	double Q;

	public VND(ArrayList<Node> nodes, ArrayList<ArrayList<Double>> distances, ArrayList<Vehicle> vehicles, double r, double speed, double Tmax, double St_customer, double Q) {
		this.nodes = nodes;
		this.distances = distances;
		this.vehicles = vehicles;
		this.r = r;
		this.speed = speed;
		this.Tmax = Tmax;
		this.St_customer = St_customer;
		this.Q = Q;
	}
	
	public ArrayList<Node> getSolution() {
		return this.solution;
	}
	
	// In order to do the variable neighbourhood descent more efficiently, this method changes
	// the representation of the routes. Instead of having a route for each vehicle, the information
	// will be stored in one array for all vehicles of the type [0, 4, 2, 0, 1, 3, 5, 0] where the 0s
	// represent the ends and beginnings of a new route. This makes it easier to move nodes around
	// between different routes etc.
	public void changeRouteRepresentation() {		
		for (Vehicle v: this.vehicles) {												// iterate over all vehicles
			
			for (Node r: v.getRoute()) {												// sequentially add all nodes in the route to the solution array
				this.solution.add(r);
			}
			
			this.solution.remove(this.solution.size()-1);								// delete the last added node (the base) to avoid double 0s in the solution (unnecessary)
		}
		
		this.solution.add(this.nodes.get(0));											// add the depot to the end of the solution
	}
	
	// Calculate time between two nodes (including time of visit at the target node)
	public double calculateTimeBetweenTwoNodes(double visitTime, Node fromNode, Node toNode) {
		double travelTime = this.distances.get(fromNode.getNumber()).get(toNode.getNumber()) / this.speed;		// calculate the time it takes to get from node i - 1 to node i
		double timeAtNode;																						// initialise a variable which states for how long the vehicle has to wait at the node
		
		if (toNode instanceof Client) {																	
			timeAtNode = visitTime;																				// the time for customer visits is fixed and passed as an argument to this function
		}
		else if (toNode instanceof Depot) {
			timeAtNode = 0.0;																					// when returning to the depot, the route is over and no time is added
		}
		else {
			timeAtNode = ((Station) toNode).getS();																// when going to a station, assume full charge for now
		}
		
		return travelTime + timeAtNode;
	}
	
	// This method calculates the value of the objective function for the current solution.
	// It adds the travel times and times at clients and stations of all vehicles.
	public double calculateObjectiveFunction() {
		double cumulativeTime = 0.0;																		// initialise the return value
		
		for (int i = 1; i < this.solution.size(); i++) {													// iterate over all nodes in the solution
			Node fromNode = this.solution.get(i - 1);
			Node toNode = this.solution.get(i);
			
			cumulativeTime += this.calculateTimeBetweenTwoNodes(this.St_customer, fromNode, toNode);		// add up times
		}
		
		return cumulativeTime;
	}
	
	// Checks if a given solution is feasible. This means that at no time the energy of a vehicle drops below 0 or
	// the duration of any route exceeds the limit
	public boolean isFeasibleSolution(ArrayList<Node> solution) {
		double routeTime = 0.0;
		double energy = this.Q;
		
		for (int i = 1; i < solution.size(); i++) {
			Node fromNode = this.solution.get(i - 1);
			Node toNode = this.solution.get(i);
			
			if (fromNode instanceof Depot) {
				energy = this.Q;
				routeTime = 0.0;
			}
			else if (fromNode instanceof Station) {
				energy = ((Station) fromNode).getQ();
			}
			
			if (toNode instanceof Client) {
				routeTime += calculateTimeBetweenTwoNodes(this.St_customer, fromNode, toNode);
			}
			else if (toNode instanceof Depot) {
				routeTime += calculateTimeBetweenTwoNodes(0.0, fromNode, toNode);
			}
			else {
				routeTime += calculateTimeBetweenTwoNodes(((Station) toNode).getS(), fromNode, toNode);
			}
			
			energy -= this.distances.get(fromNode.getNumber()).get(toNode.getNumber()) * this.r;
			
			if (energy < 0.0 || routeTime > this.Tmax) {
				System.out.println("Solution infeasible.");
				System.out.println("From " + String.valueOf(fromNode.getNumber()) + " to " + String.valueOf(toNode.getNumber()) + ": ");
				System.out.println("	Time: " + String.valueOf(routeTime));
				System.out.println("	Energy: " + String.valueOf(energy));
				return false;
			}
		}
		
		System.out.println("Solution feasible");
		return true;
	}
	
	
	// simply outputs the solution (just node ordering, no times or energy)
	public void printSolution() {
		for (Node n: this.solution) {
			System.out.print(String.valueOf(n.getNumber()) + " ");
		}
		
		System.out.println("");
	}
	
	
}
