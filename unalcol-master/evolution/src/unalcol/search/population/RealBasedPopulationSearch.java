package unalcol.search.population;

import unalcol.Tagged;
import unalcol.search.Goal;
import unalcol.search.RealValuedGoal;
import unalcol.sort.Order;

public interface RealBasedPopulationSearch<T,R> extends PopulationSearch<T, R> {
	@Override
	public default Tagged<T> pick(Tagged<T>[] pop) {
		Goal<T,R> goal = goal();
		Order<R> order = goal.order();
		int k=0;
		R q = goal.apply(pop[0]);
		for( int i=1; i<pop.length; i++ ){
			R qi = goal.apply(pop[i]);
			if( order.compare(q, qi) < 0 ){
				k=i;
				q=qi;
			}
		}
		return pop[k];
	}
}
