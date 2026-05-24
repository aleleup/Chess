package chess.pieces;

import java.util.ArrayList;

public class Rook extends Castler {
    public Rook(int teamId, int[] pos){
        super(teamId, pos);
        this.val += 5;
        this.name = "T";
    }

    @Override
    public ArrayList<int[]> rangeOfMovement() {
        int[][] dirVectors = {{0,1}, {1,0}};
        return this.rangeOfMovementByDirVector(dirVectors);
    }

}
