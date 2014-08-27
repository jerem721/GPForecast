import directionalChanges.DirectionalChanges;
import directionalChanges.algorithm.Market;
import directionalChanges.algorithm.events.EEvent;
import logger.Log;
import properties.GPProperties;
import statistic.Statistics;
import syntax.IExpression;
import syntax.Terminal.Constant;

import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.List;

/**
 * Class to evaluate the training and testing data with a specified threshold.
 */
public class ForecastThreshold {

    private int         numberOfStock;
    private double      numberOfTestingMoney;
    private double      numberOfTrainingMoney;
    private int         numberOfTestingValue;
    private int         numberOfTrainingValue;

    private Statistics  statistics;
    private Market      market;

    public ForecastThreshold(GPProperties gpProperties, Market market, Statistics statistics) {
        this.statistics = statistics;
        this.market = market;

        numberOfStock =  gpProperties.getNumberOfStock();
        numberOfTrainingMoney = gpProperties.getNumberOfTrainingMoney();
        numberOfTestingMoney = gpProperties.getNumberOfTestingMoney();
        numberOfTrainingValue = gpProperties.getNumberOfTrainingValue();
        numberOfTestingValue = gpProperties.getNumberOfTestingValue();
    }

    /**
     * Compute the fitness of the specified threshold.
     */
    public void computeFitness(double threshold, String dir){
        Hashtable<Double, List<EEvent>> dcData;
        DCListener                      dcListener;
        DirectionalChanges              dc;

        dcData = new Hashtable<Double, List<EEvent>>();

        statistics.write("\n\n=== Fitness with the threshold " + threshold + " ===");
        Log.getInstance().log("\n\n=== Fitness with the threshold " + threshold + " ===");
        dcListener = new DCListener();
        dc = new DirectionalChanges(dir, market);
        dc.setListener(dcListener);
        dc.start(threshold);
        dcData.put(threshold, dcListener.getEvents());
        trainingFitness(numberOfTrainingMoney, numberOfStock, dcData, new Constant(threshold));
        testingFitness(numberOfTestingMoney, numberOfStock, dcData, new Constant(threshold));
    }

    /**
     * Compute the fitness with the training data.
     */
    public void trainingFitness(double money, int totalOfStock, Hashtable<Double, List<EEvent>> events, IExpression expression)
    {
        int             from;
        int             to;
        double          fitness;
        DecimalFormat   df;

        from = 0;
        to = (numberOfTrainingValue < market.getStocks().size()) ? numberOfTrainingValue : (market.getStocks().size()/2);
        fitness = computeFitness(expression, money, totalOfStock, from, to, events);
        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        statistics.write("Training fitness: " + df.format(fitness));
        Log.getInstance().log("Training fitness: " + df.format(fitness));
    }

    /**
     * Compute the fitness with the testing data.
     */
    public void testingFitness(double money, int totalOfStock, Hashtable<Double, List<EEvent>> events, IExpression expression)
    {
        int             from;
        int             to;
        double          fitness;
        DecimalFormat   df;

        from = (numberOfTrainingValue < market.getStocks().size()) ? numberOfTrainingValue : (market.getStocks().size()/2);;
        to = (numberOfTrainingValue + numberOfTestingValue < market.getStocks().size()) ? (numberOfTrainingValue + numberOfTestingValue)
                                                                                        : market.getStocks().size();
        fitness = computeFitness(expression, money, totalOfStock, from, to, events);
        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        statistics.write("Testing fitness: " + df.format(fitness));
        Log.getInstance().log("Testing fitness: " + df.format(fitness));
    }

    private double computeFitness(IExpression expression, double account, int totalStock, int from, int to,
                                  Hashtable<Double, List<EEvent>> events)
    {
        double          currentPrice;
        int             numberOfStock;
        boolean         buy;

        numberOfStock = 0;
        currentPrice = 0.0;
        for (int index = from; index < to; index++)
        {
            currentPrice = market.getPrice(index).getPrice();
            buy = expression.evaluate(index, events);
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
}
