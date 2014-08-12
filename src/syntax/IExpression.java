package syntax;

/**
 * Created by jerem on 11/08/14.
 */
public interface IExpression {

    static String indent = "     ";

    public Class           getType();
    public IExpression[]   getChildren();
    public void            setChildren(IExpression[] children);
    public int             getNumberChildren();
    public Boolean         evaluate();
    public IExpression     clone();
    public String          print(String tab);

}
