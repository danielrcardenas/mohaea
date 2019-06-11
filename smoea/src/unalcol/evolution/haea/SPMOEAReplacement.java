package unalcol.evolution.haea;

import unalcol.Tagged;
import unalcol.TaggedManager;
import unalcol.Thing;
import unalcol.search.Goal;
import unalcol.search.GoalBased;
import unalcol.search.population.PopulationReplacement;
import unalcol.sort.Order;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.vector.Vector;

import java.util.*;


public class SPMOEAReplacement<T> extends Thing implements GoalBased<T, Double[]>, PopulationReplacement<T>, TaggedManager<T> {

    protected HaeaOperators<T> operators = null;

    protected boolean steady = true;



    /**
     * Default constructor
     */
    public SPMOEAReplacement(HaeaOperators<T> operators, boolean steady ){
        this.operators = operators;
        this.steady = steady;
    }




    public SPMOEAReplacement(HaeaOperators<T> operators) {
        this.operators = operators;
    }

    @Override
    public Tagged<T>[] apply(Tagged<T>[] current, Tagged<T>[] next) {

        Goal<T,Double[]> goal = goal();
        Order<Double[]> order = goal.order();
        Tagged<T>[] buffer = (Tagged<T>[])new Tagged[current.length];

        Vector<SMOEATagged> bufferParents = new Vector<>();
        Vector<SMOEATagged> bufferOffspring = new Vector<>();
        Vector<SMOEATagged> population = new Vector<>();

        int k = 0 ;
        for( int i=0; i<current.length; i++){
            Vector<SMOEATagged> offspring = new Vector<>();
            int n = operators.getSizeOffspring(i);
            for(int j = 0; j< n; j ++){
                offspring.add(new SMOEATagged(next[k], goal.apply(next[k]),i));
                k++;
            }

            SMOEATagged individual =  new SMOEATagged(current[i], goal.apply(current[i]),i);

            SMOEATagged nearest= individual.getNearest(offspring);
            // The nearest and parent are selected

            int comparator = order.compare(nearest.getFitness(), individual.getFitness()); // TODO: Cuadrar el comparador porque está al reves

            if( comparator > 0) {// si qs domina a qi -- si el hijo domina al padre
                buffer[i] = nearest.getThing();
                population.add(nearest);
                operators.reward(i);
            }
            else if (comparator == 0){
                // TODO: implementar SPEA cuando no dominan
                /*
                Para poder implementar el criterio de dominacia entre individuos se evalua la cantidad de
                individuos que domina el hijo y el padre y se realiza la comparacion
                esta comparacion se hace al final de la presente evaluacion
                 */
                bufferParents.add(individual);
                bufferOffspring.add(nearest);
            }

            else {
                buffer[i] = current[i];
                population.add(individual);
                operators.punish(i);

            }


        }






        return buffer;

    }


    protected  Tagged<T>[] SPEAFiltter(Vector<SMOEATagged> population){

        // P Es el individuo en evaluación
        HashMap<SMOEATagged, List<SMOEATagged>> S = new HashMap<>(); //Lista de todos dominados por cada P en F1
        List<List<SMOEATagged>> R = new ArrayList<>(); // Lista de todos los que dominan a P
        List<Integer> n = new ArrayList<>(); // numero de cuantos dominan a P
        int k = (int) Math.sqrt(population.size()); // Longitud de k
        double [] raw_fitness = new double[population.size()];
        double [] fitness = new double[population.size()];
        Goal<T,Double[]> goal = goal();
        Order<Double[]> order = goal.order();

        for(int i = 0; i< population.size(); i++){
            Double [] distances = new Double[population.size()];
            List<SMOEATagged> Sp = new ArrayList<>();
            List<SMOEATagged> Rp = new ArrayList<>();
            int np = 0;
            SMOEATagged p = population.get(i);

            for(int j = 0; j < population.size();j++){
                SMOEATagged q = population.get(j);
                if (p != q){
                    int compare = order.compare(p.getFitness(), q.getFitness());
                    distances[j] = p.getDistance(q);
                    // Si qi es dominado por p entonces adiciona q a SP si q domina a p np+=1
                    if( compare > 0){
                        Sp.add(q); // P domina a q
                    } else if (compare<0){
                        np +=1;
                        Rp.add(q); // q domina a p
                    }
                }
            }
            // strength(p) = |{q | q <- SolutionSet and p dominate q}|
            S.put(p,Sp);//Lista de los Q que domina P
            R.add(Rp);
            n.add(np);//Cuantos dominan a P
            p.setDistances(distances);
        }
        for(int i =0; i < population.size(); i ++){
            for(SMOEATagged individual: R.get(i)){
                raw_fitness[i] += S.get(individual).size();
            }
            double distance = population.get(i).getMaxFromDistances();
            fitness [i] = raw_fitness[i] + 1.0 / (distance + 2 ); // raw fitness + kth_distance

            if(fitness[i] < 1.0) {
                //aux.add(indv);
            }else{
                //filtered.add(indv);
            }
        }

        return null;

    }



}
