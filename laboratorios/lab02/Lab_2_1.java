/** Name: 			Laboratorio 2.1
 * 	Author: 		Bosse Bandowski
 *  Date: 			19.08.2018
 *  Description: 	This is a solution to the problem specified in ST0247, 2018-2, Laboratorio 2.1.
 *  				It is an extension to the previously handled n-Queens problem which is why many code fragments
 *  				from that exercise were recycled and used with slight adjustments.
 *  
 *  				The program reads user input line by line and processes the given information to calculate and print
 *  				the answers.
 *  
 *  				IMPORTANT: THE AUXILIARY FILE Board.java IS REQUIRED TO RUN THIS PROGRAM
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Lab_2_1 {
	
	ArrayList<Board> boards = new ArrayList<Board>();
	ArrayList<String> lines = new ArrayList<String>();
	ArrayList<Integer> pos;
	
	
	// To read the input, we can use the same method as in the bicolorable problem:
	public void readInput() {
		
		System.out.println("Your input:");
		
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
	
	// This method processes the input and creates boards with their respective bad fields (if there are any)
	public void processInput() {
		
		// this variable is used to keep track of the position of bad fields with respect to the current board.
		// Therefore, if has to be initialised with 0 every time there is a new board created.
		
		int y = 0;
		
		// Loop over every line in the input
		for (int i = 0; i < lines.size(); i++) {
			
			String currentLine = lines.get(i);
			
			// detect if number. If yes, create a new board. if no, edit current board
			try {
				boards.add(new Board(Integer.valueOf(currentLine)));
				
				// reset y
				y = 0;
			} 
			// detect bad fields and add them to the board
			catch (Exception e){
				for (int j = 0; j < currentLine.length(); j++) {
					if (currentLine.charAt(j) == '*') {
						boards.get(boards.size()-1).addBadField(j, y);
					}
				}
				y ++;
			}
		}
	}
	
	// this method iterates over all boards that were specified in the input and calls the respective methods to compute the solutions.
	public void checkAllBoards() {
		for (int j = 0; j < boards.size(); j++) {
			Board b = boards.get(j);
			initArray(b);
			placeQueens(0, b);
			System.out.println("Case " + String.valueOf(j) + ": " + String.valueOf(b.numSolutions));
		}
	}
	
	// initiate array with the size of the board with all elements equal to -1
	public void initArray(Board b) {
		pos = new ArrayList<Integer>();
		
		for (int i = 0; i < b.size; i++) {
			pos.add(-1);
		}
	}
	
	// This method calculates the number of solutions for one specific board and stores the result in the field Board.numSolutions
	public void placeQueens (int column, Board b) {
		
		int n = b.size;
		
		// iterate over all rows in the column in question (rows are investigated from left to right and columns from top to bottom)
		for (int i = 0; i < n; i++) {
			// If a viable spot is found, change the entry in the position array and try to place the next queen
			if (checkPos (column, i, b)) {
				pos.set(column, i);
				// If all queens have been placed (i.e. one queen in each of the n columns) increment the number of solutions
				if (column + 1 >= n) {
					b.numSolutions ++;
				// If there are still unfilled columns, call this function on the next column to the right.
				} else {
				placeQueens(column + 1, b);
				}
			}
		}
	}

	// This method checks whether or not a certain position of a queen is legal or not.
	public boolean checkPos (int x, int y, Board b) {
		
		// Check if bad field
		if (b.isBadField(x, y)) {
			return false;}
		else {
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
	}

	public static void main(String[] args) {
		
		Lab_2_1 test = new Lab_2_1();
		test.readInput();
		test.processInput();
		test.checkAllBoards();
		test.boards.get(0).printBadFields();
	}

}
