package unalcol.evolution.ga;

import unalcol.search.population.Generational;
import unalcol.search.population.PopulationReplacement;
import unalcol.search.population.RealBasedPopulationSearch;
import unalcol.search.population.TotalSelectionReplacement;
import unalcol.search.population.VariationReplacePopulationSearch;
import unalcol.search.variation.Variation_1_1;
import unalcol.search.variation.Variation_2_2;
import unalcol.search.selection.Selection;

/**
 * <p>Title: GeneticAlgorithm</p>
 *
 * <p>Description: The Genetic Algorithm evolutionary transformation</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * @author Jonatan Gomez
 * @version 1.0
 */
public class GAStep<T,R> extends VariationReplacePopulationSearch<T,R> implements RealBasedPopulationSearch<T,R>{
	protected Selection<T> selection;
	protected GAVariation<T> variation;
	
    public GAStep( int mu, Selection<T> selection,
    		Variation_1_1<T> mutation, Variation_2_2<T> xover,
            double probability, PopulationReplacement<T> replace ) {
    	super( mu, new GAVariation<T>(selection, mutation, xover, probability), replace);
    } 
    
    public GAStep( int mu, Selection<T> selection,
    		Variation_1_1<T> mutation, Variation_2_2<T> xover,
            double probability, boolean generational ) {
    	super( 	mu, new GAVariation<T>(selection, mutation, xover, probability), 
    			generational?new Generational<T>():new TotalSelectionReplacement<T>());
    } 
}