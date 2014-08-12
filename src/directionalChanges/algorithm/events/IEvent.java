package directionalChanges.algorithm.events;

import directionalChanges.algorithm.Price;

/**
 * Created by jerem on 02/08/14.
 */
public interface IEvent {

    public void     setStartingPoint(int index, Price price);
    public void     setEndingPoint(int index, Price price);
    public int      getStartingPointIndex();
    public Price getStartingPointPrice();
    public int      getEndingPointIndex();
    public Price getEndingPointPrice();
    public int      getTotalMove();
}
