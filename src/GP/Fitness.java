package GP;

import syntax.IExpression;

/**
 * Created by jerem on 19/08/14.
 */
public class Fitness{

    private double      value;
    private IExpression tree;

    public Fitness() {
        value = 0.0;
        tree = null;
    }

    public Fitness(double value, IExpression tree) {
        this.value = value;
        this.tree = tree;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public IExpression getTree() {
        return tree;
    }

    public void setTree(IExpression tree) {
        this.tree = tree;
    }

    public boolean isBest(Fitness fitness) {
        if (fitness.getValue() > value)
            return true;
        return false;
    }
}
