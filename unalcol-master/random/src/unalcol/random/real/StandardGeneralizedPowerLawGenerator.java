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
public class StandardGeneralizedPowerLawGenerator extends StandardPowerLawGenerator{
    protected double coarse_b=1.0;
    public StandardGeneralizedPowerLawGenerator( double alpha ){
      super(alpha);
    }
        
    @Override
    public Double next(double x){
        return coarse_b * super.next(x) - coarse_b;
    }    
    
    /*@Override
    public DoubleGenerator new_instance(){
        RawGenerator g = RawGenerator.get(this);
        DoubleGenerator dg = new StandardGeneralizedPowerLawGenerator(1.0-1.0/coarse_alpha);
        RawGenerator.set(dg, g.new_instance());
        return dg; 
    }*/        
    
}