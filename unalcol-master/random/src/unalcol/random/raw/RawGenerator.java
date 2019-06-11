package unalcol.random.raw;

import unalcol.services.AbstractMicroService;
import unalcol.services.Service;

// Unified Random generation Pack 1.0 by Jonatan Gomez-Perdomo
// https://github.com/jgomezpe/unalcol/tree/master/random/
//
/**
 *
 * RawGenerator
 * <P> Abstract class. It is a generator of uniform distributed pseudo random numbers in the interval [0.0,1.0) (x~U[0,1)).
 *  It has some useful methods for generating boolean values and uniform distributed integers. 
 *
 * <P>
 * <A HREF="https://github.com/jgomezpe/unalcol/tree/master/src/unalcol/random/raw/RawGenerator.java">
 * Source code </A> is available.
 * <P>
 *
 * <h3>License</h3>
 *
 * Copyright (c) 2014 by Jonatan Gomez-Perdomo. <br>
 * All rights reserved. <br>
 *
 * <p>Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <ul>
 * <li> Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * <li> Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <li> Neither the name of the copyright owners, their employers, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * </ul>
 * <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 *
 *
 * @author <A HREF="http://disi.unal.edu.co/profesores/jgomezpe"> Jonatan Gomez-Perdomo </A>
 * (E-mail: <A HREF="mailto:jgomezpe@unal.edu.co">jgomezpe@unal.edu.co</A> )
 * @version 1.0
 */
public interface RawGenerator extends AbstractMicroService<Object>{
	/**
	 * Generates a random number in the interval [0.0,1.0) following a uniform distribution x~U[0,1)
	 * @return a random number in the interval [0.0,1.0) following a uniform distribution x~U[0,1)
	 */
	public double next();
    
	/**
	 * Generates a boolean value (<i>false</i> or <i>true</i> with equal probability)
	 * @return A boolean value (<i>false</i> or <i>true</i> with equal probability)
	 */
	public default boolean bool(){ return (next() >= 0.5); }

	/**
	 * Generates a boolean value with the given probability
	 * @param falseProbability The probability of generating a <i>false</i>. Clearly, (1.0-falseProbability)
	 * provides the probability of generating a <i>true</i> value
	 * @return A boolean value with the given probability
	 */
	public default boolean bool(double falseProbability){ return (next() >= falseProbability); }

	/**
	 * Generates a uniform distributed integer value in the interval [0,max-1]
	 * @param max The superior limit of the half-open interval [0,max) defined for generating integer values
	 * @return An integer value in the interval [0,max) with uniform distribution
	 */
	public default int integer(int max){ return ((int) (max * next())); }    
    
	/**
	 * Returns a set of random numbers following the x ~ U[0,1) distribution
	 * @param v Array used for returning the generated random numbers
	 * @param offset Stating point for storing the generated real numbers
	 * @param m The number of random numbers to be generated and returned in array <i>v</i>.
	 */
	public default double[] raw(double[] v, int offset, int m){
		for(int i = 0; i < m; i++) v[i+offset] = next();
		return v;
	}

	/**
	 * Returns a set of random numbers following the x ~ U[0,1) distribution
	 * @param m The number of random numbers to be generated
	 * @return An array with <i>m</i> random numbers following the x ~ U[0,1) distribution
	 */
	public default double[] raw(int m){
		double[] v = null;
		if (m > 0) {
			v = new double[m];
			raw(v, 0, m);
		}
		return v;
	}
    	
	// The MicroService methods
	public static final String name="raw.random";
	public static final String next=name+".next"; 
	public static final String bool=name+".bool"; 
	public static final String integer=name+".int"; 
	public static final String raw=name+".raw"; 	

	public static final String[] methods = new String[]{name,next,bool,integer,raw};
	
	@Override
	public default String[] provides(){ return methods; }

	@Override
	public default Object run( Object... args ) throws Exception{
		String service = name();
		if(service.equals(name) || service.equals(next)) return next();
		
		if(service.equals(bool)) if( args.length==0 ) return bool(); else return bool((double)args[0]);
		
		if(service.equals(integer)) return integer((int)args[0]);
		
		if(service.equals(raw)){
			if( args.length==1 ) return raw((int)args[0]);
			else return raw((double[])args[0],(int)args[1], (int)args[2]);
		}
		
		throw new Exception("Undefined service "+service);		
	}
	
	public static double next(Object caller){
		try{ return (double)Service.run(name, caller); }catch(Exception e){ return Math.random(); }
	}

	/**
	 * Generates a boolean value (<i>false</i> or <i>true</i> with equal probability)
	 * @return A boolean value (<i>false</i> or <i>true</i> with equal probability)
	 */
	public static boolean bool(Object caller){ 
		try{ return (boolean)Service.run(bool, caller); }catch(Exception e){ return Math.random()<0.5; }
	}

	/**
	 * Generates a boolean value with the given probability
	 * @param falseProbability The probability of generating a <i>false</i>. Clearly, (1.0-falseProbability)
	 * provides the probability of generating a <i>true</i> value
	 * @return A boolean value with the given probability
	 */
	public static boolean bool(Object caller, double falseProbability){ 
		try{ return (boolean)Service.run(bool, caller, falseProbability); }catch(Exception e){ return Math.random()>falseProbability; }
	}

	/**
	 * Generates a uniform distributed integer value in the interval [0,max-1]
	 * @param max The superior limit of the half-open interval [0,max) defined for generating integer values
	 * @return An integer value in the interval [0,max) with uniform distribution
	 */
	public static int integer(Object caller, int max){
		try{ return (int)Service.run(integer, caller, max); }catch(Exception e){ return (int) (max * Math.random()); }
	}    
    
	/**
	 * Returns a set of random numbers following the x ~ U[0,1) distribution
	 * @param v Array used for returning the generated random numbers
	 * @param offset Stating point for storing the generated real numbers
	 * @param m The number of random numbers to be generated and returned in array <i>v</i>.
	 */
	public static double[] raw(Object caller, double[] v, int offset, int m){
		try{ return (double[])Service.run(integer, caller, v, offset, m); }
		catch(Exception e){ 
			for(int i = 0; i < m; i++) v[i+offset] = Math.random();
			return v;
		}
	}

	/**
	 * Returns a set of random numbers following the x ~ U[0,1) distribution
	 * @param m The number of random numbers to be generated
	 * @return An array with <i>m</i> random numbers following the x ~ U[0,1) distribution
	 */
	public static double[] raw(Object caller, int m){
		try{ return (double[])Service.run(integer, caller,m); }
		catch(Exception e){
			double[] v = new double[m];
			for(int i = 0; i < m; i++) v[i] = Math.random();
			return v;
		}
	}	
}