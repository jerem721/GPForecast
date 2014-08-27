package syntax.Function;

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
