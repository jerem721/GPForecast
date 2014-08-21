package statistic;

import GP.Fitness;
import logger.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerem on 21/08/14.
 */
public class Statistics {

    private static Statistics      statistics;

    private List<Fitness>   fitnesses;

    private Statistics(){
        fitnesses = new ArrayList<Fitness>();
    }

    public static Statistics getInstance()
    {
        if (statistics == null)
            statistics = new Statistics();
        return statistics;
    }

    public void addFitness(Fitness fitness)
    {
        fitnesses.add(fitness);
    }

    public void computeStatistics(String dir)
    {
        DecimalFormat   df;

        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        Log.getInstance().changePathLog(dir, "statistics.txt");
        Log.getInstance().log("Training average: " + df.format(trainingFitnessAverage()));
        Log.getInstance().log("Testing average: " + df.format(testingFitnessAverage()));
    }

    public double trainingFitnessAverage()
    {
        double          result;

        result = 0.0;
        for (Fitness fitness : fitnesses)
            result += fitness.getTrainingValue();
        return result / fitnesses.size();
    }

    public double testingFitnessAverage()
    {
        double              result;

        result = 0.0;
        for (Fitness fitness : fitnesses)
            result += fitness.getTestingValue();
        return result / fitnesses.size();
    }
}
