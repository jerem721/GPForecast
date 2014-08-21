package GP;

import syntax.IExpression;

/**
 * Created by jerem on 19/08/14.
 */
public class Fitness{

    private double      trainingValue;
    private double      testingValue;
    private IExpression tree;

    public Fitness() {
        trainingValue = 0.0;
        testingValue = 0.0;
        tree = null;
    }

    public Fitness(double value, IExpression tree) {
        this.trainingValue = value;
        this.tree = tree;
    }

    public double getTrainingValue() {
        return trainingValue;
    }

    public void setTrainingValue(double trainingValue) {
        this.trainingValue = trainingValue;
    }

    public double getTestingValue() {
        return testingValue;
    }

    public void setTestingValue(double testingValue) {
        this.testingValue = testingValue;
    }

    public IExpression getTree() {
        return tree;
    }

    public void setTree(IExpression tree) {
        this.tree = tree;
    }

    public boolean isBest(Fitness fitness) {
        if (fitness.getTrainingValue() > trainingValue)
            return true;
        return false;
    }
}
