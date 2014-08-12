package syntax.Function;

import syntax.IExpression;

/**
 * Created by jerem on 11/08/14.
 */
public class Or implements IExpression {

    private IExpression     children[];

    public Or()
    {}

    public Or(IExpression[] children)
    {
        this.children = children;
    }

    @Override
    public Class getType()
    {
        return Or.class;
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
    public Boolean evaluate()
    {
        return (children[0].evaluate() || children[1].evaluate());
    }

    @Override
    public IExpression clone()
    {
        Or         orCopy;
        IExpression childrenCopy[];

        orCopy = new Or();
        childrenCopy = new IExpression[children.length];
        for (int i = 0; i < children.length; i++)
            childrenCopy[i] = children[i].clone();
        orCopy.setChildren(childrenCopy);
        return orCopy;
    }

    @Override
    public String print(String tab)
    {
        return children[0].print(tab + indent) + "\n"
                + tab + "(OR) \n"
                +  children[1].print(tab + indent);
    }

    @Override
    public String toString() {
        return "Or";
    }
}
