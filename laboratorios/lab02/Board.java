import java.util.ArrayList;
import javafx.util.Pair;

public class Board {
	
	int numSolutions = 0;
	int size;
	ArrayList<Pair<Integer, Integer>> badFields = new ArrayList<Pair<Integer, Integer>>();
	
	public Board (int n) {
		this.size = n;
	}
	
	public void addBadField (int x, int y) {
		if (x < size && x >= 0 && y < size && y >= 0) {
			badFields.add(new Pair<Integer, Integer>(x, y));
		} else {
			System.out.println("Invalid input");
		}
	}
	
	public boolean isBadField (int x, int y) {
		for (Pair<Integer, Integer> bf : badFields) {
			if (bf.getKey() == x && bf.getValue() == y) {
				return true;
			}
		}
		return false;
	}	
}
