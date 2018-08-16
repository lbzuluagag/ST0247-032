import java.util.ArrayList;

/**
 * Clase en la cual se implementan los metodos del Taller de Clase #2
 * 
 * @author Mauricio Toro, Mateo Agudelo
 */
public class Taller2 {

    public static ArrayList<String> combinations(String s) {

        ArrayList<String> arreglo = new ArrayList<String>();
        combinations(s,"",arreglo);
        return arreglo;
    }

    // recomendacion
    private static void combinations(String pre, String pos, ArrayList<String> list) {
        if(pre.length()==0){
            list.add(pos);
        }else{
            combinations(pre.substring(1),pos+pre.charAt(0),list);
            combinations(pre.substring(1),pos,list);
        }

    }

    public static ArrayList<String> permutations(String s) {
        ArrayList<String> arreglo = new ArrayList<String>();
        permutations(s,"",arreglo);
        return arreglo;
    }

    // recomendacion
    private static void permutations(String pre, String pos, ArrayList<String> arreglo) {
        if(pre.length()==0){
        System.out.println();
        arreglo.add(pos);
        }else{
            for(int i=0;i<pre.length();i++){
            permutations(pre.substring(0,i)+pre.substring(i+1),pos+pre.charAt(i),arreglo);
            }
        }
  

    }

    public static void imprimirTablero(int[] tablero) {
        int n = tablero.length;
        System.out.print("    ");
        for (int i = 0; i < n; ++i)
            System.out.print(i + " ");
        System.out.println("\n");
        for (int i = 0; i < n; ++i) {
            System.out.print(i + "   ");
            for (int j = 0; j < n; ++j)
                System.out.print((tablero[i] == j ? "Q" : "#") + " ");
            System.out.println();
        }
        System.out.println();
    }

    public static boolean esValido(int[] tablero) {
        // complete...
        return true;
    }

    public static int queens(int n) {
        // complete...
        return 0;
    }

}
