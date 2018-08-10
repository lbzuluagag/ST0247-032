
/**
 * Write a description of class vertex here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;

public class vertex
{
    int num;
    LinkedList <vertex> lista = new LinkedList<vertex>();
   public vertex (int num){
       this.num = num;
    }
   public void addVecino(vertex v){
       lista.add(v);
   }

}
