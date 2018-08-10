import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Bicolorable {
	ArrayList<String> lines = new ArrayList<String>();
	ArrayList<Graph> graphs = new ArrayList<Graph>();
	
	// readInput() reads the input and stores it line by line in the field "lines"
	// The EOF character is "0" which is regarded in the code
	
	public void readInput() {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    String s;
	    try {
			while (!(s = in.readLine()).equals("0")) {
				lines.add(s);
			}
		} catch (IOException e) {
			System.out.println("Something went very wrong");
			e.printStackTrace();
		}
	}
	
	// processInput() creates the test graphs that are specified by the input. It uses the methods from part 1 of the
	// laboratory exercises. 
	
	public void processInput() {
		String[] nums;
		String[] next;
		for (int i = 0; i < lines.size(); i++) {
			nums = lines.get(i).split(" ");
			try {
				next = lines.get(i + 1).split(" ");
			} catch (Exception e) {
				next = new String[2];
			}
			if (nums.length == 1 && next.length == 1) {
				graphs.add(new DigraphAL(Integer.valueOf(nums[0])));
			} else if (nums.length == 2) {
				graphs.get(graphs.size()-1).addArc(Integer.valueOf(nums[0]), Integer.valueOf(nums[1]), 1);
				graphs.get(graphs.size()-1).addArc(Integer.valueOf(nums[1]), Integer.valueOf(nums[0]), 1);
			}
		}
	}
	
	// checkAll() iterates over all given graphs and checks whether or not they are bicolorable
	
	public void checkAll() {
		for (Graph g : graphs) {
			if (isBicolorable(g)) {
				System.out.println("BICOLORABLE.");
			} else {
				System.out.println("NOT BICOLORABLE.");
			}
		}
	}
	
	// isBicolorable conducts a BFS-like algorithm to check whether or not the given graph is bipartite and thus
	// bicolorable or not. All successors of a node a added to a queue and then processed.
	
	public boolean isBicolorable (Graph g) {
		int[] colors = new int[g.size()];
		int[] visited = new int[g.size()];
		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.add(0);
		colors[0] = 1;
		
		while (!queue.isEmpty()) {
			int node = queue.pop();
			visited[node] = 1;
			
			for (int i : g.getSuccessors(node)) {
				switch (visited[i]) {
				case 0:		queue.add(i);
							colors[i] = -colors[node];
							break;
				case 1:		if (colors[i] == colors[node]) {return false;}
				}
			}
		}
		
		return true;
	}
	
	// program to test methods
	
	public static void main (String[] args) {
		Bicolorable test = new Bicolorable();
		test.readInput();
		test.processInput();
		test.checkAll();	
	}

}