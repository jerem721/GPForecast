
import GP.GP;
import directionalChanges.DirectionalChanges;
import directionalChanges.algorithm.Market;
import directionalChanges.algorithm.events.EEvent;
import file.AFile;
import file.ReaderFile;
import logger.Log;
import properties.DCProperties;
import properties.GPProperties;
import statistic.Statistics;
import syntax.Function.EFunction;
import syntax.PrimitiveSet;
import syntax.Terminal.Constant;

import java.text.DecimalFormat;
import java.util.*;

public class GPForecast {

    private DirectionalChanges                  dc;
    private Market                              market;
    private DCProperties                        dcProperties;
    private GPProperties                        gpProperties;
    private PrimitiveSet                        primitiveSet;
    private GP                                  gp;
    private Hashtable<Double, List<EEvent>>     dcData;
    private Statistics                          statistics;

    public static void main(String[] args) {
        GPForecast GPForecast;

        GPForecast = new GPForecast();
        GPForecast.launch(args);
    }

    public void launch(String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("./GPForecast <input file> <output dir> [<config file>]");
            System.exit(0);
        }

        AFile.deleteDirectory(args[1]); // Delete all folder and file in the result directory.
        Log.getInstance().enableLog(true); // Enable logger.
        statistics = new Statistics(args[1]);

        market = new Market();
        initMarket(args[0]);
        // Get the Directional-Change properties.
        dcProperties = new DCProperties((args.length > 2) ? args[2] : null, args[0], args[1]);
        dcProperties.save();
        // Get the Genetic-Programming properties.
        gpProperties = new GPProperties((args.length > 2) ? args[2] : null, args[1], market.getStocks().size());
        gpProperties.save();

        Log.getInstance().log(dcProperties.toString());
        Log.getInstance().log(gpProperties.toString());

        Log.getInstance().logInFile(true, args[1]); // Enable the logger to write in a file.
        for (int i = 1; i <= 30; i++)
        {
            Log.getInstance().changePathLog(args[1] + "/Run" + i, "results.txt");
            dcData = new Hashtable<Double, List<EEvent>>();
            primitiveSet = new PrimitiveSet();

            gp = new GP(gpProperties, primitiveSet, market, dcData, statistics);
            AFile.createDirectory(dcProperties.getOutFolder() + "/Run" + i + "/dc");
            dc = new DirectionalChanges(dcProperties.getOutFolder() + "/Run" + i + "/dc", market);

            initFunctionSet(primitiveSet); // init the function set.
            initTerminalSet(primitiveSet); // init the terminal set.

            gp.start(); // start the GP algorithm.
        }
        statistics.computeStatistics(args[1]); // compute averages.
        resultWithoutGP(args[1]); // Test the market without GP algorithm.
    }

    /**
     * Initialise the function set.
     */
    private void initFunctionSet(PrimitiveSet primitiveSet)
    {
        Log.getInstance().log("\n===== Function Set =====");
        primitiveSet.addFunction(EFunction.AND);
        Log.getInstance().log("- Function AND");
        primitiveSet.addFunction(EFunction.NOR);
        Log.getInstance().log("- Function NOR");
        primitiveSet.addFunction(EFunction.NOT);
        Log.getInstance().log("- Function NOT");
        primitiveSet.addFunction(EFunction.OR);
        Log.getInstance().log("- Function OR");
        primitiveSet.addFunction(EFunction.XOR);
        Log.getInstance().log("- Function XOR");

    }

    /**
     * Initialise the terminal set.
     */
    private void initTerminalSet(PrimitiveSet primitiveSet)
    {
        DecimalFormat       df              = new DecimalFormat();
        double              threshold;
        Random              rd              = new Random();
        DCListener          dcListener;

        Log.getInstance().log("\n===== Terminal Set =====");
        df.setMaximumFractionDigits(2);
        for (int i = gpProperties.getnumberOfRandomThreshold(); i > 0; i--)
        {
            dcListener = new DCListener();
            dc.setListener(dcListener);
            threshold = Double.parseDouble(df.format(Math.round((((rd.nextDouble() * (dcProperties.getMaxThresholdDC()
                    - dcProperties.getMinThresholdDC())) + dcProperties.getMinThresholdDC()) / 0.05)) * 0.05));
            dc.start(threshold);
            primitiveSet.addTerminal(new Constant(new Double(threshold)));
            dcData.put(threshold, dcListener.getEvents());
            Log.getInstance().log("- Constant : " + threshold);
        }
    }

    /**
     * Initialise the market by reading the input file of historic data.
     */
    private void initMarket(String inputFile)
    {
        ReaderFile                  input;
        TreeMap<Integer, String>    stockPrices;

        input = new ReaderFile(inputFile);
        stockPrices = input.read();
        for (Map.Entry<Integer, String> price : stockPrices.entrySet())
            market.addPrice(Double.parseDouble(price.getValue()));
        input.close();
    }

    /**
     * Test the market without GP with different thresholds.
     */
    private void resultWithoutGP(String dir){
        String              thresholds[];
        ForecastThreshold   forecast;

        thresholds = dcProperties.getThresholdTest().split(" ");
        Log.getInstance().logInFile(false, null);
        AFile.createDirectory(dir + "/RunWithoutGP");
        forecast = new ForecastThreshold(gpProperties, market, statistics);
        for (String threshold : thresholds)
            forecast.computeFitness(Double.parseDouble(threshold), dir + "/RunWithoutGP/");
    }

}

