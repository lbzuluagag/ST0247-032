import java.util.ArrayList;

/**
 * Clase en la cual se implementan los metodos del Taller de Clase #4
 * 
 * @author Mauricio Toro, Mateo Agudelo
 */
public class Taller4 {
    // public ArrayList<Integer> visito=new ArrayList<>();
    public static boolean haycamino(Graph g,int i,int f) {
        boolean[] visitados=new boolean[g.size()];

        return haycamino(g,i,f,visitados);
    }

    public static boolean haycamino(Graph g,int i,int f,boolean[]v) {
        if(i==f){return true;}else{}
        v[i]=true;
        ArrayList<Integer>destino=g.getSuccessors(i);
        for(int k=0;k<destino.size();k++){
            if(v[destino.get(k)]==false){
            //Vs.
            //if(v[k]==false){
                //boolean c = haycamino(g,destino.get(k),f,v);
                ////Vs.
                if (haycamino(g,destino.get(k),f,v))
                   return true;
            }

        }
        return false;
    }
    public static int recorrido(Graph g){
    int[]hola=new int[g.size()];
    return recorrido(g,0,3,hola);
    
    }
    
    // recomendacion
    private static int recorrido(Graph g, int i,int f, int[] unvisited) {
        int c=0;
        for(int j=0;j<unvisited.length;j++){
        c=c+unvisited[j];
        
        }
        
    return 0;
    }
    /*
    // recomendacion
    private static int[] removeAt(int k, int a[]) {
    // complete...
    }

    public static int costoMinimo(Graph g, int inicio, int fin) {
    // complete...
    }

    // recomendacion
    private static void dfs(Graph g, int v, int[] costo) {
    // complete...
    }
     */
}