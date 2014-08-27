package GP;

import directionalChanges.algorithm.Market;
import directionalChanges.algorithm.events.EEvent;
import syntax.IExpression;

import java.util.Hashtable;
import java.util.List;

/**
 * Class to represent an individual in a population.
 */
public class Individual implements Comparable<Individual>{

    private IExpression         treeRoot;
    private double              fitness;

    public Individual(IExpression treeRoot)
    {
        this.treeRoot = treeRoot;
        fitness = 0.0;
    }

    /**
     * Evaluate the individual (fitness function) on training data.
     */
    public double evaluate(double account, int totalStock, int numberOfTrainingValue, Market market,
                           Hashtable<Double, List<EEvent>> dcData)
    {
        double  currentPrice;
        int     numberOfStock;
        boolean buy;

        numberOfStock = 0;
        currentPrice = 0.0;
        for (int index = 0; index <= numberOfTrainingValue; index++)
        {
            currentPrice = market.getPrice(index).getPrice();
            buy = treeRoot.evaluate(index, dcData);
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
        fitness = account + (numberOfStock * currentPrice);
        return fitness;
    }

    /**
     * Print the fitness and the GDT.
     */
    public String print()
    {
        return "Fitness: " + fitness +
               "\n" + treeRoot.print("");
    }

    /**
     * Return the GDT.
     */
    public IExpression getTreeRoot()
    {
        return treeRoot;
    }

    public void setTreeRoot(IExpression treeRoot)
    {
        this.treeRoot = treeRoot;
    }

    /**
     * Set the fitness of the individual.
     */
    public void setFitness(double fitness)
    {
        this.fitness = fitness;
    }

    /**
     * Return the fitness of the individual.
     */
    public double getFitness()
    {
        return fitness;
    }

    /**
     * Clone the individual.
     */
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
