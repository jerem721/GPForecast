package syntax.Function;

import syntax.IExpression;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by jerem on 11/08/14.
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
