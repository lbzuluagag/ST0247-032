import java.io.*;
import java.util.Scanner;
//Tomado de https://github.com/Sinclert/Heuristics-TSP/blob/master/HK_Paths.java

public class HK_Paths {
    // Matriz
    private static int[][] distances;
    
    private static int finalResults[];
    // Arreglo de posibles rutas
    private static String paths[];
    // Contador para llevar la cuenta del peso 
    private static int counter = 0;

    public static void main(String args[]) throws IOException{
        Scanner input = new Scanner(System.in);
        System.out.println("Introduzca la ruta donde está guardado el archivo .txt");
        String file = input.nextLine();
        System.out.println("Introduzca el tamaño de la matriz");
        int size = input.nextInt();
        int numSolutions = factorial(size - 1);
        distances = new int[size][size];
        finalResults = new int[numSolutions];
        paths = new String[numSolutions];
        FileReader f = new FileReader(file);
        BufferedReader b = new BufferedReader(f);
        for (int row = 0 ; row < size ; row++) {
            String line = b.readLine();
            String[] values = line.trim().split("\\s+");
            for (int col = 0; col < size; col++) {
                distances[row][col] = Integer.parseInt(values[col]);
            }
        }
        b.close();
        String path = "";
        int[] vertices = new int[size - 1];       
        for (int i = 1; i < size; i++) {
            vertices[i - 1] = i;
        }
        int distance = procedure(0, vertices, path, 0);
        int optimal = 0;
        for (int i = 0; i < numSolutions; i++) {
            System.out.print("Path: " + paths[i] + ". Distance = " + finalResults[i] + "\n");
            if (finalResults[i] == distance) {
                optimal = i;
            }
        }
        System.out.println();
        System.out.print("Path: " + paths[optimal] + ". Distance = " + finalResults[optimal] + " (OPTIMAL)");
    }

    private static int procedure(int initial, int vertices[], String path, int costUntilHere) {
        path = path + Integer.toString(initial) + " - ";
        int length = vertices.length;
        int newCostUntilHere;
        if (length == 0) {
            paths[counter] = path + "0";
            finalResults[counter] = costUntilHere + distances[initial][0];
            counter++;
            return (distances[initial][0]);
        } else {
            int[][] newVertices = new int[length][(length - 1)];
            int costCurrentNode, costChild;
            int bestCost = Integer.MAX_VALUE;
            for (int i = 0; i < length; i++) {
                for (int j = 0, k = 0; j < length; j++, k++) {
                    if (j == i) {
                        k--;
                        continue;
                    }
                    newVertices[i][k] = vertices[j];
                }
                costCurrentNode = distances[initial][vertices[i]];
                newCostUntilHere = costCurrentNode + costUntilHere;
                costChild = procedure(vertices[i], newVertices[i], path, newCostUntilHere);
                int totalCost = costChild + costCurrentNode;
                if (totalCost < bestCost) {
                    bestCost = totalCost;
                }
            }
            return (bestCost);
        }
    }

    private static int factorial(int n) {
        if (n <= 1) return 1;
        else return (n * factorial(n - 1));
    }
}