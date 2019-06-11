
package moead;

import unalcol.multiple.functions.DTLZ.DTLZ5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MOEADDE {
    
    public static final int N = 150; // Population size - the number of subproblems
    public static final int T = 5; // Number of weight vectors in the neighborhood
    public static final int ITERATIONS = 3000;
    public static final double SIGMA = 0.5; // Probability that parent solutions
                                            // are selected from the neighborhood
    
    public static unalcol.multiple.functions.F func = new DTLZ5();
    
    // --------------------------------------------------------------------- //
    
    public void run() {
        
        // Step 1 - Initialization
        List<double[]> EP = new ArrayList<>();
        List<double[]> l = getLambdaDistribution();
        List<List<Integer>> B = closestWeightVectors(l);
        List<double[]> pop = new ArrayList<>();
        for (int i = 0; i < N; i++) pop.add(func.generate());
        double[] Z = initZ(pop);
        
        for (int it = 0; it < ITERATIONS; it++) {
        
        // Step 2 - Update
        for (int i = 0; i < N; i++) {
            // Step 2.1 - Reproduction
            double[] y = polynomialMutation(DEoperator(pop.get(i),
                    pop.get(B.get(i).get(new Random().nextInt(T))), 
                    pop.get(B.get(i).get(new Random().nextInt(T)))));
            // Step 2.2 - Improvment/Repair
            for (int k = 0; k < y.length; k++)
                if (y[k] < func.getMinValue() || y[k] > func.getMaxValue())
                    y[k] = func.getMinValue() + (func.getMaxValue() - func.getMinValue()) * Math.random();
            // Step 2.3 - Update of Z
            for (int j = 0; j < func.getNoObjectives(); j++) {
                func.set(y);
                double fitY = func.evaluate()[j];
                double fitZj = Z[j];
                if (fitZj > fitY)
                    Z[j] = fitY;
            }
            // Step 2.4 - Update of neighboring solutions
            for (int j : B.get(i))
                if (gte(y,l.get(j),Z) <=  gte(pop.get(j),l.get(j),Z))
                    pop.set(j, y);
            // Step 2.5 - Update of EP
            boolean isDominated = false;
            List<double[]> copy = new ArrayList<>(EP);
            for (double[] sol : copy) {
                if (dominates(y,sol))
                    EP.remove(sol);
                if (dominates(sol,y))
                    isDominated = true;
            }
            if (!isDominated) EP.add(y);
        }
        
        }
        
        // Step 3 - Print
        printFirstFrontFormated(pop);
    }
    
    // --------------------------------------------------------------------- //
    
    private List<double[]> getLambdaDistribution() {
        List<double[]> l = new ArrayList<>();
        for (int k = 0; k < N; k++) {
            double[] lambda = new double[2];
            lambda[0] = (double) k / (double) (N - 1);
            lambda[1] = 1 - lambda[0];
            l.add(lambda);
        }
        return l;
    }
    
    private List<List<Integer>> closestWeightVectors(List<double[]> l) {
        List<List<Integer>> B = new ArrayList<>();
        l.stream().map((wv) -> {
            double[] d = new double[l.size()];
            for (int i = 0; i < l.size(); i++)
                d[i] = d(wv,l.get(i));
            List<Integer> b = new ArrayList<>();
            for (int k = 0; k < T; k++) {
                double minDistance = Integer.MAX_VALUE;
                int minDistanceIndex = -1;
                for (int i=0; i<d.length; i++)
                    if (d[i] < minDistance) {
                        minDistance = d[i];
                        minDistanceIndex = i;
                    }
                d[minDistanceIndex] = Double.MAX_VALUE;
                b.add(minDistanceIndex);
            }
            return b;
        }).forEach((b) -> {
            B.add(b);
        });
        return B;
    }
    
    private final int Z_ITS = 100000; // 100k
    private double[] initZ() {
        double[] z = new double[func.getNoObjectives()];
        for (int i = 0; i < func.getNoObjectives(); i++) {
            double min = Double.MAX_VALUE;
            for (int j = 0; j < Z_ITS; j++) {
                func.set(func.generate());
                double[] fitness = func.evaluate();
                if (fitness[i] < min)
                    min = fitness[i];
            }
            z[i] = min;
        }
        return z;
    }
    private double[] initZ(List<double[]> P) {
        double[] z = new double[func.getNoObjectives()];
        for (int i = 0; i < func.getNoObjectives(); i++) {
            double min = Double.MAX_VALUE;
            for (int j = 0; j < P.size(); j++) {
                func.set(P.get(j));
                double[] fitness = func.evaluate();
                if (fitness[i] < min)
                    min = fitness[i];
            }
            z[i] = min;
        }
        return z;
    }
    
    private boolean dominates(double[] f, double[] g) {
        return f[0] <= g[0] && f[1] <= g[1] && (f[0] < g[0] || f[1] < g[1]);
    }
    
    private double gte(double[] x, double[] l, double[] z) { 
        func.set(x);
        double[] fit = func.evaluate();
        double max = Double.MIN_VALUE;
        for (int i = 0; i < fit.length; i++)
            if (l[i] * Math.abs(fit[i] * z[i]) > max)
                max = l[i] * Math.abs(fit[i] * z[i]);
        return max;
    }
    
    // --------------------------------------------------------------------- //
    
    private final double CR = 1.0;
    private final double F = 0.01;
    private double[] DEoperator(double[] xr1, double[] xr2, double[] xr3) {
        double[] y = new double[xr1.length];
        for (int i = 0; i < xr1.length; i++) {
            if (Math.random() < CR)
                y[i] = xr1[i] + F * (xr2[i] - xr3[i]);
            else
                y[i] = xr1[i];
        }
        return y;
    }
    
    private final double eta = 1.0;
    private final double pm = 0.6;
    private double[] polynomialMutation(double[] notY) {
        double[] y = new double[notY.length];
        for (int i = 0; i < notY.length; i++) {
            if (Math.random() < pm) {
                double rand = Math.random();
                double sigma;
                if (rand < 0.5) sigma = Math.pow(2 * rand, 1 / (eta + 1)) - 1;
                else sigma = 1 - Math.pow(2 - 2 * rand, 1 / (eta + 1));
                y[i] = notY[i] + sigma * (func.getMaxValue() - func.getMinValue());
            }
            else y[i] = notY[i];
        }
        return y;
    }
    
    // --------------------------------------------------------------------- //
    
    private double d(double[] x1, double[] x2) {
        double sum = 0.0;
        for (int i=0; i<x1.length; i++)
            sum += Math.pow(x1[i] - x2[i], 2.0);
        return Math.sqrt(sum);
    }
    
    private void printFirstFrontFormated(List<double[]> pop) {
        List<double[]> graphic = new ArrayList<>();
        for (int i = 0; i < func.getNoObjectives(); i++)
            graphic.add(new double[pop.size()]);
        for (int i = 0; i < pop.size(); i++) {
            func.set(pop.get(i));
            double[] fitness = func.evaluate();
            for (int j = 0; j < func.getNoObjectives(); j++)
                graphic.get(j)[i] = fitness[j];
        }
        System.out.println("Number of objectives: " + graphic.size());
        System.out.println("Population: " + pop.size());
        System.out.println("-------");
        for (int i = 0; i < graphic.size(); i++) {
            double[] f = graphic.get(i);
            System.out.print("f" + (i+1) + " <- c(");
            for (int j = 0; j < f.length; j++) {
                System.out.print(f[j]);
                if (j!=f.length-1) System.out.print(", ");
            }
            System.out.println(");");
        }
        System.out.println("-------\n");
    }
    
}
