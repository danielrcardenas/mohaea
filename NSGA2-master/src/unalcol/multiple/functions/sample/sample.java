
package unalcol.multiple.functions.sample;

import unalcol.multiple.functions.F;

public class sample extends F {
    
    private final int MIN = -10;
    private final int MAX = 10;
    private static int M;
    private static int n;
    private double[] sol;
    
    // --------------------------------------------------------------------- //
    
    public sample() {
        sample.M = 2;
        sample.n = 1;
        sol = new double[n];
        sol[0] = MIN + (MAX - MIN) * Math.random();
    }
    public sample(int noVariables) {
        sample.M = 2;
        sample.n = noVariables;
        sol = new double[n];
        sol[0] = MIN + (MAX - MIN) * Math.random();
    }
    
    @Override
    public double[] generate() {
        double[] solution = new double[n];
        solution[0] = MIN + (MAX - MIN) * Math.random();
        return solution;
    }
    
    // --------------------------------------------------------------------- //
    
    @Override
    public double[] evaluate() {
        double[] f = new double[M];
        f[0] = Math.pow(sol[0] + 2, 2.0) - 10;
        f[1] = Math.pow(sol[0] - 2, 2.0) + 20;
        return f;
    }
    
    // --------------------------------------------------------------------- //
    
    @Override
    public double[] get() { return this.sol; }
    @Override
    public void set(double[] sol) { this.sol = sol; }
    @Override
    public int getMinValue() { return this.MIN; }
    @Override
    public int getMaxValue() { return this.MAX; }
    @Override
    public int getNoObjectives() { return M; }
    
}
