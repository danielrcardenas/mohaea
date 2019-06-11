
package moead;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import metrics.Hypervolume;
import unalcol.multiple.functions.DTLZ.DTLZ5;
import unalcol.multiple.functions.MOP.MOP2;
import unalcol.multiple.functions.MOP.MOP4;
import unalcol.multiple.functions.ZDT.ZDT1;
import unalcol.multiple.functions.ZDT.ZDT2;
import unalcol.multiple.functions.ZDT.ZDT3;
import unalcol.multiple.functions.ZDT.ZDT6;

public class MOEAD {
    
    public List<double[]> pop = new ArrayList<>();
    public final int N; // Population size - the number of subproblems
    public final int T = 5; // Number of weight vectors in the neighborhood
    public final int ITERATIONS;
    public final double SIGMA = 0.7; // Probability that parent solutions
                                     // are selected from the neighborhood
    
    public static unalcol.multiple.functions.F func = new MOP2();
    
    // --------------------------------------------------------------------- //
    
    public void run() {
        
        // Step 1 - Initialization
        List<double[]> l = getLambdaDistribution();
        List<List<Integer>> B = closestWeightVectors(l);
        for (int i = 0; i < N; i++)
            pop.add(func.generate());
        double[] Z = initZ(pop);
        
        for (int it = 0; it < ITERATIONS; it++) {
        
        // Step 2 - Update
        for (int i = 0; i < N; i++) {
            // Step 2.1 - Selection of Mating/Update Range
            double rand = Math.random();
            List<Integer> P = new ArrayList<>();
            if (rand < SIGMA)
                P = B.get(i);
            else {
                for (int j = 0; j < N; j++)
                    P.add(j);
            }
            // Step 2.2 - Reproduction
            double[] y = polynomialMutation(DEoperator(pop.get(i),
                    pop.get(P.get((new Random()).nextInt(P.size()))), 
                    pop.get(P.get((new Random()).nextInt(P.size())))));
            // Step 2.3 - Repair
            for (int k = 0; k < y.length; k++)
                if (y[k] < func.getMinValue() || y[k] > func.getMaxValue())
                    y[k] = func.getMinValue() + (func.getMaxValue() - func.getMinValue()) * Math.random();
            // Step 2.4 - Update of Z
            for (int j = 0; j < func.getNoObjectives(); j++) {
                func.set(y);
                double fitY = func.evaluate()[j];
                double fitZj = Z[j];
                if (fitZj > fitY)
                    Z[j] = fitY;
            }
            // Step 2.5 - Update of solutions
            for (int j : P)
                if (gte(y,l.get(j),Z) <=  gte(pop.get(j),l.get(j),Z)) {
                    pop.set(j, y); break; }
        }
        
        }
        
        // Step 3 - Print
//        printPopulation(pop);
    }
    
    public String[] getFitness() {
        List<double[]> graphic = new ArrayList<>();
        for (int i = 0; i < func.getNoObjectives(); i++)
            graphic.add(new double[pop.size()]);
        for (int i = 0; i < pop.size(); i++) {
            func.set(pop.get(i));
            double[] fitness = func.evaluate();
            for (int j = 0; j < func.getNoObjectives(); j++)
                graphic.get(j)[i] = fitness[j];
        }
        String[] result = new String[graphic.size()];
        for (int i = 0; i < graphic.size(); i++) {
            String r = "";
            double[] f = graphic.get(i);
            r += "f" + (i+1) + " <- c(";
            for (int j = 0; j < f.length; j++) {
                r += f[j];
                if (j!=f.length-1) r += ", ";
            }
            r += ");";
            result[i] = r;
        }
        return result;
    }
    
    public double getDelta() {
        double DELTA = 0.0;
        double davg = 0.0;
        for (int k = 1; k < pop.size(); k++)
            davg += d(pop.get(k-1), pop.get(k));
        davg = davg / (pop.size() - 1);
        for (int k = 1; k < pop.size(); k++)
            DELTA += Math.abs(d(pop.get(k-1),pop.get(k)) - davg) / N;
        return DELTA;
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
    
    private final double CR = 1;
    private final double F = 0.001;
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
    
    private final double eta = 10.0;
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
    
    private void printPopulation(List<double[]> pop) {
        for (double[] p : pop) {
            for (int i = 0; i < p.length; i++)
                System.out.print(p[i] + " ");
            System.out.println();
        }
        System.out.println("\n\n");
    }
    
    private void printHypervolume(List<double[]> pop) {
        List<double[]> front = new ArrayList<>();
        for (int i = 0; i < func.getNoObjectives(); i++)
            front.add(new double[pop.size()]);
        for (int i = 0; i < pop.size(); i++) {
            func.set(pop.get(i));
            double[] fitness = func.evaluate();
            for (int j = 0; j < func.getNoObjectives(); j++)
                front.get(j)[i] = fitness[j];
        }
        double hypervolume = new Hypervolume(func).calculateHypervolume(front, front.get(0).length, front.size());
        System.out.println("HYPERVOLUME VALUE: " + hypervolume);
        System.out.println("-------\n");
    }
    
    private void printDelta(List<double[]> pop) {
        double DELTA = 0.0;
        double davg = 0.0;
        for (int k = 1; k < pop.size(); k++)
            davg += d(pop.get(k-1), pop.get(k));
        davg = davg / (pop.size() - 1);
        for (int k = 1; k < pop.size(); k++)
            DELTA += Math.abs(d(pop.get(k-1),pop.get(k)) - davg) / N;
        System.out.println("DELTA VALUE: " + DELTA);
        System.out.println("-------\n");
    }
    
    public int[] removeElements(int[] input, int deleteMe) {
        int[] output = new int[100];
        int j = 0;
        for (int i = 0; i < input.length; i++) 
            if (deleteMe != i) {
                output[j] = input[i];
                j++;
            }
        return output;
    }
    
    private double d(double[] x1, double[] x2) {
        double sum = 0.0;
        for (int i=0; i<x1.length; i++)
            sum += Math.pow(x1[i] - x2[i], 2.0);
        return Math.sqrt(sum);
    }
    
    // --------------------------------------------------------------------- //
    
    public MOEAD(String f) {
        this.N = 100;
        this.ITERATIONS = 5000;
        this.setFunction(f);
    }
    public MOEAD(String f, int pop_size, int its) {
        this.N = pop_size;
        this.ITERATIONS = its;
        this.setFunction(f);
    }
    
    private void setFunction(String f) {
        switch(f) {
            // MOP Functions
            case "MOP2": this.func = new MOP2(); break;
            case "MOP4": this.func = new MOP4(); break;
            // DTLZ Functions
            case "DTLZ5": this.func = new DTLZ5(); break;
            // ZDT Fcuntions
            case "ZDT1": this.func = new ZDT1(); break;
            case "ZDT2": this.func = new ZDT2(); break;
            case "ZDT3": this.func = new ZDT3(); break;
            case "ZDT6": this.func = new ZDT6(); break;
        }
    }
    
    // --------------------------------------------------------------------- //
    
}
