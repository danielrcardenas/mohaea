package unalcol.multiple.search.solution;

import unalcol.Tagged;
import unalcol.descriptors.Descriptors;
import unalcol.descriptors.MultipleDescriptors;
import unalcol.search.Goal;
import unalcol.services.MicroService;

public class MultipleSolutionDescriptors<T> extends MicroService<Tagged<T>> implements MultipleDescriptors<Tagged<T>> {
	protected Goal<T,Double[]> goal;

	public MultipleSolutionDescriptors(Goal<T,Double[]> goal ) { this.goal = goal; }

	@Override
	public Double[][] descriptors(){
		return new Double[][]{goal.apply(caller())};
	}
}
