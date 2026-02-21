package chess.pieces;

import java.util.ArrayList;

public class King extends Piece{
    private boolean isInCheck; 

    public King(int teamId, int[] pos) {
        super(teamId, pos);
        this.isInCheck = false;
        this.name = "K";
    }

    @Override
    public ArrayList<int[]> rangeOfMovement() {
        ArrayList<int[]> res = new ArrayList<int[]>();
        int i = -1;
        while (i < 2) {
            if (!this.isCoordInRange(this.pos[0] + i)) continue;
            int j = -1;
            while (j < 2) {
                if (!this.isCoordInRange(this.pos[1] + j)) continue;
                int[] positionInRange = {this.pos[0]+i, this.pos[1] + j};
                res.add(positionInRange);
                j++;
            }
            i++;
        } 

        return res;
    }

    public void setIsInCheck(boolean b) {
        isInCheck = b;
    };

    public boolean getIsInCheck() { return this.isInCheck; }
}
