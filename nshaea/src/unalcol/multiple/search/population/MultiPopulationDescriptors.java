package unalcol.multiple.search.population;

import unalcol.Tagged;
import unalcol.descriptors.Descriptors;
import unalcol.multiple.search.solution.FileWriterDescriptor;
import unalcol.search.Goal;
import unalcol.search.GoalBased;
import unalcol.services.MicroService;

import java.util.ArrayList;
import java.util.List;

public class MultiPopulationDescriptors<T> extends MicroService<Tagged<T>[]> implements GoalBased<T,Double[]>, Descriptors<Tagged<T>[]> {
	@Override
	public double[] descriptors() {
		Goal<T,Double[]> goal = goal();
		Tagged<T>[] pop = caller();
		Double[][] quality = new Double[pop.length][];
        List<String> lines = new ArrayList<String>();
		for(int i=0; i<quality.length; i++ ) {
			quality[i] = goal.apply(pop[i]);
			lines.add(quality[i][0] +","+quality[i][1]);
		}
        FileWriterDescriptor.write(lines);
		double[] values = new double[6];
		values[0] = 0.0;
		values[1] = 0.0;
		values[2] = 0.0;
		values[3] = 0.0;
		values[4] = 0.0;
		values[5] = 0.0;
		return values;	}
}