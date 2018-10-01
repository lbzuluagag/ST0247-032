import java.util.Arrays;
import java.util.Collections;


public class punto21{

    public void punto2(Integer [] a, Integer [] rm, Integer [] rt){
            int n = a[0];
            int d = a[1];
            int r = a[2];
        while (a.length == 3){
            if(n==0||d==0||r==0){
                System.out.println("Datos inválidos");
                break;
            }
            Arrays.sort(rm, Collections.reverseOrder());
            System.out.println(Arrays.toString(rm));

            Arrays.sort(rt, Collections.reverseOrder());
            System.out.println(Arrays.toString(rt));

            for(int i=0; i<n; i++){
                int tot = rm[i] + rt[i];
                if(tot>d){
                    int extra = tot*r;
                    int j = i+1;
                    System.out.println("Conductor " + j + " extra: " + extra );
                }
            }
            
        
           Integer b [] = new Integer [2];
           a = b;
        } 
    }
    public static void main(String[] args) {
        Integer [] a = {4,6,1};
        Integer [] rm = {2,1,3,3};
        Integer [] rt = {4,1,5,6};
        punto21 p = new punto21();
        p.punto2(a, rm, rt);
    }
}