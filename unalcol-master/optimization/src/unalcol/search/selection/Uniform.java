package unalcol.search.selection;

import unalcol.Tagged;
import unalcol.random.integer.*;

/**
 * <p>Title: Uniform</p>
 * <p>Description: The uniform selection operator. In this selection strategy all individuals
 * have the same probability to be chosen</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * @author Jonatan Gomez
 * @version 1.0
 */

public class Uniform<T> implements Selection<T>{

  /**
   * Default constructor
   */
  public Uniform(){
  }

  /**
   * Selects a candidate solution from a set of candidate solutions
   * @param g Uniform integer number generator used for picking the candidate solution
   * @param q Quality associated to each candidate solution
   * @return Index of the selected candidate solution
   */
  protected int choose_one( IntUniform g ){
    return g.next();
  }

  /**
   * Selects a candidate solution from a set of candidate solutions
   * @param q Quality associated to each candidate solution
   * @return Index of the selected candidate solution
   */
  @Override
  public int choose_one( Tagged<T>[] x ){
    return choose_one(new IntUniform(x.length));
  }

  /**
   * Selects a subset of candidate solutions from a set of candidates
   * @param n Number of candidate solutions to be selected
   * @param q Quality associated to each candidate solution
   * @return Indices of the selected candidate solutions
   */
  @Override
  public int[] apply( int n, Tagged<T>[] x){
    IntUniform g =  new IntUniform(x.length);
    return g.generate(n);
  }
}