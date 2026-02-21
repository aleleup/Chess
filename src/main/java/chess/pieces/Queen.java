package chess.pieces;

import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(int teamId, int[] pos){
        super(teamId, pos);
        this.val += 9;
        this.name = "Q";
    }

    @Override
    public ArrayList<int[]> rangeOfMovement() {
        ArrayList<int[]> res = this.bishopRangeOfMovement();
        res.addAll(this.towerRangeOfMovement());
        return res;
    };

}
