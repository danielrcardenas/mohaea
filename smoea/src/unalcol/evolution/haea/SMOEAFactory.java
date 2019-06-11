package unalcol.evolution.haea;

import unalcol.Tagged;
import unalcol.algorithm.iterative.ForLoopCondition;
import unalcol.math.logic.Predicate;
import unalcol.search.population.IterativePopulationSearch;
import unalcol.search.population.PopulationSearch;
import unalcol.search.selection.Selection;

public class SMOEAFactory<T,R> {

    public PopulationSearch<T,R> HAEA(SMOEAStep<T,R> step, Predicate<Tagged<T>[]> tC ){
        return new IterativePopulationSearch<T,R>( step, tC );
    }

    public PopulationSearch<T,R> HAEA(int mu, HaeaOperators<T> operators, Selection<T> selection, Predicate<Tagged<T>[]> tC ){
        return HAEA( new SMOEAStep<T,R>(mu,selection,operators), tC );
    }

    public PopulationSearch<T,R> HAEA( int mu, HaeaOperators<T> operators, Selection<T> selection, int MAXITERS ){
        return HAEA( mu, operators, selection, new ForLoopCondition<Tagged<T>[]>(MAXITERS) );
    }

    public PopulationSearch<T,R> HAEA( SMOEAStep<T,R> step, int MAXITERS ){
        return HAEA( step, new ForLoopCondition<Tagged<T>[]>(MAXITERS) );
    }

    public PopulationSearch<T,R> HAEA_Generational( int mu, HaeaOperators<T> operators, Selection<T> selection, int MAXITERS ){
        return HAEA( new SMOEAStep<T,R>(mu,selection, operators, false), MAXITERS );
    }
}
