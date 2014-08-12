package directionalChanges.algorithm.runs;

import directionalChanges.algorithm.Price;

/**
 * Created by jerem on 02/08/14.
 */
public class DownwardRun extends ARun {

    private Price lowPrice;

    public DownwardRun(int initialIndex, Price initialPrice) {
        super(ERun.DOWNWARD_RUN, null, null);
                //new DownturnEvent(initialIndex, initialPrice),
                //new DownturnOvershootEvent(initialIndex, initialPrice));
        this.lowPrice = initialPrice;
    }

    @Override
    public boolean isDirectionalChange(Price current, double threshold) {
        return current.getPrice() >= lowPrice.getPrice() * (1.0 + (threshold / 100.0));
    }

    public final boolean isLowestPrice(Price price)
    {
        if (lowPrice == null)
            return false;
        return price.getPrice() < lowPrice.getPrice();
    }

    public final void updateLowPrice(Price price)
    {
        lowPrice = price;
    }

    @Override
    public String toString() {
        return "DownwardRun{" +
                event.toString() +
                ", " + overshootEvent.toString() +
                ", LowPrice=" + lowPrice +
                '}';
    }
}
