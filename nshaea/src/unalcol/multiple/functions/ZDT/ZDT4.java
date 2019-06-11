
package unalcol.multiple.functions.ZDT;

import unalcol.multiple.functions.OptimizationMultipleFunction;
import unalcol.types.real.array.DoubleArray;

public class ZDT4 extends OptimizationMultipleFunction<double[]> {

    // Not working right
    //  - because the difference of intervals on different values


    //No funciona bien porque las variables varian en diferentes dominios
    private final int MIN = -5;
    private final int MAX = 5;
    private double[] sol;


    // --------------------------------------------------------------------- //
    
    public ZDT4() {
        ZDT4.M = 2;
        ZDT4.n = 10;

    }


    
    // --------------------------------------------------------------------- //
    
    @Override
    public Double[] apply(double[] x ) {
        this.sol = x;
        Double[] f = new Double[M];
        f[0] = sol[0];
        double g = this.evalG();
        f[1] = g * (1 - Math.sqrt(f[0] / g));
        return f;
    }
    
    private double evalG() {
        double g = 1.0 + 10 * (n - 1);
        for (int i = 1; i < sol.length; i++)
            g += Math.pow(sol[i],2.0) - (10.0 * Math.cos(4.0 * Math.PI * sol[i]));
        return g;
    }
    @Override
    public boolean deterministic(){ return false; }


    // --------------------------------------------------------------------- //
    


    @Override
    public double[] getMinArrayValues() {

        double [] minArray =  DoubleArray.create(n-1, MIN);
        double [] result = new double[n];
        result [0] = 0.0;
        for(int i = 1; i<n; i++){
            result[i] = minArray[i-1];
        }
        return result;
    }

    @Override
    public double[] getMaxArrayValues() {
        double [] minArray =  DoubleArray.create(n-1, MAX);
        double [] result = new double[n];
        result[0] = 1.0;
        for(int i = 1; i<n; i++){
            result[i] = minArray[i-1];
        }
        return result;
    }

}
