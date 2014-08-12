package syntax.Function;

import syntax.Function.And;
import syntax.Function.Function;

/**
 * Created by jerem on 11/08/14.
 */
public enum EFunction {
    AND(And.class), OR(Or.class), NOT(Not.class), XOR(Xor.class), NOR(Nor.class);

    private Class           type;

    private EFunction(Class type){
        this.type = type;
    }

    public Class    getType()
    {
        return type;
    }
}
