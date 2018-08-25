import java.util.ArrayList;

// This class uses the graph implementations from earlier in this course. Make sure to have the files DigraphAL.java and Digraph Algorithm

public class Taller6 {
	
	int[] change = {50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50};
	
	// make change problem. Returns configuration of coins that match the input value.
	public int[] makeChange(int V) {
		int[] out = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		
		while (V > 0) {
			for (int i = 0; i < change.length; i++) {
				if (change[i] <= V) {
					out[i]++;
					V -= change[i];
				}
			}
		}
		
		return out;
	}
	
	// prints the result of the make change problem
	public void printOut (int[] res) {
		for (int i = 0; i < res.length; i++) {
			System.out.println(String.valueOf(change[i]) + ": " + String.valueOf(res[i]));
		}
	}
	
	
	public int greedyTSP(Graph g, int base) {
		
		// initialise
		
		int cost = 0;
		int current = base;
		
		ArrayList<Integer> visited = new ArrayList<Integer>();
		
		for (int i = 0; i < g.size(); i++) {
			visited.add(0);
		}
		
		// base is already visited
		visited.set(base, 1);
		
		// loop until all nodes are visited
		
		while (visited.contains(0)) {
			
			// find closest neighbour
			ArrayList<Integer> neighbours = g.getSuccessors(current);
			int tmpcost = Integer.MAX_VALUE;
			int currentCN = 0;
			
			for (int i = 0; i < neighbours.size(); i++) {
				if (g.getWeight(current, neighbours.get(i)) < tmpcost && visited.get(neighbours.get(i)) == 0) {
					currentCN = neighbours.get(i);
					tmpcost = g.getWeight(current, neighbours.get(i));
				}
			}
			
			// add cost, mark closest neighbour as visited and set as current
			cost += tmpcost;
			visited.set(currentCN, 1);
			current = currentCN;
		}
		
		// Add cost from latest node back to base and return the result
		cost += g.getWeight(current, base);
		
		return cost;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Taller6 prog = new Taller6();
		
		DigraphAL graph = new DigraphAL(4);
        graph.addArc(0, 1, 1);
        graph.addArc(1, 2, 1);
        graph.addArc(2, 1, 5);
        graph.addArc(1, 0, 1);
        graph.addArc(2, 3, 3);
        graph.addArc(1, 3, 5);
        graph.addArc(3, 0, 2);
		
        System.out.println("---------------------Punto 1---------------------");
        
		prog.printOut(prog.makeChange(11250));
		
		System.out.println("---------------------Punto 2---------------------");
		
		System.out.println("Total TSP cost using closest neighbour: " + String.valueOf(prog.greedyTSP(graph, 0)));
	}

}
