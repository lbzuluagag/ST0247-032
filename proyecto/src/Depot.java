import java.util.ArrayList;

public class Depot extends Station {

	public Depot(int num, String name, double x, double y, int type) {
		super(num, name, x, y, type);
		this.number = num;
		this.name = name;
		this.xpos = x;
		this.ypos = y;
	}
	
	public void planFastestRouteToBase(ArrayList<ArrayList<Double>> distances, ArrayList<Station> sortedStations, double r, double speed) {
		this.setTimeToBaseWithCharging(0);
	}
}
