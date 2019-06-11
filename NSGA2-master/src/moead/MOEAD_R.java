
package moead;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MOEAD_R {
    
    // --------------------------------------------------------------------- //
    
    public String[] getLambdaDistribution() {
        List<double[]> l = new ArrayList<>();
        for (int k = 0; k < N; k++) {
            double[] lambda = new double[2];
            lambda[0] = (double) k / (double) (N - 1);
            lambda[1] = 1 - lambda[0];
            l.add(lambda);
        }
        return genRCode1(l);
    }
    
    public String[] closestWeightVectors(double[][] lambda) {
        List<double[]> l = new ArrayList<>();
        for (double[] lam : lambda)
            l.add(lam);
        
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
        return genRCode2(B);
    }
    
    private double d(double[] x1, double[] x2) {
        double sum = 0.0;
        for (int i=0; i<x1.length; i++)
            sum += Math.pow(x1[i] - x2[i], 2.0);
        return Math.sqrt(sum);
    }
    
    public String[] update(double[][] B, double[][] pop, int min, int max, double sigma) {
        List<double[]> all_y = new ArrayList<>();
        List<int[]> all_P = new ArrayList<>();
        for (int i = 0; i < pop.length; i++) {
            int[] P = null;
            if (Math.random() < sigma) {
                P = new int[T];
                for (int j = 0; j < T; j++)
                    P[j] = (int) B[i][j];
            } else {
                int[] all_indicies = new int[pop.length];
                for (int k = 0; k < pop.length; k++)
                    all_indicies[k] = k;
                P = all_indicies;
            }
            all_P.add(P);
            double[] y = polynomialMutation(DEoperator(pop[i],
                    pop[P[(new Random()).nextInt(P.length)]], 
                    pop[P[(new Random()).nextInt(P.length)]]), min, max);
            all_y.add(y);
        }
        return genRCode3(all_P,all_y);
    }
    
    // --------------------------------------------------------------------- //
    
    private String[] genRCode1(List<double[]> R) {
        List<String> r = new ArrayList<>();
        r.add("l <- matrix(nrow=0,ncol=" + m + ");");
        for (int k = 0; k < R.size(); k++) {
            double[] sol = R.get(k);
            String v = "c(";
            for (int i = 0; i < sol.length; i++) {
                v += sol[i];
                if (i != sol.length - 1)
                    v+= ",";
            }
            v += ")";
            r.add("l <- rbind(l," + v + ");");
        }
        String result[] = new String[r.size()];
        for (int i = 0; i < r.size(); i++)
            result[i] = r.get(i);
        return result;
    }
    private String[] genRCode2(List<List<Integer>> R) {
        List<String> r = new ArrayList<>();
        r.add("B <- matrix(nrow=0,ncol=" + T + ");");
        for (int k = 0; k < R.size(); k++) {
            List<Integer> sol = R.get(k);
            String v = "c(";
            for (int i = 0; i < sol.size(); i++) {
                v += sol.get(i);
                if (i != sol.size() - 1)
                    v+= ",";
            }
            v += ")";
            r.add("B <- rbind(B," + v + ");");
        }
        String result[] = new String[r.size()];
        for (int i = 0; i < r.size(); i++)
            result[i] = r.get(i);
        return result;
    }
    private String[] genRCode3(List<int[]> all_P, List<double[]> all_y) {
        List<String> r = new ArrayList<>();
        r.add("P <- list();");
        r.add("Y <- matrix(nrow=0,ncol=" + all_y.get(0).length + ");");
        for (int k = 0; k < all_P.size(); k++) {
            // Get P
            int[] P = all_P.get(k);
            String v = "c(";
            for (int i = 0; i < P.length; i++) {
                v += P[i];
                if (i != P.length - 1)
                    v += ",";
            }
            v += ")";
            r.add("P[[" + (k+1) + "]] <- " + v + ";");
            // Get y
            double[] y = all_y.get(k);
            String c = "c(";
            for (int i = 0; i < y.length; i++) {
                c += y[i];
                if (i != y.length - 1)
                    c += ",";
            }
            c += ")";
            r.add("Y <- rbind(Y," + c + ");");
        }
        String result[] = new String[r.size()];
        for (int i = 0; i < r.size(); i++)
            result[i] = r.get(i);
        return result;
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
    private double[] polynomialMutation(double[] notY, int min, int max) {
        double[] y = new double[notY.length];
        for (int i = 0; i < notY.length; i++) {
            if (Math.random() < pm) {
                double rand = Math.random();
                double sigma;
                if (rand < 0.5) sigma = Math.pow(2 * rand, 1 / (eta + 1)) - 1;
                else sigma = 1 - Math.pow(2 - 2 * rand, 1 / (eta + 1));
                y[i] = notY[i] + sigma * (max - min);
            }
            else y[i] = notY[i];
        }
        for (int i = 0; i < y.length; i++)
            if (y[i] < min || y[i] > max)
                y[i] = min + (max - min) * Math.random();
        return y;
    }
    
    // --------------------------------------------------------------------- //
    
    private final int N; // Population size
    private final int m; // Number of objectives
    private final int T; // Number of neighbors
    public MOEAD_R(int N, int m, int T) {
        this.N = N;
        this.m = m;
        this.T = T;
    }
    
}
