package unalcol.evolution.haea;

import unalcol.Tagged;
import unalcol.algorithm.iterative.ForLoopCondition;
import unalcol.evolution.ga.GAStep;
import unalcol.math.logic.Predicate;
import unalcol.search.Goal;
import unalcol.search.population.IterativePopulationSearch;
import unalcol.search.population.PopulationSearch;
import unalcol.search.selection.Selection;
import unalcol.search.selection.Tournament;
import unalcol.search.variation.Variation_1_1;
import unalcol.search.variation.Variation_2_2;

// TODO: validar si esta clase se puede integrar con el se NSHAEA Factory

public class SPHaeaFactory<T,R> {

    public PopulationSearch<T,R> generational_ga(
            int mu, Selection<T> parent_selection,
            Variation_1_1<T> mutation,
            Variation_2_2<T> xover, double xover_probability,
            Predicate<Tagged<T>[]> tC ){
        return new IterativePopulationSearch<T,R>(
                new GAStep<T,R>( mu, parent_selection, mutation, xover, xover_probability, true),
                tC );
    }

    public PopulationSearch<T,R>	generational_ga(
            int mu, Selection<T> parent_selection,
            Variation_1_1<T> mutation,
            Variation_2_2<T> xover, double xover_probability,
            int MAXITERS ){
        return generational_ga(
                mu, parent_selection, mutation, xover, xover_probability,
                new ForLoopCondition<Tagged<T>[]>(MAXITERS) );
    }

    public PopulationSearch<T,R>	generational_ga(
            int mu, Goal<T,Double> goal,
            Variation_1_1<T> mutation,
            Variation_2_2<T> xover, double xover_probability,
            int MAXITERS ){
        return generational_ga(
                mu, new Tournament<T,Double>(goal, 4), mutation, xover, xover_probability,
                MAXITERS );
    }

    //Steady State Genetic Algorithm factory (Uses parents and offsprings in replacement)

    public PopulationSearch<T,R>	steady_ga(
            int mu, Selection<T> parent_selection,
            Variation_1_1<T> mutation,
            Variation_2_2<T> xover, double xover_probability,
            Predicate<Tagged<T>[]> tC ){
        return new IterativePopulationSearch<T,R>(
                new GAStep<T,R>( mu, parent_selection, mutation, xover, xover_probability, false),
                tC );
    }

    public PopulationSearch<T,R>	steady_ga(
            int mu, Selection<T> parent_selection,
            Variation_1_1<T> mutation,
            Variation_2_2<T> xover, double xover_probability,
            int MAXITERS ){
        return steady_ga(
                mu, parent_selection, mutation, xover, xover_probability,
                new ForLoopCondition<Tagged<T>[]>(MAXITERS) );
    }

    public PopulationSearch<T,R>	steady_ga(
            int mu, Goal<T,Double> goal,
            Variation_1_1<T> mutation,
            Variation_2_2<T> xover, double xover_probability,
            int MAXITERS ){
        return steady_ga(
                mu, new Tournament<T,Double>(goal, 4), mutation, xover, xover_probability,
                MAXITERS );
    }

    // HAEA Factory

    public PopulationSearch<T,R> HAEA( SPHaeaStep<T,R> step, Predicate<Tagged<T>[]> tC ){
        return new IterativePopulationSearch<T,R>( step, tC );
    }

    public PopulationSearch<T,R> HAEA( int mu, HaeaOperators<T> operators, Selection<T> selection, Predicate<Tagged<T>[]> tC ){
        return HAEA( new SPHaeaStep<T,R>(mu,selection,operators), tC );
    }

    public PopulationSearch<T,R> HAEA( int mu, HaeaOperators<T> operators, Selection<T> selection, int MAXITERS ){
        return HAEA( mu, operators, selection, new ForLoopCondition<Tagged<T>[]>(MAXITERS) );
    }

}
