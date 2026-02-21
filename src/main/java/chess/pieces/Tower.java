package chess.pieces;

import java.util.ArrayList;

public class Tower extends Piece {
    
    public Tower(int teamId, int[] pos){
        super(teamId, pos);
        this.val += 5;
        this.name = "T";
    }

    @Override
    public ArrayList<int[]> rangeOfMovement() {
        return this.towerRangeOfMovement();
    }
}
