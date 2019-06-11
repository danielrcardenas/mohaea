
package metrics;

import unalcol.multiple.functions.F;
import java.util.ArrayList;
import java.util.List;

public class Hypervolume {
    
    private final F f;
    
    // --------------------------------------------------------------------- //
    
    public Hypervolume(F function) {
        f = function;
    }
  
    // --------------------------------------------------------------------- //
    
    public double calculateHypervolume(List<double[]> front, int noPoints, int noObjectives) {
        int n = noPoints;
        double volume = 0.0, distance = 0.0;
        while (n > 0) {
            int noNondominatedPoints;
            double tempVolume, tempDistance;
            // noNondominatedPoints = filterNondominatedSet(front,n,noObjectives-1);
            noNondominatedPoints = noPoints;
            if (noObjectives < 3) {
                tempVolume = front.get(0)[0];
            } else {
                tempVolume = calculateHypervolume(front,noNondominatedPoints,noObjectives-1);
            }
            tempDistance = surfaceUnchangedTo(front,n,noObjectives-1);
            volume += tempVolume * (tempDistance - distance);
            distance = tempDistance;
            n = reduceNondominatedSet(front,n,noObjectives-1,distance);
        }
        return volume;
    }
    
    // --------------------------------------------------------------------- //
    
    private int filterNondominatedSet(List<double[]> front, int noPoints, int noObjectives) {
        int i = 0,j;
        int n = noPoints;
        while (i < n) {
            j = i + 1;
            while (j < n) {
                if (dominates(front.get(i),front.get(j))) {
                    n--;
                    swap(front,j,n);
                } else if (dominates(front.get(j),front.get(i))) {
                    n--;
                    swap(front,i,n);
                    i--;
                    break;
                } else 
                    j++;
            }
            i++;
        }
        return n;
    } // Filter non-dominated set
    
    private double surfaceUnchangedTo(List<double[]> front, int noPoints, int objective) {
        int i;
        double minValue, value;
        minValue = front.get(objective)[0];
        for (i = 1; i < noPoints; i++) {
            value = front.get(objective)[i];
            if (value < minValue)
                minValue = value;
        }
        return minValue;
    } // Surface unchanged to
    
    private int reduceNondominatedSet(List<double[]> front, int noPoints, int objective, double threshold) {
        int i, n = noPoints;
        for (i = 0; i < n; i++)
            if (front.get(objective)[i] <= threshold) {
                n--;
                swap(front,i,n);
            }
        return n;
    } // Reduce non-dominated set
    
    private boolean dominates(double[] x, double[] y) {
        f.set(x); double[] f1 = f.evaluate();
        f.set(y); double[] f2 = f.evaluate();
        return (f1[0] <= f2[0] && f1[1] <= f2[1] && (f1[0] < f2[0] || f1[1] < f2[1]));
    } // Dominates
    
    private void swap(List<double[]> front, int  i, int  j) {
        double[] temp = new double[front.size()];
        for (int k = 0; k < front.size(); k++) {
            temp[k] = front.get(k)[i];
            front.get(k)[i] = front.get(k)[j];
            front.get(k)[j] = temp[k];
        }
    } // Swap 
    
    // --------------------------------------------------------------------- //
    
    private double[] getMaximumValues(List<double[]> front, int noObjectives) {
        double[] maximumValues = new double[noObjectives];
        for (int i = 0; i < noObjectives; i++)
            maximumValues[i] = Double.NEGATIVE_INFINITY;
        for (int i = 0; i< front.size(); i++)
            for (int j = 0; j < front.get(i).length; j++)
                if (front.get(i)[j] > maximumValues[i])
                    maximumValues[i] = front.get(i)[j];
        return maximumValues;
    }
    
    private double[] getMinimumValues(List<double[]> front, int noObjectives) {
        double[] minimumValue  = new double[noObjectives];
        for (int i = 0; i < noObjectives; i++)
            minimumValue[i] = Double.MAX_VALUE;
        for (int i = 0; i < front.size(); i++)
            for (int j = 0; j < front.get(i).length; j++)
                if (front.get(i)[j] < minimumValue[i])
                    minimumValue[i] = front.get(i)[j];
        return minimumValue;
    }
    
    private List<double[]> getNormalizedFront(List<double[]> front, double[] maximumValue, double[] minimumValue) {
        List<double[]> normalizedFront = new ArrayList<>();
        for (int i = 0; i < front.size(); i++) {
            normalizedFront.set(i, new double[front.get(i).length]);
            for (int j = 0; j < front.get(i).length; j++)
                normalizedFront.get(i)[j] = (front.get(i)[j] - minimumValue[i]) / (maximumValue[i] - minimumValue[i]);
        }
        return normalizedFront;
    }
    
    private List<double[]> invertedFront(List<double[]> front) {
        List<double[]> invertedFront = new ArrayList<>();
        for (int i = 0; i < front.size(); i++) {
            invertedFront.set(i, new double[front.get(i).length]);
            for (int j = 0; j < front.get(i).length; j++) {
                if (front.get(i)[j] <= 1.0 && front.get(i)[j] >= 0.0)
                    invertedFront.get(i)[j] = 1.0 - front.get(i)[j]; 
                else if (front.get(i)[j] > 1.0)
                    invertedFront.get(i)[j] = 0.0; 
                else if (front.get(i)[j] < 0.0)
                    invertedFront.get(i)[j] = 1.0; 
            }
        }
        return invertedFront;
    }
    
    // --------------------------------------------------------------------- //
    
}
