package evolution;

import optimization.MethodMultiObjectiveTest;
import unalcol.Tagged;
import unalcol.evolution.haea.*;
import unalcol.multiple.functions.OptimizationMultipleFunction;
import unalcol.multiple.functions.ZDT.ZDT1;
import unalcol.multiple.functions.sample.Fonseca;
import unalcol.multiple.functions.sample.SCH;
import unalcol.multiple.search.solution.ExperimentInfo;
import unalcol.optimization.real.mutation.Mutation;
import unalcol.optimization.real.xover.LinearXOver;
import unalcol.optimization.real.xover.RealArityTwo;
import unalcol.search.population.PopulationSearch;
import unalcol.search.selection.Tournament;
import unalcol.search.space.Space;
import unalcol.services.Service;
import unalcol.services.ServicePool;

public class SMOEATest {


    public static void haea_service() {
        ServicePool service = (ServicePool) Service.get();
        service.register(new WriteHaeaStep(), HaeaStep.class);
        service.register(new SimpleHaeaOperatorsDescriptor<double[]>(), HaeaOperators.class);
    }

    public static void real() {
        // Search space
        OptimizationMultipleFunction function = new ZDT1();
        Space<double[]> space = MethodMultiObjectiveTest.real_space(function);
        // Variation definition
        Mutation mutation = MethodMultiObjectiveTest.real_variation();
        RealArityTwo xover = new LinearXOver();
        HaeaOperators<double[]> operators = new SimpleHaeaOperators<double[]>(mutation, xover);
        int POPSIZE = 100;
        int MAXITERS = 250;
        SMOEAFactory<double[], Double[]> factory = new SMOEAFactory();
        PopulationSearch<double[], Double[]> search =
                factory.HAEA(POPSIZE, operators, new Tournament<double[], Double[]>(function, 4), MAXITERS);

        search.setGoal(function);
        // Services
        MethodMultiObjectiveTest.real_service(function, search);
        MethodMultiObjectiveTest.population_service(function);
        haea_service();
// Apply the search method
        search.solve(space);

    }

    public static void main(String[] args) {
        int experiments = 30;
        String name = "_zdt1_";
        ExperimentInfo ex = ExperimentInfo.getInstance();
        ex.setName(name);
        for (int i = 0; i<experiments; i++) {
            ex.setIteration(i+1);
            real(); // Uncomment if testing real valued functions
        }
    }
}
