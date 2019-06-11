
package unalcol.multiple.functions.ZDT;

import unalcol.multiple.functions.F;

public class ZDT2 extends F {
    
    private final int MIN = 0;
    private final int MAX = 1;
    private static int M;
    private static int n;
    private double[] sol;
    
    // --------------------------------------------------------------------- //
    
    public ZDT2() {
        ZDT2.M = 2;
        ZDT2.n = 30;
        sol = new double[n];
        for (int i = 0; i < n; i++)
            sol[i] = MIN + (MAX - MIN) * Math.random();
    }
    public ZDT2(int noVariables) {
        ZDT2.M = 2;
        ZDT2.n = noVariables;
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
