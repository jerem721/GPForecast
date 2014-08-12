package directionalChanges.algorithm;

import directionalChanges.algorithm.Market;
import directionalChanges.algorithm.Price;
import directionalChanges.algorithm.events.DownturnEvent;
import directionalChanges.algorithm.events.DownturnOvershootEvent;
import directionalChanges.algorithm.events.UpturnEvent;
import directionalChanges.algorithm.events.UpturnOvershootEvent;
import directionalChanges.algorithm.runs.*;

/**
 * Created by jerem on 05/08/14.
 */
public class Algorithm2 {

    private Market                          market;
    private double                          threshold;

    private OnDirectionalChangeListener     listener;

    private UpwardRun                       upwardRun;
    private DownwardRun                     downwardRun;

    private UpturnEvent upturnEvent;
    private UpturnOvershootEvent upturnOvershootEvent;
    private DownturnEvent downturnEvent;
    private DownturnOvershootEvent downturnOvershootEvent;

    private ARun                            currentRun;

    public interface OnDirectionalChangeListener{
        public void onUpwardRun(IRun upwardRun);
        public void onDownwardRun(IRun downwardRun);
    }

    public Algorithm2(Market market)
    {
        this.market = market;
        upwardRun = new UpwardRun(0, market.getPrice(0));
        downwardRun = new DownwardRun(0, market.getPrice(0));
        currentRun = upwardRun;

        upturnEvent = new UpturnEvent(0, market.getPrice(0));
        upturnOvershootEvent = new UpturnOvershootEvent(0, market.getPrice(0));
        downturnEvent = new DownturnEvent(0, market.getPrice(0));
        downturnOvershootEvent = new DownturnOvershootEvent(0, market.getPrice(0));

    }

    public void setOnDirectionalChangeListener(OnDirectionalChangeListener listener)
    {
        this.listener = listener;
    }

    public void launch(double threshold){
        Price       currentPrice;

        this.threshold = threshold;
        if (market.isNext())
        {

            while (market.isNext()) {
                currentPrice = market.nextPrice();
                if (currentRun.getRunType() == ERun.UPWARD_RUN){
                    if (upwardRun.isDirectionalChange(currentPrice, threshold) == true)
                    {

                        if (upwardRun.getOvershootEvent() != null && upwardRun.getOvershootEvent().getTotalMove() <= 0)
                            upwardRun.setOvershootEvent(null);
                        if (listener != null)
                            listener.onUpwardRun(currentRun);

                        downwardRun = new DownwardRun(market.getIndex(), currentPrice);

                        downturnEvent.setEndingPoint(market.getIndex(), currentPrice);
                        downwardRun.setEvent(downturnEvent);
                        currentRun = downwardRun;

                        downturnOvershootEvent.setStartingPoint(market.getNextIndex(), market.getPrice(market.getNextIndex()));

                        upturnEvent.setStartingPoint(market.getNextIndex(), market.getPrice(market.getNextIndex()));
                    }
                    else if (upwardRun.isHighestPrice(currentPrice))
                    {
                        upwardRun.updateHighPrice(currentPrice);
                        upturnOvershootEvent.setEndingPoint(market.getPreviousIndex(), market.getPrice(market.getPreviousIndex()));
                        upwardRun.setOvershootEvent(upturnOvershootEvent);

                        downturnEvent.setStartingPoint(market.getIndex(), currentPrice);
                    }
                }
                else
                {
                    if (downwardRun.isDirectionalChange(currentPrice, threshold) == true)
                    {

                        if (downwardRun.getOvershootEvent() != null && downwardRun.getOvershootEvent().getTotalMove() <= 0)
                            downwardRun.setOvershootEvent(null);

                        if (listener != null)
                            listener.onDownwardRun(currentRun);

                        upwardRun = new UpwardRun(market.getIndex(), currentPrice);

                        upturnEvent.setEndingPoint(market.getIndex(), currentPrice);
                        upwardRun.setEvent(upturnEvent);
                        currentRun = upwardRun;

                        upturnOvershootEvent.setStartingPoint(market.getNextIndex(), market.getPrice(market.getNextIndex()));

                        downturnEvent.setStartingPoint(market.getNextIndex(), market.getPrice(market.getNextIndex()));
                    }
                    else if (downwardRun.isLowestPrice(currentPrice) == true)
                    {
                        downwardRun.updateLowPrice(currentPrice);
                        downturnOvershootEvent.setEndingPoint(market.getPreviousIndex(), market.getPrice(market.getPreviousIndex()));
                        downwardRun.setOvershootEvent(downturnOvershootEvent);
                        upturnEvent.setStartingPoint(market.getIndex(), currentPrice);
                    }
                }
            }
        }
    }
}
