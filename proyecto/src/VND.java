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
		
		for (int i = 1; i < solution.size(); i++) {
			Node fromNode = solution.get(i - 1);
			Node toNode = solution.get(i);
			
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
			
			if ((!toNode.equals(fromNode))) {energy -= this.distances.get(fromNode.getNumber()).get(toNode.getNumber()) * this.r;}
			
			if (energy < 0.0 || routeTime > this.Tmax) {
				return false;
			}
		}
		
		return true;
	}
	
	// moves a node from its current position in the solution to the specified index
	public void move (List<Node> solution, int x, int pos) {
		Node tmp = solution.get(x);
		solution.remove(x);
		solution.add(pos, tmp);
	}
	
	public void swap(List<Node> solution, int x, int y) {
		Node xn = solution.get(x);
		Node yn = solution.get(y);
		
		solution.set(x, yn);
		solution.set(y, xn);
	}
	
	// reconnects the tails of two different solutions
	public void reconnect(List<Node> solution, int x, int y) {
		// Find the 0s in the current solution and store their positions
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
		
		if (!(xEnd == yEnd)) {
			List<Node> out = new ArrayList<Node>();
			
			if (x < y) {
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
					this.tempSolution = new ArrayList<Node>();									// this HAS to be improved. Takes waaaay too long
					this.tempSolution.addAll(this.currentSolution);
					
					switch (neighbourhood) {
						case 0:		this.move(this.tempSolution, x, y);
									break;
						case 1:		this.swap(this.tempSolution, x, y);
									break;
						case 2:		this.reconnect(this.tempSolution, x, y);					// never feasible! Change
									break;
					}
					
					if (this.isFeasibleSolution(this.tempSolution)) {
						this.tempCost = this.calculateObjectiveFunction(this.tempSolution);
						if (neighbourhood == 2) {
							if (this.tempCost != this.currentCost) {
								System.out.println("new Solution");
							}
						}
						if (this.tempCost < this.currentCost) {
							bestMove[0] = x;
							bestMove[1] = y;
							this.currentCost = this.getTempCost();
						}
					}
				}
			}
			
			switch (neighbourhood) {
			case 0:		this.move(this.currentSolution, bestMove[0], bestMove[1]);
						break;
			case 1:		this.swap(this.currentSolution, bestMove[0], bestMove[1]);
						break;
			case 2:		this.reconnect(this.currentSolution, bestMove[0], bestMove[1]);
						break;
			}
			
			
			if ((initCost - this.currentCost) / initCost < 0.001) {
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
	
	public void minimizeChargingTimes() {
		double requiredEnergy = 0.0;
		for (int i = this.currentSolution.size() - 1; i > 0; i--) {
			Node toNode = this.currentSolution.get(i);
			Node fromNode = this.currentSolution.get(i);
			
			if (fromNode instanceof Depot) {
				requiredEnergy = 0.0;
			}
			else if (fromNode instanceof Client) {
				requiredEnergy += this.distances.get(fromNode.getNumber()).get(toNode.getNumber()) * this.r;
			}
			else {
				requiredEnergy += this.distances.get(fromNode.getNumber()).get(toNode.getNumber()) * this.r;
				Station sNode = (Station) fromNode;
				sNode.setS(requiredEnergy * (sNode.getS()/sNode.getQ()));
				sNode.setQ(requiredEnergy);
				requiredEnergy = 0.0;
			}
		}
	}

	// simply outputs the solution (just node ordering, no times or energy)
	public void printSolution(List<Node> solution) {
		for (Node n: solution) {
			System.out.print(String.valueOf(n.getNumber()) + " ");
		}
		
		System.out.println("");
	}
	
	
}
