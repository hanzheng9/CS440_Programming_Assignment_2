package src.pas.othello.heuristics;


import java.util.Set;

import edu.bu.pas.othello.game.Game;
import edu.bu.pas.othello.game.PlayerType;
// SYSTEM IMPORTS
import edu.bu.pas.othello.traversal.Node;
import edu.bu.pas.othello.utils.Coordinate;


// JAVA PROJECT IMPORTS



public class Heuristics
    extends Object
{

    public static double calculateHeuristicValue(Node node)
    {
        double heuristics = 0;
        double playerCount = 0;
        double oppCount = 0;
        Game game = new Game(node.getGameView());
        final PlayerType player = node.getMaxPlayerType();
        final PlayerType opp;
        PlayerType[][] cells = node.getGameView().getCells();
        double oppCorners = 0;
        double playerCorners = 0;

        //messed up the code oppNear is good for opp and same for player to lazy to fix in all those if statements
        double oppNear = 0;
        double playerNear = 0;

        double playerLegal = 0;
        double oppLegal = 0;


        

            if(player==PlayerType.BLACK)
            {
                opp = PlayerType.WHITE;
            } 
            else 
            {
                opp = PlayerType.BLACK;
            }

            Set<Coordinate> platerLegalMoves = game.getFrontier(player);
            playerLegal = platerLegalMoves.size() * 1.0;


            Set<Coordinate> oppLegalMoves = game.getFrontier(opp);
            oppLegal = platerLegalMoves.size() * 1.0;
            for(int y=0; y<cells.length; y++) 
            {
                for(int x=0; x<cells[y].length; x++) 
                {
                    PlayerType tile = cells[y][x]; 
                    //checks for corners
                    if((x == 0 && y ==0) || (x == 0 && y == cells.length - 1) || (x == cells.length - 1 && y == 0) || (x == cells.length - 1 && y == cells.length - 1)){
                    
                    if(tile==player) 
                    {
                        playerCorners++;
                    } 
                    else if(tile==opp) 
                    {
                        oppCorners++;
                    }

                    }

                    //checks for next to corners 
                    // the if true is so the whole thing is collaspable in the text editor
                    if(true){
                    if((x == 1 && y ==0) ||(x == 1 && y ==1) ||(x==0 && y ==1)){
                        
                    if(tile==player) 
                    {
                        oppNear++;
                    } 
                    else if(tile==opp) 
                    {
                        playerNear++;
                    }

                    }
                    if((x == cells.length - 1 && y ==0) ||(x == cells.length - 2 && y ==1) ||(x== cells.length && y ==1)){
                        
                    if(tile==player) 
                    {
                        oppNear++;
                    } 
                    else if(tile==opp) 
                    {
                        playerNear++;
                    }

                    }
                    if((x == 0 && y == cells.length - 1) ||(x == 1 && y == cells.length - 2) ||(x== 1 && y ==cells.length - 1)){
                        
                    if(tile==player) 
                    {
                        oppNear++;
                    } 
                    else if(tile==opp) 
                    {
                        playerNear++;
                    }

                    }
                     if((x == cells.length - 2 && y == cells.length - 2) ||(x == cells.length - 1 && y == cells.length - 2) ||(x== cells.length - 2&& y ==cells.length - 1)){
                        
                    if(tile==player) 
                    {
                        oppNear++;
                    } 
                    else if(tile==opp) 
                    {
                        playerNear++;
                    }

                    }
                }

                    
                    if(tile==player) 
                    {
                        playerCount++;
                    } 
                    else if(tile==opp) 
                    {
                        oppCount++;
                    }
                }
            }
        //does a simple difference, might not take into account relative advantage
        heuristics += playerCount - oppCount;


        // scales better as game goes on
        heuristics += ((double) playerCount / (playerCount + oppCount)) * node.getGameView().getTurnNumber();

        //deals with corners
        heuristics += 10 * playerCorners;
        heuristics -= 10 * oppCorners;

        heuristics += 4.5 * playerNear;
        heuristics -= 4.5 * oppNear;

        //legal moves
        heuristics += playerLegal;
        heuristics += oppLegal;

        if(heuristics >= 100)heuristics = 99;

        return heuristics;
    }

}
