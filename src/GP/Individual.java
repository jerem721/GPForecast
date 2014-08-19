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

    public double evaluate(double account, int totalStock, int numberOfTrainingValue, Market market)
    {
        double  currentPrice;
        int     numberOfStock;
        boolean buy;

        numberOfStock = 0;
        currentPrice = 0.0;
        for (int index = 0; index <= numberOfTrainingValue; index++)
        {
            currentPrice = market.getPrice(index).getPrice();
            buy = treeRoot.evaluate(index);
            if (buy == true && account >= currentPrice && totalStock > 0)
            {
                totalStock--;
                numberOfStock++;
                account -= currentPrice;
            }else if (buy == false && numberOfStock > 0)
            {
                totalStock++;
                numberOfStock--;
                account += currentPrice;
            }
        }
        return account + (numberOfStock * currentPrice);
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
