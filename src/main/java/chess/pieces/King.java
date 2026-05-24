package chess.pieces;

import java.util.ArrayList;

public class King extends Castler{
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
            if (this.isCoordInRange(this.pos.getPos()[0] + i)) {
                int j = -1;
                while (j < 2) {
                    if (this.isCoordInRange(this.pos.getPos()[1] + j)){ 
                        int[] positionInRange = {this.pos.getPos()[0]+i, this.pos.getPos()[1] + j};
                        res.add(positionInRange);
                    }
                    j++;
                }
            }
            i++;
        } 

        return res;
    }

    public boolean getIsInCheck() { return this.isInCheck; }

    public void setIsInCheck(boolean b) { this.isInCheck = b; }

    public ArrayList<Piece> getAnnoyers() { return new ArrayList<>(annoyers); }

    public void addAnnoyer(Piece p) { 
        this.annoyers.add(p); 
    }


}
