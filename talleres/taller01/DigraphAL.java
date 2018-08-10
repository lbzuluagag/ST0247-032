import java.util.ArrayList;

/**
 * Implementacion de un grafo dirigido usando listas de adyacencia
 *
 * @author Mauricio Toro, Mateo Agudelo, <su nombre>
 */
public class DigraphAL extends Digraph {
    // complete...

    public DigraphAL(int size) {
        super(size);
        // complete...
    }

    public void addArc(int source, int destination, int weight) {
        // complete...
        // recuerde: grafo dirigido!
    }

    public ArrayList<Integer> getSuccessors(int vertex) {
        ArrayList<Integer> hola=new ArrayList<>();
        // complete...
        // recuerde: null si no hay!
        return hola;
    }

    public int getWeight(int source, int destination) {
        // complete...
        return 0;
    }

}
