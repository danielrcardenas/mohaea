package unalcol.types.collection.vector;
import unalcol.algorithm.*;
import unalcol.sort.*;
import unalcol.sort.algorithm.*;
import unalcol.clone.*;
import unalcol.services.Service;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: Kunsamu </p>
 *
 * @author Jonatan Gomez
 * @version 1.0
 */
public class VectorSort<T> extends Algorithm<Vector<T>,Vector<T>> {

    protected boolean overwrite = true;

    protected Sort<T> sort = null;

    public VectorSort(){
        sort = new MergeSort<T>();
    }

    public VectorSort( Sort<T> _sort ) {
        sort = _sort;
    }

    public VectorSort( Order<T> _order ) {
        sort = new MergeSort<T>( _order );
    }

    public void apply( Vector<T> input, Order<T> order ){
        apply( input, 0, input.size, order );
    }

    public void apply( Vector<T> input, int start, int end, Order<T> order ){
        T[] obj = (T[])input.buffer;
        sort.apply( obj, start, end, order );
    }

    @SuppressWarnings("unchecked")
	public Vector<T> apply( Vector<T> input ){
        if( input.size() > 0 ){
            if (!overwrite) try{ input = (Vector<T>)Service.run(Clone.name, input); }catch( Exception e ){}
            Order<T> order = sort.getOrder();
            apply(input, 0, input.size, order);
        }
        return input;
    }
}
