package syntax;

import syntax.Function.EFunction;
import syntax.Function.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jerem on 11/08/14.
 */
public class PrimitiveSet {

    private List<Function>          functionSet;
    private List<IExpression>       terminalSet;
    private Random                  random;

    public PrimitiveSet()
    {
        functionSet = new ArrayList<Function>();
        terminalSet = new ArrayList<IExpression>();
        random = new Random();
    }

    public void addFunction(EFunction function)
    {
        functionSet.add(new Function(function.getType()));
    }

    public void addTerminal(IExpression variable)
    {
        terminalSet.add(variable);
    }

    public List<Function>     getFunctionSet()
    {
        return functionSet;
    }

    public List<IExpression>   getTerminalSet()
    {
        return terminalSet;
    }

    public IExpression getRandomTerminal()
    {
        return terminalSet.get(random.nextInt(terminalSet.size())).clone();
    }

    public IExpression getRandomFunction()
    {
        Function          method;

        method = functionSet.get(random.nextInt(functionSet.size()));
        return method.invoke();
    }
}
