
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class Laboratorio_3 {
	
	// --------------------------- Object Fields Problem 1.1 --------------------------- //
	ArrayList<Integer> pos = new ArrayList<Integer>();
	ArrayList<ArrayList<Integer>> sol = new ArrayList<ArrayList<Integer>>();
	int n = 0;
	
	// --------------------------- Object Fields Problem 2.1 --------------------------- //
	
	ArrayList<Integer> bestSolution = new ArrayList<Integer>();
	int bestWeight;
	
		
	// ------------------------------ Methods Problem 1.1 ------------------------------ //
	
	// Constructor: the argument defines the size of the board
	public Laboratorio_3 (int n) {
		this.n = n;
	}
	
	public Laboratorio_3 () {}
	
	// initiate array of length n with all elements equal to -1
	public void initArray() {
		for (int i = 0; i < n; i++) {
			pos.add(-1);
		}
	}
	
	// This method adds all possible solutions to the n queens problem on a size (n*n) board to a solution space (obj field "sol")
	public void placeQueens (int column) {
		// iterate over all rows in the column in question (rows are investigated from left to right and columns from top to bottom)
		for (int i = 0; i < n; i++) {
			// If a viable spot is found, change the entry in the position array and try to place the next queen
			if (checkPos (column, i)) {
				pos.set(column, i);
				// If all queens have been placed (i.e. one queen in each of the n columns), 
				if (column + 1 >= n) {
					ArrayList<Integer> copypos = new ArrayList<Integer>();
					copypos = (ArrayList<Integer>) pos.clone();
					this.sol.add(copypos);
					this.printOut();
					System.exit(0);
				// If there are still unfilled columns, call this function on the next column to the right.
				} else {
				placeQueens(column + 1);
				}
			}
		}
	}

	// This method checks whether or not a certain position of a queen is legal or not.
	public boolean checkPos (int x, int y) {
		// Compare the current queen's position to all previously placed queens
		for (int i = 0; i < x; i++) {
			// Check horizontally (y = y)
			if (this.pos.get(i).equals(y)) {
				return false;}
			// Check diagonally up (x - i = y - y')
			if (x - i == y - this.pos.get(i)) {
				return false;}
			// Check diagonally down (x - i = y' - y)
			if (x - i == this.pos.get(i) - y) {
				return false;}
		}
		return true;
	}
	
	// print the solution
	public void printOut () {
		int j = 0;
		for (ArrayList<Integer> i : this.sol) {
			System.out.print("	Solution " + Integer.toString(j) + ": ");
			for (int k : i) {
				System.out.print(Integer.toString(k) + " ");
			}
			System.out.print("\n");
			j++;
		}
	}
	
	// ------------------------------ Methods Problem 1.5 ------------------------------ //
	
	// This method requires the previously implemented superclass Graph and its subclass DigraphAL
	
	// Given a graph, we run a BFS algorithm on it and mark all visited vertices. If any node has a child
	// that is marked as visited, there is a cycle in the graph and it is no DAC
	
	public boolean isDAC (Graph g) {
		// initialise
		int n = g.size();
		int[] visited = new int[n];
		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.add(0);
		visited[0] = 1;
		
		while (!queue.isEmpty()) {
			// Iterate over all children. Check if any child has been visited. If yes, return false
			// Simultaneously, add children to queue
			int current = queue.pop();
			ArrayList<Integer> children = g.getSuccessors(current);
			
			for (int child : children) {
				if (visited[child] == 1) {return false;}
				queue.add(child);
			}
		}
		
		return true;
	}
	
	// ------------------------------ Methods Problem 2.1 ------------------------------ //
	
	public Graph readInput() {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    String s;
	    int[] nums = new int[3];
	    
	    try {
	    	// Identify number of arches and vertices
	    	
	    	s = in.readLine();
	    	String[] strings = s.split(" ");
	    		    	
	    	int num_vertices = Integer.valueOf(strings[0]);
	    	int num_edges = Integer.valueOf(strings[1]);
	    	
	    	Graph out = new DigraphAL(num_vertices);
	    	
	    	// Add edges. Our graph implementation is base 0, which means that all node numbers have to be
	    	// reduced by 1
			for (int i = 0; i < num_edges; i++) {
				s = in.readLine();
				strings = s.split(" ");
				nums[0] = Integer.valueOf(strings[0]);
				nums[1] = Integer.valueOf(strings[1]);
				nums[2] = Integer.valueOf(strings[2]);
				out.addArc(nums[0] - 1, nums[1] - 1, nums[2]);
				out.addArc(nums[1] - 1, nums[0] - 1, nums[2]);
			}
			
			return out;
			
			
		} catch (IOException e) {
			System.out.println("Something went very wrong");
			e.printStackTrace();
			
			return new DigraphAL(1);
		}
	}
	
	public void shortestPath (Graph g) {
		
		// initialise
		ArrayList<Integer> currentSolution = new ArrayList<Integer>();
		int bestWeight = Integer.MAX_VALUE;
		int[] minweights = new int[g.size()];
		
		for (int i = 1; i < g.size(); i++) {
			minweights[i] = Integer.MAX_VALUE;
		}
		
		int init = 0;
		int target = g.size() - 1;
		
		bestSolution.add(-1);
		currentSolution.add(init);
		
		// call recursive function to find best solution
		shortestPath(g, init, target, currentSolution, minweights, 0);
		
		// increment every element in the best solution by 1 to compensate for the base 0 / base 1 difference
		for (int i = 0; i < this.bestSolution.size(); i++) {
			this.bestSolution.set(i, this.bestSolution.get(i) + 1);
		}
		
		// print result. If no path has been found, print -1
		if (this.bestWeight == Integer.MAX_VALUE) {
			System.out.println(-1);
		} else {
			System.out.println(this.bestSolution.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void shortestPath (Graph g, int current, int target, ArrayList<Integer> currentSolution, int[] minWeights, int currentWeight) {
		
		// iterate over all children of current node
		
		ArrayList<Integer> children = g.getSuccessors(current);
						
		for (int child : children) {
			
			// calculate updated weight for child
			int updatedWeight = currentWeight + g.getWeight(current, child);
			
			// if child is the target node and its weight can be decreased, update best solution
			if (child == target && updatedWeight < minWeights[child]) {
				this.bestSolution = (ArrayList<Integer>) currentSolution.clone();
				this.bestSolution.add(child);
				
				minWeights[child] = updatedWeight;
				this.bestWeight = updatedWeight;
			}
			
			// if child's weight can be improved, call recursive function on child. Update weight and add to solution
			else if (updatedWeight < minWeights[child]) {
				ArrayList<Integer> tmp = (ArrayList<Integer>) currentSolution.clone();
				tmp.add(child);
				minWeights[child] = updatedWeight;
				shortestPath(g, child, target, tmp, minWeights, updatedWeight);
			}
		}
		
	}
	
	

	public static void main(String[] args) {
		/*
		System.out.println("Punto 1.1:");
		
		Laboratorio_3 board = new Laboratorio_3(8);
		
		board.initArray();
		board.placeQueens(0);
		*/
		System.out.println("Punto 1.5");
		
		Laboratorio_3 prog = new Laboratorio_3();
		
		DigraphAL cyclicgraph = new DigraphAL(3);
		cyclicgraph.addArc(0, 1, 1);
		cyclicgraph.addArc(1, 2, 1);
		cyclicgraph.addArc(2, 0, 5);
		
		DigraphAL acyclicgraph = new DigraphAL(4);
		acyclicgraph.addArc(0, 1, 1);
		acyclicgraph.addArc(1, 2, 1);
		acyclicgraph.addArc(1, 3, 1);
		acyclicgraph.addArc(2, 3, 1);
		
		System.out.print("	");
		System.out.println(prog.isDAC(cyclicgraph));
		
		System.out.print("	");
		System.out.println(prog.isDAC(acyclicgraph));
		
		System.out.println("Punto 2.1");
		
		Graph g = prog.readInput();
		prog.shortestPath(g);
		
	}
}
