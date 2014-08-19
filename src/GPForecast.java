
import GP.GP;
import directionalChanges.DirectionalChanges;
import directionalChanges.algorithm.Market;
import directionalChanges.algorithm.events.IEvent;
import directionalChanges.algorithm.runs.IRun;
import file.ReaderFile;
import logger.Log;
import properties.PropertiesGp;
import syntax.Function.EFunction;
import syntax.PrimitiveSet;
import syntax.Terminal.Constant;

import java.text.DecimalFormat;
import java.util.*;

public class GPForecast {

    private DirectionalChanges dc;

    private int                 numberOfRandomConstant;
    private double              maxThresholdDC;
    private double              minThresholdDC;

    private Market              market;
    private PropertiesGp        propertiesGp;
    private PrimitiveSet        primitiveSet;
    private GP                  gp;

    public static void main(String[] args) {
        GPForecast GPForecast;

        Log.getInstance().enableLog(true);

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

        Log.getInstance().log("===== Configuration DC =====");
        Log.getInstance().log("Data file : " + args[0]);
        Log.getInstance().log("Output dir : " + args[1]);
        numberOfRandomConstant = propertiesGp.getIntProperty("numberOfRandomConstant", 5);
        Log.getInstance().log("Number of random constant : " + numberOfRandomConstant);
        maxThresholdDC = propertiesGp.getDoubleProperty("maxThresholdDC", 100);
        Log.getInstance().log("Maximum threshold : " + maxThresholdDC);
        minThresholdDC = propertiesGp.getDoubleProperty("minThresholdDC", 0);
        Log.getInstance().log("Minimum threshold : " + minThresholdDC);

        primitiveSet = new PrimitiveSet();

        market = new Market();
        initMarket(args[0]);

        gp = new GP(propertiesGp, primitiveSet, market);
        dc = new DirectionalChanges(args[1], market);

        initFunctionSet(primitiveSet);
        initTerminalSet(primitiveSet);

        gp.start();
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
            threshold = Double.parseDouble(df.format(((rd.nextDouble() * (maxThresholdDC - minThresholdDC)) + minThresholdDC)));
            dc.start(threshold);
            primitiveSet.addTerminal(new Constant(new Double(threshold), dcListener.getEvents()));
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

}

