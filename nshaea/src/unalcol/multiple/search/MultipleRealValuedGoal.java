package unalcol.multiple.search;

import unalcol.search.Goal;

public interface MultipleRealValuedGoal<T> extends Goal<T, Double[]> {
    public boolean[] minimizing();
    public void minimize( boolean[] minimize );
}
