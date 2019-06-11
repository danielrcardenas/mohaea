/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package random;

import unalcol.random.raw.JavaGenerator;
import unalcol.random.real.RandDouble;
import unalcol.random.real.SimplestGeneralizedPowerLawGenerator;
import unalcol.random.real.StandardGaussianGenerator;
import unalcol.random.real.SymmetricGenerator;
import unalcol.random.real.UniformGenerator;
import unalcol.services.Service;
import unalcol.services.ServicePool;

/**
 *
 * @author jgomez
 */
public class DoubleGeneratorTest{
	public static void init_services(){
		ServicePool service = new ServicePool();
        service.register(new JavaGenerator(), Object.class);         
//        service.register(new ConsoleTracer(), Object.class);
        Service.set(service);
	}
	
	public static RandDouble uniform(){
		System.out.println( "Uniform" );
		return  new UniformGenerator(0.0,2.0);
	}  
  
	public static RandDouble gaussian(){
		System.out.println( "Gaussian" );
		return  new StandardGaussianGenerator();
	}  
  
	public static RandDouble power_law(){
		System.out.println( "Power Law" );
		return new SimplestGeneralizedPowerLawGenerator();
	}
    
	public static RandDouble symmetric_power_law(){
	    System.out.println( "Symmetric Power Law" );
	    SymmetricGenerator g = new SymmetricGenerator();
	    g.set(SymmetricGenerator.one_side, new SimplestGeneralizedPowerLawGenerator() );
	    return g;
	}
      
	public static void main( String[] args ){
		init_services();
		//RandDouble g = uniform();
		//RandDouble g = gaussian();
		 RandDouble g = power_law();
		// RandDouble g = symmetric_power_law();
		int n = 10;
		// Generating an array of ten random values
		double[] x = g.generate(n);
		for( int i=0; i<n; i++ ){
			System.out.println( x[i] );
		}
		System.out.println("****************");
		// Generating ten random values
		for( int i=0; i<n; i++ ){
			System.out.println(g.next());
		}
	}    
}