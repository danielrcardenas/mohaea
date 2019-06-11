
package unalcol.multiple.functions.DTLZ;

import unalcol.multiple.functions.F;

public class DTLZ1 extends F { // not working right
    
    private final int MIN = 0;
    private final int MAX = 1;
    private static final int K = 5;
    private static int M;
    private static int n;
    private double[] sol;
    
    // --------------------------------------------------------------------- //
    
    public DTLZ1() {
        DTLZ1.M = 2;
        DTLZ1.n = M + K - 1;
        sol = new double[n];
        for (int i = 0; i < n; i++)
            sol[i] = MIN + (MAX - MIN) * Math.random();
    }
    public DTLZ1(int noObjectives) {
        DTLZ1.M = noObjectives;
        DTLZ1.n = M + K - 1;
        sol = new double[n];
        for (int i = 0; i < n; i++)
            sol[i] = MIN + (MAX - MIN) * Math.random();
    }
    public DTLZ1(double[] sol, int noObjectives) {
        DTLZ1.M = noObjectives;
        DTLZ1.n = M + K - 1;
        this.sol = sol;
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
        double g = 0.0;
        
        for (int i = M-1; i < n; i++)
            g += (sol[i] - 0.5) * (sol[i] - 0.5) - Math.cos(20.0 * Math.PI * (sol[i] - 0.5));
        g = 100 * (K + g);
        
        for (int i = 0; i < M; i++)
            f[i] = (1.0 + g) * 0.5;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M - (i + 1); j++)
                f[i] *= sol[j];
            if (i != 0)
                f[i] *= 1 - sol[(M - (i + 1))];
        }
        
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
