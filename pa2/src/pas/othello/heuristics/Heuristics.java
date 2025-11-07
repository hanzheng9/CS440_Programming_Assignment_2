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


        double playerStable = 0;
        double oppStable = 0;

        double playerEdge =0;
        double oppEdge = 0;
        

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
            oppLegal = oppLegalMoves.size() * 1.0;
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
                    if((x == cells.length - 2 && y == 0 ) ||(x == cells.length - 2 && y ==1) ||(x== cells.length -1 && y ==1)){
                        
                    if(tile==player) 
                    {
                        oppNear++;
                    } 
                    else if(tile==opp) 
                    {
                        playerNear++;
                    }

                    }
                    if((x == 0 && y == cells.length - 2) ||(x == 1 && y == cells.length - 2) ||(x== 1 && y ==cells.length - 1)){
                        
                    if(tile==player) 
                    {
                        oppNear++;
                    } 
                    else if(tile==opp) 
                    {
                        playerNear++;
                    }

                    }
                     if((x == cells.length - 2 && y == cells.length - 2) ||(x == cells.length - 1 && y == cells.length - 2) ||(x== cells.length - 2 && y ==cells.length - 1)){
                        
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

                    //counts tile of each player
                    if(tile==player) 
                    {
                        playerCount++;
                    } 
                    else if(tile==opp) 
                    {
                        oppCount++;
                    }
                    //gets stable tiles 
                    if(true){
                    boolean stable = false;
                    if(x == 0 || x == cells.length - 1) {                   
                        boolean sameEdge = true;
                        for(int yy = 0; yy < cells.length; yy++) {
                            if(cells[yy][x] != tile) {
                                sameEdge = false;
                                    break;
                            }
                        }
                            if(sameEdge) stable = true;
                        } 
                    else if(y == 0 || y == cells.length - 1) {
                        boolean sameEdge = true;
                        for(int xx = 0; xx < cells.length; xx++) {
                            if(cells[y][xx] != tile) {
                                sameEdge = false;
                                break;
                            }
                        }
                        if(sameEdge) stable = true;
                        }
                    if(stable) {
                        if(tile == player) {
                            playerStable++;
                        }else if(tile == opp) {
                            oppStable++;
                        }
}

                    }
                    //gets edges
                    if(true){
                        if(x == 0 && (y != 0 || y != cells.length - 1)){
                            if(tile == player){
                                playerEdge ++;
                            }else{
                                oppEdge ++;
                            }
                        }
                        if(x == cells.length - 1 && (y != 0 || y != cells.length - 1)){
                            if(tile == player){
                                playerEdge ++;
                            }else{
                                oppEdge ++;
                            }
                        }
                        if((x != 0 || x != cells.length - 1) && y == 0){
                            if(tile == player){
                                playerEdge ++;
                            }else{
                                oppEdge ++;
                            }
                        }
                        if((x != 0 || x != cells.length - 1) && y == cells.length - 1){
                            if(tile == player){
                                playerEdge ++;
                            }else{
                                oppEdge ++;
                            }
                        }
                    }
                }

            }
        //does a simple difference, might not take into account relative advantage
        heuristics += playerCount - oppCount;


        // scales better as game goes on
        heuristics += ((playerCount - oppCount) / (playerCount + oppCount)) * 10.0;

        //deals with corners
        heuristics += 25 * playerCorners;
        heuristics -= 25 * oppCorners;

        //remeber swtiched to near corners is bad, just to lazy to fix
        heuristics += 12 * playerNear;
        heuristics -= 12 * oppNear;

        //legal moves
        heuristics += playerLegal;
        heuristics += oppLegal;

        //stable
         heuristics += 25 * playerStable;
        heuristics -= 25 * oppStable;

        //edges 
        heuristics += 5 * playerEdge;
        heuristics += 5 * oppEdge;

        //incase heuristics is bigger than 100, it will have a better herustic than a victory
        if(heuristics >= 1000)heuristics = 999;

        return heuristics;
    }

}
