package unalcol.evolution.haea;
import unalcol.search.Goal;
import unalcol.search.RealValuedGoal;
import unalcol.Tagged;
import unalcol.sort.Order;
import unalcol.math.metric.*;

/**
 * <p>Title: DCHaeaReplacement</p>
 * <p>Description: The Deterministic Crowding Replacement Strategy for the HAEA Algorithm.</p>
 * <p>Copyright:    Copyright (c) 2010</p>
 * @author Jonatan Gomez
 * @version 1.0
 *
 */
public class DCHaeaReplacement<T,R> extends HaeaReplacement<T,R>{
    protected QuasiMetric<T> metric;

    public DCHaeaReplacement( HaeaOperators<T> operators, QuasiMetric<T> metric ){
        super( operators );
        this.metric = metric;
    }

    /**
     * Adds a subpopulation of parents and associated offsprings to the replacement strategy.
     * These method chooses between parents and offspring in order to define the individuals that
     * will be maintained into the next generation using the HAEA mechanism
     * @param parents Parents
     * @param children
     */
    @SuppressWarnings("unchecked")
	public Tagged<T>[] apply( Tagged<T>[] current, Tagged<T>[] next ){
		Goal<T,R> goal = goal();
		Order<R> order = goal.order();
        Tagged<T>[] buffer = new Tagged[current.length];
        int k=0;
        for( int i=0; i<current.length; i++){
            T parent = current[i].unwrap();
            int child = k;
            double d = metric.apply(parent, next[child].unwrap());
            k++;
            for(int h=1; h<operators.getSizeOffspring(i); h++){
                double d2 = metric.apply(parent, next[k].unwrap());
                if( d2 < d ){
                    child = k;
                    d = d2;
                }
                k++;
            }
            R qp = goal.apply(current[i]);
            R qc = goal.apply(next[child]);
            if(order.compare(qp,qc) < 0 ){
                operators.reward(i);
            } else {
                operators.punish(i);
            }
            if(order.compare(qp, qc) <= 0){
                buffer[i] = next[child];
            }else{
                buffer[i] = current[i];
            }
        }
        return buffer;
    }    
}