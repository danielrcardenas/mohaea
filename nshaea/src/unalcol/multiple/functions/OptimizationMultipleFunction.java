/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.multiple.functions;

import unalcol.algorithm.Algorithm;
import unalcol.multiple.search.MultipleRealValuedGoal;
import unalcol.real.DoubleDominanceOrder;
import unalcol.sort.Order;
import unalcol.sort.ReversedOrder;
import unalcol.types.real.DoubleOrder;
import unalcol.types.real.array.DoubleArray;

/**
 *
 * @author danielrc
 */
public abstract class OptimizationMultipleFunction<T> extends Algorithm<T,Double[]> implements MultipleRealValuedGoal<T> {

    // Indicates if the function minimizes or maximizes
    protected boolean[] minimize;

    // Number of variables
    protected static int n;
    // Number of objectives
    protected static int M;

    protected Order<Double[]> order = null;

    public Order<Double[]> order(){
        if( order == null ) order = new DoubleDominanceOrder();
        return order;
    }

    public boolean[] minimizing(){ return this.minimize; }

    public void minimize( boolean[] minimize ){
        this.minimize = minimize;
        order = null;
    }


    //public double[] generate() { return null; }

    public int getNoObjectives() { return M; }
    public int getNVariables() {return n;}

    public abstract  double [] getMinArrayValues();
    public abstract double [] getMaxArrayValues();
    
}
