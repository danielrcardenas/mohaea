
package unalcol.multiple.functions.ZDT;

import unalcol.multiple.functions.F;

public class ZDT6 extends F {
    
    private final int MIN = 0;
    private final int MAX = 1;
    private static int M;
    private static int n;
    private double[] sol;
    
    // --------------------------------------------------------------------- //
    
    public ZDT6() {
        ZDT6.M = 2;
        ZDT6.n = 10;
        sol = new double[n];
        for (int i = 0; i < n; i++)
            sol[i] = MIN + (MAX - MIN) * Math.random();
    }
    public ZDT6(int noVariables) {
        ZDT6.M = 2;
        ZDT6.n = noVariables;
        sol = new double[n];
        for (int i = 0; i < n; i++)
            sol[i] = MIN + (MAX - MIN) * Math.random();
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
        f[0] = 1.0 - Math.exp((-4.0) * sol[0]) * Math.pow(Math.sin(6.0 * Math.PI * sol[0]), 6.0);
        double g = 0.0;
        for(int i = 1; i < sol.length; i++)
            g += sol[i];
        g = 1.0 + 9.0 * Math.pow(g/(n-1), 0.25);
        double h = 1.0 - Math.pow(f[0]/g, 2.0);
        f[1] = h;
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
    @Override
    public int getNVariables() {return n;}
    
}
