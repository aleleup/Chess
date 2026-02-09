package chess.pieces;

public class Pawn extends Piece {
    
    public Pawn (int teamId, int[] position) { 
        super(teamId, position);
        this.val = 16;
    };
    
    @Override
    public int[][] rangeOfMovement() {
        int[][] res = new int[3][2];
        int i = -1;
        while (i < 2) {
            if (pos[0] + 1  < 8 && pos[1] + i < 8) {
                int[] positionInRange = {pos[0]+1, pos[1] + i};
                res[i+1] = positionInRange;
            }
            i++;
        }

        return res;
    }
}
