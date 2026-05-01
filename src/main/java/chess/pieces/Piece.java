package chess.pieces;
import chess.MatrixPoint;
import java.util.ArrayList;
public abstract class Piece {
    protected int val;
    protected MatrixPoint pos;
    protected String name;

    public Piece(int teamId, int[] position) {
        val = teamId == 0 ? 0 : 16;
        pos = new MatrixPoint(position[0], position[1]);

    };

    public ArrayList<int[]> rangeOfMovement() {
        return new ArrayList<int[]>();
    }

    public void move(int[] n_p) {
        pos.set(0, n_p[0]);
        pos.set(1, n_p[1]);
;
    };

    public int getVal() {
        return val;
    }

    // Returning new MatrixPoint to avoid passing the reference of the piece position
    public MatrixPoint getPosition() {
        return new MatrixPoint(pos.getPos()[0], pos.getPos()[1]);
    }

    public int getTeamId() {
        return val < 16 ? 0 : 1;
    } // 0 -> white team; 1 -> black team;

    public boolean isPieceInRange() {
        return this.pos.isPointInRange(0, 8);
    };

    protected ArrayList<int[]> sidewaysAndVertically() {
        ArrayList<int[]> res = new ArrayList<int[]>();
        int i = 0;
        while (i < 8) {
            if (i != this.pos.getPos()[0]) {
                int[] rowPosInRange = { i, this.pos.getPos()[1] };
                res.add(rowPosInRange);
            }
            if (i != this.pos.getPos()[1]) {
                int[] colPosInRange = { this.pos.getPos()[0], i };
                res.add(colPosInRange);
            }

        }

        return res;
    }

    protected ArrayList<int[]> diagonals() {
        ArrayList<int[]> res = new ArrayList<int[]>();
        int i = 0;
        while (i < 8) {
            if (i != this.pos.getPos()[0]) {
                int[] rowPosInRange = { i, this.pos.getPos()[1] };
                res.add(rowPosInRange);
            }
            if (i != this.pos.getPos()[1]) {
                int[] colPosInRange = { this.pos.getPos()[0], i };
                res.add(colPosInRange);
            }

        }

        return res;
    }

    public boolean isCoordInRange(int coord) {
        return 0 <= coord && coord < 8;
    };


    public String getName() {
        return this.name;
    }

}
