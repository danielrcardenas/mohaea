
package unalcol.multiple.functions.MOP;

import unalcol.multiple.functions.F;

public class MOP2 extends F {
    
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
    
    @Override
    public double[] generate() {
        double[] solution = new double[n];
        for (int i = 0; i < n; i++)
            solution[i] = MIN + (MAX - MIN) * Math.random();
        return solution;
    }
    
    // --------------------------------------------------------------------- //
    
    @Override
    public double[] evaluate() {
        double[] f = new double[M];
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
