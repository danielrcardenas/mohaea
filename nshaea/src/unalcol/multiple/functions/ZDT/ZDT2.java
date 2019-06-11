
package unalcol.multiple.functions.ZDT;

import unalcol.multiple.functions.OptimizationMultipleFunction;
import unalcol.types.real.array.DoubleArray;

public class ZDT2 extends OptimizationMultipleFunction<double[]> {
    
    private final int MIN = 0;
    private final int MAX = 1;

    private double[] sol;
    
    // --------------------------------------------------------------------- //
    
    public ZDT2() {
        ZDT2.M = 2;
        ZDT2.n = 30;
    }


    // --------------------------------------------------------------------- //
    
    @Override
    public Double[] apply(double[] x ) {
        this.sol = x;
        Double[] f = new Double[M];
        f[0] = sol[0];
        double g = this.evalG();
        double h = this.evalH(f[0],g);
        f[1] = h * g;
        return f;
    }
    
    private double evalG() {
        double g = 0.0;
        for (int i = 1; i < sol.length; i++)
            g += sol[i];
        double constant = 9.0 / (sol.length - 1);
        g = constant * g;
        g = g + 1.0;
        return g;
    }
    
    private double evalH(double f, double g) {
        double h;
        h = 1.0 - Math.pow(f/g, 2.0);
        return h;
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
