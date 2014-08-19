package syntax.Function;

import syntax.IExpression;

/**
 * Created by jerem on 11/08/14.
 */
public class Nor implements IExpression {

    private IExpression     children[];

    public Nor()
    {}

    public Nor(IExpression[] children)
    {
        this.children = children;
    }

    @Override
    public Class getType()
    {
        return Nor.class;
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
        return 2;
    }

    @Override
    public Boolean evaluate(int index)
    {
        return (!(children[0].evaluate(index) || children[1].evaluate(index)));
    }

    @Override
    public IExpression clone()
    {
        Nor         norCopy;
        IExpression childrenCopy[];

        norCopy = new Nor();
        childrenCopy = new IExpression[children.length];
        for (int i = 0; i < children.length; i++)
            childrenCopy[i] = children[i].clone();
        norCopy.setChildren(childrenCopy);
        return norCopy;
    }

    @Override
    public String print(String tab)
    {
        return children[0].print(tab + indent) + "\n"
                + tab + "(Nor)\n"
                +  children[1].print(tab + indent);
    }

    @Override
    public String toString() {
        return "Nor";
    }
}
