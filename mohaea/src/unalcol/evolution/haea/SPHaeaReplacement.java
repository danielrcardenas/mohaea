package unalcol.evolution.haea;

import unalcol.Tagged;
import unalcol.TaggedManager;
import unalcol.Thing;
import unalcol.search.Goal;
import unalcol.search.GoalBased;
import unalcol.search.population.PopulationReplacement;
import unalcol.sort.Order;

import java.util.*;

public class SPHaeaReplacement <T> extends Thing implements GoalBased<T, Double[]>, PopulationReplacement<T>, TaggedManager<T> {



    /**
     * Set of genetic operators that are used by CEA for evolving the Tagged chromosomes
     */
    protected HaeaOperators<T> operators = null;




    /**
     * Default constructor
     */
    public SPHaeaReplacement(HaeaOperators<T> operators) {
        this.operators = operators;
    }



    public HaeaOperators<T> operators() {
        return operators;
    }

    /**
     * Adds a subpopulation of parents and associated offsprings to the replacement strategy.
     * These method uses Non Dominated Sorting in order to define the individuals that
     * will be maintained into the next generation using the HAEA mechanism
     *
     * @param current Parents
     * @param next
     */
    @Override
    public Tagged<T>[] apply(Tagged<T>[] current, Tagged<T>[] next) {
        List<Tagged<T>> population = new ArrayList<>();
        List<SPFitness<T>> filtered = new ArrayList<>();
        List<SPFitness<T>> newPopulation = new ArrayList<>();

        //Joining fathers and children
        for (Tagged i : current) {
            population.add(i);
        }
        for (Tagged i : next) {
            population.add(i);
        }

        HashMap<Tagged<T>,List<Tagged<T>>> S = new HashMap<>(); //Lista de todos dominados por cada P en F1
        List<List<Tagged<T>>> R = new ArrayList<>(); // Lista de todos los que dominan a P
        List<Integer> n = new ArrayList<>(); // numero de cuantos
        int k = (int) Math.sqrt(population.size()); // Longitud de k

        Goal<T,Double[]> goal = goal();
        Order<Double[]> order = goal.order();
        List<SPFitness<T>> aux = new ArrayList<>();

        double [] raw_fitness = new double[population.size()];
        double [] fitness = new double[population.size()];

        for(int i = 0; i< population.size(); i++){
            double [] distances = new double[population.size()];
            Tagged<T> p = population.get(i);
            List<Tagged<T>> Sp = new ArrayList<>();
            List<Tagged<T>> Rp = new ArrayList<>();
            int np = 0;
            SPFitness indv = new SPFitness(p,i);

            for(int j = 0; j < population.size();j++){
                Tagged<T> q = population.get(j);
                if (p != q) {
                    Double[] qs = goal.apply(p);
                    Double[] qi = goal.apply(q);
                    int compare = order.compare(qs, qi);
                    distances[j] = getDistance(qs,qi);
                    indv.setObjective_fitness(qs);
                    // Si qi es dominado por p entonces adiciona q a SP si q domina a p np+=1
                    if( compare > 0){
                        Sp.add(q); // P domina a q
                    } else if (compare<0){
                        np +=1;
                        Rp.add(q); // q domina a p
                    }

                }
            }

            indv.setDistances(distances);
            newPopulation.add(indv);


            // strength(p) = |{q | q <- SolutionSet and p dominate q}|
            S.put(p,Sp);//Lista de los Q que domina P
            R.add(Rp);
            n.add(np);//Cuantos dominan a P
        }


        for(int i =0; i < population.size(); i ++){
            for(Tagged<T> individual: R.get(i)){
                raw_fitness[i] += S.get(individual).size();
            }
            double[] distances = newPopulation.get(i).getOrderedDistances();
            fitness [i] = raw_fitness[i] + 1.0 / (distances[k] + 2 ); // raw fitness + kth_distance
            SPFitness indv = newPopulation.get(i);
            indv.setFitness(fitness[i]);
            indv.setRaw_fitness(raw_fitness[i]);
            if(fitness[i] < 1.0) {
                aux.add(indv);
            }else{
                filtered.add(indv);
            }
        }
        SPFitnessSorter sorter = new SPFitnessSorter();


        if(aux.size()< current.length){
            int remain = current.length - aux.size();
            Collections.sort(filtered,sorter);
            for(int i =0; i < remain;i++){
                aux.add(filtered.get(i));
            }
        } // Fin hasta llenar el archivo

        else if (aux.size()> current.length){


            while(aux.size()> current.length){
                double minDistance = Double.MAX_VALUE;
                int toRemove = 0;
                int i = 0;
                List<double[]> distanceMatrix = getDistanceMatrix(aux);
                for(double[] distances: distanceMatrix){
                    if(distances[0] < minDistance){
                        toRemove = i;
                        minDistance = distances[0];
                    }else  if (distances[0] == minDistance){
                        int flag =0;
                        while (distances[0] == distanceMatrix.get(toRemove)[flag] && flag < distances.length-1){
                            flag ++;
                        }
                        if(distances[flag] < distanceMatrix.get(toRemove)[flag]){
                            toRemove = i;
                        }
                    }
                    i++;
                }

                aux.remove(toRemove);

            }
        }



        Tagged<T>[] result = new Tagged[aux.size()];

        for(int i = 0; i < aux.size(); i ++){
            result[i] = aux.get(i).getThing();
        }
        return result;
    }


    private double getDistance(Double[] one, Double[] two) throws  IndexOutOfBoundsException{
        double distance = 0.0;
        for(int i =0; i< one.length; i ++){
            double val= one[i] - two[i];
            distance += val * val;
        }
        return distance;
    }

    private List<double[]> getDistanceMatrix( List<SPFitness<T>> aux){
        Integer [] indexes = new Integer[aux.size()];
        List<double[]> distanceMatirx = new ArrayList<>();
        for(int i=0; i < aux.size(); i++)
        {
            indexes[i]= aux.get(i).getIndex();
        }
        for(SPFitness thing: aux){
            double [] filteredDistances = thing.getFilteredDistances(indexes);
            Arrays.sort(filteredDistances);
            distanceMatirx.add(filteredDistances);
        }
        return  distanceMatirx;
    }


}
