/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.multiple.functions;

/**
 *
 * @author ashcrok
 */
public abstract class F {


    public double[] generate() { return null; }
    public double[] evaluate() { return null; }
    public double[] get() { return null; }
    public void set(double[] sol) {}
    public int getMinValue() { return 0; }
    public int getMaxValue() { return 0; }
    public int getNoObjectives() { return 0; }
    public int getNVariables() {return 0;}
    
}
