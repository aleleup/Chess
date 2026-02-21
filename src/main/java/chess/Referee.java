package chess;

import java.util.ArrayList;
import java.util.HashMap;

public class Referee {
    private Board board;
    private ArrayList<String[]> whitePlayerCaptures; // Each String[] is [PieceKey, PieceVal]
    private ArrayList<String[]> blackPlayerCaptures;
    private ArrayList<String[]> historyArray; // Each String[] is [PieceKey, PosX, PosY]
    private HashMap<Integer, Integer> boardStatusCounter;

    public Referee() {
        this.board = new Board();
        this.whitePlayerCaptures = new ArrayList<String[]>();
        this.blackPlayerCaptures = new ArrayList<String[]>();
        this.historyArray = new ArrayList<String[]>();
        this.boardStatusCounter = new HashMap<Integer,Integer>();

        // for (int teamId = 0; teamId < 2; teamId++) {
        //     // Pawns
        //     for (int j = 0; j < 8; j++){
        //         int pawnRow = teamId == 0 ? 6 : 
        //     }
        // }
    };


}
