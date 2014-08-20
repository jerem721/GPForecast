package syntax.Terminal;

import directionalChanges.algorithm.events.EEvent;
import directionalChanges.algorithm.events.IEvent;
import syntax.IExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerem on 11/08/14.
 */
public class Constant implements IExpression {

    private Double          value;              // threshold
    private IExpression     children[] = {};
    private List<EEvent>    directionalChangeEvent;

    public Constant(Double value)
    {
        this.value = value;
        directionalChangeEvent = new ArrayList<EEvent>();
    }

    public Constant(Double value, List<EEvent> eventList)
    {
        this.value = value;
        directionalChangeEvent = eventList;
    }

    @Override
    public String toString() {
        return "Variable= " + value;
    }

    @Override
    public Class getType() {
        return Constant.class;
    }

    @Override
    public IExpression[] getChildren() {
        return children;
    }

    @Override
    public void setChildren(IExpression[] children) {}

    public void addDCEvent(EEvent event)
    {
        directionalChangeEvent.add(event);
    }

    public List<EEvent> getDCEvent()
    {
        return directionalChangeEvent;
    }

    @Override
    public int             getNumberChildren(){
        return 0;
    }

    @Override
    public String print(String tab)
    {
        return tab + "(Constant " + value +")";
    }

    @Override
    public Boolean evaluate(int index) {
        if (index <= directionalChangeEvent.size())
        {
            switch (directionalChangeEvent.get(index))
            {
                case DOWNTURN:
                    return false;
                case DOWNTURN_OVERSHOOT:
                    return false;
                case UPTURN:
                    return true;
                case UPTURN_OVERSHOOT:
                    return true;
            }
        }
        return false;
    }

    @Override
    public IExpression clone() {
        return this;
    }
}
