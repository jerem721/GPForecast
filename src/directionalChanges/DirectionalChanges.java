package directionalChanges;



import directionalChanges.algorithm.Algorithm;
import directionalChanges.algorithm.Market;
import directionalChanges.algorithm.runs.IRun;
import file.WriterFile;

/**
 * Compute the direction changes of the price in a specified market.
 */
public class DirectionalChanges implements Algorithm.OnDirectionalChangeListener {

    private WriterFile                      outputFile;
    private String                          outFileDirectory;
    private Market                          market;
    private OnDirectionalChangesListener    listener;

    public interface OnDirectionalChangesListener{
        public void onDownwardRun(IRun downwardRun);
        public void onUpwardRun(IRun upwardRun);
    }

    public DirectionalChanges(String outputDirectory, Market market)
    {
        this.outFileDirectory = outputDirectory;
        this.market = market;
    }

    /**
     * Set a listener to get each event of the market
     */
    public void setListener(OnDirectionalChangesListener listener)
    {
        this.listener = listener;
    }

    /**
     * Run the algorithm of the Directional-Change method.
     */
    public void start(double threshold)
    {
        Algorithm algo;

        market.resetMarket();
        outputFile = new WriterFile(outFileDirectory +"/directional_changes_" + threshold + "%.txt");
        outputFile.write(threshold + "%\t\t\tStart\tPrice\tEnd\tPrice\t\tTotal move");
        market.resetMarket();
        algo = new Algorithm(market);
        algo.setOnDirectionalChangeListener(this);
        algo.launch(threshold);
        outputFile.close();
    }

    @Override
    public void onUpwardRun(IRun upwardRun) {

        if (upwardRun.getEvent() != null) {
          //  System.out.println("Upturn Event: " + upwardRun.getEvent());
            outputFile.write("Upturn\t\t\t" + upwardRun.getEvent().getStartingPointIndex() + "\t" + upwardRun.getEvent().getStartingPointPrice().getPrice()
                                + "\t " + upwardRun.getEvent().getEndingPointIndex() + "\t" + upwardRun.getEvent().getEndingPointPrice().getPrice()
                                + "\t\t" + upwardRun.getEvent().getTotalMove());
        }if (upwardRun.getOvershootEvent() != null) {
        //    System.out.println("UpturnOvershoot Event: " + upwardRun.getOvershootEvent());
            outputFile.write("UpturnOvershoot \t"+ upwardRun.getOvershootEvent().getStartingPointIndex() + "\t" + upwardRun.getOvershootEvent().getStartingPointPrice().getPrice()
                            + "\t " + upwardRun.getOvershootEvent().getEndingPointIndex() + "\t" + upwardRun.getOvershootEvent().getEndingPointPrice().getPrice()
                            + "\t\t" + upwardRun.getOvershootEvent().getTotalMove());
        }

        if (listener != null)
            listener.onUpwardRun(upwardRun);
    }

    @Override
    public void onDownwardRun(IRun downwardRun) {

        if (downwardRun.getEvent() != null) {
        //    System.out.println("Downturn Event: " + downwardRun.getEvent());
            outputFile.write("Downturn\t\t" + downwardRun.getEvent().getStartingPointIndex() + "\t" + downwardRun.getEvent().getStartingPointPrice().getPrice()
                    + "\t " + downwardRun.getEvent().getEndingPointIndex() + "\t" + downwardRun.getEvent().getEndingPointPrice().getPrice()
                    + "\t\t" + downwardRun.getEvent().getTotalMove());
        }if (downwardRun.getOvershootEvent() != null) {
        //    System.out.println("DownturnOvershoot Event: " + downwardRun.getOvershootEvent());
            outputFile.write("DownturnOvershoot \t"+ downwardRun.getOvershootEvent().getStartingPointIndex() + "\t" + downwardRun.getOvershootEvent().getStartingPointPrice().getPrice()
                    + "\t " + downwardRun.getOvershootEvent().getEndingPointIndex() + "\t" + downwardRun.getOvershootEvent().getEndingPointPrice().getPrice()
                    + "\t\t" + downwardRun.getOvershootEvent().getTotalMove());
        }

        if (listener != null)
            listener.onDownwardRun(downwardRun);
    }
}
