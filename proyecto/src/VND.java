import java.util.ArrayList;
import java.util.List;

public class VND {
	List<Node> initSolution = new ArrayList<Node>();
	List<Node> optimizedSolution = new ArrayList<Node>();
	double optimizedCost;
	List<Node> tempSolution = new ArrayList<Node>();
	double tempCost;
	List<Node> currentSolution = new ArrayList<Node>();
	double currentCost;
	ArrayList<Node> nodes;											// all nodes
	ArrayList<ArrayList<Double>> distances;							// distance matrix
	ArrayList<Vehicle> vehicles;									// all vehicles
	double r;														// energy consumption per km
	double speed;													// speed
	double Tmax;													// maximum route duration
	double St_customer;												// customer visit time
	double Q;
	double startTime;

	public VND(ArrayList<Node> nodes, ArrayList<ArrayList<Double>> distances, ArrayList<Vehicle> vehicles, double r, double speed, double Tmax, double St_customer, double Q, double startTime) {
		this.nodes = nodes;
		this.distances = distances;
		this.vehicles = vehicles;
		this.r = r;
		this.speed = speed;
		this.Tmax = Tmax;
		this.St_customer = St_customer;
		this.Q = Q;
		this.startTime = startTime;
	}
	
	public List<Node> getSolution() {
		return this.initSolution;
	}
	
	// In order to do the variable neighbourhood descent more efficiently, this method changes
	// the representation of the routes. Instead of having a route for each vehicle, the information
	// will be stored in one array of nodes for all vehicles of the type [0, 4, 2, 0, 1, 3, 5, 0] where the 0s
	// represent the ends and beginnings of a new route. This makes it easier to move nodes around
	// between different routes etc.
	public void changeRouteRepresentation() {		
		for (Vehicle v: this.vehicles) {												// iterate over all vehicles
			
			for (Node r: v.getRoute()) {												// sequentially add all nodes in the route to the solution array
				this.initSolution.add(r);
			}
			
			this.initSolution.remove(this.initSolution.size()-1);						// delete the last added node (the base) to avoid double 0s in the solution (unnecessary)
		}
		
		this.initSolution.add(this.nodes.get(0));										// add the depot to the end of the solution
	}
	
	// Calculate time between two nodes (including time of visit at the target node)
	public double calculateTimeBetweenTwoNodes(double visitTime, Node fromNode, Node toNode) {
		double travelTime = this.distances.get(fromNode.getNumber()).get(toNode.getNumber()) / this.speed;		// calculate the time it takes to get from node i - 1 to node i
		double timeAtNode;																						// initialise a variable which states for how long the vehicle has to wait at the node
		
		if (fromNode.equals(toNode)) {
			return 0;
		}
		else if (toNode instanceof Client) {																	
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
	public double calculateObjectiveFunction(List<Node> solution) {
		double cumulativeTime = 0.0;																		// initialise the return value
		
		for (int i = 1; i < solution.size(); i++) {															// iterate over all nodes in the solution
			Node fromNode = solution.get(i - 1);
			Node toNode = solution.get(i);
			
			cumulativeTime += this.calculateTimeBetweenTwoNodes(this.St_customer, fromNode, toNode);		// add up times
		}
		
		return cumulativeTime;
	}
	
	// Checks if a given solution is feasible. This means that at no time the energy of a vehicle drops below 0 or
	// the duration of any route exceeds the limit
	public boolean isFeasibleSolution(List<Node> solution) {
		double routeTime = 0.0;
		double energy = this.Q;
		
		for (int i = 1; i < solution.size(); i++) {																								// iterate over all nodes
			Node fromNode = solution.get(i - 1);
			Node toNode = solution.get(i);
			
			if (fromNode instanceof Depot) {																									// if the vehicle is leaving the depot, set the time to 0 and the energy to full capacity
				energy = this.Q;
				routeTime = 0.0;
			}
			else if (fromNode instanceof Station) {																								// if the vehicle is leaving a station, set the energy to the station's maximum charge
				energy = ((Station) fromNode).getQ();
			}
			
			if (toNode instanceof Client) {																										// if the vehicle goes to a client, add travel time plus time at client to time
				routeTime += calculateTimeBetweenTwoNodes(this.St_customer, fromNode, toNode);					
			}
			else if (toNode instanceof Depot) {																									// if the vehicle goes to the depot, simply add the travel time to time
				routeTime += calculateTimeBetweenTwoNodes(0.0, fromNode, toNode);
			}
			else {																																// if the vehicle goes to a station, add travel time plus charg time to time
				routeTime += calculateTimeBetweenTwoNodes(((Station) toNode).getS(), fromNode, toNode);
			}
			
			if ((!toNode.equals(fromNode))) {energy -= this.distances.get(fromNode.getNumber()).get(toNode.getNumber()) * this.r;}				// if the vehicle moves to a new node, subtract the required energy
			
			if (energy < 0.0 || routeTime > this.Tmax) {																						// if the energy drops below 0 at any point or the time exceeds the maximum route time, return false
				return false;
			}
		}
		
		return true;																															// return true if no error is encountered
	}
	
	// moves a node from its current position in the solution to the specified index
	public void move (List<Node> solution, int x, int pos) {
		Node tmp = solution.get(x);
		solution.remove(x);
		solution.add(pos, tmp);
	}
	
	// swap the positions of two nodes in the solution
	public void swap(List<Node> solution, int x, int y) {
		Node xn = solution.get(x);
		Node yn = solution.get(y);
		
		solution.set(x, yn);
		solution.set(y, xn);
	}
	
	// reconnects the tails of two different solutions
	public void reconnect(List<Node> solution, int x, int y) {
		// Find the 0s in the current solution and store their positions. They are indicating ends and beginnings of routes
		ArrayList<Integer> zeros = new ArrayList<Integer>();
		int xEnd = -1;
		int yEnd = -1;
		
		for (int i = 0; i < solution.size(); i++) {
			if (solution.get(i).getNumber() == 0) {
				zeros.add(i);
				
				if (i > x && xEnd == -1) {
					xEnd = i;
				}
				
				if (i > y && yEnd == -1) {
					yEnd = i;
				}
			}
		}
		
		if (!(xEnd == yEnd)) {												// do nothing if the two specified nodes belong to the same route
			List<Node> out = new ArrayList<Node>();
			
			if (x < y) {													// how the heads and tails are reconnected depends on wether the index of x is smaller or bigger than the index of y
				for (int i = 0; i < x + 1; i++) {
					out.add(solution.get(i));
				}
				for (int i = y + 1; i < solution.size() - 1; i++) {
					out.add(solution.get(i));
				}
				for (int i = xEnd + 1; i < y + 1; i++) {
					out.add(solution.get(i));
				}
				for (int i = x + 1; i < yEnd + 1; i++) {
					out.add(solution.get(i));
				}
			}
			else {
				for (int i = 0; i < y + 1; i++) {
					out.add(solution.get(i));
				}
				for (int i = x + 1; i < solution.size() - 1; i++) {
					out.add(solution.get(i));
				}
				for (int i = yEnd + 1; i < x + 1; i++) {
					out.add(solution.get(i));
				}
				for (int i = y + 1; i < xEnd + 1; i++) {
					out.add(solution.get(i));
				}
			}
			
			solution = new ArrayList<Node>();
			
			solution.addAll(out);
		}		
	}
	
	// this method actually runs the VND. A prerequisite for running this algorithm is that an initial solution has been supplied (by the constructive method.
	// It will run as long as the total runtime does not exceed 29 seconds.
	// The three neighbourhoods that are being evaluated are defined by the three operators move, swap, and reconnect. A switch in neighbourhood is realised if
	// the improvement to the current solution obtained by applying the best operation of the neighbourhood is lower than 0.1%
	// The best operation of one neighbourhood is found by looking at all possible operations within the neighbourhood before actually performing a move. That requires
	// iterating over the entire solution for both x and y.
	public void optimize () {
		for (Node n : this.initSolution) {														// Initialize a temporary solution that will be modified
			this.currentSolution.add(n);
		}
		
		int[] bestMove = {0, 0};
		
		this.currentCost = this.calculateObjectiveFunction(this.currentSolution);
		this.tempCost = this.calculateObjectiveFunction(this.tempSolution);
		
		int n = this.initSolution.size();
		
		int neighbourhood = 0;
		
		while (System.currentTimeMillis() - this.startTime < 29000) {
			double initCost = this.getCurrentCost();
			
			for (int x = 1; x < n - 1; x++) {
				for (int y = 1; y < n - 1; y++) {
					this.tempSolution = new ArrayList<Node>();									// initialise a temporary solution based on the current solution
					this.tempSolution.addAll(this.currentSolution);
					
					switch (neighbourhood) {													// act according to which neighbourhood is under investigation. Only changes the temporary solution and NOT the current solution
						case 0:		this.move(this.tempSolution, x, y);
									break;
						case 1:		this.swap(this.tempSolution, x, y);
									break;
						case 2:		this.reconnect(this.tempSolution, x, y);
									break;
					}
					
					if (this.isFeasibleSolution(this.tempSolution)) {							// Check if the change to the temporary solution is feasible
						this.tempCost = this.calculateObjectiveFunction(this.tempSolution);		// Calculate the cost of the new temporary solution

						if (this.tempCost < this.currentCost) {									// If the temp cost would improve the solution, update the best move of the current neighbourhood
							bestMove[0] = x;
							bestMove[1] = y;
							this.currentCost = this.getTempCost();
						}
					}
				}
			}
			
			switch (neighbourhood) {															// after evaluating the entire neighbourhood, actually realize an operation on the current solution
			case 0:		this.move(this.currentSolution, bestMove[0], bestMove[1]);
						break;
			case 1:		this.swap(this.currentSolution, bestMove[0], bestMove[1]);
						break;
			case 2:		this.reconnect(this.currentSolution, bestMove[0], bestMove[1]);
						break;
			}
			
			
			if ((initCost - this.currentCost) / initCost < 0.001) {								// switch criteria for neighbourhood: if the solution is improved by less than 0.1%, switch to the next neighbourhood
				neighbourhood = (neighbourhood + 1) % 3;
			}
		}	
	}
	
	public double getCurrentCost() {
		return currentCost;
	}

	public void setCurrentCost(double currentCost) {
		this.currentCost = currentCost;
	}

	public List<Node> getCurrentSolution() {
		return currentSolution;
	}

	public void setCurrentSolution(List<Node> currentSolution) {
		this.currentSolution = currentSolution;
	}

	public double getTempCost() {
		return tempCost;
	}

	// prints the argument solution plus total route duration and number of vehicles.
	public void printSolution(List<Node> solution) {
		StringBuilder out = new StringBuilder();
		int numVehicles = 0;
		out.append("[[{0, 0.0}, ");
		for (int i = 1; i< solution.size(); i++) {
			Node n = solution.get(i);
			if (n instanceof Depot) {
				out.append("{0, 0.0}], [{0, 0.0}, ");
				numVehicles++;
			} else if (n instanceof Client) {
				out.append("{" + String.valueOf(n.getNumber()) + ", " + String.valueOf(this.St_customer) + "}, ");
			} else if (n instanceof Station) {
				out.append("{" + String.valueOf(n.getNumber()) + ", " + String.valueOf(((Station) n).getS()) + "}, ");
			}
		}
		
		out.setLength(out.length() - 13);
		out.append("]");
		System.out.println(out);
		System.out.println("----------------------------------------------------");
		System.out.println("Final value of objective function [h]: " + String.valueOf(this.calculateObjectiveFunction(this.getCurrentSolution())));
		System.out.println("Final number of vehicles:              " + String.valueOf(numVehicles));
	}
	
	
}
