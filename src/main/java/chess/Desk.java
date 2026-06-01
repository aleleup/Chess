package chess;

import java.util.ArrayList;
import java.util.HashMap;


public class Desk {
    private Board board;
    private HashMap<String, Integer> boardStatusCounter;
    // private Connections[];
    public Desk() {
        this.board = new Board();
        this.boardStatusCounter = new HashMap<String,Integer>();
    };

    private boolean move(int[] currentPos, int[] newPos, String pawnUpgrade){
        boolean isLegalMove;
        
        if (pawnUpgrade == "") {
            isLegalMove = board.move(currentPos, newPos);
            if (!isLegalMove) return false;
            String statusKey = board.statusKey();
            if (boardStatusCounter.containsKey(statusKey)){
                boardStatusCounter.compute(statusKey, (k, v) -> v++);
            }
            else boardStatusCounter.put(statusKey, 1);
        } else {
            isLegalMove = board.moveAndUpgradePawn(currentPos, newPos, pawnUpgrade);
            if (!isLegalMove) return false;
            
            
        }

        return true;
    }



}
