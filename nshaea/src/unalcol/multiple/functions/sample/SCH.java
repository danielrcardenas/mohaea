package unalcol.multiple.functions.sample;

import unalcol.multiple.functions.OptimizationMultipleFunction;
import unalcol.types.real.array.DoubleArray;

public class SCH extends OptimizationMultipleFunction<double[]> {
    private final int MIN = -10000;
    private final int MAX = 10000;

    private double[] sol;

    // --------------------------------------------------------------------- //

    public SCH() {
        SCH.M = 2;
        SCH.n = 1;

    }


    // --------------------------------------------------------------------- //

    @Override
    public Double[] apply(double[] x ) {
        this.sol = x;
        Double[] f = new Double[M];
        f[0] = Math.pow(sol[0],2);
        f[1] = Math.pow((sol[0]-2),2);
        return f;
    }

    // --------------------------------------------------------------------- //
    @Override
    public boolean deterministic(){ return false; }



    @Override
    public double[] getMinArrayValues() {
        return DoubleArray.create(n, MIN);
    }

    @Override
    public double[] getMaxArrayValues() {
        return DoubleArray.create(n, MAX);
    }
}
