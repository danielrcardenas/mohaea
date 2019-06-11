package unalcol.evolution.haea;

import unalcol.search.Goal;
import unalcol.search.GoalBased;
import unalcol.search.population.RealBasedPopulationSearch;
import unalcol.search.population.VariationReplacePopulationSearch;
import unalcol.search.selection.Selection;

/**
 * <p>Title: HAEA</p>
 * <p>Description: The Hybrid Adaptive Evolutionary Algorithm Offspring Generation strategy as 
 * proposed by Gomez in "Self Adaptation of Operator Rates in Evolutionary Algorithms",
 * Proceedings of Gecco 2004.</p>
 * <p>Copyright:    Copyright (c) 2010</p>
 * @author Jonatan Gomez
 * @version 1.0
 *
 */
public class HaeaStep<T,R> extends VariationReplacePopulationSearch<T,R> implements RealBasedPopulationSearch<T,R>{
    /**
     * Constructor: Creates a Haea offspring generation strategy
     * @param operators Genetic operators used to evolve the solution
     * @param grow Growing function
     * @param selection Extra parent selection mechanism
     */
    public HaeaStep(int mu, Selection<T> parent_selection, HaeaReplacement<T,R> replacement) {
    	super( mu, new HaeaVariation<T>(parent_selection, replacement.operators()), replacement);
    }

    /**
     * Constructor: Creates a Haea offspring generation strategy
     * @param operators Genetic operators used to evolve the solution
     * @param grow Growing function
     * @param selection Extra parent selection mechanism
     */
    public HaeaStep(int mu, Selection<T> parent_selection, HaeaOperators<T> operators) {
    	super(mu, new HaeaVariation<T>(parent_selection, operators ),
    			 new HaeaReplacement<T,R>( operators ) );
    }

    /**
     * Constructor: Creates a Haea offspring generation strategy
     * @param operators Genetic operators used to evolve the solution
     * @param grow Growing function
     * @param selection Extra parent selection mechanism
     */
    public HaeaStep(int mu, Selection<T> parent_selection, HaeaOperators<T> operators, boolean steady) {
    	super(mu, new HaeaVariation<T>(parent_selection, operators ),
    			 new HaeaReplacement<T,R>( operators, steady ) );
    }

    /**
     * Constructor: Creates a Haea offspring generation strategy
     * @param operators Genetic operators used to evolve the solution
     * @param grow Growing function
     * @param selection Extra parent selection mechanism
     */
    public HaeaStep(int mu, HaeaVariation<T> variation, HaeaReplacement<T,R> replacement ){
    	super( mu, variation, replacement);
    }

	
	public HaeaOperators<T> operators(){
		return ((HaeaVariation<T>)variation).operators();
	}
	
	@SuppressWarnings("unchecked")
	public void setGoal(Goal<T,R> goal ){ ((GoalBased<T,R>)replace).setGoal(goal); }


	@SuppressWarnings("unchecked")
	public Goal<T,R> goal(){
		return ((GoalBased<T,R>)replace).goal();
	}
	
}