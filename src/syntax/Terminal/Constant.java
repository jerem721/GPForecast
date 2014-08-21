package syntax.Terminal;

import directionalChanges.algorithm.events.EEvent;
import directionalChanges.algorithm.events.IEvent;
import syntax.IExpression;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by jerem on 11/08/14.
 */
public class Constant implements IExpression {

    private Double          value;              // threshold
    private IExpression     children[] = {};

    public Constant(Double value)
    {
        this.value = value;
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
    public Boolean evaluate(int index, Hashtable<Double, List<EEvent>> dcData) {
        List<EEvent>        directionalChangeEvent;

        directionalChangeEvent = dcData.get(value);
        if (directionalChangeEvent!= null && index <= directionalChangeEvent.size())
        {
            switch (directionalChangeEvent.get(index))
            {
                case DOWNTURN:
                    return true;
                case DOWNTURN_OVERSHOOT:
                    return false;
                case UPTURN:
                    return false;
                case UPTURN_OVERSHOOT:
                    return true;
            }
        }
        return false;
    }

    @Override
    public IExpression clone() {
        return new Constant(value);
    }
}
