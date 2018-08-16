import java.util.ArrayList;
import java.util.LinkedList;
import javafx.util.Pair;
/**
 * Esta clase es una implementación de un digrafo usando matrices de adyacencia
 * 
 * @author Mauricio Toro 
 * @version 1
 */
public class DigraphAM extends Graph
{
  private int[][] matriz; 
   
   public DigraphAM(int size)
   {
       super(size);
       matriz= new int[size][size];

   }
   
   public int getWeight(int source, int destination)
   {//hecho por luis
     return matriz[source][destination];
    }
   
   public void addArc(int source, int destination, int weight)
   {//hecho por luis
    matriz[source][destination]=weight;
   }
  
   public ArrayList<Integer> getSuccessors(int vertex)
   {//hecho por luis
       ArrayList<Integer> lista=new ArrayList<Integer>();
       for(int i=0;i<size;i++)
        if(matriz[vertex][i]>0) lista.add(i);
        return lista;
   }
}
