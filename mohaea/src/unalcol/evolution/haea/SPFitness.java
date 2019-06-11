package unalcol.evolution.haea;

import unalcol.Tagged;

import java.util.Arrays;
import java.util.List;

public class SPFitness<T>  {

    private Tagged<T> thing;
    private double fitness;
    private double [] distances;
    private double raw_fitness;
    private Double[] objective_fitness;
    private int index;


    public  SPFitness(Tagged<T> thing, double fitness)
    {
        this.thing = thing;
        this.fitness = fitness;
    }

    public SPFitness(Tagged<T> thing, int index) {
        this(thing, 0.0);
        this.index = index;
    }
    public SPFitness(Tagged<T> thing, int index, Double[] objective_fitness) {
        this(thing, index);
        this.objective_fitness = objective_fitness;
    }

    public void  setFitness(double fitness){
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

    public Tagged<T> getThing() {
        return thing;
    }

    public double getRaw_fitness() {
        return raw_fitness;
    }

    public double[] getDistances() {
        return distances;
    }

    public void setDistances(double[] distances) {
        this.distances = distances;
    }

    public void setRaw_fitness(double raw_fitness) {
        this.raw_fitness = raw_fitness;
    }

    public double[] getOrderedDistances(){
        double[] newDistances = new double[this.distances.length];
        System.arraycopy(this.distances,0,newDistances,0,this.distances.length);
        return  newDistances;

    }

    public int getIndex() {
        return index;
    }

    public Double[] getObjective_fitness() {
        return objective_fitness;
    }

    public double [] getFilteredDistances(Integer [] indexes){

        double [] result = new double[indexes.length - 1  ];
        int j = 0;
        for (int i = 0 ; i < this.distances.length; i ++){

            if(Arrays.asList(indexes).contains(i) && i != index){
                result[j] = this.distances[i];
                j++;
            }

        }

        return result;

    }

    public void setObjective_fitness(Double[] objective_fitness) {
        this.objective_fitness = objective_fitness;
    }
}
