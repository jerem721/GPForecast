
import GP.GP;
import directionalChanges.DirectionalChanges;
import directionalChanges.algorithm.events.IEvent;
import directionalChanges.algorithm.runs.IRun;
import logger.Log;
import properties.PropertiesGp;
import syntax.Function.EFunction;
import syntax.PrimitiveSet;
import syntax.Terminal.Constant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GPForecast implements DirectionalChanges.OnDirectionalChangesListener {

    private DirectionalChanges dc;

    private int                 numberOfRandomConstant;
    private double              maxThresholdDC;
    private double              minThresholdDC;

    private List<IEvent>        eventList;

    public static void main(String[] args) {
        GPForecast GPForecast;

        Log.getInstance().enableLog(true);

        GPForecast = new GPForecast();
        GPForecast.launch(args);
    }

    public void launch(String[] args)
    {
        PropertiesGp    propertiesGp;
        PrimitiveSet    primitiveSet;
        GP              gp;

        if (args.length < 2){
            System.out.println("./GeneticProgramming <input file> <output dir> [<config file>]");
            System.exit(0);
        }

        if (args.length > 2)
            propertiesGp = new PropertiesGp(args[2]);
        else
            propertiesGp = new PropertiesGp();
        primitiveSet = new PrimitiveSet();

        Log.getInstance().log("===== Configuration DC =====");
        Log.getInstance().log("Data file : " + args[0]);
        Log.getInstance().log("Output dir : " + args[1]);
        numberOfRandomConstant = propertiesGp.getIntProperty("numberOfRandomConstant", 5);
        Log.getInstance().log("Number of random constant : " + numberOfRandomConstant);
        maxThresholdDC = propertiesGp.getDoubleProperty("maxThresholdDC", 100);
        Log.getInstance().log("Maximum threshold : " + maxThresholdDC);
        minThresholdDC = propertiesGp.getDoubleProperty("minThresholdDC", 0);
        Log.getInstance().log("Minimum threshold : " + minThresholdDC);

        gp = new GP(propertiesGp, primitiveSet);
        dc = new DirectionalChanges(args[0], args[1]);

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

        Log.getInstance().log("\n===== Terminal Set =====");
        df.setMaximumFractionDigits(2);
        dc.init();
        dc.setListener(this);
        for (int i = numberOfRandomConstant; i > 0; i--)
        {
            eventList = new ArrayList<IEvent>();
            threshold = Double.parseDouble(df.format(((rd.nextDouble() * (maxThresholdDC - minThresholdDC)) + minThresholdDC)));
            dc.start(threshold);
            primitiveSet.addTerminal(new Constant(new Double(threshold), eventList));
            Log.getInstance().log("- Constant : " + threshold);
        }
        dc.finish();
    }

    @Override
    public void onDownwardRun(IRun downwardRun) {
        if (downwardRun != null && downwardRun.getEvent() != null && eventList != null)
            eventList.add(downwardRun.getEvent());
    }

    @Override
    public void onUpwardRun(IRun upwardRun) {
        if (upwardRun != null && upwardRun.getEvent() != null && eventList != null)
            eventList.add(upwardRun.getEvent());
    }
}

