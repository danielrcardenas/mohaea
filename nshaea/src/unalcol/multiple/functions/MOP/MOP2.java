
package unalcol.multiple.functions.MOP;

import unalcol.multiple.functions.OptimizationMultipleFunction;
import unalcol.types.real.array.DoubleArray;

public class MOP2 extends OptimizationMultipleFunction<double[]> {
    
    private final int MIN = -4;
    private final int MAX = 4;
    private static int M;
    private static int n;
    private double[] sol;
    
    // --------------------------------------------------------------------- //
    
    public MOP2() {
        MOP2.M = 2;
        MOP2.n = 3;
        sol = new double[n];
        for (int i = 0; i < n; i++) sol[i] = MIN + (MAX - MIN) * Math.random();
    }
    public MOP2(int noVariables) {
        MOP2.M = 2;
        MOP2.n = noVariables;
        sol = new double[n];
        for (int i = 0; i < n; i++) sol[i] = MIN + (MAX - MIN) * Math.random();
    }
    

    
    // --------------------------------------------------------------------- //
    
    @Override
    public Double[] apply(double[] x ) {
        this.sol = x;
        Double[] f = new Double[M];
        f[0] = this.MOP2f1(sol);
        f[1] = this.MOP2f2(sol);
        return f;
    }
    
    private double MOP2f1(double x[]) {
        double result = 1;
        double sum = 0;
        for (int i=0; i<x.length; i++)
            sum += Math.pow((x[i] - 1/Math.sqrt(x.length)),2);
        result -= Math.exp(-sum);
        return result;
    }
    public double MOP2f2(double x[]) {
        double result = 1;
        double sum = 0;
        for (int i=0; i<x.length; i++)
            sum += Math.pow((x[i] + 1/Math.sqrt(x.length)),2);
        result -= Math.exp(-sum);
        return result;
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
