package chess.pieces;

import java.util.ArrayList;

public class Pawn extends Piece {
    
    public Pawn (int teamId, int[] position) { 
        super(teamId, position);
        this.val += 1;
        this.name = "P";
    };

    @Override 
    public ArrayList<int[]> rangeOfMovement() {
        // Check with 
        ArrayList<int[]> res = new ArrayList<int[]>();
        int i = -1;
        int pieceTeam = this.getTeamId();
        // If pieceTeam == 1 -> the next row position is in pos[0] + 1. If piece team == 0 -> the next row position is in pos[0] - 1
        int newRowOption = pieceTeam == 1 ? pos[0] + 1 : pos[0] - 1;
        if (!this.isCoordInRange(newRowOption)) return res;
        while (i < 2) {
            if (this.isCoordInRange(pos[1] + i)){
                int[] positionInRange = {newRowOption, pos[1] + i};
                res.add(positionInRange);
            }
            i++;
        }
        
        if (pieceTeam == 0 && this.pos[0] == 6) {
            int[] startingPawnJump = {4, this.pos[1]};
            res.add(startingPawnJump);
        }

        if (pieceTeam == 1 && this.pos[0] == 1) {
            int[] startingPawnJump = {3, this.pos[1]};
            res.add(startingPawnJump);
        }
        return res;
    }
    
    
}
