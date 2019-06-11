package unalcol.multiple.functions.sample;

import unalcol.multiple.functions.OptimizationMultipleFunction;
import unalcol.types.real.array.DoubleArray;

public class Kursawe extends OptimizationMultipleFunction<double[]> {
    private final int MIN = -5;
    private final int MAX = 5;

    private double[] sol;

    // --------------------------------------------------------------------- //

    public Kursawe() {
        M = 2;
        n = 3;

    }


    // --------------------------------------------------------------------- //

    @Override
    public Double[] apply(double[] x ) {
        this.sol = x;
        Double[] f = new Double[M];
        double var1 = 0.0;
        for (int i = 0; i<2;i++){
            var1+= -10*Math.exp(-0.2*Math.sqrt(Math.pow(sol[i],2)+Math.pow(sol[i+1],2)));
        }
        double var2 = 0.0;
        for (int i = 0; i<3;i++){
            var2+= Math.pow(Math.abs(sol[i]),0.8)+ (5*Math.sin(Math.pow(sol[i],3)));
        }
        f[0] = var1;
        f[1] = var2;
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
