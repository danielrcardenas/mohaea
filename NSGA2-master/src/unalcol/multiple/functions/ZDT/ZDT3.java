
package unalcol.multiple.functions.ZDT;

import unalcol.multiple.functions.F;

public class ZDT3 extends F {
    
    private final int MIN = 0;
    private final int MAX = 1;
    private static int M;
    private static int n;
    private double[] sol;
    
    // --------------------------------------------------------------------- //
    
    public ZDT3() {
        ZDT3.M = 2;
        ZDT3.n = 30;
        sol = new double[n];
        for (int i = 0; i < n; i++)
            sol[i] = MIN + (MAX - MIN) * Math.random();
    }
    public ZDT3(int noVariables) {
        ZDT3.M = 2;
        ZDT3.n = noVariables;
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
