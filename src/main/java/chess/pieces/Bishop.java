package chess.pieces;

import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(int teamId, int[] pos) {
        super(teamId, pos);
        this.val += 3; 
        this.name = "B";
    }

    @Override
    public ArrayList<int[]> rangeOfMovement() {
        return this.bishopRangeOfMovement();
    }
}
