package src.pas.othello.ordering;


// SYSTEM IMPORTS
import edu.bu.pas.othello.traversal.Node;
import src.pas.othello.heuristics.Heuristics;

import java.util.Comparator;
import java.util.List;


// JAVA PROJECT IMPORTS



public class MoveOrderer
    extends Object
{

    public static List<Node> orderChildren(List<Node> children)
    {
        // TODO: complete me!
        if(children==null || children.isEmpty()) 
            return children;

        children.sort(Comparator.comparingDouble((Node n) -> Heuristics.calculateHeuristicValue(n)).reversed());

        return children;
    }

}
