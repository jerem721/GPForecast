package GP;

import directionalChanges.algorithm.Market;
import directionalChanges.algorithm.events.EEvent;
import syntax.IExpression;

import java.util.Hashtable;
import java.util.List;

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

    public double evaluate(double account, int totalStock, int numberOfTrainingValue, Market market,
                           Hashtable<Double, List<EEvent>> dcData)
    {
        double  currentPrice;
        int     numberOfStock;
        boolean buy;
        Boolean oldAction;

        numberOfStock = 0;
        currentPrice = 0.0;
        oldAction = null;
        for (int index = 0; index <= numberOfTrainingValue; index++)
        {
            currentPrice = market.getPrice(index).getPrice();
            buy = treeRoot.evaluate(index, dcData);
            if ((oldAction == null || oldAction == false) && buy == true && account >= currentPrice && totalStock > 0)
            {
                totalStock--;
                numberOfStock++;
                account -= currentPrice;
                oldAction = true;
            }else if ((oldAction == null || oldAction == true) && buy == false && numberOfStock > 0)
            {
                totalStock++;
                numberOfStock--;
                account += currentPrice;
                oldAction = false;
            }
        }
        fitness = account + (numberOfStock * currentPrice);
        return fitness;
    }

    public String print()
    {
        return "Fitness: " + fitness +
               "\n" + treeRoot.print("");
        //return "Fitness: " + fitness;
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
            return -1;
        if (fitness < o.getFitness())
            return 1;
        return  0;
    }
}
