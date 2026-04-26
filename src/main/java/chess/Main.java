package chess;

/**
 * Hello world!
 * 
 */
public class Main
{
    public static void main( String[] args )
    {
       Board board = new Board();
        // 1st status
        System.out.println(board.toString());
        String firstKey = "6061626364656667707172737475767710111213141516170001020304050607";
        System.out.println(board.statusKey());
        System.out.println(board.statusKey() == firstKey);

        // white moves with success
        int [] wpawnPos = {6,4};
        int [] wnewPawnPos = {4,4};
        System.out.println(board.move(wpawnPos, wnewPawnPos));
        System.out.println(board.toString());
        String secondKey = "6061626344656667707172737475767710111213141516170001020304050607";
        System.out.println(board.statusKey());
        System.out.println(board.statusKey() == secondKey);

        // black moves with success
        int [] bpawnPos = {1,4};
        int [] bnewPawnPos = {3,4};
        System.out.println(board.move(bpawnPos, bnewPawnPos));
        System.out.println(board.toString());
        String thirdKey = "6061626344656667707172737475767710111213341516170001020304050607";
        System.out.println(board.statusKey());
        System.out.println(board.statusKey() == thirdKey);

        // white moves with success
        wpawnPos[1] = 5;
        wnewPawnPos[0] = 5; wnewPawnPos[1] = 5; 
        System.out.println(board.move(wpawnPos, wnewPawnPos));
        System.out.println(board.toString());
        String fourthKey = "6061626344556667707172737475767710111213341516170001020304050607";
        System.out.println(board.statusKey());
        System.out.println(board.statusKey() == fourthKey);

        // black moves with success
        bpawnPos[1] = 3;
        bnewPawnPos[0] = 3; bnewPawnPos[1] = 3; 
        System.out.println(board.move(bpawnPos, bnewPawnPos));
        System.out.println(board.toString());
        String fivethKey = "6061626344556667707172737475767710111233341516170001020304050607";
        System.out.println(board.statusKey());
        System.out.println(board.statusKey() == fivethKey);

        // white fails to move horse at 6,5
        int [] wHorse = {7, 6};
        int [] invalidHorse = {5,5};

        System.out.println(board.move(wHorse, invalidHorse));
        System.out.println(board.toString());
        String sixthKey = "6061626344556667707172737475767710111233341516170001020304050607";
        System.out.println(board.statusKey());
        System.out.println(board.statusKey() == sixthKey);

         // white eats 
        wpawnPos[0] = 4 ;wpawnPos[1] = 4;
        wnewPawnPos[0] = 3; wnewPawnPos[1] = 3; 
        System.out.println(board.move(wpawnPos, wnewPawnPos));
        System.out.println(board.toString());
        String seventhKey = "6061626333556667707172737475767710111288341516170001020304050607";
        System.out.println(board.statusKey());
        // System.out.println(board.statusKey(), seventhKey);

        // ### TESTING PAWN VALIDATION

        // black advances  
        bpawnPos[0] = 1 ;bpawnPos[1] = 0;
        bnewPawnPos[0] = 3; bnewPawnPos[1] = 0; 
        System.out.println(board.move(bpawnPos, bnewPawnPos));
        System.out.println(board.toString());
        String eighthKey = "6061626333556667707172737475767730111288341516170001020304050607";
        System.out.println(board.statusKey());
        // System.out.println(board.statusKey(), eighthKey);

        // white advances  
        wpawnPos[0] = 6 ;wpawnPos[1] = 0;
        wnewPawnPos[0] = 4; wnewPawnPos[1] = 0; 
        System.out.println(board.move(wpawnPos, wnewPawnPos));
        System.out.println(board.toString());
        String ninethKey = "4061626333556667707172737475767730111288341516170001020304050607";
        System.out.println(board.statusKey());
        // System.out.println(board.statusKey(), ninethKey);

        // black tries to take pawn in front of him
        bpawnPos[0] = 3 ;bpawnPos[1] = 0;
        bnewPawnPos[0] = 4; bnewPawnPos[1] = 0; 
        System.out.println(board.move(bpawnPos, bnewPawnPos));
        System.out.println(board.toString());
        System.out.println(board.statusKey());
        // System.out.println(board.statusKey(), ninethKey);
    }
}
