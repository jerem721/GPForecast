package GP;

import logger.Log;
import properties.PropertiesGp;
import syntax.PrimitiveSet;

import java.util.Random;

/**
 * Created by jerem on 12/08/14.
 */
public class GP{

    private Random          rd;
    private PrimitiveSet    primitiveSet;
    private Population      population;

    private int             populationSize;
    private int             maxDepthSizeInitial;
    private int             maxDepthSize;
    private int             tournamentSize;
    private int             numberOfGeneration;
    private double          elitismPercentage;
    private double          reproductionProbability;
    private double          mutationProbability;
    private double          terminalNodeBias;
    private double          primProbability;

    private int             numberOfRandomConstant;
    private double          maxThresholdDC;
    private double          minThresholdDC;

    public GP(PropertiesGp propertiesGp, PrimitiveSet primitiveSet)
    {
        rd = new Random();
        this.primitiveSet = primitiveSet;
        population = new Population();

        Log.getInstance().log("\n===== Configuration GP =====");

        populationSize = propertiesGp.getIntProperty("populationSize", 20);
        Log.getInstance().log("Population size : " + populationSize);
        maxDepthSize = propertiesGp.getIntProperty("maxDepthSize", 8);
        Log.getInstance().log("Max depth size : " + maxDepthSize);
        maxDepthSizeInitial = propertiesGp.getIntProperty(" maxDepthSizeInitial", 5);
        Log.getInstance().log("Max depth size initial : " + maxDepthSizeInitial);
        tournamentSize = propertiesGp.getIntProperty("tournamentSize", 2);
        Log.getInstance().log("Tournament size : " + tournamentSize);
        numberOfGeneration = propertiesGp.getIntProperty("numberOfGeneration", 30);
        Log.getInstance().log("Number of generation : " + numberOfGeneration);
        elitismPercentage = propertiesGp.getDoubleProperty("elitismPercentage", 0.01);
        Log.getInstance().log("Elitism percentage : " + elitismPercentage);
        reproductionProbability = propertiesGp.getDoubleProperty("reproductionProbability", 0.01);
        Log.getInstance().log("Reproduction probability : " + reproductionProbability);
        mutationProbability = propertiesGp.getDoubleProperty("mutationProbability", 0.01);
        Log.getInstance().log("Mutation probability : " + mutationProbability);
        terminalNodeBias = propertiesGp.getDoubleProperty("terminalNodeBias", 0.1);
        Log.getInstance().log("Terminal node bias : " + terminalNodeBias);
        primProbability = propertiesGp.getDoubleProperty("primProbability", 0.6);
        Log.getInstance().log("Prim probability : " + primProbability);

        numberOfRandomConstant = propertiesGp.getIntProperty("numberOfRandomConstant", 5);
        maxThresholdDC = propertiesGp.getDoubleProperty("maxThresholdDC", 100);
        minThresholdDC = propertiesGp.getDoubleProperty("minThresholdDC", 0);
    }

    public void start()
    {
        int         i;

        i = 0;
        population.createPopulation(Population.EPopulationGeneration.RAMPER_HALF_AND_HALF, populationSize,
                maxDepthSizeInitial, primitiveSet, primProbability);
        while (i < numberOfGeneration)
        {
            Log.getInstance().log("\n\n\n========== Run " + i + " ==========");
            population.print();

            population.fitnessFunction();
            population.sortPopulation();
            if (i < numberOfGeneration + 1)
                population = breed();
            i++;
        }
    }

    private Population breed()
    {
        Population  nextPopulation;
        int         elitism;
        double      probability;

        nextPopulation = new Population();
        elitism = (int)(elitismPercentage * populationSize);
        for (int i = 0; i < populationSize; i++) {
            probability = rd.nextDouble();
            if (elitism > 0)
            {
                nextPopulation.addIndividual(population.getIndividuals().get(elitism - 1));
                elitism--;
                continue;
            }else if (probability <= reproductionProbability)
                nextPopulation.addIndividual(population.reproduction());
            else if (probability >= (1.0 - mutationProbability))
                nextPopulation.addIndividual(population.mutate(tournamentSize, maxDepthSize,
                        primitiveSet, primProbability));
            else
                nextPopulation.addIndividual(population.crossover(tournamentSize, terminalNodeBias, maxDepthSize));
        }
        return nextPopulation;
    }
}
