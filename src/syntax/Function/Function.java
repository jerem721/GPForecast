package syntax.Function;

import syntax.IExpression;

import java.lang.reflect.InvocationTargetException;

/**
 * Class to create an instance of function with a specified type.
 */
public class Function {

    private Class   type;

    public Function(Class type)
    {
        this.type = type;
    }

    public IExpression invoke(Object... args){
        IExpression     object;

        object = null;
        try {
            object = (IExpression) type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }


}
