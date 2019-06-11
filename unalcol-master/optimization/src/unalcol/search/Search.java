/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unalcol.search;

import unalcol.Tagged;
import unalcol.search.space.Space;
import unalcol.search.space.SpaceSampler;

/**
 *
 * @author jgomez
 */
public interface Search<T,R> extends GoalBased<T, R>, SpaceSampler<T>{	
	public Tagged<T> solve( Space<T> space ); //TODO: Este metodo toca revisarlo para multiobjetivo
	
	@Override
	public default T apply( Space<T> space ){ return solve( space ).unwrap(); }
	
	public default void init(){}
}