
package paes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PAES_R {
    
    public String[] generate_new_solution(double[][] archive, double[][] fitness) {
        double[] current_solution;
        if (archive.length > 5) current_solution = this.getBestCandidate(archive,fitness);
        else current_solution = archive[new Random().nextInt(archive.length)];
        double[] new_solution = mutation(current_solution);
        return solutionToString(current_solution, new_solution);
    }
    
    public String[] update_archive(double[][] arc, double[][] fit,
            double[] curr_sol, double[] curr_fit, 
            double[] new_sol, double[] new_fit) {
        
        List<double[]> archive = new ArrayList<>();
        for (double[] a : arc) 
            archive.add(a);
        List<double[]> fitness = new ArrayList<>();
        for (double[] f : fit)
            fitness.add(f);
        
        if (dominates(curr_fit,new_fit)) {
            // do nothing
        } else if (dominates(new_fit,curr_fit)) {
            archive.remove(curr_sol);
            archive.add(new_sol);
            updateArchive(archive,fitness,new_sol,new_fit);
        } else if (isDominatedByArchive(new_fit,fitness)) {
            // do nothing
        } else {
            archive.add(new_sol);
            updateArchive(archive,fitness,new_sol,new_fit);
            if (archive.size() > MAX_SIZE)
                archive.remove(getWorstCandidate(archive,fitness));
        }
        
        return archiveToString(archive);
    }
    
    // --------------------------------------------------------------------- //
    
    private void updateArchive(List<double[]> archive, List<double[]> fitness,
            double[] new_sol, double[] new_fit) {
        for (double[] x : fitness)
            if (dominates(new_fit,x))
                archive.remove(x);
    }
    
    private boolean isDominatedByArchive(double[] solution, List<double[]> archive) {
        return archive.stream().anyMatch((x) -> (dominates(x,solution)));
    }
    
    private double[] getBestCandidate(double[][] archive, double[][] fitness) {
        List<Double> crowd = new ArrayList<>();
        for (int i = 0; i < archive.length; i++)
            crowd.add(0.0);
        for (int i = 0; i < fitness[0].length; i++) {
            double[] x = new double[archive.length];
            for (int j = 0; j < fitness.length; j++)
                x[j] = fitness[j][i];
            double[] localCrowd = crowd(x);
            for (int j = 0 ; j < crowd.size(); j++)
                crowd.set(j,crowd.get(j) + localCrowd[j]);
        }
        double max = Double.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < crowd.size(); i++) {
            if (crowd.get(i) > max) {
                max = crowd.get(i);
                index = i;
            }
        }
        return archive[index];
    }
    
    private double[] getWorstCandidate(List<double[]> archive, List<double[]> fitness) {
        List<Double> crowd = new ArrayList<>();
        for (int i = 0; i < archive.size(); i++)
            crowd.add(0.0);
        for (int i = 0; i < fitness.get(0).length; i++) {
            double[] x = new double[archive.size()];
            for (int j = 0; j < fitness.size(); j++)
                x[j] = fitness.get(j)[i];
            double[] localCrowd = crowd(x);
            for (int j = 0 ; j < crowd.size(); j++)
                crowd.set(j,crowd.get(j) + localCrowd[j]);
        }
        double min = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < crowd.size(); i++) {
            if (crowd.get(i) < min) {
                min = crowd.get(i);
                index = i;
            }
        }
        return archive.get(index);
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
        score[0] = fit2[1] - fit2[0];
        score[score.length-1] = fit2[score.length-1] - fit2[score.length-2];
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
    private boolean dominates(double[] f, double[] g) {
        boolean dominates = false;
        for (int i = 0; i < f.length; i++)
            if (f[i] > g[i])
                return false;
        for (int i = 0; i < f.length; i++)
            if (f[i] < g[i])
                dominates = true;
        return dominates;
    }
    
    private double[] mutation(double[] p) {
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
    
    // DOUBLE TO BITS AND BACK
    private String doubleToBinary(double x) {
        int d = 6;
        double N = (MAX - MIN) * Math.pow(10,d);
        int n = (int) Math.ceil(Math.log(N) / Math.log(2));
        long decimal = (long) ((x - MIN) * (Math.pow(2,n) - 1) / (MAX - MIN));
        String binary = Long.toBinaryString(decimal);
        if (binary.length() < 5) binary = "000" + binary;
        return binary;
    }
    private double binaryToDouble(String b) {
        int d = 6;
        double N = (MAX - MIN) * Math.pow(10,d);
        int n = (int) Math.ceil(Math.log(N) / Math.log(2));
        long decimal = Long.parseLong(b, 2);
        return (MIN + decimal * (MAX - MIN) / (Math.pow(2,n) - 1));
    }
    
    // --------------------------------------------------------------------- //
    
    private String[] solutionToString(double[] current_solution, double[] new_solution) {
        List<String> r = new ArrayList<>();
        // new sol
        String v = "c(";
        for (int i = 0; i < new_solution.length; i++) {
            v += new_solution[i];
            if (i != new_solution.length - 1)
                v+= ",";
        } v += ")";
        r.add("new_solution <- " + v + ";");
        r.add("new_fit <- f(" + v + ");");
        // curr sol
        v = "c(";
        for (int i = 0; i < current_solution.length; i++) {
            v += current_solution[i];
            if (i != current_solution.length - 1)
                v+= ",";
        } v += ")";
        r.add("current_solution <- " + v + ";");
        r.add("current_fit <- f(" + v + ");");
        
        String result[] = new String[r.size()];
        for (int i = 0; i < r.size(); i++)
            result[i] = r.get(i);
        return result;
    }
    
    private String[] archiveToString(List<double[]> archive) {
        List<String> r = new ArrayList<>();
        r.add("A <- matrix(nrow=0,ncol=" + N + ");");
        r.add("fitness <- matrix(nrow=0,ncol=" + M + ");");
        for (int k = 0; k < archive.size(); k++) {
            double[] sol = archive.get(k);
            String v = "c(";
            for (int i = 0; i < sol.length; i++) {
                v += sol[i];
                if (i != sol.length - 1)
                    v+= ",";
            }
            v += ")";
            r.add("A <- rbind(A," + v + ");");
            r.add("fitness <- rbind(fitness,f(" + v + "));");
        }
        String result[] = new String[r.size()];
        for (int i = 0; i < r.size(); i++)
            result[i] = r.get(i);
        return result;
    }
    
    // --------------------------------------------------------------------- //
    
    private final int MIN;
    private final int MAX;
    private final int N;
    private final int M;
    private final int MAX_SIZE;
    public PAES_R(int n, int m, int min, int max, int pop_size) {
        this.MIN = min;
        this.MAX = max;
        this.N = n;
        this.M = m;
        this.MAX_SIZE = pop_size;
    }
    
}
