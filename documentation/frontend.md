# DOCUMENTATIONS FOR THE FRONTEND LOGIC/FUNCTIONALITIES:
Store the ideas of my blackboard so I can reutilize it

## Board representation at front end:

- white player board POV:

|   |   |   |   |   |
|---|---|---|---|---|
| (0,0) | (0,1) | (0,2) | ... | (0,7)  |  
| (1,0) | (1,1) | (1,2) | ... | (1,7)  |  
...
| (7,0) | (7,1) | (7,2) | ... | (7,7)  |  


- black player board POV:

|   |   |   |   |   |
|---|---|---|---|---|
| (7,0) | (7,1) | (7,2) | ... | (7,7)  |  
| (6,0) | (6,1) | (6,2) | ... | (6,7)  |  
...| ...| ...| ...| ...
| (0,0) | (0,1) | (0,2) | ... | (0,7)  |  


*   How to create a relative board to respect the players POV while mantaining the objectiveness of the board (for server processing)

Solution: Create a `Block` Class/Component wich stores it's objective position, but then the block is stored in a matrix and rendered in it's relative position.

```ts
class Block {
    private color: string;
    private position: Array<number> //Array of length 2 for simplicity
    // .... 
    public Block(i: number, j: number ){
        position = [i, j]
    }
}

// ...

const board: Array<Array<Block>>
for (let i = 0; i < 8; i++) {
    const row = new Array(8);
    relativeRow = player.id == 0? i : 7 - i // Core of relative POV!!!
    // id = 0 -> white; id = 1 -> black
    for (let j = 0; j < 8; j++) {
        b = new Block(i, j);
        row[j] = b;
    }
    board[newRow] = row
}
```
Then I can guide the block by it's objetive position and determine it's color. Then it's needed just to render the board with it's blocks.

