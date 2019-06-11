package unalcol.evolution.haea;

import java.util.Comparator;

public class SPFitnessSorter  implements Comparator <SPFitness> {



    public int compare(SPFitness o1, SPFitness o2) {
        return Double.compare(o1.getFitness(),  o2.getFitness());
    }
}
