package syntax.Function;

import syntax.IExpression;

/**
 * Created by jerem on 11/08/14.
 */
public class Not implements IExpression {

    private IExpression     children[];

    public Not()
    {}

    public Not(IExpression[] children)
    {
        this.children = children;
    }

    @Override
    public Class getType()
    {
        return Not.class;
    }

    @Override
    public IExpression[] getChildren()
    {
        return children;
    }

    @Override
    public void setChildren(IExpression[] children)
    {
        this.children = children;
    }

    @Override
    public int getNumberChildren()
    {
        return 1;
    }

    @Override
    public Boolean evaluate(int index)
    {
        return !children[0].evaluate(index);
    }

    @Override
    public IExpression clone()
    {
        Not         notCopy;
        IExpression childrenCopy[];

        notCopy = new Not();
        childrenCopy = new IExpression[children.length];
        for (int i = 0; i < children.length; i++)
            childrenCopy[i] = children[i].clone();
        notCopy.setChildren(childrenCopy);
        return notCopy;
    }

    @Override
    public String print(String tab)
    {
        return  tab + "(Not)\n"
                + children[0].print(tab + indent);
    }

    @Override
    public String toString() {
        return "Not";
    }
}
