package directionalChanges.algorithm.runs;

import directionalChanges.algorithm.Price;

/**
 * Created by jerem on 02/08/14.
 */
public class UpwardRun extends ARun {

    private Price highPrice;

    public UpwardRun(int initialIndex, Price initialPrice) {
        super(ERun.UPWARD_RUN, null, null);
                //new UpturnEvent(initialIndex, initialPrice),
                //new UpturnOvershootEvent(initialIndex, initialPrice));
        this.highPrice = initialPrice;
    }

    @Override
    public final boolean isDirectionalChange(Price current, double threshold) {
        return current.getPrice() <= highPrice.getPrice() * (1.0 - (threshold / 100.0));
    }

    public final boolean isHighestPrice(Price price)
    {
        if (highPrice == null)
            return false;
        return price.getPrice() > highPrice.getPrice();
    }

    public final void updateHighPrice(Price price)
    {
        highPrice = price;
    }

    @Override
    public String toString() {
        return "UpwardRun{" +
                event.toString() +
                ", " + overshootEvent.toString() +
                ", HighPrice=" + highPrice +
                '}';
    }
}
