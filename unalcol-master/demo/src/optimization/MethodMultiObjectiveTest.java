package optimization;

import unalcol.Tagged;
import unalcol.clone.DefaultClone;
import unalcol.descriptors.WriteDescriptors;
import unalcol.multiple.functions.OptimizationMultipleFunction;
import unalcol.multiple.search.population.MultiPopulationDescriptors;
import unalcol.multiple.search.solution.MultipleSolutionDescriptors;
import unalcol.multiple.search.solution.MultipleSolutionWrite;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.binary.BinarySpace;
import unalcol.optimization.binary.BitMutation;
import unalcol.optimization.binary.testbed.Deceptive;
import unalcol.optimization.integer.IntHyperCube;
import unalcol.optimization.integer.MutationIntArray;
import unalcol.optimization.integer.testbed.QueenFitness;
import unalcol.optimization.real.HyperCube;
import unalcol.optimization.real.mutation.IntensityMutation;
import unalcol.optimization.real.mutation.Mutation;
import unalcol.optimization.real.mutation.PermutationPick;
import unalcol.optimization.real.mutation.PickComponents;
import unalcol.optimization.real.testbed.Rastrigin;
import unalcol.random.raw.JavaGenerator;
import unalcol.random.real.RandDouble;
import unalcol.random.real.SimplestSymmetricPowerLawGenerator;
import unalcol.search.Search;
import unalcol.search.population.PopulationDescriptors;
import unalcol.search.solution.SolutionDescriptors;
import unalcol.search.solution.SolutionWrite;
import unalcol.search.space.Space;
import unalcol.services.Service;
import unalcol.services.ServicePool;
import unalcol.tracer.ConsoleTracer;
import unalcol.tracer.Tracer;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.integer.array.IntArray;
import unalcol.types.integer.array.IntArrayPlainWrite;
import unalcol.types.real.array.DoubleArray;
import unalcol.types.real.array.DoubleArrayPlainWrite;

public class MethodMultiObjectiveTest {

	// ******* Real space problem ******** //

	public static Space<double[]> real_space(OptimizationMultipleFunction function){
		// Search Space definitio
    	return new HyperCube( function.getMinArrayValues(), function.getMaxArrayValues() );
	}
	

	
	public static Mutation real_variation(){
    	// Variation definition
    	RandDouble random = new SimplestSymmetricPowerLawGenerator(); // It can be set to Gaussian or other symmetric number generator (centered in zero)
    	PickComponents pick = new PermutationPick(6); // It can be set to null if the mutation operator is applied to every component of the Tagged array
    	return new IntensityMutation( 0.1, random, pick );
	}
	
	public static void real_service(OptimizationMultipleFunction<double[]> function, Search<double[], Double[]> search){
        // Tracking the goal evaluations
		ServicePool service = new ServicePool();
		service.register(new JavaGenerator(), Object.class);      
		service.register(new DefaultClone(), Object.class);
		Tracer<Object> t = new ConsoleTracer<Object>();
		t.start();
		service.register(t, search);
        //service.register(new MultipleSolutionDescriptors<double[]>(function), Tagged.class);
        service.register(new DoubleArrayPlainWrite(',',false), double[].class);
        //service.register(new MultipleSolutionWrite<double[]>(function,true), Tagged.class);
		Service.set(service);
	}

	public static OptimizationFunction<int[]> queen_f(){
    	// Optimization Function
    	OptimizationFunction<int[]> function = new QueenFitness();
    	function.minimize(true);
    	return function;
	}
	
	public static MutationIntArray queen_variation(int DIM){
    	// Variation definition
    	return new MutationIntArray(DIM);
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void population_service( OptimizationMultipleFunction function ){
		ServicePool service = (ServicePool)Service.get();
		MultiPopulationDescriptors pd= new MultiPopulationDescriptors();
		pd.setGoal(function);
		service.register(pd, Tagged[].class);
		service.register(new WriteDescriptors<Tagged[]>(), Tagged[].class);
	}	
}