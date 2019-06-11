package unalcol.search;

import unalcol.math.metric.Distance;
import unalcol.math.metric.DistanceOrder;
import unalcol.sort.Order;

public abstract class DistanceToGoal<T,R> implements Goal<T,R>{
    protected Order<R> order;
    
    public DistanceToGoal( R goal_value, Distance<R> distance ){
    	order = new DistanceOrder<R>(distance, goal_value);
    }   
    
    @Override
    public Order<R> order(){ return order; }    
}
