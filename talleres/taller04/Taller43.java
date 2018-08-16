import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

// This class uses the implementation of directed graphs that was written for Laboratorio 1.

// Taller 4.3
public class Taller43 {
	int totalCost = Integer.MAX_VALUE;
	
	// ########################################## Punto 3 ############################################################
	
	// Auxiliary function that initialises an array tsp to keep track of visited nodes and calls the actual recursive function
	public int travellingSalesmanProblem (Graph g, int base) {
		int[] tsp = new int[g.size()];
		
		travellingSalesmanProblem (g, base, base, 0, tsp);
		
		return totalCost;
		
	}
		
	// Recursive function using backtracking
	public void travellingSalesmanProblem (Graph g, int init, int base, int currentCost, int[] tsp) {
		// Mark current node as visited
		tsp[init] = 1;
		
		// Check if all nodes have been visited
		int sum = 0;
		for (int e : tsp) {
			sum += e;
		}
		
		if (sum == tsp.length) {
			// if all nodes have been visited, check if there is an edge from the current node to the base
			if (g.getSuccessors(init).contains(base)) {
				// if yes, check if the total cost could be decreased by choosing the current route
				if (currentCost + g.getWeight(init, base) < totalCost) {
					// if yes, update the total cost
					totalCost = currentCost + g.getWeight(init, base);
				}
			}
		} 
		else {
			// if there are still unvisited nodes, call this function on every unvisited child of the current node.
			// The graph, base, and tsp are simply passed. The currentCost argument is updated.
			for (int child : g.getSuccessors(init)) {
				if (tsp[child] == 0) {
					travellingSalesmanProblem (g, child, base, currentCost + g.getWeight(init, child), tsp);
				}
			}
		}
	}
	
	public static void main(String[] args) {

		Taller43 check3 = new Taller43();
		
        DigraphAL graph = new DigraphAL(4);
        graph.addArc(0, 1, 1);
        graph.addArc(1, 2, 1);
        graph.addArc(2, 1, 5);
        graph.addArc(1, 0, 1);
        graph.addArc(2, 3, 3);
        graph.addArc(1, 3, 5);
        graph.addArc(3, 0, 2);
        
		// Checks Punto 3
		System.out.println(check3.travellingSalesmanProblem(graph, 0));
	}

}
