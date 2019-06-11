
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import moead.MOEAD;
import moead.MOEAD2;
import moead.MOEADDE;
import nsga2.NSGA;
import nsga2.NSGA2;
import paes.PAES;

public class main {

    public static void main(String[] args) {
        
        long startTime = System.nanoTime();
        
        NSGA2 nsga2 = new NSGA2("ZDT6");
        nsga2.run();
        String[] out = nsga2.getFitness();
        for (int i = 0; i < out.length; i++) {
            System.out.println(out[i]);
        }
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("duration: " + (duration / 1000000));
        
    }
    
}


