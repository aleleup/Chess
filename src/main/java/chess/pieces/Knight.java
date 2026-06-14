package chess.pieces;

import java.util.ArrayList;

import chess.MatrixPoint;

public class Knight extends Piece {
    public Knight(int teamId, int[] pos) {
        super(teamId, pos);
        this.val += 3;
        this.name = "Kn";
    }

    @Override
    public ArrayList<int[]> rangeOfMovement() {
        ArrayList<int[]> res = new ArrayList<int[]>();

        int[][] relativeJumps = {
            {2,1}, {2, -1}, {1, 2}, {1, -2},
            {-2,-1}, {-2, 1}, {-1, -2}, {-1, 2}

        };


        int[] knightPos = pos.getPos();
            for (int[] relJump : relativeJumps){
                MatrixPoint point = new MatrixPoint(relJump[0] + knightPos[0], relJump[1] + knightPos[1]);
                if (!point.isPointInRange(0, 8)) continue;
                res.add(point.getPos());
            }

        return res;
    }
}
