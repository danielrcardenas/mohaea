package unalcol.evolution.haea;

import unalcol.search.Goal;
import unalcol.search.GoalBased;
import unalcol.search.population.RealBasedPopulationSearch;
import unalcol.search.population.VariationReplacePopulationSearch;
import unalcol.search.selection.Selection;

/**
 * <p>Title: SPHaEa</p>
 * <p>Description:
 * <p>Copyright:    Copyright (c) 2019</p>
 * @author Daniel Rodríguez
 * @version 1.0
 *
 */

public class SPHaeaStep <T,R> extends VariationReplacePopulationSearch<T,R> implements RealBasedPopulationSearch<T,R> {
    /**
     * Constructor: Creates a Haea offspring generation strategy
     * @param mu Genetic operators used to evolve the solution
     * @param replacement Growing function
     * @param parent_selection Extra parent selection mechanism
     */
    public SPHaeaStep(int mu, Selection<T> parent_selection, SPHaeaReplacement<T> replacement) {
        super( mu, new HaeaVariation<T>(parent_selection, replacement.operators()), replacement);
    }

    /**
     * Constructor: Creates a Haea offspring generation strategy
     * @param operators Genetic operators used to evolve the solution
     * @param mu Growing function
     * @param parent_selection Extra parent selection mechanism
     */
    public SPHaeaStep(int mu, Selection<T> parent_selection, HaeaOperators<T> operators) {
        super(mu, new HaeaVariation<T>(parent_selection, operators ),
                new SPHaeaReplacement<T>( operators ) );
    }


    /**
     *
     * @param mu
     * @param variation
     * @param replacement
     */
    public SPHaeaStep(int mu, HaeaVariation<T> variation, SPHaeaReplacement<T> replacement ){
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
