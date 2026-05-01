package chess.pieces;

import java.util.ArrayList;

public class King extends Piece{
    private boolean isInCheck; 
    private ArrayList<Piece> annoyers; //Pieces that are checking me
    public King(int teamId, int[] pos) {
        super(teamId, pos);
        this.isInCheck = false;
        this.name = "K";
        this.annoyers = new ArrayList<Piece>();
    }

    @Override
    public ArrayList<int[]> rangeOfMovement() {
        ArrayList<int[]> res = new ArrayList<int[]>();
        int i = -1;
        while (i < 2) {
            if (!this.isCoordInRange(this.pos.getPos()[0] + i)) continue;
            int j = -1;
            while (j < 2) {
                if (!this.isCoordInRange(this.pos.getPos()[1] + j)) continue;
                int[] positionInRange = {this.pos.getPos()[0]+i, this.pos.getPos()[1] + j};
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

    public ArrayList<Piece> getAnnoyers() { return new ArrayList<>(annoyers); }

    public void addAnnoyer(Piece p) { annoyers.add(p); }

    public Piece delAnnoyer(int i) { return annoyers.remove(i); }
}
