package chess.pieces;

import java.util.ArrayList;

public abstract class Piece {
    protected int val;
    protected int[] pos = new int[2];
    protected String name;
    public Piece(int teamId, int[] position) {
        val = teamId == 0 ? 0 : 16;
        pos[0] = position[0]; pos[1] = position[1];
    };

    public ArrayList<int[]> rangeOfMovement() {
        return new ArrayList<int[]>();
    }

    public void move(int[] n_p) {
        pos[0] = n_p[0]; pos[1] = n_p[1];
    };

    public int getVal() {return val;}

    public int getCoordenate(int i) { return pos[i]; }
    
    public int getTeamId() { return val < 16 ? 0 : 1; } // 0 -> white team; 1 -> black team;

    public boolean isCoordInRange(int coord) {
        return 0 <= coord && coord < 8;
    };

    protected ArrayList<int[]> towerRangeOfMovement() {
        ArrayList<int[]> res = new ArrayList<int[]>();
        int i = 0;
        while (i < 8) {
            if (i != this.pos[0]) {
                int[] rowPosInRange = { i , this.pos[1]};
                res.add(rowPosInRange);
            }
            if (i != this.pos[1]) {
                int[] colPosInRange = {this.pos[0], i};
                res.add(colPosInRange);
            }
            
        }

        return res;
    }

    protected ArrayList<int[]> bishopRangeOfMovement() {
        ArrayList<int[]> res = new ArrayList<int[]>();
        int i = 0;
        while (i < 8) {
            if (i != this.pos[0]) {
                int[] rowPosInRange = { i , this.pos[1]};
                res.add(rowPosInRange);
            }
            if (i != this.pos[1]) {
                int[] colPosInRange = {this.pos[0], i};
                res.add(colPosInRange);
            }
            
        }

        return res;
    }

    public String getName(){
        return this.name;
    }
}
