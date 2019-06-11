/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unalcol.optimization.real.mutation;

import unalcol.search.variation.Variation_1_1;
import unalcol.services.MicroService;

/**
 *
 * @author jgomezpe
 */
public abstract class Mutation extends MicroService<double[]> implements Variation_1_1<double[]>{
    // Mutation definitions
    protected PickComponents components = null;
    protected int[] indices = new int[0];
    
    public Mutation(PickComponents components){
        this.components = components;
    }
    
    public Mutation(){}            
}