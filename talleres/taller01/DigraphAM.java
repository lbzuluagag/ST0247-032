import java.util.ArrayList;

/**
 * Implementacion de un grafo dirigido usando matrices de adyacencia
 *
 * @author Mauricio Toro, Mateo Agudelo, <su nombre>
 */
public class DigraphAM extends Digraph {
    private int[][] matriz;

    public DigraphAM(int size) {
        super(size);
        matriz=new int[size][size];
    }

    public void addArc(int source, int destination, int weight) {
        matriz[source][destination]=weight;
    }

    public ArrayList<Integer> getSuccessors(int vertex) {
        ArrayList<Integer> succ=new ArrayList<>();
        for(int i=0;i<size;i++){
            if(matriz[vertex][i]>0)succ.add(i);
        }
        return succ;
    }

    public int getWeight(int source, int destination) {
        return matriz[source][destination];
    }

}
