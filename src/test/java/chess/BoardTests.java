package chess;
import chess.pieces.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;               

public class BoardTests {
    @Test
    void testExecutions() {
        Board board = new Board();
        //Add Pawns
        for (int i = 0; i< 8; i++) {
            for (int j = 0; j < 2; j++){
                int initRow = j == 0 ? 6 : 1; 
                int[] initialPos = { initRow , i};
                board.insert(new Pawn(j, initialPos), initialPos);
            }
        }
        board.toString();

        assertTrue(true);
    }

}
