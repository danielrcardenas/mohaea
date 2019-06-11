package unalcol.evolution.haea;

import unalcol.descriptors.Descriptors;
import unalcol.services.MicroService;

public class HaeaStepDescriptors<T,R> extends MicroService<HaeaStep<T,R>> implements Descriptors<HaeaStep<T,R>> {
	@Override
	public double[] descriptors() {
		return (double[])Descriptors.create(caller().operators());
	}
}