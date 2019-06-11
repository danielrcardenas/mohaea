/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.random.real;

//
//Unified Random generation Pack 1.0 by Jonatan Gómez-Perdomo
//https://github.com/jgomezpe/unalcol/tree/master/random/
//
/**
 *
 * @author jgomez
 */
public class SimplestPowerLawGenerator extends StandardPowerLawGenerator{
    // A standard power law  with alpha = 2 and b = 1
    public SimplestPowerLawGenerator(){    
        super(2.0);        
    }
        
    @Override
    public Double next(double x){
        return 1.0/(1.0-x);
    } 

    /*@Override
    public DoubleGenerator new_instance(){
        RawGenerator g = RawGenerator.get(this);
        DoubleGenerator dg = new SimplestPowerLawGenerator();
        RawGenerator.set(dg, g.new_instance());
        return dg; 
    }*/
}