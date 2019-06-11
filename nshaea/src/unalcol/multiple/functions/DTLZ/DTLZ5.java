
package unalcol.multiple.functions.DTLZ;

import unalcol.multiple.functions.OptimizationMultipleFunction;
import unalcol.types.real.array.DoubleArray;

public class DTLZ5 extends OptimizationMultipleFunction<double []> {
    
    private final int MIN = 0;
    private final int MAX = 1;
    private static final int K = 10;
    private static int M;
    private static int n;
    private double[] sol;
    
    // --------------------------------------------------------------------- //
    
    public DTLZ5() {
        DTLZ5.M = 2;
        DTLZ5.n = M + K - 1;
        sol = new double[n];
        for (int i = 0; i < n; i++)
            sol[i] = MIN + (MAX - MIN) * Math.random();
    }
    public DTLZ5(int noObjectives) {
        DTLZ5.M = noObjectives;
        DTLZ5.n = M + K - 1;
        sol = new double[n];
        for (int i = 0; i < n; i++)
            sol[i] = MIN + (MAX - MIN) * Math.random();
    }
    public DTLZ5(double[] sol, int noObjectives) {
        DTLZ5.M = noObjectives;
        DTLZ5.n = M + K - 1;
        this.sol = sol;
    }

    // --------------------------------------------------------------------- //
    
    @Override
    public Double[] apply(double[] x ) {
        this.sol = x;
        Double[] f = new Double[M];
        double[] theta = new double[M-1];
        double g = 0.0;
        
        for (int i = M-1; i < n; i++)
            g += Math.pow((sol[i] - 0.5),2);
        
        double t = Math.PI / (4.0 * (1.0 + g));
        
        theta[0] = sol[0] * Math.PI / 2.0;
        for (int i = 1; i < M-1; i++)
            theta[i] = t * (1.0 + 2.0 * g * sol[i]);
        
        for (int i = 0; i < M; i++)
            f[i] = 1.0 + g;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M - (i + 1); j++)
                f[i] *= Math.cos(theta[j]);
            if (i != 0)
                f[i] *= Math.sin(theta[(M - (i + 1))]);
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
