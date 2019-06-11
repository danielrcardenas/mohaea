package unalcol.evolution.haea;

import unalcol.services.MicroService;
import unalcol.sort.Order;

public class SMOEADistanceOrder extends MicroService<SMOEATagged>  implements Order<SMOEATagged> {
    @Override
    public int compare(SMOEATagged one, SMOEATagged two) {
        return 0;
    }
}
