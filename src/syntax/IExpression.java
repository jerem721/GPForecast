package syntax;

import directionalChanges.algorithm.events.EEvent;

import java.util.Hashtable;
import java.util.List;

/**
 * Interface to create GDTs.
 */
public interface IExpression {

    static String indent = "     ";

    /**
     * Return the type of the node.
     */
    public Class           getType();

    /**
     * Return the children nodes of the expression.
     */
    public IExpression[]   getChildren();

    /**
     * Set the specified children node for the node.
     */
    public void            setChildren(IExpression[] children);

    /**
     * Return the number of children node authorized for this node.
     */
    public int             getNumberChildren();

    /**
     * Evaluate the result of the children node.
     */
    public Boolean         evaluate(int index, Hashtable<Double, List<EEvent>> dcData);

    /**
     * Clone the node and its children.
     */
    public IExpression     clone();

    /**
     * Print some information of the node.
     */
    public String          print(String tab);

}
