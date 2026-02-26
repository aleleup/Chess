# DOCUMENTATIONS FOR THE BACKEND LOGIC/FUNCTIONALITIES:
Store the ideas of my blackboard so I can reutilize it

## Check Draws:
Very important feature for the game, for each time that a legal move is made the `Referee` instance will check if a draw is made. And this is an optimized version of how to do it.

Having this represatiantion structure:
``` java
    public class Board {
        private Piece[][] board; // 8x8 chess board
        private Piece[] piecesArr; // 0 -> 15 whites val pieces // 15 -> 31 blacks val pieces
        // val pieces -> {King, Queen, Tower, Knights, Bishops}
        public Board(){
            board = new Piece[8][8];
            piecesArr = new Piece[32]; 
        };
     //   ...
    }
    
```

The idea is to create a key  of the status of the position of the pieces in the board. The key function is injective, and it will take as an "input" the `piecesArr` Array. So there will be one and only one output for each input. This is the math function I've "created":

$ A = { n \in \mathbb{N}_0 : n ≤ 9 }$. Then $h: A^m -> \mathbb{N}/ h(x) =  \sum_{i=0}^{|x|-1} (10^ix_i)$
 

In an easy explanation, this function basically takes as an input a vector on n dimensions where each coordenate is a natural number less than 9 (very important) and creates a number of n digits. Ex: $v = (1,2,3,4) -> h(v) = \sum_{i=0}^{|v|-1} (10^iv_i)$ = $\sum_{i=0}^{3} (10^ix_i) = $1*10^0 + 2*10^1 + 3*10^2 + 4*10^3 = 1 + 20 + 300 + 4000 = 4321$.

Demo of inyective: $  (\forall v \in A^n)( \forall i : \mathbb{Z} ) (0 < i < |v| - 1  \implies v_i = ((h(v) div 10^{i-1})  mod 10)  ) $

v[i] is the i th digit in h(v).

* Why then?

After implemeting this funcion, and having at the referee class this structure:
```java
public class Referee {
    private Board board;
    private Dictionary<int, int> boardStatusCounter;
//...
}
```
Where key of `boardStatusCounter` is the status key of the board & value is the times it repeated itself.

The algoithm is the following
```java
private boolean checkDraw() {
    int h = board.statusKey();
    if (!areEnoughPieces(h)) return true;

    if (!boardStatusCounter.keys().contains(h)) {
        boardStatusCounter.insert(h, 1);
    }
    else {
        int val = boardStatusCounter.get(h)
        boardStatusCounter.update(h, val+1)
    };

    if (boardStatusCounter.get(h) == 3) return true;
    // More draw logic...

    return false
}
```
There are many other cases but the most crucial is this one.