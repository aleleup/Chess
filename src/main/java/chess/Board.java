package chess;


import chess.pieces.Piece;
import java.lang.Math;
public class Board {
    private Piece[][] board; // 8x8 chess board
    private Piece[] piecesArr; // 0 -> 15 whites val pieces // 15 -> 31 blacks val pieces
    // val pieces -> {King, Queen, Tower, Knights, Bishops}
    public Board(){
        board = new Piece[8][8];
        piecesArr = new Piece[32]; 
    };
    
    public void insert(Piece piece, int[] pos) {
        board[pos[0]][pos[1]] = piece;
        //White pieces store
        if (pos[0] >= 6) piecesArr[pos[1]] = piece;
        
        //Black pieces store
        if (pos[0] <= 1) piecesArr[pos[1] + 8] = piece;
        // val pieces order: 
        // {wP, wP, wP,wP ,wP,wP ,wP ,wP, wT, wKn, wB, wQ, wK, wB, wKn, wT, 
        // bP, bP, bP,bP ,bP,bP ,bP ,bP,bT, bKn, bB, bQ, bK, bB, bKn, bT}
    };

    public void move(int[] currentPos, int[] newPos) {
        Piece pieceTaken = board[newPos[0]][newPos[1]];
        if (pieceTaken != null) {
            int[] outOfRangePos = {8, 8};
            pieceTaken.move(outOfRangePos);
        };
        Piece pieceToMove = board[currentPos[0]][currentPos[1]];
        pieceToMove.move(newPos);
        board[newPos[0]][newPos[1]] = pieceToMove;
        board[currentPos[0]][currentPos[1]] = null;
    };

    public int boardStatusHash() {
        int res = 0;
        int i = 0;
        for (Piece piece : piecesArr) {
            int cord = 0;
            while (cord < 2) {
                res += piece.getCoordenate(cord) * Math.pow(10, i);
                i++; cord++;
            }
        }
        return res;
    }
    
    public Piece[] getPieces() {
        return piecesArr;
    };
    

    @Override
    public String toString() {
        String res = "";
        for (Piece[] row : this.board) {
            String rowString = "|";
            for (Piece p : row) {
                rowString +=  p.getName() + "|";
            }
            res += "\n" + rowString;
        }
        return res;
    }
}