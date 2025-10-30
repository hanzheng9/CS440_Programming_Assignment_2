package src.pas.othello.agents;


// SYSTEM IMPORTS
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


// JAVA PROJECT IMPORTS
import edu.bu.pas.othello.agents.Agent;
import edu.bu.pas.othello.agents.TimedTreeSearchAgent;
import edu.bu.pas.othello.game.Game.GameView;
import edu.bu.pas.othello.game.PlayerType;
import edu.bu.pas.othello.traversal.Node;
import edu.bu.pas.othello.utils.Coordinate;
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

            final double temp = 100.0; 
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
            return null;
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
        return new OthelloNode(this.getMyPlayerType(), game, 0);
    }

    @Override
    public Node treeSearch(Node n)
    {
        // TODO: complete me!
        return null;
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
