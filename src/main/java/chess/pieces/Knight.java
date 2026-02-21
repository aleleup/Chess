package chess.pieces;

import java.util.ArrayList;

public class Knight extends Pawn {
    public Knight(int teamId, int[] pos) {
        super(teamId, pos);
        this.val += 3;
        this.name = "Kn";
    }

    @Override
    public ArrayList<int[]> rangeOfMovement() {
        ArrayList<int[]> res = new ArrayList<int[]>();
        int i = 2;
        while (i > -3){
            if (i == 0) continue;
            int j = 1 + i % 2;
            if (this.isCoordInRange(this.pos[0] + i) && this.isCoordInRange(this.pos[1] + j)) {
                int[] posInRange = {this.pos[0] + i, this.pos[1] + j};
                res.add(posInRange);
            }
            if (this.isCoordInRange(this.pos[0] + i) && this.isCoordInRange(this.pos[1] - j)) {
                int[] posInRange = {this.pos[0] + i, this.pos[1] - j};
                res.add(posInRange);
            }

        }
        return res;
    }
}
