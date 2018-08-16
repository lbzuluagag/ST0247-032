import java.util.ArrayList;
import java.util.LinkedList;
/**
 * Esta clase es una implementación de un digrafo usando listas de adyacencia
 * 
 * @author Mauricio Toro 
 * @version 1
 */
public class DigraphAL extends Graph
{
   // Lista de listas que contiene Vertices
    private LinkedList<LinkedList<Pareja>> grafoAl;
    /**
    * Constructor de DiagraphAL
    */
   public DigraphAL(int size)
   {
      super(size);
      grafoAl = new LinkedList<>(); 
      for(int i = 0;i < size; i++){
          grafoAl.add(new LinkedList());
      }
   }
   /**
    * Metodo que agrega un arco entre dos vertices
    */
    public void addArc(int source, int destination, int weight)
   {
      grafoAl.get(source).add(new Pareja(weight,destination));
   }
   /**
    * Metodo que retorna el un ArrayList con el numero de todos los vertices que tengan una conexion
    * con uno
    */
   public ArrayList<Integer> getSuccessors(int vertex)
   {
      ArrayList<Integer> sucesores = new ArrayList<Integer>();
      for(int i = 0; i < grafoAl.get(vertex).size(); i++){
           sucesores.add(grafoAl.get(vertex).get(i).vertice);
      }
      return sucesores;
   }
   /**
    * Metodo que retorna el valor o peso de un arco entre dos vertices
    */
   public int getWeight(int source, int destination)
   {
       for (int i = 0 ;i < grafoAl.get(source).size();i++){
        if(grafoAl.get(source).get(i).vertice== destination) return grafoAl.get(source).get(i).peso;
       }
        return 0;
    }
}