package unalcol.multiple.search.solution;

public class ExperimentInfo{
    private static ExperimentInfo ex = null;
    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    private int iteration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    public static ExperimentInfo getInstance(){
        if(ex == null) {
            ex = new ExperimentInfo();
        }
        return ex;
    }
    private ExperimentInfo(){}

    @Override
    public String toString() {
        return name + iteration;
    }
}