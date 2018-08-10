import java.io.*;
import java.util.*;
import java.util.Scanner;
/**
 * This class contains algorithms for digraphs
 * Adapted from: http://cs.fit.edu/~ryan/java/programs/graph/Dijkstra-java.html
 * @author Mauricio Toro
 * @version 1
 */
public class DigraphAlgorithms
{
    public DigraphAlgorithms(){

    }
    private static int minVertex (int [] dist, boolean [] v) {
        int x = Integer.MAX_VALUE;
        int y = -1;   // graph not connected, or no unvisited vertices
        for (int i=0; i<dist.length; i++) {
            if (!v[i] && dist[i]<x) {y=i; x=dist[i];}
        }
        return y;
    }

    static int [] dijsktra(Graph dg, int source)
    {
        final int [] dist = new int [dg.size()];  // shortest known distance from "s"
        final int [] pred = new int [dg.size()];  // preceeding node in path
        final boolean [] visited = new boolean [dg.size()]; // all false initially

        for (int i=0; i<dist.length; i++) {
            dist[i] = Integer.MAX_VALUE; //Infinity
        }
        dist[source] = 0;

        for (int i=0; i<dist.length; i++) {
            final int next = minVertex (dist, visited);
            visited[next] = true;

            // The shortest path to next is dist[next] and via pred[next].

            final ArrayList<Integer> n = dg.getSuccessors (next); 
            for (int j=0; j<n.size(); j++) {
                final int v = n.get(j);
                final int d = dist[next] + dg.getWeight(next,v);
                if (dist[v] > d) {
                    dist[v] = d;
                    pred[v] = next;
                }
            }
        }
        return pred;  // (ignore pred[s]==0!)
    }

    public static ArrayList getPath (int [] pred, int s, int e) {
        final java.util.ArrayList path = new java.util.ArrayList();
        int x = e;
        while (x!=s) {
            path.add (0, x);
            x = pred[x];
        }
        path.add (0, s);
        return path;
    }

    // Código para dibujar el grafo en GraphViz
    // Recomiendo www.webgraphviz.com/
    public static void dibujarGrafo(Graph g)
    {
        System.out.println("digraph Grafo {");
        System.out.println("node [color=cyan, style=filled];");
        int nv = g.size();
        for (int i = 0; i < nv; i++)
        {
            ArrayList<Integer> lista = g.getSuccessors(i);
            for (int j = 0; j < lista.size(); j++)
                System.out.println("\"" + i + "\" -> \"" + lista.get(j) + "\" [ label=\""+ g.getWeight(i,lista.get(j)) +"\"];");
        }
        System.out.println("}");
    }

    public static void ALsuccesors(DigraphAL x){
        int mayor=0;
        int pos=0;
        for(int i=0;i<x.size();i++){
            ArrayList<Integer>a;
            a=x.getSuccessors(i);
            if(a.size()>mayor){
                mayor=a.size();
                pos=i;
            }
        }
        System.out.println("el nodo con mas hijos es "+pos);
    }

    public static void AMsuccesors(DigraphAM x){
        int mayor=0;
        int pos=0;
        int temp=0;
        for(int i=0;i<x.size();i++){
            ArrayList<Integer>a;
            a=x.getSuccessors(i);
            if(a.size()>mayor){
                mayor=a.size();
                pos=i;
            }
        }
        System.out.println("el nodo con mas hijos es "+pos);
    }

    public void leer(String archivo) throws FileNotFoundException, IOException {
        String cadena;
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        Queue<Double> lista=new LinkedList<Double>();
        String linea;
        String linea2="";
        int i=0;
        while((linea=b.readLine()) !=null) && i<10000){
            i++;
            System.out.println(i);
            //String[]cont=linea.split(" ");
            linea2=linea2+b;
            /*
            for(int i=0;i<cont.length;i++){
                try{
                    lista.add(Double.parseDouble(cont[i]));
                }catch(Exception e){

                }

            }
            */
            //cont=b.split(" ");
            //if(b.hasNextDouble()){

        }
        //System.out.println(linea2);        
        
        b.close();
    }

    public static void main(String[] args)throws Exception
    {
        DigraphAL dgal = new DigraphAL(5);
        dgal.addArc(0,1,10);
        dgal.addArc(0,2,3);
        dgal.addArc(1,2,1);
        dgal.addArc(1,3,2);
        dgal.addArc(2,1,4);
        dgal.addArc(2,3,8);
        dgal.addArc(2,4,2);
        dgal.addArc(3,4,7);
        dgal.addArc(4,3,9);
        ALsuccesors(dgal);

        //System.out.println(getPath(dijsktra(dgal,0),0,3));

        DigraphAM dgam = new DigraphAM(5);
        dgam.addArc(0,1,10);
        dgam.addArc(0,2,3);
        dgam.addArc(1,2,1);
        dgam.addArc(1,3,2);
        dgam.addArc(2,1,4);
        dgam.addArc(2,3,8);
        dgam.addArc(2,4,2);
        dgam.addArc(3,4,7);
        dgam.addArc(4,3,9);
        AMsuccesors(dgam);
        //System.out.println(getPath(dijsktra(dgam,0),0,3)); 

        DigraphAlgorithms.dibujarGrafo(dgal);
        DigraphAlgorithms a=new DigraphAlgorithms();
        try{
            a.leer("Texto.TXT");
        }catch(FileNotFoundException e){

        
        }

    }
}
