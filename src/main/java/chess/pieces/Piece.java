package chess.pieces;

import java.util.ArrayList;
import java.lang.Math;
public abstract class Piece {
    protected int val;
    protected int[] pos = new int[2];
    protected String name;

    public Piece(int teamId, int[] position) {
        val = teamId == 0 ? 0 : 16;
        pos[0] = position[0];
        pos[1] = position[1];
    };

    public ArrayList<int[]> rangeOfMovement() {
        return new ArrayList<int[]>();
    }

    public void move(int[] n_p) {
        pos[0] = n_p[0];
        pos[1] = n_p[1];
    };

    public int getVal() {
        return val;
    }

    public int getCoordenate(int i) {
        return pos[i];
    }

    public int getTeamId() {
        return val < 16 ? 0 : 1;
    } // 0 -> white team; 1 -> black team;

    public boolean isCoordInRange(int coord) {
        return 0 <= coord && coord < 8;
    };

    protected ArrayList<int[]> sidewaysAndVertically() {
        ArrayList<int[]> res = new ArrayList<int[]>();
        int i = 0;
        while (i < 8) {
            if (i != this.pos[0]) {
                int[] rowPosInRange = { i, this.pos[1] };
                res.add(rowPosInRange);
            }
            if (i != this.pos[1]) {
                int[] colPosInRange = { this.pos[0], i };
                res.add(colPosInRange);
            }

        }

        return res;
    }

    protected ArrayList<int[]> diagonals() {
        ArrayList<int[]> res = new ArrayList<int[]>();
        int i = 0;
        while (i < 8) {
            if (i != this.pos[0]) {
                int[] rowPosInRange = { i, this.pos[1] };
                res.add(rowPosInRange);
            }
            if (i != this.pos[1]) {
                int[] colPosInRange = { this.pos[0], i };
                res.add(colPosInRange);
            }

        }

        return res;
    }

    /*
     * Sub-sequence of rangeOfMovement where all positions in the response are the
     * vectors of the line L: x*v + p
     * @var int x: Comes from iteration, determines the points between pos and this.pos
     * @var int[] v: unitary vector. Has same direction as (pos - this.pos)
     * @var int[] p: Passing point. this.pos
        REQUIERS: @atribute pos is a position in sequence this.rangeOfMovement
     */
    public ArrayList<int[]> subRangeOfMovement(int[] pos) {
        ArrayList<int[]> res = new ArrayList<int[]>();

        int[] v = {this.pos[0]-pos[0], this.pos[1]-pos[1]};
        // pos is at same row, at same col or at same diagonal
        // If they are at a diagonal, then abs(v[0]) == abs(v[1]). 
        // As I want the unitary vector I'll divide each coordinate by abs(v[0]) if v[0] != 0, else by abs(v[1]) 
        v[0] = v[0] != 0 ? v[0]/Math.abs(v[0]) : 0;   
        v[1] = v[1] != 0 ? v[1]/Math.abs(v[1]) : 0;
        
        if (distance(pointsInLine(1, v, this.pos), pos) < distance(this.pos, pos)) { // Getting closer to `pos` by incrementing x
            int x = 1;
            int[] point = pointsInLine(x, v, this.pos);
            while (point[0] != pos[0] || point[1] != point[1]){
                res.add(point);
                x++;
                point = pointsInLine(x, v, this.pos);
            }
            // while 
        }
        else { //decreasing x
            int x = -1;
            int[] point = pointsInLine(x, v, this.pos);
            while (point[0] != pos[0] || point[1] != point[1]){
                res.add(point);
                x--;
                point = pointsInLine(x, v, this.pos);
            }
        } 
        return res;
    }

    public String getName() {
        return this.name;
    }

    private double distance (int[] p1, int[] p2) {
        return Math.hypot(p1[0]-p2[0] , p1[1]-p2[1]);
    }

    private int[] pointsInLine(int x, int[] v, int[] p){
        int[] res = new int[2];
        res[0] = (x*v[0]+p[0]);
        res[1] = (x*v[1]+p[1]);
        return res;
    }
}
