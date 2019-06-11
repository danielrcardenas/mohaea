package unalcol.real;

import unalcol.services.MicroService;
import unalcol.sort.Order;

/**
 * @author Daniel Rodríguez
 */
public class DoubleDominanceOrder extends MicroService<Double[]> implements Order<Double[]> {

    @Override
    //TODO: Arreglar el comparativo dado que si one domina a teo entonces -1
    public int compare(Double[] one, Double[] two) {

        if (dominate(one,two))
            return 1;
        else if (dominate(two,one))
            return -1;
        else return 0;




    }

    protected boolean dominate (Double[] one, Double[] two){
        int sum_dom = 0;
        int sum_les=0;
        for (int i = 0; i < one.length; i++) {
            if (one[i] <= two[i])
                sum_dom += 1;
            if (one[i] < two[i])
                sum_les += 1;
        }
        if (sum_dom==one.length && sum_les>0)
            return true;
        return false;
    }


}
