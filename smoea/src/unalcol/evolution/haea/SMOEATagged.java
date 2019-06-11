package unalcol.evolution.haea;

import unalcol.Tagged;
import unalcol.types.collection.vector.SortedVector;
import unalcol.types.collection.vector.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SMOEATagged<T> {
    private Tagged<T> thing;
    private Double[] fitness;

    public Double[] getDistances() {
        return distances;
    }

    public void setDistances(Double[] distances) {
        this.distances = distances;
    }

    private Double [] distances;



    public int getIndex() {
        return index;
    }

    private int index;

    public  SMOEATagged(Tagged<T> thing, Double[] fitness, int index)
    {
        this.thing = thing;
        this.fitness = fitness;
        this.index = index;

    }

    public void  setFitness(Double[] fitness){
        this.fitness = fitness;
    }

    public Double[] getFitness() {
        return fitness;
    }

    public Tagged<T> getThing() {
        return thing;
    }

    /**
     * Returns the euclidean distance between this element and other
     * @param other
     * @return
     */
    protected double getDistance(SMOEATagged other){
        double distance = 0.0;
        for(int i =0; i< fitness.length; i ++){
            double val= fitness[i] - other.fitness[i];
            distance += val * val;
        }
        return Math.sqrt(distance);
    }


    public SMOEATagged getNearest(Vector<SMOEATagged> others){
        SMOEATagged result = null;
        Double min = Double.MAX_VALUE;
        for(int i = 0; i< others.size(); i++){
            Double  distance = getDistance(others.get(i));
            if(distance < min) {
                result=others.get(i);
                min = distance;
            }
        }
        return result;
    }

    public double getMaxFromDistances(){
        if(distances == null){
            return 0.0;
        }

        List<Double> dlist = Arrays.asList(distances);
        return Collections.max(dlist);

    }



}
