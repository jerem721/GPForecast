
import GP.GP;
import directionalChanges.DirectionalChanges;
import directionalChanges.algorithm.Market;
import directionalChanges.algorithm.events.EEvent;
import directionalChanges.algorithm.events.IEvent;
import directionalChanges.algorithm.runs.IRun;
import file.AFile;
import file.ReaderFile;
import logger.Log;
import properties.PropertiesGp;
import statistic.Statistics;
import syntax.Function.EFunction;
import syntax.PrimitiveSet;
import syntax.Terminal.Constant;

import java.text.DecimalFormat;
import java.util.*;

public class GPForecast {

    private DirectionalChanges dc;

    private int                                 numberOfRandomConstant;
    private double                              maxThresholdDC;
    private double                              minThresholdDC;

    private Market                              market;
    private PropertiesGp                        propertiesGp;
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
            System.out.println("./GeneticProgramming <input file> <output dir> [<config file>]");
            System.exit(0);
        }else  if (args.length > 2)
            propertiesGp = new PropertiesGp(args[2]);
        else
            propertiesGp = new PropertiesGp();

        Log.getInstance().enableLog(true);
        Log.getInstance().logInFile(true, args[1]);
        statistics = new Statistics(args[1]);

        market = new Market();
        initMarket(args[0]);

        for (int i = 1; i <= 30; i++)
        {
            Log.getInstance().changePathLog(args[1] + "/Run" + i, "results.txt");
            Log.getInstance().log("===== Configuration DC =====");
            Log.getInstance().log("Data file : " + args[0]);
            Log.getInstance().log("Output dir : " + args[1]);
            numberOfRandomConstant = propertiesGp.getIntProperty("numberOfRandomConstant", 5);
            Log.getInstance().log("Number of random constant : " + numberOfRandomConstant);
            maxThresholdDC = propertiesGp.getDoubleProperty("maxThresholdDC", 100);
            Log.getInstance().log("Maximum threshold : " + maxThresholdDC);
            minThresholdDC = propertiesGp.getDoubleProperty("minThresholdDC", 0);
            Log.getInstance().log("Minimum threshold : " + minThresholdDC);

            dcData = new Hashtable<Double, List<EEvent>>();
            primitiveSet = new PrimitiveSet();

            gp = new GP(propertiesGp, primitiveSet, market, dcData, statistics);
            AFile.createDirectory(args[1] + "/Run" + i + "/dc");
            dc = new DirectionalChanges(args[1] + "/Run" + i + "/dc", market);

            initFunctionSet(primitiveSet);
            initTerminalSet(primitiveSet);

            gp.start();
        }
        statistics.computeStatistics(args[1]);
        resultWithoutGP(args[1]);
    }

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

    private void initTerminalSet(PrimitiveSet primitiveSet)
    {
        DecimalFormat       df              = new DecimalFormat();
        double              threshold;
        Random              rd              = new Random();
        DCListener          dcListener;

        Log.getInstance().log("\n===== Terminal Set =====");
        df.setMaximumFractionDigits(2);
        for (int i = numberOfRandomConstant; i > 0; i--)
        {
            dcListener = new DCListener();
            dc.setListener(dcListener);
            threshold = Double.parseDouble(df.format(Math.round((((rd.nextDouble() * (maxThresholdDC - minThresholdDC)) + minThresholdDC) / 0.05)) * 0.05));
            dc.start(threshold);
            primitiveSet.addTerminal(new Constant(new Double(threshold)));
            dcData.put(threshold, dcListener.getEvents());
            Log.getInstance().log("- Constant : " + threshold);
        }
    }

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

    private void resultWithoutGP(String dir){
        String              thresholds[];
        ForecastThreshold   forecast;

        thresholds = propertiesGp.getStringProperty("thresholdWithoutGP", "1 2 3 4").split(" ");
        Log.getInstance().logInFile(false, null);
        AFile.createDirectory(dir + "/RunWithoutGP");
        forecast = new ForecastThreshold(propertiesGp, market, statistics);
        for (String threshold : thresholds)
            forecast.computeFitness(Double.parseDouble(threshold), dir + "/RunWithoutGP/");
    }

}

