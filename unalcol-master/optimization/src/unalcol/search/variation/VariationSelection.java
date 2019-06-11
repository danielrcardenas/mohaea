package unalcol.search.variation;

import unalcol.Tagged;
import unalcol.search.selection.Selection;
import unalcol.services.MicroService;

public class VariationSelection<T> extends MicroService<T> implements Variation<T>{
	protected int lambda;
	protected Variation<T> variation;
	protected Selection<T> selection;

	public VariationSelection( int lambda, Variation<T> variation, Selection<T> selection ){
		this.lambda = lambda;
		this.variation = variation;
		this.selection = selection;
	}
	
	@SuppressWarnings("unchecked")
	public Tagged<T>[] apply( Tagged<T>... pop ){
    	return selection.pick(lambda, variation.apply(pop));
	}
    
	@Override
	public int range_arity(){
    	return lambda;
	}
}