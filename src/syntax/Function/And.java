package syntax.Function;

import syntax.IExpression;

/**
 * Created by jerem on 11/08/14.
 */
public class And implements IExpression {

    private IExpression     children[];

    public And()
    {}

    public And(IExpression[] children)
    {
        this.children = children;
    }

    @Override
    public Class getType()
    {
        return And.class;
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
        return (children[0].evaluate(index) && children[1].evaluate(index));
    }

    @Override
    public IExpression clone()
    {
        And         andCopy;
        IExpression childrenCopy[];

        andCopy = new And();
        childrenCopy = new IExpression[children.length];
        for (int i = 0; i < children.length; i++)
            childrenCopy[i] = children[i].clone();
        andCopy.setChildren(childrenCopy);
        return andCopy;
    }

    @Override
    public String print(String tab)
    {
        return  children[0].print(tab + indent) + "\n"
                + tab + "(And)\n"
                + children[1].print(tab + indent);
    }

    @Override
    public String toString() {
        return "And";
    }

}
