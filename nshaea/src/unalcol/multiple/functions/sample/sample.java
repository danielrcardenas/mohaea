
package unalcol.multiple.functions.sample;

import unalcol.multiple.functions.OptimizationMultipleFunction;
import unalcol.types.real.array.DoubleArray;

public class sample extends OptimizationMultipleFunction<double[]> {
    
    private final int MIN = -10;
    private final int MAX = 10;
    private static int M;
    private static int n;
    private double[] sol;
    
    // --------------------------------------------------------------------- //
    
    public sample() {
        sample.M = 2;
        sample.n = 1;
        sol = new double[n];
        sol[0] = MIN + (MAX - MIN) * Math.random();
    }
    public sample(int noVariables) {
        sample.M = 2;
        sample.n = noVariables;
        sol = new double[n];
        sol[0] = MIN + (MAX - MIN) * Math.random();
    }
    

    
    // --------------------------------------------------------------------- //
    
    @Override
    public Double[] apply(double[] x ) {
        this.sol = x;
        Double[] f = new Double[M];
        f[0] = Math.pow(sol[0] + 2, 2.0) - 10;
        f[1] = Math.pow(sol[0] - 2, 2.0) + 20;
        return f;
    }
    
    // --------------------------------------------------------------------- //



    @Override
    public double[] getMinArrayValues() {
        return DoubleArray.create(n, MIN);
    }

    @Override
    public double[] getMaxArrayValues() {
        return DoubleArray.create(n, MAX);
    }

    
}
