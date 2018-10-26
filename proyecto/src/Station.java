import java.util.ArrayList;

public class Station extends Node {
	protected int type;			// Type of charging station: (0: fast, 1: medium, 2: slow)
	protected double S;			// Maximum charging time
	protected double Q;			// Maximum battery level
	
	// Constructor to initialize any station
	public Station (int num, String name, double x, double y, int type) {
		this.number = num;
		this.name = name;
		this.xpos = x;
		this.ypos = y;
		this.type = type;
	}

	// getters and setters
	public double getS() {
		return S;
	}

	public void setS(double s) {
		S = s;
	}

	public double getQ() {
		return Q;
	}

	public void setQ(double q) {
		Q = q;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public void planFastestRouteToBase(ArrayList<ArrayList<Double>> distances, ArrayList<Station> sortedStations, double r, double speed) {
		double energy = this.getQ();
		double time = 0;
		
		this.fastestRouteToBase.add(this);
		
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
		
		this.fastestRouteToBase.remove(this);
		this.setTimeToBaseWithCharging(time);		
	}
	
	
}
