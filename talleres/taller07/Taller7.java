import java.util.ArrayList;
import java.util.LinkedList;

// This class relies on the "DigraphAL" implementation of the abstract class "Graph" that has been written as part of an earlier project in this course.
public class Taller7 {
	int[] weights;
	int[] visited;
	int[] prev;
	int current;
	int next;
	
	// This method applies the dijkstra algorithm on a given graph "g" and an initial node "init".
	public void dijkstra(int init, Graph g) {
		
		// initialise
		weights = new int[g.size()];
		visited = new int[g.size()];
		prev = new int[g.size()];
		
		current = init;
		next = -1;
		
		ArrayList<LinkedList<Edge>> out = new ArrayList<LinkedList<Edge>>();
		
		
		for (int i = 0; i < weights.length; i++) {
			weights[i] = Integer.MAX_VALUE;
			out.add(new LinkedList<Edge>());
		}
		
		weights[init] = 0;
		visited[init] = 1;
		
		// Visit every node max once.
		for (int i = 0; i < g.size(); i++) {
			
			// update weights of all children of latest node:
			updateWeights(g);
			
			// find the node with the smallest weight that has not been visited
			smallestNotVisited();		
			
			// If there is no more nodes in the connected component of the initial node (i.e. smallestNotVisited returns -1),
			// nothing will happen for the last couple of iterations because the current node will not be updated.
			if (next != -1) {
				current = next;
				visited[current] = 1;
			}

		}
		
		printResults(g, init, weights, prev);
	}
	
	// Tries to update the weights of the neighbours of a given node in a graph g.
	// Only updates the weight if it can be lowered.
	public void updateWeights(Graph g) {
		ArrayList<Integer> children = g.getSuccessors(current);
		
		for (int child : children) {
			if (weights[child] > weights[current] + g.getWeight(current, child)) {
				weights[child] = weights[current] + g.getWeight(current, child);
				prev[child] = current;
			}
		}
	}
	
	// returns the node with the lowest distance to the initial node that is still unvisited
	public void smallestNotVisited () {
		int smallest = Integer.MAX_VALUE;
		int vertex = -1;
		
		for (int i = 0; i < visited.length; i++) {
			if (weights[i] < smallest && visited[i] == 0) {
				smallest = weights[i];
				vertex = i;
			}
		}
		
		next = vertex;
	}
	
	// Output results in desired format
	public void printResults(Graph g, int init, int[] weights, int[] prev) {
		for (int i = 0; i < g.size(); i++) {
			if (weights[i] == Integer.MAX_VALUE) {
				System.out.println("No route from initial node " + String.valueOf(init) + " to " + String.valueOf(i));
			} 
			else {
				String s = String.valueOf(init) + " -> " + String.valueOf(i) + ": ";
				s += "Distance: " + String.valueOf(weights[i]) + ", Route: ";
				s += backtrackRoute(i, init, prev);
				System.out.println(s);
			}
		}
	}
	
	// Only used for printing the final result.
	public String backtrackRoute(int i, int init, int[] prev) {
		if (i == init) {
			return String.valueOf(i);
		}
		else {
			return backtrackRoute(prev[i], init, prev) + " -> " + String.valueOf(i);
		}
	}
	
	public void minimumSpanningTree(int init, Graph g) {
		
	}
	
	public static void main(String[] args) {
		
		DigraphAL graph = new DigraphAL(8);
        graph.addArc(0, 1, 1);
        graph.addArc(1, 2, 1);
        graph.addArc(2, 1, 5);
        graph.addArc(1, 0, 1);
        graph.addArc(2, 3, 3);
        graph.addArc(1, 3, 1);
        graph.addArc(3, 0, 2);
        graph.addArc(3, 7, 7);
        graph.addArc(3, 4, 1);
        graph.addArc(7, 5, 4);
        graph.addArc(6, 7, 1);
        graph.addArc(6, 0, 1);
		
		
		// ------------------------------- Test Exercise 1 ------------------------------ //
		
		Taller7 prog1 = new Taller7();
		prog1.dijkstra(0, graph);
		
		// ------------------------------- Test Exercise 1 ------------------------------ //
	}

}
