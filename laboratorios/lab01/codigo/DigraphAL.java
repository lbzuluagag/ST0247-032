import java.util.ArrayList;
import java.util.LinkedList;
import javafx.util.Pair;
/**
 * Esta clase es una implementación de un digrafo usando listas de adyacencia
 * 
 * @author Mauricio Toro 
 * @version 1
 */
public class DigraphAL extends Graph
{
    LinkedList<LinkedList<Pair<Integer,Integer>>> listas;
    public DigraphAL(int size)
    {
        super(size);
        listas = new LinkedList<LinkedList<Pair<Integer,Integer>>>();
        for (int i = 0; i< size; i++)
            listas.add(new LinkedList<Pair<Integer,Integer>>());
    }

    public void addArc(int source, int destination, int weight)
    {
        listas.get(source).add(new Pair(destination, weight));
    }

    public int getWeight(int source, int destination)
    {
        //Error comun
        //return listas.get(source).get(destination);
        LinkedList<Pair<Integer,Integer>> listaQueVaALaDerecha = listas.get(source);
        for(Pair<Integer,Integer> pareja: listaQueVaALaDerecha)
            if (pareja.getKey() == destination)
                return pareja.getValue(); 
        return 0;
    }

    public ArrayList<Integer> getSuccessors(int vertice)
    {
        ArrayList<Integer> listaa=new ArrayList<>();
        LinkedList<Pair<Integer,Integer>> listaQueVaALaDerecha = listas.get(vertice);
        for(Pair<Integer,Integer> pareja: listaQueVaALaDerecha)
            listaa.add(pareja.getValue());
        return listaa; 
    }
}
