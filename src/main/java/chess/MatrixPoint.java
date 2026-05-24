package chess;
import java.lang.Math;
import java.util.ArrayList;

public class MatrixPoint {
    private int[] pos;
    
    public MatrixPoint(int pos_0, int pos_1) {
        pos = new int[2];
        pos[0] = pos_0; pos[1] = pos_1;
    } 

    /**
     * 
     * @param i:int \in {0,1}
     * @return this.pos[i]
     */
    public int[] getPos () {
        int[] res = new int[2];
        res[0] = this.pos[0]; res[1] = this.pos[1];
        return res;
    }

    /**
     * 
     * @param i:int \in {0, 1}
     * @param val:int
     * sets pos[i] to val 
     */
    public void set (int i, int val) { this.pos[i] = val; }
    
    /**
     * 
     * @param point:MatrixPoint valid point
     * @return Distance between point p and q
    */
    public double distance (MatrixPoint p, MatrixPoint q) {
        return Math.hypot(p.getPos()[0] - q.getPos()[0] , p.getPos()[1] - q.getPos()[1]);
    }


    /**
     * 
     * @param x:int
     * @param v:int[2]
     *  line L: x*v + pos;
     * @return MatrixPoint with coordenate p(x) = x*v + pos -> point in the
     */
    public MatrixPoint pointsInLine(int x, int[] v, MatrixPoint p){
        return new MatrixPoint((x*v[0]+ p.getPos()[0]), (x*v[1]+ p.getPos()[1]));
    }
    
     /**
      * 
      * @var x:int Comes from iteration, determines the points between q and this.pos
      * @var int[] v: unitary vector. Has same direction as (pos - this.pos)
      * @var int[] p: Passing point. {this.pos[0], this.pos[1]}
      * @requires: param q is a vertically, diagonally 
     * @return  List of vectors where all positions in the response are the
    *            vectors of the line L: x*v + p
     */


    public ArrayList<MatrixPoint> vectorsInBetween(MatrixPoint q) {
        ArrayList<MatrixPoint> res = new ArrayList<MatrixPoint>();

        int[] v = {this.pos[0] - q.getPos()[0], this.pos[1] - q.getPos()[1]};
        // pos is at same row, at same col or at same diagonal
        // If they are at a diagonal, then abs(v[0]) == abs(v[1]). 
        // As I want a vector of the form {1,1}, {1,0} or {0,1} I'll divide each coordinate by abs(v[0]) if v[0] != 0, else by abs(v[1]) 
        v[0] = v[0] != 0 ? v[0]/Math.abs(v[0]) : 0;   
        v[1] = v[1] != 0 ? v[1]/Math.abs(v[1]) : 0;
        
        if (distance(pointsInLine(1, v, this), q) < distance(this, q)) { // Getting closer to `pos` by incrementing x
            int x = 1;
            MatrixPoint point = pointsInLine(x, v, this);
            while (point.getPos()[0] != q.getPos()[0] || point.getPos()[1] != q.getPos()[1]){
                res.add(point);
                x++;
                point = pointsInLine(x, v, this);
            }
            // while 
        }
        else { //decreasing x
            int x = -1;
            MatrixPoint point = pointsInLine(x, v, this);
            while (point.getPos()[0] != q.getPos()[0] || point.getPos()[1] != q.getPos()[1]){
                res.add(point);
                x--;
                point = pointsInLine(x, v, this);
            }
        } 
        return res;
    }

    public boolean isPointInRange(int infLim, int supLim) {
        return infLim <= this.pos[0] && this.pos[0] < supLim &&
         infLim <= this.pos[1] && this.pos[1] < supLim ;
    }
}