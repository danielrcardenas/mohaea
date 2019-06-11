package unalcol.evolution.haea;

import unalcol.Tagged;
import unalcol.TaggedManager;
import unalcol.Thing;
import unalcol.search.Goal;
import unalcol.search.GoalBased;
import unalcol.search.population.PopulationReplacement;
import unalcol.sort.Order;

import javax.swing.text.html.HTML;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * <p>Title: HaeaReplacement</p>
 *
 * <p>Description: The HAEA Replacement strategy</p>
 *
 * <p>Copyright: Copyright (c) 2017</p>
 *
 * @author Daniel Rodriguez
 * @version 1.0
 */
public class NSHaeaReplacement<T> extends Thing implements GoalBased<T,Double[]>, PopulationReplacement<T>, TaggedManager<T>{
    /**
     * Set of genetic operators that are used by CEA for evolving the Tagged chromosomes
     */
    protected HaeaOperators<T> operators = null;

    protected boolean steady = true;

    /**
     * Default constructor
     */
    public NSHaeaReplacement(HaeaOperators<T> operators){
       this.operators = operators;
    }

    /**
     * Default constructor
     */
    public NSHaeaReplacement(HaeaOperators<T> operators, boolean steady ){
       this.operators = operators;
       this.steady = steady;
    }
    
    
    public HaeaOperators<T> operators(){ return operators; }

	/**
	 * Adds a subpopulation of parents and associated offsprings to the replacement strategy.
	 * These method uses Non Dominated Sorting in order to define the individuals that
	 * will be maintained into the next generation using the HAEA mechanism
	 * @param current Parents
	 * @param next
	 */
	@Override
	public Tagged<T>[] apply( Tagged<T>[] current, Tagged<T>[] next ){
		//Tagged<T>[] buffer = (Tagged<T>[])new Tagged[current.length];//Resultado
        List<Tagged<T>> population = new ArrayList<>();
        for (Tagged i : current){
            population.add(i);
        }
        for (Tagged i : next){
            population.add(i);
        }//Unir la generaciones

        List<List<Tagged<T>>> fronts = fast_nondominated_sort(population);
        List<Tagged<T>> buffer = new ArrayList<>();
        for (List<Tagged<T>> front : fronts) {
            if (front.size() + buffer.size() > current.length) {
                buffer.addAll(crowding_distance_assignment(front,(current.length-buffer.size())));
                break;
            }
            else {
                buffer.addAll(front);
            }
        }
        int k=0;
        // Castigar operadores
        /**
         *
         */
        for( int i=0; i<current.length; i++){
            int n=operators.getSizeOffspring(i);
            int sel = k;
            int qs = buffer.indexOf(next[sel]);
            if (qs < 0) qs = Integer.MAX_VALUE;
            k+=n;
            /*for(int h=1; h<n; h++){
                int qk = buffer.indexOf(next[k]);
                if (qk<0) qk = Integer.MAX_VALUE;
                if(qs<qk){
                    sel = k;
                }
                k++;
            }*/
            int qi = buffer.indexOf(current[i]);
            if (qi<0) qi = Integer.MAX_VALUE;
            if( qi > qs)
                operators.reward(i);
            else
                operators.punish(i);

        }
        Tagged<T> [] result = buffer.toArray(new Tagged[buffer.size()]);
        return result;
    }

    public List<List<Tagged<T>>> fast_nondominated_sort(List<Tagged<T>> population){
        Goal<T,Double[]> goal = goal();
        Order<Double[]> order = goal.order();
        List<Tagged<T>> F1 = new ArrayList<>();
        List<List<Tagged<T>>> S = new ArrayList<>();
        List<Integer> n = new ArrayList<>();
        //First Phase
        for(Tagged<T> p:population){
            int np = 0;
            List<Tagged<T>> Sp = new ArrayList<>();
            for(Tagged<T> q:population){
                if (p != q) {
                    Double[] qs = goal.apply(p);
                    Double[] qi = goal.apply(q);
                    int compare = order.compare(qs, qi);
                    if( compare > 0) Sp.add(q); else if (compare<0) np +=1; // Si P domina a q entonces adiciona q a SP si Q domina a p np+=1

                }
            }
            if (np == 0) F1.add(p);
            S.add(Sp);//Lista de los Q que domina P
            n.add(np);//Cuantos dominan a P
        }
        List<Tagged<T>> F = new ArrayList<>(F1);
        List<List<Tagged<T>>> fronts = new ArrayList<>();
        fronts.add(F1);
        // Second phase
        while (!F.isEmpty()) {
            List<Tagged<T>> H = new ArrayList<>();
            for (Tagged<T> p : F) {
                for (Tagged q : S.get(population.indexOf(p))) {//Los dominados por P
                    int index_q = population.indexOf(q);
                    n.set(index_q, n.get(index_q)-1);
                    if (n.get(index_q) == 0)
                        H.add(q);
                }
            }
            if (!H.isEmpty()) fronts.add(H);
            F = H;
        }
        List<Tagged<T>> last_front = new ArrayList<>();
        for (int i=0; i<n.size(); i++)
            if (n.get(i) > 0)
                last_front.add(population.get(i));
        if (!last_front.isEmpty()) fronts.add(last_front);
        return fronts;
    }

    // Crowding distance assignment
   public List<Tagged<T>> crowding_distance_assignment(List<Tagged<T>> front, int size) {
        Goal<T,Double[]> goal = goal();
        Order<Double[]> order = goal.order();
        List<Double[]> fitness = new ArrayList<>();
        for (Tagged<T> sol : front) {
            fitness.add(goal.apply(sol));
        }
        List<Double> crowd = new ArrayList<>();

        for (int i = 0; i < front.size(); i++) {
            crowd.add(0.0);
        }

        for (int i=0; i<fitness.get(0).length; i++){
            double [] x = getColumn(fitness,i);
            double[] localCrowd = crowd(x);
            for (int j = 0 ; j < crowd.size(); j++)
                crowd.set(j,crowd.get(j) + localCrowd[j]);
        }

        List<Tagged<T>> new_front = new ArrayList<>();
        for (int k = 0; k < size; k++) {
            double max = 0.0;
            int index = -1;
            for (int i = 0; i < crowd.size(); i++) {
                if (crowd.get(i) > max) {
                    max = crowd.get(i);
                    index = i;
                }
            }
            if (index <0){
                System.out.println("Error: " + index);
            }
            new_front.add(front.get(index));
            crowd.set(index,Double.MIN_VALUE);
        }
        return new_front;
    }

    public double[] getColumn(List<Double[]> data, int column) {
        double [] result = new double[data.size()];
        for(int i =0; i<data.size();i++){
            result[i]= data.get(i)[column];
        }
        return result;
    }

    private double[] crowd(double[] fit) {
        double[] fit2 = new double[fit.length];
        List<Integer> mid = new ArrayList<>();
        for (int i = 0; i < fit.length; i++) {
            mid.add(i);
            fit2[i] = fit[i];
        }
        // Bubble sort
        boolean swapped = true;
        while(swapped) {
            swapped = false;
            for (int i = 1; i < fit2.length; i++) {
                if (fit2[i-1] > fit2[i]) {
                    swapped = true;
                    double temp = fit2[i-1];
                    fit2[i-1] = fit2[i];
                    fit2[i] = temp;
                    int temp2 = mid.get(i-1);
                    mid.set(i-1,mid.get(i));
                    mid.set(i,temp2);
                }
            }
        }
        // crowd
        double[] score = new double[fit2.length];
        score[0] = Double.MAX_VALUE;
        score[score.length-1] = Double.MAX_VALUE;
        for (int i = 1; i < fit2.length - 1; i++) {
            score[i] = fit2[i+1] - fit2[i-1];
        }
        // final crowd
        double[] crowd = new double[fit2.length];
        for (int i = 0; i < crowd.length; i++) {
            crowd[i] = score[mid.indexOf(i)];

        }
        return crowd;
    }
}