
package unalcol.multiple.functions.ZDT;

import unalcol.multiple.functions.OptimizationMultipleFunction;
import unalcol.types.real.array.DoubleArray;

public class ZDT6 extends OptimizationMultipleFunction<double []> {
    
    private final int MIN = 0;
    private final int MAX = 1;

    private double[] sol;
    
    // --------------------------------------------------------------------- //
    
    public ZDT6() {
        ZDT6.M = 2;
        ZDT6.n = 10;

    }


    public double[] generate() {
        double[] solution = new double[n];
        for (int i = 0; i < n; i++)
            solution[i] = MIN + (MAX - MIN) * Math.random();
        return solution;
    }
    
    // --------------------------------------------------------------------- //
    public Double[] apply(double[] x ) {
        this.sol = x;
        Double[] f = new Double[M];
        f[0] = 1.0 - Math.exp((-4.0) * sol[0]) * Math.pow(Math.sin(6.0 * Math.PI * sol[0]), 6.0);
        double g = 0.0;
        for(int i = 1; i < sol.length; i++)
            g += sol[i];
        g = 1.0 + 9.0 * Math.pow(g/(n-1), 0.25);
        double h = 1.0 - Math.pow(f[0]/g, 2.0);
        f[1] = h*g;
        return f;
    }
    @Override
    public boolean deterministic(){ return false; }
    
    // --------------------------------------------------------------------- //
    


    public int getNoObjectives() { return M; }

    @Override
    public double[] getMinArrayValues() {
        return DoubleArray.create(n, MIN);
    }

    @Override
    public double[] getMaxArrayValues() {
        return DoubleArray.create(n, MAX);
    }


}
