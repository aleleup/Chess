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

    /**
     * 
     * @param dirVectors: matrix of 2xn with n the amounts of directional vectors
     * It requires each coordenate of each vector to be 1 XOR 0
     * 
     * @return
     */
    protected ArrayList<int[]> rangeOfMovementByDirVector(int[][] dirVectors) {
        ArrayList<int[]> res = new ArrayList<int[]>();
        int[] piecePos = this.pos.getPos();
        for (int[] v : dirVectors) {
            int x = 1;
            boolean[] directionInRange = {true, true};
            while (directionInRange[0] || directionInRange[1]) {
                int[][] opositePoints = {
                    {x*v[0] + piecePos[0], x*v[1] + piecePos[1]},
                    {-x*v[0] + piecePos[0], -x*v[1] + piecePos[1]},
                };
                for (int i = 0; i < 2 ; i++) {
                    int[] point =opositePoints[i];
                    if (this.isCoordInRange(point[0]) && this.isCoordInRange(point[1])) res.add(point);
                    else directionInRange[i] = false;
                }
                x++;
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
