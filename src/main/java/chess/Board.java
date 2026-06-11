package chess;
import java.util.ArrayList;
import java.util.Arrays;

import chess.pieces.*;

public class Board {
    private Piece[][] board; // 8x8 chess board
    private Piece[] piecesArr; // 0 -> 15 whites val pieces // 15 -> 31 blacks val pieces
    private King[] kingsAccess;
    private int[] outOfRangePos  = {8,8};
    // [TODO] CHECK KNIGHT RANGE OF MOVEMENT.
    // [TODO] 
    private ArrayList<String[]> whitePlayerCaptures; 
    private ArrayList<String[]> blackPlayerCaptures;
    private ArrayList<String[]> historyArray;
    private int[][] casteledPiecePos;
    // val pieces -> {King, Queen, Rook, Knights, Bishops}
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
                    Rook r = new Rook(id, valPiecesPos);
                    this.insert(r, valPiecesRow, col, piecesArrIndex);
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

    public boolean move(int[] currentPos, int[] newPos) {
        Piece pieceTaken = board[newPos[0]][newPos[1]];
        Piece pieceToMove = board[currentPos[0]][currentPos[1]];
        King myKing = kingsAccess[pieceToMove.getTeamId()];

        if (!isLegalMove(pieceToMove, pieceTaken, newPos)) return false;
       
        if (pieceTaken != null) {
            pieceTaken.move(this.outOfRangePos);
        };
        pieceToMove.move(newPos);
        board[newPos[0]][newPos[1]] = pieceToMove;
        board[currentPos[0]][currentPos[1]] = null;
        this.checkNewCheck(myKing);
        if (myKing.getIsInCheck()) { // Case the move makes my king at check (Ilegal)
            if (pieceTaken != null) pieceTaken.move(newPos);
            pieceToMove.move(currentPos);
            board[newPos[0]][newPos[1]] = pieceTaken;
            board[currentPos[0]][currentPos[1]] = pieceToMove;
            return false;
        } // Everything back to normal
        //myKing || rook moved -> that piece can not castle anymore
        this.checkAndSetCasteling(pieceToMove);        
        this.checkNewCheck(kingsAccess[(pieceToMove.getTeamId() + 1) % 2]); // Checking if other team's got checked
        return true;
    };

    public boolean moveAndUpgradePawn(int[] currentPos, int[] newPos, String newPieceName){

        Piece pawn = board[currentPos[0]][currentPos[1]]; 
        Piece pawnUpgraded;
        int pawnId = pawn.getTeamId();
        
        if (!isLegalMove(pawn, board[newPos[0]][newPos[1]], newPos)) return false;

        if (newPieceName.equals("Kn")){
            pawnUpgraded = new Knight(pawnId, newPos);
        }
        else if (newPieceName.equals("B")){
            pawnUpgraded = new Bishop(pawnId, newPos);
        } else if (newPieceName.equals("R")){
            pawnUpgraded = new Rook(pawnId, newPos);
        } else if (newPieceName.equals("Q")){
            pawnUpgraded = new Queen(pawnId, newPos);
        } else { return false; }

        // moveing pawn
        boolean canMove = this.move(currentPos, newPos);

        if (!canMove) return false;

        // exchanging pawn for it's upgrade at board
        board[newPos[0]][newPos[1]] = pawnUpgraded;
        // getting pawn out of range
        pawn.move(this.outOfRangePos);

        // exchanging pawn for it's upgrade at board
        int startIndex = pawnId == 0 ? 0 : 16;
        int pawnPos = 0;
        for (int i = startIndex; i < startIndex + 16;  i++){
            if (piecesArr[i] == pawn) { // If addreses are the same.
                pawnPos = i; break;
            }
        }
        piecesArr[pawnPos] = pawnUpgraded;
        return true;

    }

    /**
     * @param kingsPos Requires to be the current position of the king
     * @param rookPos Requires to be the current position of a rook with same id than the king.
     * @return Whether it is valid to castle the king or not

        res = false ^ board_0 = bord_f <==> (
                ¬(king.canCast ^ rook.canCast) v 
                // there are pieces in the middle or there is a point in between kingsPos and rookPos where the king gets checked
            ) v
        res = true ^ (
            bord_f[kingsPos[0]][kingsPos[1]] = bord_f[rookPos[0]][rookPos[1]] = null ^ (
                (|rookPos - kingsPos| = 4 -> bord_f[kingsPos[0]][2] = king ^ bord_f[kingsPos[0]][3] = rook) v
                (|rookPos - kingsPos| = 3 -> bord_f[kingsPos[0]][1] = king ^ bord_f[kingsPos[0]][2] = rook) 

            )
        )
    */

    public boolean castleKing(int[] kingsPos, int[] rookPos) {
        King king = (King) board[kingsPos[0]][kingsPos[1]];
        Rook rook = (Rook) board[rookPos[0]][rookPos[1]];


        // Some piece moved or the king is already at check
        if (!(king.getCanCastle() && rook.getCanCastle()) || king.getIsInCheck() ) return false;

        ArrayList<MatrixPoint> pointsInBetween = king.getPosition().vectorsInBetween(rook.getPosition());
        //If a piece is attacking a block in between the king and the rook but not in the 2 blocks where the king will move,
        //  then the castle is valid.
        int blockMoves = 0;
        for (MatrixPoint currentBlock : pointsInBetween) {
            int[] pos = currentBlock.getPos();
            if (board[pos[0]][pos[1]] != null) return false;
            
            if (blockMoves > 1) continue;
            // Moveing king to currentBlock pos
            board[kingsPos[0]][kingsPos[1]] = null;
            board[pos[0]][pos[1]] = king;
            king.move(pos);
            this.checkNewCheck(king);

            // Going back to original board and king status
            board[kingsPos[0]][kingsPos[1]] = king;
            board[pos[0]][pos[1]] = null;
            king.move(kingsPos);

            // checking if previous movement got my king checked
            if (king.getIsInCheck()) {
                king.setIsInCheck(false);
                return false;
            }
            blockMoves++;
        }
        // If we get here, then it is legal to castle:
        int distanceBetweenKingAndRook = Math.abs(kingsPos[1] - rookPos[1]);
        board[kingsPos[0]][kingsPos[1]] = null;
        board[rookPos[0]][rookPos[1]] = null;
        int[] kingNewPos = kingsPos.clone();
        int[] rookNewPos = rookPos.clone();
        // King is moveing to the "right"
        if (distanceBetweenKingAndRook == 3) {
            kingNewPos[1] = 6; rookNewPos[1] = 5;            
        } else if (distanceBetweenKingAndRook == 4) { // King is moveing to the "left"
            kingNewPos[1] = 2; rookNewPos[1] = 3;            
        }
        board[kingNewPos[0]][kingNewPos[1]] = king;
        board[rookNewPos[0]][rookNewPos[1]] = rook;

        king.move(kingNewPos);
        king.setCanCastle(false);
        
        rook.move(rookNewPos);
        rook.setCanCastle(false);

        this.casteledPiecePos = new int[2][];
        this.casteledPiecePos[0] = king.getPosition().getPos();
        this.casteledPiecePos[1] = rook.getPosition().getPos();
        return true;
    }


    public int[][] getCasteledPiecesPos(){
        return this.casteledPiecePos;
    }
    /**
     * @param id
     * @return if there is any legal move for the player {id}
     * The core idea is to check if there is legal move looking at all the pieces of the {id} team.
     * The reason of this method is to check when a game is over. 
     */
    public boolean isStalmate(int id) {
        int startIndex = id == 0 ? 0 : 16;
        for (int i = startIndex; i < startIndex + 16 ; i++) {
            Piece p = this.piecesArr[i];
            //Piece is out of game
            if (!p.getPosition().isPointInRange(0, 8)) continue;
            for (int[] t : p.rangeOfMovement()) {
                // Saving current board status
                Piece possiblePiece = board[t[0]][t[1]];
                int[] originalPos = p.getPosition().getPos();
                // Moveing piece, checkig if it is legal (Is a possible move that kills the )
                boolean isPotencialMove = this.move(originalPos, t);

                if (isPotencialMove) { // then the move has been done. We need to keep things as they were.
                    
                    p.move(originalPos);
                    board[originalPos[0]][originalPos[1]] = p;
    
                    if (possiblePiece != null) possiblePiece.move(t);
                    board[t[0]][t[1]] = possiblePiece;
                    
                    
                    // After all we return false because it exists a legal movement and there isn't a checkmate.
                    return false;
                }
            }
        }


        return true;
    }

    public String statusKey() {
        String res = "";
        for (Piece piece : this.piecesArr) {
            int[] piecePos = piece.getPosition().getPos();
            String pieceNotation = "" + piecePos[0]  + piecePos[1];            
            res += pieceNotation;
        }
        return res;
    }
    

    /**
     * 
     * @param key: Numeric String of length 64 where every digit is in between 0 to 8
     * Clears current status of the board
     * Sets the pieces specified in the key
     * {}
     */
    public void setBoardFromKey(String key) {
        int i = 0;
        int c = 0;
        this.board = new Piece[8][8];
        while (i < 63) {
            int[] pos = new int[2];
            pos[0] = key.charAt(i) - '0'; pos[1] = key.charAt(i + 1) - '0';
            Piece p = this.piecesArr[c];
            p.move(pos);
            MatrixPoint point = new MatrixPoint(pos[0], pos[1]);
            if (point.isPointInRange(0, 8)) {
                this.board[pos[0]][pos[1]] = p;
            }
            i+=2;
            c++;
        }
        // this.checkNewCheck(kingsAccess[0]); this.checkAndSetCasteling(kingsAccess[1]);
    }
    
    
    public boolean isKingCheckedById(int id) {
        return this.kingsAccess[id].getIsInCheck();
    }

    public boolean arePiecesEnough(int id){
        int kingIndex = id == 0 ? 12 : 28;
        int horse1Index = id == 0 ? 9 : 25;
        int horse2Index = id == 0 ? 14 : 30;
        int bishop1Index = id == 0 ? 10 : 26;
        int bishop2Index = id == 0 ? 13 : 29;
        int oponentKing = id == 0 ? 28 : 12;

        // King against King
       Integer[] caseKings = {kingIndex, oponentKing}; 
        
        // King and Knight againts King
        Integer[] caseKn1 = {kingIndex, horse1Index, oponentKing};
        Integer[] caseKn2 = {kingIndex, horse2Index, oponentKing};


        // King and Both Kinghts againts King
        Integer[] caseBothKns = {kingIndex, horse1Index, horse2Index, oponentKing};
        
        // King and Bishop against King
        Integer[] caseBi1 = {kingIndex, bishop1Index, oponentKing};
        Integer[] caseBi2 = {kingIndex, bishop2Index, oponentKing};

        Integer[][] allCases = {caseKings, caseKn1, caseKn2, caseBothKns, caseBi1, caseBi2,};

        for (Integer[] caseIndex : allCases) {
            ArrayList<Integer> exceptionIndexArr = new ArrayList<Integer>(Arrays.asList(caseIndex));
            if (this.allPiecesOutOfRange(exceptionIndexArr)) return false;
        }
        return true;
    }
    
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
        for (Piece piece : this.piecesArr) {
            res += "|" + (piece == null ? "ERROR: PIECE NOT FOUND" : piece.getName()) + "|";
        }
        
        res += " \n" + "\n Board Hash: \n" + this.statusKey() + " \n" ;
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
        ArrayList<Piece> kingsAnnoyers = new ArrayList<Piece>();
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

                    // Case avoiding when an enemy pawn is infront of the king to not get a game error (falsy check)   
                    else {
                        dirValidator[i] = false;
                        if (possiblePiece.getName() != "P" || possiblePiece.getPosition().getPos()[0] != kPos.getPos()[0]) {
                            kingsAnnoyers.add(possiblePiece);
                       }
                }
                }
                if (!dirValidator[0] && !dirValidator[1]) inRange = false;
                x++;
            }
        }

        // SPECIAL CASES: Kingts. White knights are at indexes 9 and 14. black knights are at indexes 9 + 16 = 25 and 14 + 16 = 30
        int kn1Index = k.getTeamId() == 0 ? 25 : 9;
        int k2Index = k.getTeamId() == 0 ?  30 : 14;
        Piece[] knightsArray = {this.piecesArr[kn1Index],this.piecesArr[k2Index]};

        for (Piece kn : knightsArray) {
            if (this.isInRangeOfMovement(kn, k.getPosition().getPos())) {
                kingsAnnoyers.add(kn);
            }
        }

        // size(kingsAnnoyers > 0) -> k.isIncheck = true;
        if (kingsAnnoyers.size() > 0) k.setIsInCheck(true);
        else k.setIsInCheck(false);

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

    private void checkAndSetCasteling (Piece p) {
        if (p.getName() == "K") {
            King k = (King) p;
            k.setCanCastle(false);
        }
        if (p.getName() == "R") {
            Rook r = (Rook) p;
            r.setCanCastle(false);
        }

    }


    private boolean allPiecesOutOfRange(ArrayList<Integer> exceptionIndexes){
        for (int i = 0; i < this.piecesArr.length; i++) {
            if (exceptionIndexes.contains(i)) continue;
            if (this.piecesArr[i].getPosition().isPointInRange(0, 8)) return false;
        }
        return true;
    }
}