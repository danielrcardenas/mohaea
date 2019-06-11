
package unalcol.multiple.functions.ZDT;

import unalcol.multiple.functions.OptimizationMultipleFunction;
import unalcol.types.real.array.DoubleArray;

public class ZDT3 extends OptimizationMultipleFunction<double[]> {
    
    private final int MIN = 0;
    private final int MAX = 1;

    private double[] sol;
    
    // --------------------------------------------------------------------- //
    
    public ZDT3() {
        ZDT3.M = 2;
        ZDT3.n = 30;

    }

    
    // --------------------------------------------------------------------- //
    
    @Override
    public Double[] apply(double[] x ) {
        this.sol = x;
        Double[] f = new Double[M];
        f[0] = sol[0];
        double g = 0;
        for (int i = 1; i < sol.length; i++)
            g += sol[i];
        g = 1 + 9 * g / (sol.length - 1);
        double h = 1 - Math.sqrt(f[0] / g) - f[0] / g * Math.sin(10 * Math.PI * f[0]);
        f[1] = g * h;
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
