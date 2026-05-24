package chess.pieces;

public abstract class Castler extends Piece {
    boolean canCastle;
    public Castler(int val, int[] position) {
        super(val, position);
        this.canCastle = true;
    }

    public boolean getCanCastle () { return this.canCastle;}
    public void setCanCastle (boolean b) { this.canCastle = b; }


}
