package chess;
import java.util.ArrayList;

import chess.pieces.*;

public class Board {
    private Piece[][] board; // 8x8 chess board
    private Piece[] piecesArr; // 0 -> 15 whites val pieces // 15 -> 31 blacks val pieces
    private King[] kingsAccess;
    // val pieces -> {King, Queen, Tower, Knights, Bishops}
    public Board(){
        board = new Piece[8][8];
        piecesArr = new Piece[32];
        kingsAccess = new King[2];
        int piecesArrIndex = 0;
        for (int id = 0; id < 2; id ++) {
            int pawnRow = id == 1 ? 1 : 6;
            int valPiecesRow = id == 1 ? 0 : 7;
            // In two spread loops because it will be prettier to have pieceArray in this order: 
            //      piecesArr = [P,P,P,P,P,P,P,P,T,Kn,B,Q,K,B,Kn,T, ...]

            for (int col = 0; col < 8; col++) {
                // Pawn Creation
                int[] pawnPos = {pawnRow, col};
                Pawn p = new Pawn(id, pawnPos);
                this.insert(p, pawnRow, col, piecesArrIndex);
                piecesArrIndex++;
                System.out.print(piecesArrIndex);
            }
            // Valuable Pieces Creation.
            for (int col = 0; col < 8; col++) {
                int[] valPiecesPos = {valPiecesRow, col};
                if (col % 7 == 0) { // col in {0, 7}
                    Tower t = new Tower(id, valPiecesPos);
                    this.insert(t, valPiecesRow, col, piecesArrIndex);
                    piecesArrIndex++;
                }
                if (col % 5 == 1) { // col in {1, 6}
                    Knight kn = new Knight(id, valPiecesPos);
                    this.insert(kn, valPiecesRow, col, piecesArrIndex);
                    piecesArrIndex++;
                }
                if (col % 3 == 2){ // col in {2, 5}
                    Bishop b = new Bishop(id, valPiecesPos);
                    this.insert(b, valPiecesRow, col, piecesArrIndex);                
                    piecesArrIndex++;
                }    
                if (col == 3) {
                    Queen q = new Queen(id, valPiecesPos);
                    this.insert(q, valPiecesRow, col, piecesArrIndex);                
                    piecesArrIndex++;
                }        
                if (col == 4) {
                    King k = new King(id, valPiecesPos);
                    this.insert(k, valPiecesRow, col, piecesArrIndex);                
                    piecesArrIndex++;

                    kingsAccess[id] = k;
                } 
                System.out.print(piecesArrIndex);
   
            }
        }
    };

    public Boolean move(int[] currentPos, int[] newPos) {
        Piece pieceTaken = board[newPos[0]][newPos[1]];
        Piece pieceToMove = board[currentPos[0]][currentPos[1]];
        King myKing = kingsAccess[pieceToMove.getTeamId()];
        if (myKing.getIsInCheck()) {
            // return this.kingInCheckCase()
        }

        if (!isLegalMove(pieceToMove, pieceTaken, newPos)) return false;
       
        if (pieceTaken != null) {
            int[] outOfRangePos = {8, 8};
            pieceTaken.move(outOfRangePos);
        };
        pieceToMove.move(newPos);
        board[newPos[0]][newPos[1]] = pieceToMove;
        board[currentPos[0]][currentPos[1]] = null;
        this.checkNewCheck(myKing);
        if (myKing.getIsInCheck()) { // Case the move makes my king at check (Ilegal)
            if (pieceTaken != null) {
                pieceTaken.move(newPos);
            };
            pieceToMove.move(currentPos);
            board[newPos[0]][newPos[1]] = pieceTaken;
            board[currentPos[0]][currentPos[1]] = pieceToMove;
            return false;
            } // Everything back to normal

        // 
        this.checkNewCheck(kingsAccess[(pieceToMove.getTeamId() + 1) % 2]); // Checking if other team's got checked
        return true;
    };

    public String statusKey() {
        String res = "";
        for (Piece piece : piecesArr) {
            int[] piecePos = piece.getPosition().getPos();
            String pieceNotation = "" + piecePos[0]  + piecePos[1];            
            res += pieceNotation;
        }
        return res;
    }
    
    public Piece[] getPieces() {
        return piecesArr;
    };
    

    @Override
    public String toString() {
        String res = "\n Board: \n \n";
        for (Piece[] row : this.board) {
            String rowString = "|";
            for (Piece p : row) {
                if (p == null) rowString+= "  |";
                else rowString += (p.getTeamId() == 0 ? "w" : "b") + p.getName() + "|";

            }
            res += "\n" + rowString;
        }
        res += "\n Pieces Array: \n";
        for (Piece piece : piecesArr) {
            res += "|" + (piece == null ? "ERROR: PIECE NOT FOUND" : piece.getName()) + "|";
        }
        return res;
    }
    
    // #### Private Methods: ###
    private void insert(Piece p, int row, int col, int pieceArrIndex) {
            this.piecesArr[pieceArrIndex] = p;
            this.board[row][col] = p;
            // pieceArrIndex++; JAVA passes primitive values by copy and there's
            //  no way to pass the reference of pieceArrIndex. There's nothing else to do but to
            //  increment  pieceArrIndex outside the function
    }
    private boolean isLegalMove(Piece piece, Piece pieceTaken,int[] newPos) {

        
        // TODO [VALIDATION]: KING IN CHECK CASE
        
        // PAWN ILEGAL CASES
        if  (piece.getName() == "P") { 
            int[] piecePos = piece.getPosition().getPos();
            if ( // Taking a piece in front of a pawn || Moveing a pawn diagonaly
                (pieceTaken != null && piecePos[1] == newPos[1]) || 
                (pieceTaken == null && piecePos[1] != newPos[1])
            ) return false;
        }
        
    
        if (!this.isInRangeOfMovement(piece, newPos)) return false;


        // Check if there's a piece between pieceToMove and newPos. I only care if `piece` isn't a Knight 
        if (piece.getName() != "Kn"){
            MatrixPoint piecePos = piece.getPosition();
            MatrixPoint newPosPoint = new MatrixPoint(newPos[0], newPos[1]);
            ArrayList<MatrixPoint> subRange = piecePos.vectorsInBetween(newPosPoint);
            for (int i = 0; i < subRange.size(); i++) {
                MatrixPoint p = subRange.get(i);
                int[] pos = p.getPos();
                Piece pieceInTheMiddle = this.board[pos[0]][pos[1]];
                if (pieceInTheMiddle != null) return false;
            }
        }


        // Check if player is taking own piece.
        if (pieceTaken != null && 
        (pieceTaken.getTeamId() == piece.getTeamId() || (pieceTaken == kingsAccess[0] || pieceTaken == kingsAccess[1])) ) return false;
        return true;
    }

    /**
     * process `checkNewCheck`: Inspects if a given King k is being attacked by an other teams key.
     * Core Idea: From the kings perspective, look for enemies pieces.
     *  A king has, in the worts case, 8 positions to move. But these positions are in this directions:
     *      2 diagonals: v_1 = (k.pos) + (-1, 1); v_2 = (k.pos) + (1,1)
     *      side-ways: v_3 = (k.pos) + (0, 1);
     *      Front & Back: v_4 = (k.pos) + (1, 0)
     *  I can make a Line for each directional vector,  L : x * v_n + (k.pos)
     * Then there is the function p(x) = x * v_n + (k.pos) that gives a point in the line.
     * Then I assume there is an int a for then to store P = {p(a), p(-a)} and for each P_m I check:
     *      1) Is P_m outside of the board?
     *      2) Is there a piece at P_m?
     * If either of those questions is true, then I do not want to continue searching at that direction. 
     * For that I'll use dirValidator: {BoolxBool} where dirValidator_m is related to P_m. 
     * Else if I'm inside the board and there is no piece at P_m I continue the search with P = {p(a+1), p(-a-1)}

        When Is the king at check:
            1) When at P_m is a piece from k.id != piece.id ^ k is at rangeOfMovement of piece
            2) Special cases:
                Pawn: King needs to be at a diagonal of a pawn
                Knight: See them appart beacuse of their range of movement

    */

    private void checkNewCheck(King k) {
        int[][] directionalVectors = {
            {1, -1}, {1, 0}, {1, 1}, {0,1}
        };
        MatrixPoint kPos = k.getPosition();

        for (int[] v : directionalVectors) {
            boolean inRange = true;
            int x = 1;
            boolean[] dirValidator = {true, true}; // Answers if it's worth to keep looking at positionInLine[i]?  
            while (inRange) {
                int[][] pointsInLine = {
                {x * v[0] + kPos.getPos()[0], x * v[1] + kPos.getPos()[1]},
                {-x * v[0] + kPos.getPos()[0], -x * v[1] + kPos.getPos()[1]}
                };  
                for (int i = 0; i < 2; i++) {
                    MatrixPoint p = new MatrixPoint(pointsInLine[i][0], pointsInLine[i][1]);
                    
                    if (!dirValidator[i] ) continue;
                    if (!p.isPointInRange(0, 8)) { 
                        dirValidator[i] = false; 
                        continue;
                     }
                    int[] pPos = p.getPos();
                    Piece possiblePiece = this.board[pPos[0]][pPos[1]];
                    
                    if (possiblePiece == null) continue;
                    if (possiblePiece.getTeamId() == k.getTeamId() || 
                        (possiblePiece.getTeamId() != k.getTeamId() && 
                        !this.isInRangeOfMovement(possiblePiece, kPos.getPos()))
                        ) dirValidator[i] = false; 

                    else { // TODO: VALIDATE PAWN IN FRONT OF KING CASE
                            dirValidator[i] = false;
                            k.setIsInCheck(true);
                            k.addAnnoyer(possiblePiece);
                    }
                }
                if (!dirValidator[0] && !dirValidator[1]) inRange = false;
                x++;
            }
        }
    }

    private boolean isInRangeOfMovement(Piece piece, int[] newPos) {
        Boolean isInRange = false;
        ArrayList<int[]> range = piece.rangeOfMovement();
        for (int i = 0; (i < range.size() && !isInRange); i++){
            int[] pos = range.get(i);
             if (pos[0] == newPos[0] && pos[1] == newPos[1]) {
                isInRange = true;
            }
        }
        return isInRange;
    }
}