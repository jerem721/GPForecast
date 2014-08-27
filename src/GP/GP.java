package GP;

import directionalChanges.algorithm.Market;
import directionalChanges.algorithm.events.EEvent;
import logger.Log;
import properties.GPProperties;
import statistic.Statistics;
import syntax.PrimitiveSet;

import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

/**
 * Class to create and launch the GP Algorithm
 */
public class GP{

    private Random                          rd;
    private PrimitiveSet                    primitiveSet;
    private Population                      population;
    private Market                          market;
    private Hashtable<Double, List<EEvent>> dcData;
    private Statistics                      statistics;
    private GPProperties                    gpProperties;

    private Fitness                         bestFitness;


    public GP(GPProperties gpProperties, PrimitiveSet primitiveSet, Market market, Hashtable<Double,
            List<EEvent>> dcData, Statistics statistics)
    {
        rd = new Random();
        this.gpProperties = gpProperties;
        this.primitiveSet = primitiveSet;
        this.market = market;
        this.dcData = dcData;
        this.statistics = statistics;
        bestFitness = null;
    }

    /**
     * Function to start the GP.
     */
    public void start()
    {
        int         i;

        i = 0;
        population = new Population();
        // create an initial population randomly with the Ramped_Half-And-Half method.
        population.createPopulation(Population.EPopulationGeneration.RAMPED_HALF_AND_HALF, gpProperties.getPopulationSize(),
                gpProperties.getMaxDepthSizeInitial(), primitiveSet, gpProperties.getPrimProbability());
        bestFitness = null;
        market.resetMarket(); // reset the market to the first value.
        while (i < gpProperties.getNumberOfGeneration())
        {
            Log.getInstance().log("\n========== Generation " + i + " ==========");

            //Compute the fitnessof the current population.
            population.fitnessFunction(gpProperties.getNumberOfTrainingMoney(), gpProperties.getNumberOfStock(),
                    gpProperties.getNumberOfTrainingValue(), market, dcData);
            //Check if the best fitness of the current population is better than the current best fitness.
           if (bestFitness == null || bestFitness.isBest(population.getBestFitness()))
                bestFitness = population.getBestFitness();
            population.printBestFitness();
            // Order the population by fitness.
            population.sortPopulation();
            if ((i + 1) < gpProperties.getNumberOfGeneration())
                population = breed(); // Create the next population of individuals.
            i++;
        }
        // Run the best individual of the run on untested data.
        validateIndividual(gpProperties.getNumberOfTestingMoney(), gpProperties.getNumberOfStock());
        statistics.addFitness(bestFitness);
    }

    /**
     * Method to create the next population from the current population.
     * Used Mutation, Crossover, Reproduction operators and Elitism.
     * @return  The next population.
     */
    private Population breed()
    {
        Population  nextPopulation;
        int         elitism;
        double      probability;

        nextPopulation = new Population();
        elitism = (int)(gpProperties.getElitismPercentage() * gpProperties.getPopulationSize());
        for (int i = 0; i < gpProperties.getPopulationSize(); i++) {
            probability = rd.nextDouble();
            if (elitism > 0)
            {
                nextPopulation.addIndividual(population.getIndividuals().get(elitism - 1));
                elitism--;
                continue;
            }else if (probability <= gpProperties.getReproductionProbability())
                nextPopulation.addIndividual(population.reproduction());
            else if (probability >= (1.0 - gpProperties.getMutationProbability()))
                nextPopulation.addIndividual(population.mutate(gpProperties.getTournamentSize(),
                        gpProperties.getMaxDepthSize(), primitiveSet, gpProperties.getPrimProbability()));
            else
                nextPopulation.addIndividual(population.crossover(gpProperties.getTournamentSize(),
                        gpProperties.getTerminalNodeBias(), gpProperties.getMaxDepthSize()));
        }
        return nextPopulation;
    }

    /**
     * Method to check the best individual on untested function.
     * @param account       account of money for the test.
     * @param totalStock    total of stock available for the test.
     */
    private void validateIndividual(double account, int totalStock)
    {
        double          currentPrice;
        int             numberOfStock;
        boolean         buy;
        double          fitness;
        DecimalFormat   df;

        if (bestFitness != null)
        {
            df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            numberOfStock = 0;
            currentPrice = 0.0;
            for (int index = gpProperties.getNumberOfTrainingValue();
                 index < gpProperties.getNumberOfTrainingValue() + gpProperties.getNumberOfTestingValue(); index++)
            {
                currentPrice = market.getPrice(index).getPrice();
                buy = bestFitness.getTree().evaluate(index, dcData);
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
            Log.getInstance().log("\n\n=== BEST PREDICTOR PROGRAM ===");
            Log.getInstance().log("Training fitness: " + df.format(bestFitness.getTrainingValue()));
            Log.getInstance().log("Testing fitness: " + df.format(fitness));
            Log.getInstance().log(bestFitness.getTree().print(""));
            bestFitness.setTestingValue(fitness);
        }
    }
}
