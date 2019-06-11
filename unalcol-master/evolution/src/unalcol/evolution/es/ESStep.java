package unalcol.evolution.es;

import unalcol.Tagged;
import unalcol.search.population.RealBasedPopulationSearch;
import unalcol.search.population.VariationReplacePopulationSearch;
import unalcol.search.space.Space;
import unalcol.search.variation.Variation_1_1;
import unalcol.search.variation.Variation_n_1;

public class ESStep<T,P,R> extends VariationReplacePopulationSearch<T,R> implements RealBasedPopulationSearch<T,R>{
	protected Space<P> s_space;
	public ESStep(int mu, int lambda, int ro, 
			Variation_n_1<T> y_recombination, Variation_1_1<T> mutation, 
			Variation_n_1<P> s_recombination, Variation_1_1<P> s_mutation, Space<P> s_space,
       		ESReplacement<T> replacement ){
		super( 	mu, new ESVariation<T,P>(lambda, ro, y_recombination, mutation, s_recombination, s_mutation), 
				replacement);
		this.s_space = s_space;
	}

	public ESStep(int mu, int lambda, int ro, 
			Variation_n_1<T> y_recombination, Variation_1_1<T> mutation, 
			Variation_n_1<P> s_recombination, Variation_1_1<P> s_mutation, Space<P> s_space,
       		boolean plus_replacement ){
		this(	mu, lambda, ro, y_recombination, mutation, s_recombination, s_mutation, s_space, 
				plus_replacement? new PlusReplacement<T>(mu):new CommaReplacement<T>(mu) );
	}
	
	@Override
	public Tagged<T>[] init(Space<T> space) {
    	Tagged<T>[] pop = super.init(space);
    	for( int i=0; i<pop.length; i++ ){
    		pop[i].set(ESVariation.PARAMETERS_OPERATOR, s_space.pick() );
    	}
    	return pop;
	}
}