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
        for(int i=0;i<hola.length;i++)hola[i]=0;
        ArrayList<Integer>a=new ArrayList<Integer>();
        System.out.println(recorrido(g,0,3,a,hola)+" final");
        return 0;
    }

    // recomendacion
    private static int recorrido(Graph g, int i,int f,ArrayList<Integer> peso, int[] unvisited) {
        
        if(i==f){
            int c=0;
            for(int j=0;j<unvisited.length;j++){
                c+=unvisited[j];
            }
            return c;
        }
        ArrayList<Integer>destino=g.getSuccessors(i);
        for(int j=0;j<destino.size();j++){
            if(unvisited[destino.get(j)]==0){
                unvisited[i]=g.getWeight(i,destino.get(j)); 
                int c=(recorrido(g,destino.get(j),f,peso,unvisited));
                if(destino.get(j)!=0){
                for(int k=i;k<unvisited.length;k++){
                    unvisited[k]=0;
                }
                }
                if(destino.get(j)==0)unvisited[destino.get(j)]=0;
                peso.add(c);
            }
        }
        int c=99999;
        for(int j=0;j<peso.size();j++){
        if(peso.get(j)<c)c=peso.get(j);
        
        }
        return c;
    }
    
    // recomendacion
    /*
    private static int[] removeAt(int k, int a[]) {
    // complete...
    }
    */ 
    public static int costoMinimo(Graph g, int inicio, int fin) {
        
    }
    // recomendacion
    /*
    private static void dfs(Graph g, int v, int[] costo) {
    // complete...
    }
    */
     
}