import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.lang.Math;

/* TODO:
 * implement Vehicle.canReturnToBase()
 * Finish implementation of wrap up routes: lead vehicles to charging stations before base
 * Deal with times at charging stations
 * Finish reading input: l and g are momentarily disregarded
 * in Vehicle.move: subtract travel time from rem time for stations and depot
 * Update Run.printOutput() to ensure conformity with project specifications
 * 
 * implement route optimization
 * 				-	Local search
 * 				-	Charging time optimization
 * 
 * write methods for complextiy analysis
 * 
 * Optional: graphical implementation of routes
 * 				-	Draw all nodes in an x and y plane
 * 				-	Draw vehicle routes in this plan
 * 				-	Use different colours or line thicknesses
 */

public class Run {
	ArrayList<Node> nodes = new ArrayList<Node>();											// all nodes
	ArrayList<Station> stations = new ArrayList<Station>();									// all stations. Stored seperately to decrease time complexity when finding closest stations to clients
	ArrayList<Station> sortedStations = new ArrayList<Station>();							// all stations, sorted by distance to depot
	ArrayList<ArrayList<Double>> distances = new ArrayList<ArrayList<Double>>();			// distance matrix
	ArrayList<ArrayList<Station>> closestStations = new ArrayList<ArrayList<Station>>();	// The closest station for every pair of nodes (clients particularly)
	ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();									// all vehicles
	int n;																					// number of nodes
	int m;																					// number of clients
	int unvisited;																			// number of unvisited clients
	int u;																					// number of charging stations
	int breaks;																				// number of break points (depot + stations?)
	double r;																				// energy consumption per km
	double speed;																			// speed
	double Tmax;																			// maximum route duration
	double Smax;																			// maximum charging time
	double St_customer;																		// customer visit time
	double Q;																				// battery capacity
	double startTime;
	
	// Checks input. Input must
	//		- consist of one argument that describes a file location
	//		- the file must be of type .txt
	//		- the file must be accessible
	public void checkInput (String[] args) {
		// Check number of arguments
		if (args.length != 1) {
			System.out.println("ERROR: Wrong number of arguments provided. Must only specify name of input file.");
			System.exit(1);
			}
		else {
			// Check filename and accessibility of file
			String filename = args[0];
			if (filename.length() > 4 && (filename.substring(filename.length()-4).equals(".txt"))) {
				try {
					FileReader fr = new FileReader(filename);
					fr.close();
				}
				catch (Exception e) {
					System.out.println("ERROR: Specified file not accessible.");
					System.exit(1);
				}
			}
			else {
				System.out.println("ERROR: Invalid filename.");
				System.exit(1);
			}
		}
		
		
	}

	// Read the information of the specified file into the respective fields, create nodes etc.
	public void readInput (String filename) {
		try {
			// Create file and scanner object
			File file =  new File(filename);
		    Scanner sc = new Scanner(file);
		    
		    // Read all values specified at start of file as save them in their respective fields
	    	String linen = sc.nextLine();
	    	this.n = Integer.valueOf(linen.split(" ")[2]);
	    	String linem = sc.nextLine();
	    	this.m = Integer.valueOf(linem.split(" ")[2]);
	    	this.unvisited = this.m;
	    	String lineu = sc.nextLine();
	    	this.u = Integer.valueOf(lineu.split(" ")[2]);
	    	String linebreaks = sc.nextLine();
	    	this.breaks = Integer.valueOf(linebreaks.split(" ")[2]);
	    	String liner = sc.nextLine();
	    	this.r = Float.valueOf(liner.split(" ")[2]);
	    	String linespeed = sc.nextLine();
	    	this.speed = Float.valueOf(linespeed.split(" ")[2]);
	    	String lineTmax = sc.nextLine();
	    	this.Tmax = Float.valueOf(lineTmax.split(" ")[2]);
	    	String lineSmax = sc.nextLine();
	    	this.Smax = Float.valueOf(lineSmax.split(" ")[2]);
	    	String linest_customer = sc.nextLine();
	    	this.St_customer = Float.valueOf(linest_customer.split(" ")[2]);
	    	String lineQ = sc.nextLine();
	    	this.Q = Float.valueOf(lineQ.split(" ")[2]);
	    	
	    	// Skip the next three lines
	    	sc.nextLine();
	    	sc.nextLine();
	    	sc.nextLine();
	    
	    	// Read node configuration (ends with an empty line)
	    	String nextLine = sc.nextLine();
	    	
	    	while (!nextLine.equals("")) {
	    		// Decompose input line
	    		String[] parts = nextLine.split(" ");
	    		int number = Integer.valueOf(parts[0]);
	    		String name = parts[1];
	    		float x = Float.valueOf(parts[2]);
	    		float y = Float.valueOf(parts[3]);
	    		String type = parts[4];
	    		int sType = Integer.valueOf(parts[5]);
	    		
	    		// If the type is "d", add a Depot to the Array of nodes; if the type is "c", add a client and if the type is "s", add a station
	    		switch (type) {
	    		case "d":		this.nodes.add(new Depot(number, name, x, y, 0));
	    						break;
	    		case "c":		this.nodes.add(new Client(number, name, x, y));
	    						break;
	    		case "s":		Station s = new Station(number, name, x, y, sType);
	    						this.nodes.add(s);
	    						this.stations.add(s);
	    						break;
	    		}
	    		
	    		// switch to next line of file
	    		nextLine = sc.nextLine();
	    	}
	    	
	    	// Skip the next two lines
			sc.nextLine();
			sc.nextLine();
			nextLine = sc.nextLine();
			
			// Matrix to save l for each station (charging time)
			double lmatrix [][] = new double[3][3];
			int cont = 0; // Variable to know which line we're reading on the .txt file.
			
			while (!nextLine.equals("")) {			
				// Decompose input line			
				String[] parts = nextLine.split(" ");				
				//lmatrix = new double [3][parts.length];				
					for(int j=1; j<parts.length; j++){												
						lmatrix[cont][j-1] = Double.parseDouble(parts[j]);
					}				
				cont++;	    					
				nextLine = sc.nextLine();								
			}	
			
			double gmatrix [][] =  new double [3][3];
			cont = 0; // Variable to know which line we're reading on the .txt file.
			
			sc.nextLine();
			sc.nextLine();

			while (sc.hasNextLine()) {
				nextLine = sc.nextLine();
				// Decompose input line			
				String[] parts = nextLine.split(" ");				
				//gmatrix = new double [3][parts.length];				
					for(int j=1; j<parts.length; j++){												
						gmatrix[cont][j-1] = Double.parseDouble(parts[j]);						
					}				
				cont++;	    								
			}
			
	    	for (Node node : this.nodes) {
	    		if (node instanceof Depot) {
	    			((Depot) node).setQ(gmatrix[2][2]);
	    			((Depot) node).setS(0);
	    		}
	    		else if (node instanceof Station) { // If the node is a station, check what type of station it is 
					//and assign a max charging value and a time for that value. The values are stored in the matrices. 
										
					switch(((Station) node).getType()){
						
						case 0: 													
							((Station) node).setQ(gmatrix[0][2]);							
							((Station) node).setS(lmatrix[0][2]);																		
						break;

						case 1:
							((Station) node).setQ(gmatrix[1][2]);
							((Station) node).setS(lmatrix[1][2]);							
						break;

						case 2:
							((Station) node).setQ(gmatrix[2][2]);
							((Station) node).setS(lmatrix[2][2]);							
						break;

						default:
							break;
					} 
				}				
	    	}
	    	
	    	// close scanner
	    	
	    	sc.close();
	    	
	    	
		} catch (Exception e) {
			e.printStackTrace();;
			System.out.println("ERROR: Error while reading input file.");
			System.exit(1);
		}
	}

	// Create a matrix that contains all distances between the nodes
	public void createDistanceMatrix() {
		
		// for every node, create an array list containing the distances to all other nodes.
		// the distance is calculated with the formula d = sqrt((x1 - x2)^2 + (y1 - y2)^2)
		for (int i = 0; i < this.n; i++) {
			// add a new array list
			this.distances.add(new ArrayList<Double>());
			for (int j = 0; j < this.n; j++) {
				// caluclate the distance
				double distance = Math.sqrt((this.nodes.get(i).getXpos() - this.nodes.get(j).getXpos()) * (this.nodes.get(i).getXpos() - this.nodes.get(j).getXpos())
						+ (this.nodes.get(i).getYpos() - this.nodes.get(j).getYpos()) * (this.nodes.get(i).getYpos() - this.nodes.get(j).getYpos()));
				// add the distance to the array list
				this.distances.get(i).add(distance);
			}
		// set the distance of any node to itself to infinity
		this.distances.get(i).set(i, (double) Integer.MAX_VALUE);
		}
	}
	
	// Create a matrix that contains the closest station to every pair of nodes. The closest station is defined to be the station with the shortest cumulative distance to both nodes
	public void createStationMatrix() {
		
		// for every node, create an array list containing the closest station to itself and all other nodes
		for (int i = 0; i < this.n; i++) {
			this.closestStations.add(new ArrayList<Station>());
			for (int j = 0; j < this.n; j++) {
				// Initialise a tempory fake station with an infinite distance
				Station tmp = new Station(-1, "tmp", 0.0, 0.0, 0);
				double currentMin = (double) Integer.MAX_VALUE;
				// Iterate over all stations to find closest
				for (Station s : this.stations) {
					if (this.distances.get(i).get(s.getNumber()) + this.distances.get(j).get(s.getNumber()) < currentMin) {
						tmp = s;
						currentMin = (this.distances.get(i).get(s.getNumber()) + this.distances.get(j).get(s.getNumber()));
					}
				}
				
				// Add closest station in respective position
				this.closestStations.get(i).add(tmp);
			}
		}
	}
	
	// Store the closest station to each client, the distance to the closest station and the distance to the base in the respective fields of the Client object to avoid repeated calculation
	public void setImportantDistances() {
		for (Node node : this.nodes) {
			if (node instanceof Client) {
				((Client) node).setClosestStation(this.distances, this.nodes);
			}
			node.setDistanceToBase(this.distances);
		}
	}
	
	// This method sorts all stations according to their distance to the base. Simple insertion sort.
	public void orderStationsByDistanceToBase() {
		for (Station currentStation : this.stations) {
			int i = 0;
			while (i < this.sortedStations.size() && currentStation.getDistanceToBase() > this.sortedStations.get(i).getDistanceToBase()) {
				i++;
			}
			this.sortedStations.add(i, currentStation);
		}
		this.sortedStations.add(0, (Station) this.nodes.get(0));
	}
	
	// This method plans the quickest return to the base from every node which is used to route the vehicles when their energy is not sufficient to go the the base directly.
	public void planReturnToBase() {
		for (Node n : this.nodes) {
			n.planFastestRouteToBase(this.distances, this.sortedStations, this.r, this.speed);
		}
	}
	
	// Iterate over all vehicles and find the global closest neighbour by comparing .getClosestNeighbour nodes
	public Client findGlobalClosestNeighbour() {
		// create a dummy output node
		Client out = new Client(-1, "tmp", -1.0, -1.0);
		double minDistance = (double) Integer.MAX_VALUE;
		
		// start iteration
		for (Vehicle v : this.vehicles) {
			// compare distance to closest client of current vehicle to overall minimum distance. Update if smaller
			if (v.getDistanceToClient() < minDistance && !v.isOutOfTime()) {
				out = (Client) v.getClosestClient();
				minDistance = v.getDistanceToClient();
				out.setAssignedVehicle(v);
			}
		}
		
		return out;
	}
	
	// Adds a new vehicle to the fleet at the base with full battery
	public void addVehicle() {
		this.vehicles.add(new Vehicle(this.vehicles.size(), this.Q, this.nodes.get(0), this.r, this.speed, this.Tmax, this.distances, this.nodes));
	}
	
	// Checks if an idle vehicle is at the base
	public boolean vehicleAtBase() {
		for (Vehicle v : this.vehicles) {
			if (v.getCurrentNode().equals(this.nodes.get(0)) && !v.isOutOfTime()) {return true;}
		}
		return false;
	}
	
	// Runs the actual VRP algorithm. In this first version, a simple constructive method for this type of problem is used that is based on the closest neighbour principle.
	public void planRoutes() {
		// Initialise by adding the first vehicle to the fleet
		this.addVehicle();
		int count = 0;
		
		// Loop until there are no more unvisited nodes
		while (this.unvisited > 0) {
			
			// Find closest neighbour
			Client currentCandidate = findGlobalClosestNeighbour();
			Vehicle v = currentCandidate.getAssignedVehicle();
			// Initialise a target node
			Node target = new Client(-1, "tmp", 0, 0);

			// Check if the vehicle can visit the closest client and still make it to the base in time. If not, move vehicle to base
			if (!v.canGoToCNInTime(this.St_customer)) {
				v.returnToBase(this.distances);
			}
			// Check if the vehicle can still make it to a station without running out of fuel. If not, move vehicle to station
			else if (!v.canGoToCNWithFuel()) {
				// Check which station to move to. If the vehicle cannot move to the preferred station with the smallest cumulative distance, simply move to the current closest station
				int current = v.getCurrentNode().getNumber();
				int neighbour = currentCandidate.getNumber();
				double distanceToPrefStation = this.distances.get(current).get(this.closestStations.get(current).get(neighbour).getNumber());
				
				// If the energy level would not fall below 0 when going to the preferred station, move there. Else move to the closest station.
				if (v.getEnergyLevel() - distanceToPrefStation * this.r >= 0) {
					target = this.closestStations.get(current).get(neighbour);
				}
				else {
					target = v.getCurrentNode().getClosestStation();					
				}
			}
			
			else {
				target = currentCandidate;
			}
			
			
			if (!(target.getNumber() == -1)) {
				if (target instanceof Depot) {
					currentCandidate.getAssignedVehicle().moveTo(target, this.distances, 0);
				}
				else if (target instanceof Station) {
					currentCandidate.getAssignedVehicle().moveTo(target, this.distances, ((Station) target).getS());
				}
				else {
					currentCandidate.getAssignedVehicle().moveTo(target, this.distances, this.St_customer);
				}
				
				// Decrement unvisited if the vehicle was moved to a client.
				if (target.equals(currentCandidate)) {
					this.unvisited --;
				}
			}
			
			// Potentially update closest client of other vehicles (if the current candidate was the closest to several different vehicles, it has become the current node of the closer one and it cannot
			// remain the closest neighbour of the other ones.
			for (Vehicle vehicle : this.vehicles) {
				if (vehicle.getClosestClient().isVisited()) {
					vehicle.findClosestClient(this.distances, this.nodes);
				}
			}
			
			// Check if there is a car at the depot. If not, create a new one there.
			if (!this.vehicleAtBase()) {
				this.addVehicle();
			}
			
			if (count > 10000) {
				System.out.println("Couldn't find solution");
				System.exit(1);
			}
			
			count++;
		}
		
		// Finalize routes
		this.wrapUpRoutes();
	}
	
	// Return all vehicles to the base after all clients have been visited. Some vehicles might have to go to a charging station along the way.
	public void wrapUpRoutes() {
		// Iterate over all vehicles
		for (Vehicle v : this.vehicles) {
			// Check if vehicle is already at base. If yes, do nothing. Else move vehicle to base.
			if (!v.getCurrentNode().equals(this.nodes.get(0))) {
				v.returnToBase(this.distances);
			}
		}
	}
	
	public void printOutput() {
		for (Vehicle v : this.vehicles) {
			System.out.println("Vehicle " + String.valueOf(v.getNumber()) + ": ");
			System.out.print("	Route: ");
			for (Node n : v.getRoute()) {
				System.out.print(String.valueOf(n.getNumber()) + ", ");
			}
			System.out.println("");
			System.out.print("	Time:  ");
			System.out.println(String.valueOf(this.Tmax - v.getRemTime()).substring(0, 3));
		}
	}

	public void setup(String[] args) {
		this.checkInput(args);														// Check input
		this.readInput(args[0]);													// Read input
		this.createDistanceMatrix();												// Create distance matrix
		this.createStationMatrix();													// Create matrix of closest stations
		this.setImportantDistances(); 												// Calculate distances to closest station and base for each client
		this.orderStationsByDistanceToBase();										// sort stations based on their distance to the depot
		this.planReturnToBase();													// Create a feasible route to return to base for every node
	}
	
	public static void main(String[] args) {
		Run prog = new Run();														// Initialise object
		prog.startTime = System.currentTimeMillis();
		prog.setup(args);															// run setup sequence
		prog.planRoutes();															// Run the actual algorithm
		// prog.printOutput();														// print output
		
		VND test = new VND(prog.nodes, prog.distances, prog.vehicles, prog.r, prog.speed, prog.Tmax, prog.St_customer, prog.Q, prog.startTime);
		test.changeRouteRepresentation();
		System.out.println("Value of initial solution: " + String.valueOf(test.calculateObjectiveFunction(test.getSolution())));
		test.isFeasibleSolution(test.getSolution());
		test.optimize();
		System.out.println("Final value: " + String.valueOf(test.calculateObjectiveFunction(test.getCurrentSolution())));
	}

}
