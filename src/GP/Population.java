package GP;

import directionalChanges.algorithm.Market;
import directionalChanges.algorithm.events.EEvent;
import logger.Log;
import syntax.IExpression;
import syntax.PrimitiveSet;

import javax.rmi.CORBA.Util;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Class to manage individuals.
 */
public class Population {

    private List<Individual>    individuals;
    private Random              random;
    private Fitness             bestFitness;


    public enum EPopulationGeneration{
        GROW_METHOD, FULL_METHOD, RAMPED_HALF_AND_HALF;
    }

    public Population()
    {
        individuals = new ArrayList<Individual>();
        random = new Random();
    }

    /**
     * Add an individual into population.
     */
    public void addIndividual(Individual individual)
    {
        individuals.add(individual);
    }

    /**
     * Add an individual into population.
     */
    public void addIndividual(IExpression treeRoot)
    {
        individuals.add(new Individual(treeRoot));
    }

    /**
     * Return a list of individuals.
     */
    public List<Individual>  getIndividuals()
    {
        return individuals;
    }

    /**
     * Print every GDTs (individuals) contained in the population and the best fitness.
     */
    public void print(){
        int     i;

        i = 1;
        if (bestFitness != null)
            Log.getInstance().log("Best Fitness: " + bestFitness.getTrainingValue());
        for (Individual individual : individuals)
        {
            Log.getInstance().log("\n===== Individual " + i + " =====");
            Log.getInstance().log(individual.print());
            i++;
        }
    }

    /**
     * Print the best fitness of the population.
     */
    public void printBestFitness()
    {
        DecimalFormat df;

        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        Log.getInstance().log("Best fitness: " + df.format(bestFitness.getTrainingValue()));
    }

    /**
     * Initialise the population with GDTs (individuals) with either Grow or Full or Ramped half-and-half method
     */
    public void createPopulation(EPopulationGeneration type, int numberOfPopulation, int depth, PrimitiveSet primitiveSet, double primpProb)
    {
        individuals.clear();
        for (int i = 0; i < numberOfPopulation; i++)
        {
            switch (type)
            {
                case GROW_METHOD:
                    addIndividual(growMethod(depth, primitiveSet, primpProb));
                    break;
                case FULL_METHOD:
                    addIndividual(fullMethod(depth, primitiveSet));
                    break;
                case RAMPED_HALF_AND_HALF:
                    if (i % 2 == 0)
                        addIndividual(growMethod(random.nextInt(depth - 1) + 1, primitiveSet, primpProb));
                    else
                        addIndividual(fullMethod(random.nextInt(depth - 1) + 1, primitiveSet));
                    break;
            }
        }
    }

    /**
     * Generate a GDT (individual) with the full method.
     * @return  A GDT(individual)
     */
    private IExpression fullMethod(int depth, PrimitiveSet primitiveSet)
    {
        IExpression children[];
        IExpression function;

        if (depth > 1)
        {
            function = primitiveSet.getRandomFunction();
            children = new IExpression[function.getNumberChildren()];
            for (int i = 0; i < children.length; i++)
                children[i] = fullMethod(depth - 1, primitiveSet);
            function.setChildren(children);
            return function;
        }
        return primitiveSet.getRandomTerminal();
    }

    /**
     * Generate a GDT (individual) with the grow method.
     * @return  A GDT(individual)
     */
    private IExpression growMethod(int depth, PrimitiveSet primitiveSet, double primProb)
    {
        IExpression children[];
        IExpression function;

        if (depth > 1 && random.nextDouble() > primProb)
        {
            function = primitiveSet.getRandomFunction();
            children = new IExpression[function.getNumberChildren()];
            for (int i = 0; i < children.length; i++)
                children[i] = growMethod(depth - 1, primitiveSet, primProb);
            function.setChildren(children);
            return function;
        }
        return primitiveSet.getRandomTerminal();
    }

    /**
     * Launch the fitness function and evaluate each individual contained in the population.
     */
    public void fitnessFunction(double account, int stock, int numberOfTrainingValue, Market market, Hashtable<Double,
            List<EEvent>> dcData)
    {
        Iterator<Individual>        iterator;
        double                      fitness;
        Individual                  individual;

        bestFitness = new Fitness(0.0, null);
        for (iterator = individuals.iterator(); iterator.hasNext();)
        {
            individual = iterator.next();
            fitness = individual.evaluate(account, stock, numberOfTrainingValue, market, dcData);
            if (bestFitness.getTrainingValue() <= fitness)
            {
                bestFitness.setTrainingValue(fitness);
                bestFitness.setTree(individual.getTreeRoot());
            }
        }
    }

    /**
     * Return the best fitness of the population.
     */
    public Fitness getBestFitness()
    {
        return bestFitness;
    }

    /**
     * Set the specified fitness as the best fitness.
     */
    public void setBestFitness(Fitness bestFitness)
    {
        this.bestFitness = bestFitness;
    }

    /**
     * Sort individuals.
     */
    public void sortPopulation()
    {
        Collections.sort(individuals);
    }

    /**
     * Tournament selection to select randomly an individual in the population.
     * @return An individual.
     */
    public Individual tournamentSelection(int tournamentSize)
    {
        List<Individual>        individuals;
        Individual              best;
        Individual              competitor;

        individuals = getIndividuals();
        best = individuals.get(random.nextInt(individuals.size() - 1));
        for (int i = 0; i < tournamentSize; i++)
        {
            competitor = individuals.get(random.nextInt(individuals.size() - 1));
            if (competitor.getFitness() > best.getFitness())
                best = competitor;
        }
        return best;
    }

    /**
     * Crossover operator.
     * @return  An individual.
     */
    public Individual crossover(int tournamentSize, double terminalNodeBias, int maxDepth)
    {
        Individual  copy1;
        Individual  copy2;
        IExpression randomNode1;
        IExpression randomNode2;
        int         maxAllowedDepth;

        copy1 = tournamentSelection(tournamentSize).clone();
        copy2 = tournamentSelection(tournamentSize).clone();
        randomNode1 = Utils.selectRandomNode(copy1.getTreeRoot(), 0.5, random);
        maxAllowedDepth = maxDepth - Utils.getDepth(copy1.getTreeRoot(), randomNode1);
        randomNode2 = Utils.selectRandomNode(copy2.getTreeRoot(), maxAllowedDepth, random);
        Utils.replace(copy1, randomNode1, randomNode2);
        return copy1;
    }

    /**
     * Mutation Operator.
     * @return An individual.
     */
    public Individual mutate(int tournamentSize, int maxDepth, PrimitiveSet primitiveSet, double primProb)
    {
        Individual  copy;
        IExpression randomNode;
        IExpression newNode;
        int         maxDepthAllow;

        copy = tournamentSelection(tournamentSize).clone();
        randomNode = Utils.selectRandomNode(copy.getTreeRoot(), random);
        if (randomNode == copy.getTreeRoot())
            return new Individual(random.nextDouble() < 0.5
                                        ? growMethod(maxDepth, primitiveSet, primProb)
                                        : fullMethod(maxDepth, primitiveSet));
        maxDepthAllow = maxDepth - Utils.getDepth(copy.getTreeRoot(), randomNode);
        if (maxDepthAllow != 0)
            maxDepthAllow = random.nextInt(maxDepthAllow) + 1;
        if (random.nextDouble() < 0.5)
            newNode = fullMethod(maxDepthAllow, primitiveSet);
        else
            newNode = growMethod(maxDepthAllow, primitiveSet, primProb);
        Utils.replace(copy, randomNode, newNode);
        return copy;
    }

    /**
     * Reproduction Operator.
     * @return An individual.
     */
    public Individual reproduction()
    {
        return individuals.get(random.nextInt(individuals.size())).clone();
    }
}
