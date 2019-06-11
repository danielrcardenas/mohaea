
package unalcol.multiple.functions.ZDT;

import unalcol.multiple.functions.F;

public class ZDT4 extends F {

    // Not working right
    //  - because the difference of intervals on different values
    
    private final int MIN = 0;
    private final int MAX = 1;
    private static int M;
    private static int n;
    private double[] sol;
    
    // --------------------------------------------------------------------- //
    
    public ZDT4() {
        ZDT4.M = 2;
        ZDT4.n = 10;
        sol = new double[n];
        sol[0] = Math.random();
        for (int i = 1; i < n; i++)
            sol[i] = MIN + (MAX - MIN) * Math.random();
    }
    public ZDT4(int noVariables) {
        ZDT4.M = 2;
        ZDT4.n = noVariables;
        sol = new double[n];
        sol[0] = Math.random();
        for (int i = 1; i < n; i++)
            sol[i] = MIN + (MAX - MIN) * Math.random();
    }
    
    @Override
    public double[] generate() {
        double[] solution = new double[n];
        solution[0] = Math.random();
        for (int i = 1; i < n; i++)
            solution[i] = MIN + (MAX - MIN) * Math.random();
        return solution;
    }
    
    // --------------------------------------------------------------------- //
    
    @Override
    public double[] evaluate() {
        double[] f = new double[M];
        f[0] = sol[0];
        double g = this.evalG();
        f[1] = g * (1 - Math.pow(f[0] / g, 2.0));
        return f;
    }
    
    private double evalG() {
        double g = 0.0;
        for (int i = 1; i < sol.length; i++)
            g += Math.pow(sol[i],2.0) - 10.0 * Math.cos(4.0 * Math.PI * sol[i]);
        g += 1.0 + 10 * (sol.length - 1);
        return g;
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
