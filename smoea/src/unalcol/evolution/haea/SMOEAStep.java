package unalcol.evolution.haea;

import unalcol.search.Goal;
import unalcol.search.GoalBased;
import unalcol.search.population.RealBasedPopulationSearch;
import unalcol.search.population.VariationReplacePopulationSearch;
import unalcol.search.selection.Selection;

public class SMOEAStep<T,R> extends VariationReplacePopulationSearch<T,R> implements RealBasedPopulationSearch<T,R> {
    public SMOEAStep(int mu, Selection<T> parent_selection, HaeaOperators<T> operators) {
        super(mu, new HaeaVariation<T>(parent_selection, operators ),
                new SPMOEAReplacement<T>( operators ) );
    }

    public SMOEAStep(int mu, Selection<T> parent_selection, HaeaOperators<T> operators, boolean steady) {
        super(mu, new HaeaVariation<T>(parent_selection, operators ),
                new SPMOEAReplacement<>( operators, steady ) );
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
