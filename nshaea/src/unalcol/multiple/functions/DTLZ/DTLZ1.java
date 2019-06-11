
package unalcol.multiple.functions.DTLZ;

import unalcol.multiple.functions.OptimizationMultipleFunction;
import unalcol.types.real.array.DoubleArray;

public class DTLZ1 extends OptimizationMultipleFunction<double[]> { // not working right
    
    private final int MIN = 0;
    private final int MAX = 1;
    private static final int K = 5;

    private double[] sol;
    
    // --------------------------------------------------------------------- //
    
    public DTLZ1() {
        DTLZ1.M = 2;
        DTLZ1.n = M + K - 1;

    }

    

    // --------------------------------------------------------------------- //
    
    @Override
    public Double[] apply(double[] x ) {
        this.sol = x;
        Double[] f = new Double[M];
        double g = 0.0;
        
        for (int i = M-1; i < n; i++)
            g += (sol[i] - 0.5) * (sol[i] - 0.5) - Math.cos(20.0 * Math.PI * (sol[i] - 0.5));
        g = 100 * (K + g);
        
        for (int i = 0; i < M; i++)
            f[i] = (1.0 + g) * 0.5;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M - (i + 1); j++)
                f[i] *= sol[j];
            if (i != 0)
                f[i] *= 1 - sol[(M - (i + 1))];
        }
        
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
