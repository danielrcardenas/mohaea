package unalcol.multiple.functions.sample;

import unalcol.multiple.functions.OptimizationMultipleFunction;
import unalcol.types.real.array.DoubleArray;

public class Fonseca extends OptimizationMultipleFunction<double[]> {
    private final int MIN = -4;
    private final int MAX = 4;

    private double[] sol;

    // --------------------------------------------------------------------- //

    public Fonseca() {
        Fonseca.M = 2;
        Fonseca.n = 3;

    }


    // --------------------------------------------------------------------- //

    @Override
    public Double[] apply(double[] x ) {
        this.sol = x;
        Double[] f = new Double[M];
        double var1 = 0.0;
        for (int i = 0; i<Fonseca.n;i++){
            var1+= Math.pow(sol[i]- (1/Math.sqrt(3.0)),2);
        }
        double var2 = 0.0;
        for (int i = 0; i<3;i++){
            var2+= Math.pow(sol[i] + (1/Math.sqrt(3.0)),2);
        }
        f[0] = 1 -Math.exp(-var1);
        f[1] = 1 -Math.exp(-var2);
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
