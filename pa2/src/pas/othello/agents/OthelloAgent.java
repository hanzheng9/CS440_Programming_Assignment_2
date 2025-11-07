package src.pas.othello.agents;


// SYSTEM IMPORTS
import java.util.*; 


// JAVA PROJECT IMPORTS
import edu.bu.pas.othello.agents.Agent;
import edu.bu.pas.othello.agents.TimedTreeSearchAgent;
import edu.bu.pas.othello.game.Game.GameView;
import edu.bu.pas.othello.game.PlayerType;
import edu.bu.pas.othello.traversal.Node;
import edu.bu.pas.othello.utils.Coordinate;
import src.pas.othello.heuristics.Heuristics;
import edu.bu.pas.othello.game.Game;  


public class OthelloAgent
    extends TimedTreeSearchAgent
{

    public static class OthelloNode
        extends Node
    {
        public OthelloNode(final PlayerType maxPlayerType,  // who is MAX (me)
                           final GameView gameView,         // current state of the game
                           final int depth)                 // the depth of this node
        {
            super(maxPlayerType, gameView, depth);
        }

        @Override
        public double getTerminalUtility()
        {
            // TODO: complete me!
            if(!isTerminal()) 
                return 0.0;

            final double temp = 1000.0; 
            final Game.GameView view = getGameView();
            final PlayerType max = getMaxPlayerType();
            final PlayerType min;
            int maxCount = 0; 
            int minCount = 0;
            PlayerType[][] cells = view.getCells();

            if(max==PlayerType.BLACK)
            {
                min = PlayerType.WHITE;
            } 
            else 
            {
                min = PlayerType.BLACK;
            }

            for(int y=0; y<cells.length; y++) 
            {
                for(int x=0; x<cells[y].length; x++) 
                {
                    PlayerType tile = cells[y][x]; // tile = who owns the tile essentially
                    if(tile==max) 
                    {
                        maxCount++;
                    } 
                    else if(tile==min) 
                    {
                        minCount++;
                    }
                }
            }

            if(maxCount==minCount) 
                return 0.0;

            if((maxCount-minCount)/64.0<-1.0)
                return -1.0 * temp;
            else if((maxCount-minCount)/64.0>1.0)
                return  1.0 * temp;
            else
                return((maxCount-minCount)/64.0) * temp;
        }

        @Override
        public List<Node> getChildren()
        {
            // TODO: complete me!
            List<Node> children = new ArrayList<>();
            final Game.GameView view = getGameView();
            final PlayerType current = getCurrentPlayerType();
            final PlayerType opponent;

            if(current == PlayerType.BLACK)
            {
                opponent = PlayerType.WHITE;
            }
            else
            {
                opponent = PlayerType.BLACK;
            }

            Game curGame = new Game(view);
            curGame.setCurrentPlayerType(current);
            curGame.calculateFrontiers();
            Set<Coordinate> legalMoves = curGame.getFrontier(current);

            // If there are legal moves, create one child per move
            if(legalMoves!=null && !legalMoves.isEmpty())
            {
                for(Coordinate move: legalMoves)
                {
                    Game newGame = new Game(view);

                    newGame.applyMove(move);
                    newGame.setCurrentPlayerType(opponent);  
                    newGame.setTurnNumber(view.getTurnNumber() + 1);
                    newGame.calculateFrontiers();  
                    OthelloNode child = new OthelloNode(getMaxPlayerType(), newGame.getView(), getDepth() + 1);
                    child.setLastMove(move);
                    children.add(child);
                }
                return children;
            }

            // Otherwise player must pass 
            Game passGame = new Game(view);
            passGame.setCurrentPlayerType(opponent);
            passGame.setTurnNumber(view.getTurnNumber() + 1); 
            passGame.calculateFrontiers();  
            OthelloNode passChild = new OthelloNode(getMaxPlayerType(), passGame.getView(), getDepth() + 1);
            passChild.setLastMove(null);
            children.add(passChild);

            return children;
        }
    }

    private final Random random;

    public OthelloAgent(final PlayerType myPlayerType,
                        final long maxMoveThinkingTimeInMS)
    {
        super(myPlayerType,
              maxMoveThinkingTimeInMS);
        this.random = new Random();
    }

    public final Random getRandom() { return this.random; }

    @Override
    public OthelloNode makeRootNode(final GameView game)
    {
        // if you change OthelloNode's constructor, you will want to change this!
        // Note: I am starting the initial depth at 0 (because I like to count up)
        //       change this if you want to count depth differently
        PlayerType myPlayer = this.getMyPlayerType();
        OthelloNode rootNode = new OthelloNode(myPlayer, game, 0);
        return rootNode;
    }

    @Override
    public Node treeSearch(Node n)
    {
        // TODO: complete me!
        List<Node> rootChildren = root.getChildren();
        if(rootChildren==null || rootChildren.isEmpty()) 
        {
            return n;
        }
        final int DEPTH_LIMIT = 3; 
        final long deadline = System.currentTimeMillis() + Math.max(0L, this.getMaxThinkingTimeInMS() - 5L); // for timeout purposes
        double bestValue = Double.NEGATIVE_INFINITY;
        Node bestChild = rootChildren.get(0);

        for(Node child: rootChildren) 
        {
            double value = alphaBeta(child, DEPTH_LIMIT, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false, deadline);
            child.setUtilityValue(value);

            if(value>bestValue) 
            {
                bestValue = value;
                bestChild = child;
            }
        }

        return bestChild;
    }

    private double alphaBeta(Node node, int depth, double alpha, double beta, boolean maximizing, long deadline) 
    {
        if(System.currentTimeMillis()>=deadline) // timeout concerns
        {
            double temp = Heuristics.calculateHeuristicValue(node);
            node.setUtilityValue(temp);
            return temp;
        }
        if(node.isTerminal() || depth<=0) 
        {
            double val;

            if(node.isTerminal()) 
            {
                val = node.getTerminalUtility();
            } 
            else 
            {
                val = Heuristics.calculateHeuristicValue(node);
            }

            node.setUtilityValue(val);

            return val;
        }
        List<Node> children = node.getChildren();
        if(children==null || children.isEmpty()) 
        {
            double temp = Heuristics.calculateHeuristicValue(node);
            node.setUtilityValue(temp);
            return temp;
        }

        if(maximizing) 
        {
            double value = Double.NEGATIVE_INFINITY;

            for(Node child: children) 
            {
                double v = alphaBeta(child, depth - 1, alpha, beta, false, deadline);
                value = Math.max(value, v);
                alpha = Math.max(alpha, value);

                if(beta<=alpha) 
                    break;
            }
            node.setUtilityValue(value);

            return value;
        } 
        else 
        {
            double value = Double.POSITIVE_INFINITY;

            for(Node child: children) 
            {
                double v = alphaBeta(child, depth - 1, alpha, beta, true, deadline);
                value = Math.min(value, v);
                beta = Math.min(beta, value);

                if(beta<=alpha) 
                    break;
            }
            node.setUtilityValue(value);

            return value;
        }
    }

    @Override
    public Coordinate chooseCoordinateToPlaceTile(final GameView game)
    {
        // TODO: this move will be called once per turn
        //       you may want to use this method to add to data structures and whatnot
        //       that your algorithm finds useful

        // make the root node
        Node node = this.makeRootNode(game);

        // call tree search
        Node moveNode = this.treeSearch(node);

        // return the move inside that node
        return moveNode.getLastMove();
    }

    @Override
    public void afterGameEnds(final GameView game) {}
}
