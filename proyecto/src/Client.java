import java.util.ArrayList;

public class Client extends Node {
	private boolean visited = false;
	private Vehicle assignedVehicle;

	
	
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
		
	public void planFastestRouteToBase(ArrayList<ArrayList<Double>> distances, ArrayList<Station> sortedStations, double r, double speed) {
		this.fastestRouteToBase.add(this.closestStation);
		double energy = this.closestStation.getQ();
		double time = this.closestStation.getS() + distances.get(this.getNumber()).get(this.closestStation.getNumber()) / speed;
				
		while (!(this.fastestRouteToBase.contains(sortedStations.get(0)))) {
			boolean addedStation = false;
			for (Station s : sortedStations) {
				double distance = distances.get(this.fastestRouteToBase.get(this.fastestRouteToBase.size()-1).getNumber()).get(s.getNumber());
				if (energy -  distance * r > 0 && !addedStation) {
					this.fastestRouteToBase.add(s);
					addedStation = true;
					energy = s.getQ();
					time += distance / speed + s.getS();
				}
			}
		}
		
		this.setTimeToBaseWithCharging(time);
	}
}
