package syntax;

import directionalChanges.algorithm.events.EEvent;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by jerem on 11/08/14.
 */
public interface IExpression {

    static String indent = "     ";

    public Class           getType();
    public IExpression[]   getChildren();
    public void            setChildren(IExpression[] children);
    public int             getNumberChildren();
    public Boolean         evaluate(int index, Hashtable<Double, List<EEvent>> dcData);
    public IExpression     clone();
    public String          print(String tab);

}
