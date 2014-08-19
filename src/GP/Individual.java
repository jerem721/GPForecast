package GP;

import directionalChanges.algorithm.Market;
import syntax.IExpression;

/**
 * Created by jerem on 11/08/14.
 */
public class Individual implements Comparable<Individual>{

    private IExpression         treeRoot;
    private double              fitness;

    public Individual(IExpression treeRoot)
    {
        this.treeRoot = treeRoot;
        fitness = 0.0;
    }

    public double evaluate(double account, int stock, int numberOfTrainingValue, Market market)
    {
        for (int index = 0; index <= numberOfTrainingValue; index++)
        {
            treeRoot.evaluate(index);
        }
        return 0.0;
    }

    public String print()
    {
        return treeRoot.print("");
    }

    public IExpression getTreeRoot()
    {
        return treeRoot;
    }

    public void setTreeRoot(IExpression treeRoot)
    {
        this.treeRoot = treeRoot;
    }

    public void setFitness(double fitness)
    {
        this.fitness = fitness;
    }

    public double getFitness()
    {
        return fitness;
    }

    public Individual clone()
    {
        Individual  copy;

        copy = new Individual(treeRoot.clone());
        copy.setFitness(fitness);
        return copy;
    }

    @Override
    public int compareTo(Individual o) {
        if (fitness > o.getFitness())
            return 1;
        if (fitness < o.getFitness())
            return -1;
        return  0;
    }
}
