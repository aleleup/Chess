package chess;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;               

public class BoardTests {
    
    @Test
    void testExecutions() {
        Board board = new Board();
        // 1st status
        System.out.println(board.toString());
        String firstKey = "6061626364656667707172737475767710111213141516170001020304050607";
        System.out.println(board.statusKey());
        assertEquals(board.statusKey(), firstKey);
        // white moves with success
        int [] wpawnPos = {6,4};
        int [] wnewPawnPos = {4,4};
        assertTrue(board.move(wpawnPos, wnewPawnPos));
        System.out.println(board.toString());
        String secondKey = "6061626344656667707172737475767710111213141516170001020304050607";
        System.out.println(board.statusKey());
        assertEquals(board.statusKey(), secondKey);

        // black moves with success
        int [] bpawnPos = {1,4};
        int [] bnewPawnPos = {3,4};
        assertTrue(board.move(bpawnPos, bnewPawnPos));
        System.out.println(board.toString());
        String thirdKey = "6061626344656667707172737475767710111213341516170001020304050607";
        System.out.println(board.statusKey());
        assertEquals(board.statusKey(), thirdKey);

        // white moves with success
        wpawnPos[1] = 5;
        wnewPawnPos[0] = 5; wnewPawnPos[1] = 5; 
        assertTrue(board.move(wpawnPos, wnewPawnPos));
        System.out.println(board.toString());
        String fourthKey = "6061626344556667707172737475767710111213341516170001020304050607";
        System.out.println(board.statusKey());
        assertEquals(board.statusKey(), fourthKey);

        // black moves with success
        bpawnPos[1] = 3;
        bnewPawnPos[0] = 3; bnewPawnPos[1] = 3; 
        assertTrue(board.move(bpawnPos, bnewPawnPos));
        System.out.println(board.toString());
        String fivethKey = "6061626344556667707172737475767710111233341516170001020304050607";
        System.out.println(board.statusKey());
        assertEquals(board.statusKey(), fivethKey);

        // white fails to move horse at 6,5
        int [] wHorse = {7, 6};
        int [] invalidHorse = {5,5};

        assertFalse(board.move(wHorse, invalidHorse));
        System.out.println(board.toString());
        String sixthKey = "6061626344556667707172737475767710111233341516170001020304050607";
        System.out.println(board.statusKey());
        assertEquals(board.statusKey(), sixthKey);

        // white eats 
        wpawnPos[0] = 4 ;wpawnPos[1] = 4;
        wnewPawnPos[0] = 3; wnewPawnPos[1] = 3; 
        assertTrue(board.move(wpawnPos, wnewPawnPos));
        System.out.println(board.toString());
        String seventhKey = "6061626333556667707172737475767710111288341516170001020304050607";
        System.out.println(board.statusKey());
        assertEquals(board.statusKey(), seventhKey);

        // ### TESTING PAWN VALIDATION

        // black advances  
        bpawnPos[0] = 1 ;bpawnPos[1] = 0;
        bnewPawnPos[0] = 3; bnewPawnPos[1] = 0; 
        assertTrue(board.move(bpawnPos, bnewPawnPos));
        System.out.println(board.toString());
        String eighthKey = "6061626333556667707172737475767730111288341516170001020304050607";
        System.out.println(board.statusKey());
        assertEquals(board.statusKey(), eighthKey);

        // white advances  
        wpawnPos[0] = 6 ;wpawnPos[1] = 0;
        wnewPawnPos[0] = 4; wnewPawnPos[1] = 0; 
        assertTrue(board.move(wpawnPos, wnewPawnPos));
        System.out.println(board.toString());
        String ninethKey = "4061626333556667707172737475767730111288341516170001020304050607";
        System.out.println(board.statusKey());
        assertEquals(board.statusKey(), ninethKey);

        // black tries to take pawn in front of him
        bpawnPos[0] = 3 ;bpawnPos[1] = 0;
        bnewPawnPos[0] = 4; bnewPawnPos[1] = 0; 
        assertFalse(board.move(bpawnPos, bnewPawnPos));
        System.out.println(board.toString());
        System.out.println(board.statusKey());
        assertEquals(board.statusKey(), ninethKey);

        
        // black tries to advance by it's diagonal
        bpawnPos[0] = 3 ;bpawnPos[1] = 0;
        bnewPawnPos[0] = 4; bnewPawnPos[1] = 1; 
        assertFalse(board.move(bpawnPos, bnewPawnPos));
        System.out.println(board.toString());
        System.out.println(board.statusKey());
        assertEquals(board.statusKey(), ninethKey);


        // black advances
        bpawnPos[0] = 1 ;bpawnPos[1] = 1;
        bnewPawnPos[0] = 3; bnewPawnPos[1] = 1; 
        assertTrue(board.move(bpawnPos, bnewPawnPos));
        System.out.println(board.toString());
        String tenthKey = "4061626333556667707172737475767730311288341516170001020304050607";
        System.out.println(board.statusKey());
        assertEquals(board.statusKey(), tenthKey);

        // white takes pawn legaly
        wpawnPos[0] = 4 ;wpawnPos[1] = 0;
        wnewPawnPos[0] = 3; wnewPawnPos[1] = 1; 
        assertTrue(board.move(wpawnPos, wnewPawnPos));
        System.out.println(board.toString());
        String eleventhKey = "3161626333556667707172737475767730881288341516170001020304050607";
        System.out.println(board.statusKey());
        assertEquals(board.statusKey(), eleventhKey);
    }

    // @Test
    // void testSettingBoardFromKey() {
    //     Board board = new Board();
    //     // 1st status
    //     System.out.println(board.toString());
    //     String firstKey = "6061626364656667707172737475767710111213141516170001020304050607";
    //     System.out.println(board.statusKey());
    //     assertEquals(board.statusKey(), firstKey);

    //     // 2nd Status
    //     String key2 = "3161626333556667707172737475767730881288341516170001020304050607";
    //     board.setBoardFromKey(key2);
    //     System.out.println(board.toString());
    //     System.out.println(board.statusKey());
    //     assertEquals(board.statusKey(), key2);

    //     // 3rd status random
    //     String key3 = "6061626344556667707172737475767710111233341516170001020304050607";
    //     board.setBoardFromKey(key3);
    //     System.out.println(board.toString());
    //     System.out.println(board.statusKey());
    //     assertEquals(board.statusKey(), key3);
    // }

    @Test
    void testMoveingFreelySomePieces(){
        Board board = new Board();
        // Moveing a Bishop
        String keyb = "8888888888888888888844888888888888888888888888888888888888888888";
        board.setBoardFromKey(keyb);
        System.out.println(board.toString());

        int[] bPos = {4,4};
        int[] newPos = {5,5};
        assertTrue(board.move(bPos, newPos));

        System.out.println(board.toString());
        String keyb2 = "8888888888888888888855888888888888888888888888888888888888888888";
        System.out.println(board.statusKey());
        assertEquals(board.statusKey(), keyb2);
        bPos = newPos.clone();
        newPos[0] = 3; newPos[1] = 5;
        assertFalse(board.move(bPos,newPos));

        newPos[0] = 7; newPos[1] = 3;
        assertTrue(board.move(bPos, newPos));
        System.out.println(board.toString());



        // Tower
        String keyt = "8888888888888888888888888888888888888888888888888888888888888844";
        board.setBoardFromKey(keyt);
        System.out.println(board.toString());

        int[] towerPos = {4,4};
        int[] towerNewPos = {0, 4};
        assertTrue(board.move(towerPos, towerNewPos));
        System.out.println(board.toString());
        towerPos = towerNewPos.clone();
        
        towerNewPos[1] = 0;
        assertTrue(board.move(towerPos, towerNewPos));
        System.out.println(board.toString());

        towerPos = towerNewPos.clone();
        towerNewPos[0] = 7; towerNewPos[1] = 7;

        assertFalse(board.move(towerPos, towerNewPos));
        System.out.println(board.toString());
    }

    @Test 
    void testKingChecked(){
        // Checking and unChecking white king
        System.out.println("\n TEST 3 \n");

        Board board = new Board();


        String keyb = "8888888888888888178888745588888888888888888888888888880100888802";
        board.setBoardFromKey(keyb);
        System.out.println(board.toString());
        assertFalse(board.isKingCheckedById(0));

        int[] t1 = {0,2}; int[] nt = {5,2};
        assertTrue(board.move(t1, nt));
        System.out.println(board.toString());

        // Trying to avoid check
        int[] wq = {7,4}; int[] wq1 = {0,7}; int[] wq2 = {4,4}; int[] wq3 = {7,7}; int[] nq = {5,2};
        assertFalse(board.move(wq, wq1)); 
        assertFalse(board.move(wq, wq2)); 
        assertFalse(board.move(wq, wq3)); 
  

        System.out.println(board.toString());


        assertTrue(board.move(wq, nq));
        assertFalse(board.isKingCheckedById(0));
        assertFalse(board.isKingCheckedById(1));

        System.out.println(board.toString());


        int[] bq = {0,1}; int[] nbq = {1,1};

        assertTrue(board.move(bq, nbq));

        System.out.println(board.toString());

        assertTrue(board.isKingCheckedById(0));
        assertFalse(board.isCheckMate(0));
        assertFalse(board.isKingCheckedById(1));    
        System.out.println(board.toString());


        int[] tower = {1,7}; int[] trytower = {0,7};
        assertFalse(board.move(tower, trytower));
        assertTrue(board.isKingCheckedById(0));
        assertFalse(board.isCheckMate(0));
        assertFalse(board.isKingCheckedById(1));    
        System.out.println(board.toString());


        int[] king = {5,5}; int[] tryking = {6,6};
        assertFalse(board.move(king, tryking));
        assertTrue(board.isKingCheckedById(0));
        assertFalse(board.isCheckMate(0));
        assertFalse(board.isKingCheckedById(1));
        System.out.println(board.toString());

         int[] tryking2 = {6,5};
        assertTrue(board.move(king, tryking2));
        assertFalse(board.isKingCheckedById(0));
        // assertFalse(board.isCheckMate(0));
        assertFalse(board.isKingCheckedById(1));
        System.out.println(board.toString());



        int[]blackqueen = {1,1}; int[] blackqueenError = {3,3};  
        assertTrue(board.move(blackqueen, blackqueenError));
        assertFalse(board.isKingCheckedById(0));
        // assertFalse(board.isCheckMate(0));
        assertFalse(board.isKingCheckedById(1));
        System.out.println(board.toString());


        int[] whitequeen = {5,2}; int[] whitequeenMate = {0,2};
        assertTrue(board.move(whitequeen, whitequeenMate));
        System.out.println(board.toString());

        assertFalse(board.isKingCheckedById(0));
        assertTrue(board.isKingCheckedById(1));
        assertTrue(board.isCheckMate(1));

    }
}
    