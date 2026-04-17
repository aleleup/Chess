package chess;
import java.util.ArrayList;

import chess.pieces.*;

public class Board {
    private Piece[][] board; // 8x8 chess board
    private Piece[] piecesArr; // 0 -> 15 whites val pieces // 15 -> 31 blacks val pieces
    // val pieces -> {King, Queen, Tower, Knights, Bishops}
    public Board(){
        board = new Piece[8][8];
        piecesArr = new Piece[32]; 
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
                } 
                System.out.print(piecesArrIndex);
   
            }
        }
    };

    public Boolean move(int[] currentPos, int[] newPos) {
        Piece pieceTaken = board[newPos[0]][newPos[1]];
        Piece pieceToMove = board[currentPos[0]][currentPos[1]];
        
    //      ### Check if it is a legal move ###
        Boolean isInRange = false;
        ArrayList<int[]> validRange = pieceToMove.rangeOfMovement();
        int i = 0;
        while (!isInRange && i < validRange.size()) {
            int[] pos = validRange.get(i);
             if (pos[0] == newPos[0] && pos[1] == newPos[1]) {
                isInRange = true;
            }
            i++;
        }

        if (!isInRange || (pieceTaken != null && pieceTaken.getTeamId() == pieceToMove.getTeamId()) ) return false;
       
        if (pieceTaken != null && pieceTaken.getTeamId() != pieceToMove.getTeamId()) {
            int[] outOfRangePos = {8, 8};
            pieceTaken.move(outOfRangePos);
        };
        pieceToMove.move(newPos);
        board[newPos[0]][newPos[1]] = pieceToMove;
        board[currentPos[0]][currentPos[1]] = null;

        return true;
    };

    public String statusKey() {
        String res = "";
        for (Piece piece : piecesArr) {
            String pieceNotation = "" + piece.getCoordenate(0)  + piece.getCoordenate(1);            
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
                if (p == null) {rowString+= " |"; continue;}
                rowString +=  p.getName() + "|";
            }
            res += "\n" + rowString;
        }
        res += "\n Pieces Array: \n";
        for (Piece piece : piecesArr) {
            res += "|" + (piece == null ? "ERROR: PIECE NOT FOUND" : piece.getName()) + "|";
        }
        return res;
    }
    
    // Private Methods:
        private void insert(Piece p, int row, int col, int pieceArrIndex) {
            this.piecesArr[pieceArrIndex] = p;
            this.board[row][col] = p;
            // pieceArrIndex++; JAVA passes primitive values by copy and there's
            //  no way to pass the reference of pieceArrIndex. There's nothing else to do but to
            //  increment  pieceArrIndex outside the function
    }
}