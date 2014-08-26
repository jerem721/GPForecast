package statistic;

import GP.Fitness;
import file.WriterFile;
import logger.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerem on 21/08/14.
 */
public class Statistics {

    private static Statistics      statistics;

    private WriterFile      writerFile;
    private List<Fitness>   fitnesses;

    public Statistics(String dir){
        fitnesses = new ArrayList<Fitness>();
        writerFile = new WriterFile(dir + "/statistics.txt");
    }

    public void addFitness(Fitness fitness)
    {
        fitnesses.add(fitness);
    }

    public void write(String str)
    {
        writerFile.write(str);
    }

    public void computeStatistics(String dir)
    {
        DecimalFormat   df;

        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        writerFile.write("======= With GP ======");
        writerFile.write("Training average: " + df.format(trainingFitnessAverage()));
        writerFile.write("Testing average: " + df.format(testingFitnessAverage()));
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
