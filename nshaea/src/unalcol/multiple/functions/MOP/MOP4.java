
package unalcol.multiple.functions.MOP;

import unalcol.multiple.functions.OptimizationMultipleFunction;
import unalcol.types.real.array.DoubleArray;

public class MOP4 extends OptimizationMultipleFunction<double[]> {
    
    private final int MIN = -5;
    private final int MAX = 5;
    private static int M;
    private static int n;
    private double[] sol;
    
    // --------------------------------------------------------------------- //
    
    public MOP4() {
        MOP4.M = 2;
        MOP4.n = 3;
        sol = new double[n];
        for (int i = 0; i < n; i++) sol[i] = MIN + (MAX - MIN) * Math.random();
    }
    public MOP4(int noVariables) {
        MOP4.M = 2;
        MOP4.n = noVariables;
        sol = new double[n];
        for (int i = 0; i < n; i++) sol[i] = MIN + (MAX - MIN) * Math.random();
    }
    

    public double[] generate() {
        double[] solution = new double[n];
        for (int i = 0; i < n; i++)
            solution[i] = MIN + (MAX - MIN) * Math.random();
        return solution;
    }
    
    // --------------------------------------------------------------------- //
    
    @Override
    public Double[] apply(double[] x ) {
        this.sol = x;
        Double[] f = new Double[M];
        f[0] = this.MOP4f1(sol);
        f[1] = this.MOP4f2(sol);
        return f;
    }
    
    private double MOP4f1(double x[]) {
        double result = 0;
        for (int i=0; i<x.length-1; i++)
            result += -10.0 * Math.exp(-0.2 * Math.sqrt(Math.pow(x[i],2) + Math.pow(x[i+1],2)));
        return result;
    }
    public double MOP4f2(double x[]) {
        double result = 0;
        for (int i=0; i<x.length; i++)
            result += Math.pow(Math.abs(x[i]),0.8) + 5 * Math.pow(Math.sin(x[i]),3.0);
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
