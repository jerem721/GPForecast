package syntax.Terminal;

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
    private List<IEvent>    directionalChangeEvent;

    public Constant(Double value)
    {
        this.value = value;
        directionalChangeEvent = new ArrayList<IEvent>();
    }

    public Constant(Double value, List<IEvent> eventList)
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

    public void addDCEvent(IEvent event)
    {
        directionalChangeEvent.add(event);
    }

    public List<IEvent> getDCEvent()
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
    public Boolean evaluate() {
       return true;
    }

    @Override
    public IExpression clone() {
        Constant        constant;

        constant = new Constant(value);
        return constant;
    }
}
