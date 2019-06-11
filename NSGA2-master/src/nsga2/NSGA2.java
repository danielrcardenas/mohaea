
package nsga2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import metrics.Hypervolume;
import unalcol.multiple.functions.DTLZ.DTLZ5;
import unalcol.multiple.functions.F;
import unalcol.multiple.functions.MOP.MOP2;
import unalcol.multiple.functions.MOP.MOP4;
import unalcol.multiple.functions.ZDT.ZDT1;
import unalcol.multiple.functions.ZDT.ZDT2;
import unalcol.multiple.functions.ZDT.ZDT3;
import unalcol.multiple.functions.ZDT.ZDT6;

public class NSGA2 {
    
    public final int POPULATION;
    public final int ITERATIONS;
    
    public F func = new DTLZ5(3);
    
    public List<double[]> finalPop;
    
    // --------------------------------------------------------------------- //
    
    public void run() {
        // Initial population
        List<double[]> P = new ArrayList<>();
        for (int i=0; i<POPULATION; i++)
            P.add(func.generate());
        // Q is generated with genetic operators
        List<double[]> Q = selection(P);
        // Main Loop
        for (int i=0; i<ITERATIONS; i++) {
            // R is the reunion of P with Q
            List<double[]> R = new ArrayList<>(P);
            R.addAll(Q);
            clearDuplicates(R);
            // Get fronts from R and put the first POP_SIZE into the new P
            List<List<double[]>> fronts = fast_nondominated_sort(R);
            P = new ArrayList<>();
            for (List<double[]> front : fronts) {
                if (front.size() + P.size() > POPULATION) {
                    P.addAll(crowding_distance_assignment(front,(100-P.size())));
                    break;
                } else { P.addAll(front); }
            }
            // GA Operators
            Q = selection(P);
        }
        // Get First Front
        List<double[]> R = P; R.addAll(Q);
        clearDuplicates(R);
        List<List<double[]>> fronts = fast_nondominated_sort(R);
        finalPop = fronts.get(0);
    }
    
    // --------------------------------------------------------------------- //
    // Methods for Java console manipulation
    
    public void printPopulation(List<double[]> pop) {
        for (double[] p : pop) {
            for (int i = 0; i < p.length; i++)
                System.out.print(p[i] + " ");
            System.out.println();
        }
        System.out.println("\n\n");
    }
    
    public void printHypervolume(List<double[]> pop) {
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
    
    public void printFirstFrontFormated(List<double[]> pop) {
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
    
    public void clearDuplicates(List<double[]> R) {
        for (int i=0; i<R.size()-1; i++)
            for (int j=i+1; j<R.size(); j++) {
                double[] p = R.get(i);
                double[] q = R.get(j);
                boolean duplicate = true;
                if (p[0] != q[0])
                    duplicate = false;
                if (duplicate)
                    R.set(i, func.generate());
            }
    }
    
    public void printDelta(List<double[]> pop) {
        double DELTA = 0.0;
        double davg = 0.0;
        for (int k = 1; k < pop.size(); k++)
            davg += d(pop.get(k-1), pop.get(k));
        davg = davg / (pop.size() - 1);
        for (int k = 1; k < pop.size(); k++)
            DELTA += Math.abs(d(pop.get(k-1),pop.get(k)) - davg) / POPULATION;
        System.out.println("DELTA VALUE: " + DELTA);
        System.out.println("-------\n");
    }
    
    public double d(double[] x1, double[] x2) {
        double sum = 0.0;
        for (int i=0; i<x1.length; i++)
            sum += Math.pow(x1[i] - x2[i], 2.0);
        return Math.sqrt(sum);
    }
    
    // --------------------------------------------------------------------- //
    // Methods for R package manipulation
    
    public double getDelta() {
        double DELTA = 0.0;
        double davg = 0.0;
        for (int k = 1; k < finalPop.size(); k++)
            davg += d(finalPop.get(k-1), finalPop.get(k));
        davg = davg / (finalPop.size() - 1);
        for (int k = 1; k < finalPop.size(); k++)
            DELTA += Math.abs(d(finalPop.get(k-1),finalPop.get(k)) - davg) / POPULATION;
        return DELTA;
    }
    
    public double getHypervolume() {
        List<double[]> front = new ArrayList<>();
        for (int i = 0; i < func.getNoObjectives(); i++)
            front.add(new double[finalPop.size()]);
        for (int i = 0; i < finalPop.size(); i++) {
            func.set(finalPop.get(i));
            double[] fitness = func.evaluate();
            for (int j = 0; j < func.getNoObjectives(); j++)
                front.get(j)[i] = fitness[j];
        }
        return new Hypervolume(func).calculateHypervolume(front, front.get(0).length, front.size());
    }
    
    public String[] getFitness() {
        List<double[]> graphic = new ArrayList<>();
        for (int i = 0; i < func.getNoObjectives(); i++)
            graphic.add(new double[finalPop.size()]);
        for (int i = 0; i < finalPop.size(); i++) {
            func.set(finalPop.get(i));
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
    
    // --------------------------------------------------------------------- //
    
    // Fast non-dominated sort
    public List<List<double[]>> fast_nondominated_sort(List<double[]> P) {
        List<double[]> F1 = new ArrayList<>();
        List<List<double[]>> S = new ArrayList<>();
        List<Integer> n = new ArrayList<>();
        
        // First phase
        for (double[] p : P) {
            int np = 0;
            List<double[]> Sp = new ArrayList<>();
            for (double[] q : P) {
                if (p != q) {
                    if (dominates(p,q)) Sp.add(q); else if (dominates(q,p)) np += 1;
                }
            }
            if (np == 0) F1.add(p);
            S.add(Sp);
            n.add(np);
        }
        
        List<double[]> F = new ArrayList<>(F1);
        List<List<double[]>> fronts = new ArrayList<>();
        fronts.add(F1);
        
        // Second phase
        while (!F.isEmpty()) {
            List<double[]> H = new ArrayList<>();
            for (double[] p : F) {
                for (double[] q : S.get(P.indexOf(p))) {
                    n.set(P.indexOf(q), n.get(P.indexOf(q))-1);
                    if (n.get(P.indexOf(q)) == 0)
                        H.add(q);
                }
            }
            if (!H.isEmpty()) fronts.add(H);
            F = H;
        }
        
        List<double[]> last_front = new ArrayList<>();
        for (int i=0; i<n.size(); i++)
            if (n.get(i) > 0)
                last_front.add(P.get(i));
        if (!last_front.isEmpty()) fronts.add(last_front);
        
        return fronts;
    }
    
    // Crowding distance assignment
    public List<double[]> crowding_distance_assignment(List<double[]> front, int size) {
        List<double[]> fitness = new ArrayList<>();
        for (double[] sol : front) {
            func.set(sol); fitness.add(func.evaluate());
        }
        List<Double> crowd = new ArrayList<>();
        for (int i = 0; i < front.size(); i++)
            crowd.add(0.0);
        for (int i = 0; i < func.getNoObjectives(); i++) {
            double[] x = new double[front.size()];
            for (int j = 0; j < fitness.size(); j++)
                x[j] = fitness.get(j)[i];
            double[] localCrowd = crowd(x);
            for (int j = 0 ; j < crowd.size(); j++)
                crowd.set(j,crowd.get(j) + localCrowd[j]);
        }
        List<double[]> new_front = new ArrayList<>();
        for (int k = 0; k < size; k++) {
            double max = Double.MIN_VALUE;
            int index = -1;
            for (int i = 0; i < crowd.size(); i++) {
                if (crowd.get(i) > max) {
                    max = crowd.get(i);
                    index = i;
                }
            }
            new_front.add(front.get(index));
            crowd.set(index,Double.MIN_VALUE);
        }
        return new_front;
    }
    
    private double[] crowd(double[] fit) {
        // init
        double[] fit2 = new double[fit.length];
        List<Integer> mid = new ArrayList<>();
        for (int i = 0; i < fit.length; i++) {
            mid.add(i);
            fit2[i] = fit[i];
        }
        // Bubble sort
        boolean swapped = true;
        while(swapped) {
            swapped = false;
            for (int i = 1; i < fit2.length; i++) {
                if (fit2[i-1] > fit2[i]) {
                    swapped = true;
                    double temp = fit2[i-1];
                    fit2[i-1] = fit2[i];
                    fit2[i] = temp;
                    int temp2 = mid.get(i-1);
                    mid.set(i-1,mid.get(i));
                    mid.set(i,temp2);
                }
            }
        }
        // crowd
        double[] score = new double[fit2.length];
        score[0] = Double.MAX_VALUE;
        score[score.length-1] = Double.MAX_VALUE;
        for (int i = 1; i < fit2.length - 1; i++) {
            score[i] = fit2[i+1] - fit2[i-1];
        }
        // final crowd
        double[] crowd = new double[fit2.length];
        for (int i = 0; i < crowd.length; i++) {
            crowd[i] = score[mid.indexOf(i)];
        }
        return crowd;
    }
    
    // Dominance operator
    public boolean dominates(double[] x, double[] y) {
        func.set(x); double[] f = func.evaluate();
        func.set(y); double[] g = func.evaluate();
        boolean dominates = false;
        for (int i = 0; i < f.length; i++)
            if (f[i] > g[i])
                return false;
        for (int i = 0; i < f.length; i++)
            if (f[i] < g[i])
                dominates = true;
        return dominates;
    }
    
    // Sort population after dominance operator
    public void sort(List<double[]> P) {
        P.stream().forEach((p) -> {
            P.stream().filter((q) -> (!dominates(p,q))).forEach((q) -> {
                double[] temp = p;
                P.set(P.indexOf(p), q);
                P.set(P.indexOf(q), temp);
            });
        });
    }
    
    // --------------------------------------------------------------------- //
    
    public double[] mutation(double[] p) {
        double q[] = new double[p.length];
        for (int i=0; i<p.length; i++) {
            String bval = doubleToBinary(p[i]);
            int cut = (new Random()).nextInt(bval.length() - 1);
            char c = bval.charAt(cut);
            if (c == '0') c = '1'; else c = '0';
            bval = bval.substring(0,cut) + c + bval.substring(cut+1);
            q[i] = binaryToDouble(bval);
        }
        return q;
    }
    public double[] crossover(double[] p1, double[] p2) {
        double[] kid = new double[p1.length];
        for (int i=0; i<p1.length; i++) {
            String binaryString1 = doubleToBinary(p1[i]);
            String binaryString2 = doubleToBinary(p2[i]);
            int limit; if (binaryString1.length() < binaryString2.length()) limit = binaryString1.length(); else limit = binaryString2.length();
            int cutPoint1 = (new Random()).nextInt(limit/2-1);
            int cutPoint2 = (new Random()).nextInt(limit/2-1) + limit/2;
            String child = binaryString1.substring(0,cutPoint1) + binaryString2.substring(cutPoint1, cutPoint2)
                     + binaryString1.substring(cutPoint2);
            kid[i] = binaryToDouble(child);
        }
        return kid;
    }
    public List<double[]> selection(List<double[]> P) {
        List<double[]> Q = new ArrayList<>(P);
        List<double[]> new_pop = new ArrayList<>();
        for (int i=0; i<Q.size()-1; i=i+2) {
            double[] parent1 = Q.get(i);
            double[] parent2 = Q.get(i+1);
            double[] bro = crossover(parent1,parent2);
            double[] sis = crossover(parent2,parent1);
            new_pop.add(mutation(bro));
            new_pop.add(mutation(sis));
        }
        return new_pop;
    }
    
    // --------------------------------------------------------------------- //
    
    // DOUBLE TO BITS AND BACK
    private String doubleToBinary(double x) {
        int d = 6;
        double N = (func.getMaxValue() - func.getMinValue()) * Math.pow(10,d);
        int n = (int) Math.ceil(Math.log(N) / Math.log(2));
        long decimal = (long) ((x - func.getMinValue()) * (Math.pow(2,n) - 1) / (func.getMaxValue() - func.getMinValue()));
        String binary = Long.toBinaryString(decimal);
        if (binary.length() < 5) binary = "000" + binary;
        return binary;
    }
    private double binaryToDouble(String b) {
        int d = 6;
        double N = (func.getMaxValue() - func.getMinValue()) * Math.pow(10,d);
        int n = (int) Math.ceil(Math.log(N) / Math.log(2));
        long decimal = Long.parseLong(b, 2);
        return (func.getMinValue() + decimal * (func.getMaxValue() - func.getMinValue()) / (Math.pow(2,n) - 1));
    }
    
    // --------------------------------------------------------------------- //
    
    public NSGA2(String f) {
        this.POPULATION = 100;
        this.ITERATIONS = 250;
        this.setFunction(f);
    }
    public NSGA2(String f, int pop_size, int iterations) {
        this.POPULATION = pop_size;
        this.ITERATIONS = iterations;
        this.setFunction(f);
    }
    public NSGA2() {
        this.POPULATION = 100;
        this.ITERATIONS = 250;
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
