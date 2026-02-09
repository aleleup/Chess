package chess.pieces;

public abstract class Piece {
    protected int val;
    protected int[] pos = new int[2];

    public Piece(int teamId, int[] position) {
        val = teamId == 0 ? 0 : 16;
        pos[0] = position[0]; pos[1] = position[1];
    };

    public int[][] rangeOfMovement() {
        return new int[0][0];
    }

    public void move(int[] n_p) {}
}
