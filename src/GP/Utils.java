package GP;

import syntax.IExpression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by jerem on 11/08/14.
 */
public class Utils {

    public static void traverse(IExpression expression, List nodes)
    {
        IExpression     children[];

        children = expression.getChildren();
        nodes.add(expression);
        for (IExpression child : children)
            traverse(child, nodes);
    }

    public static void traverse(IExpression expression, List functionNode, List terminalNode)
    {
        IExpression     children[];

        children = expression.getChildren();
        if (children.length == 0)
            terminalNode.add(expression);
        else
        {
            functionNode.add(expression);
            for (IExpression child : children)
                traverse(child, functionNode, terminalNode);
        }
    }

    static public IExpression selectRandomNode(IExpression tree, Random random)
    {
        List<IExpression>       nodes;

        nodes = new ArrayList<IExpression>();
        traverse(tree, nodes);
        return nodes.get(random.nextInt(nodes.size()));
    }

    static public IExpression selectRandomNode(IExpression tree, double terminalNodeBias, Random random)
    {
        List<IExpression> functionNodes = new ArrayList<IExpression>();
        List<IExpression> terminalNodes = new ArrayList<IExpression>();

        traverse(tree, functionNodes, terminalNodes);
        if (random.nextDouble() < terminalNodeBias && functionNodes.size() > 0)
            return terminalNodes.get(random.nextInt(terminalNodes.size()));
        if (functionNodes.size() > 0)
            return functionNodes.get(random.nextInt(functionNodes.size()));
        return terminalNodes.get(random.nextInt(terminalNodes.size()));
    }

    static public IExpression selectRandomNode(IExpression tree, int maxDepthAllow, Random random)
    {
        List<IExpression> functionNodes = new ArrayList<IExpression>();
        List<IExpression> terminalNodes = new ArrayList<IExpression>();

        traverse(tree, functionNodes, terminalNodes);
        if (maxDepthAllow == 0)
            return terminalNodes.get(random.nextInt(terminalNodes.size()));
        functionNodes = getNonBloatedNodes(functionNodes, maxDepthAllow);
        if (functionNodes.size() > 0)
            return functionNodes.get(random.nextInt(functionNodes.size()));
        return terminalNodes.get(random.nextInt(terminalNodes.size()));
    }

    public static void replace(Individual individual, IExpression oldNode, IExpression newNode)
    {
        if (individual.getTreeRoot() == oldNode)
            individual.setTreeRoot(newNode);
        else
            replace(individual.getTreeRoot(), oldNode, newNode);
    }

    public static void replace(IExpression currentNode, IExpression oldNode, IExpression newNode)
    {
        IExpression     children[];

        children = currentNode.getChildren();
        for (int i = 0; i < children.length; i++)
        {
            if (children[i] == oldNode)
            {
                children[i] = newNode;
                return ;
            }else
                replace(children[i], oldNode, newNode);
        }
    }

    private static int get_depth(IExpression node, int currDepth, int treeDepth)
    {
        IExpression children[];
        int         tmpDepth;

        if (node.getNumberChildren() == 0)
            return currDepth ;
        else
        {
            currDepth++;
            children = node.getChildren();
            for (int i = 0; i < children.length; i++)
            {
                tmpDepth = get_depth(children[i], currDepth, treeDepth);
                if (tmpDepth > treeDepth)
                    treeDepth = tmpDepth;
            }
            currDepth--;
        }
        return treeDepth;
    }

    public static int getDepth(IExpression node)
    {
        return get_depth(node, 1, 1);
    }

    private static int get_depth2(IExpression currentNode, IExpression targetNode, int currentDepth)
    {
        IExpression     children[];
        int             depth;

        if (currentNode == targetNode)
            return currentDepth;
        else
        {
            children = currentNode.getChildren();
            for (int i = 0; i < children.length; i++)
            {
                depth = get_depth2(children[i], targetNode, currentDepth + 1);
                if (depth != 0)
                    return depth;
            }
        }
        return 0;
    }

    public static int getDepth(IExpression tree, IExpression node)
    {
        return get_depth2(tree, node, 0);
    }

    public static List<IExpression> getNonBloatedNodes(List<IExpression> nodes, int maxDepthAllow)
    {
        List<IExpression>   nonBloatedNodes;
        IExpression         node;

        nonBloatedNodes = new ArrayList<IExpression>();
        for(Iterator<IExpression> i= nodes.iterator(); i.hasNext();){
            node = i.next();
            if(Utils.getDepth(node) <= maxDepthAllow){
                nonBloatedNodes.add(node);
            }
        }
        return nonBloatedNodes;
    }
}
