package directionalChanges.algorithm.runs;

import directionalChanges.algorithm.Price;
import directionalChanges.algorithm.events.IEvent;

/**
 * Created by jerem on 02/08/14.
 */
public abstract class ARun implements IRun
{

    protected ERun          eventType;
    protected IEvent        event;
    protected IEvent        overshootEvent;

    public ARun(ERun eventType, IEvent event, IEvent overshootEvent)
    {
        this.eventType = eventType;
        this.event = event;
        this.overshootEvent = overshootEvent;
    }

    public ERun getRunType()
    {
        return eventType;
    }

    public void setRunType(ERun eventType)
    {
        this.eventType = eventType;
    }

    @Override
    public IEvent getEvent() {
        return event;
    }

    public void setEvent(IEvent event){
        this.event = event;
    }

    @Override
    public IEvent getOvershootEvent() {
        return overshootEvent;
    }

    public void setOvershootEvent(IEvent event){
       this.overshootEvent = event;
    }

    @Override
    public int getDirectionalChangeIndex() {
        return event.getEndingPointIndex();
    }

    @Override
    public Price getDirectionalChangePrice() {
        return event.getEndingPointPrice();
    }

    @Override
    public final int getTotalMove() {
        int     total = 0;

        if (event != null)
            total += event.getTotalMove();
        if (overshootEvent != null)
            total += overshootEvent.getTotalMove();
        return total;
    }

    @Override
    public String toString() {
        return "ARun{" +
                "eventType=" + eventType +
                ", event=" + event.toString() +
                ", overshootEvent=" + overshootEvent.toString() +
                '}';
    }


}
