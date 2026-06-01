package chess;
import io.javalin.Javalin;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//* [TODO] Document the idea of front-end messages: 
//          [MOVE]: calls move, [PAWN_UPGRADE]: calls moveAndUpgradePawn, [CASTLE]: calls castleKing
// TODO: DELETE PREVIOUS STATUS HASH MAP WHENEVER THE FRONT-END MESSAGE IS [TRY_UPGRADE_PAWN] and the method return true. 



public class WebSocketServer {
    // Simple carrier of data
    //Map<String, Set<WsContext>> playGround -> future
    private static final Set<WsContext> players = ConcurrentHashMap.newKeySet();
    private static Board board;
    private static HashMap<String, Integer> boardStatusCounter;
    private static int playerTurn = 0;
    /*RECORDS*/
    private record BodyMessage( 
        String typeOfMove, int[] currentPos, int[] newPos, 
        int playerId, String pawnUpgrade, double timeStamp
    ){};

    private static record BrodcastMessage(boolean wasLegalMove, int playerTurn, GameOverData gameOverData){};

    private static record GameOverData(boolean isTie, int winnerId, String reasson){};

    public static void main(String[] args){
        ObjectMapper mapper = new ObjectMapper();
        Javalin app = Javalin.create().start(7070);
        app.ws("/test", ws -> {
            ws.onConnect(context -> {
                System.out.println("Client connected: " + context.sessionId());
                keepConnectionAlive(context);
            });
            ws.onMessage(context -> {
                String message = context.message();
                System.out.println("Client mesage: " + message);

            });

            ws.onClose(context -> {
                System.out.println("Client has disconnected: " + context.sessionId());
            });

            ws.onError(context -> {
                System.out.println("Client has disconnected with error: " + context.error());
            });


        });
        
        // app.ws("/test-structures", ws -> {
            
        //     ws.onConnect(context -> {
        //         System.out.println("Client connected: " + context.sessionId());
        //         keepConnectionAlive(context);
        //         connections++;
        //         context.send("Amount of connections now: " + connections);
        //         keep2Connections(context, connections);
        //     });
        //     ws.onMessage(context -> {
        //         String message = context.message();
        //         System.out.println("Client mesage: " + message);
        //         try {
                
        //             BodyMessage playerMessage = mapper.readValue(message, BodyMessage.class);
        //             System.out.println("player data " + playerMessage.typeOfMove() + playerMessage.playerId() + playerMessage.timeStamp); 
        //             BrodcastMessage serverMessage = new BrodcastMessage(true, 1, false, null);
        //             String brodcastMessage = mapper.writeValueAsString(serverMessage);
        //             brodcast(brodcastMessage);
        //         } catch (Exception e) {
        //             System.out.println("ERROR: " + e);
        //         }

        //     });

        //     ws.onClose(context -> {
        //         System.out.println("Client has disconnected: " + context.sessionId());
        //         connections--;
        //     });

        //     ws.onError(context -> {
        //         System.out.println("Client has disconnected with error: " + context.error());
        //     });

            
        // });
    
        app.ws("/board", ws -> {
            ws.onConnect(context -> {
                System.out.println("Client connected: " + context.sessionId());
                if (board == null) board = new Board();
                keepConnectionAlive(context);
                keep2Connections(context);
                context.send("Amount of connections now: " + players.size());
            });
            ws.onMessage(context -> {
                String message = context.message();
                System.out.println("Client mesage: " + message);
                try {
                
                    BodyMessage playerMessage = mapper.readValue(message, BodyMessage.class);
                    System.out.println("player data " + playerMessage.typeOfMove() + playerMessage.playerId() + playerMessage.timeStamp); 
                    BrodcastMessage serverMessage = movesHandle(playerMessage);
                    String brodcastMessage = mapper.writeValueAsString(serverMessage);
                    brodcast(brodcastMessage);
                } catch (Exception e) {
                    System.out.println("ERROR: " + e);
                }

            });

            ws.onClose(context -> {
                System.out.println("Client has disconnected: " + context.sessionId());
            });

            ws.onError(context -> {
                System.out.println("Client has disconnected with error: " + context.error());
            });

        });
    }


    private static void keepConnectionAlive(WsConnectContext ctx) {
        // 1. Evitar que el servidor cierre la conexión por inactividad
        // Lo configuramos a un tiempo altísimo (ej. 1 día entero) o a 0.
        ctx.session.setIdleTimeout(Duration.ofDays(1)); 
        // 2. Enviar pings invisibles periódicamente para que el router/red no corte la conexión
        ctx.enableAutomaticPings();
    }; 

    private static void keep2Connections(WsContext ctx) {
        if (players.size() > 1) {
            ctx.send("To many Users"); ctx.closeSession();
        }
        else {
            players.add(ctx);
        }
    }

    private static void brodcast(String message) {
        for (WsContext ctx : players){
            ctx.send(message);
        }
    }

    private static BrodcastMessage movesHandle(BodyMessage mssg) {
        boolean wasLegalMove = false;
        int playerTurn = mssg.playerId;
        GameOverData gameOverData = null;

        if (mssg.typeOfMove == "MOVE") {
            wasLegalMove = board.move(mssg.currentPos(), mssg.newPos());
            if (wasLegalMove) {
                updateBoardStatusCounter();
                String boardKey = updateBoardStatusCounter();
                gameOverData = checkIsGameOver(mssg.playerId(), mssg.timeStamp(), boardKey);
                playerTurn = (mssg.playerId + 1) % 2;
            } 
        }
        if (mssg.typeOfMove == "CASTLE") {
            wasLegalMove = board.castleKing(mssg.currentPos(), mssg.newPos());
            if (wasLegalMove) {
                updateBoardStatusCounter();
                String boardKey = updateBoardStatusCounter();
                gameOverData = checkIsGameOver(mssg.playerId(), mssg.timeStamp(), boardKey);
                playerTurn = (mssg.playerId + 1) % 2;
            } 
        }
        if (mssg.typeOfMove == "PAWN_UPGRADE") {
            wasLegalMove = board.moveAndUpgradePawn(mssg.currentPos(), mssg.newPos(), mssg.pawnUpgrade);
            if (wasLegalMove) {
                boardStatusCounter.clear();
                String boardKey = updateBoardStatusCounter();
                gameOverData = checkIsGameOver(mssg.playerId(), mssg.timeStamp(), boardKey);
                playerTurn = (mssg.playerId + 1) % 2;
            }
        }
        System.out.println(board.toString());
        return new BrodcastMessage(wasLegalMove, playerTurn, gameOverData);
    }

    /**
     * Checking if there is a game over or not
     * @param playerId
     * @param time
     * @param boardKey
     * @return Game over data is game is over, else null
     *
     *  Chess ends either when there is a checkmate, timeout or when there is a tie
     *  Ties:   Stalmate -> No possible move;
     *          Timeout with no pieces -> time = 0 and boardKey has a pattern;
     *          Repetition -> boardStatusKey[boardKey] == 3. The pieces got to the same place 3 times.
     *          Not enough pieces -> boardKey has a pattern
    */
    private static GameOverData checkIsGameOver(int playerId, double time, String boardKey) {
        // boolean isTie;
        // int winnerId;
        // String reasson;
        int contrincantId = (playerId + 1) % 2;
        
        if (time <= 0) {
            // if (board.hasMaterial(contrincantId)) return new GameOverData(false, playerId, "TIMEOUT");
            // else return new GameOverData(true, -1, "TIMEOUT_WITHOUT_MATERIAL");
            return new GameOverData(false, contrincantId, "TIMEOUT");
        }
        
        // There is no possible move for the contrincant and ...
        if (board.isStalmate(contrincantId)) {
            // The king is under attack
            if (board.isKingCheckedById(contrincantId)) return new GameOverData(false, playerId, "CHECK_MATE");

            // Simply no piece can move.
            else return new GameOverData(true, -1, "STALMATE"); 
        }

        if (boardStatusCounter.get(boardKey) == 3) {
            return new GameOverData(true, -1, "REPETITION");
        }

        //[TODO] LACK OF MATERIALS TIE.

        return null;
    }

    private static String updateBoardStatusCounter() {
        String boardKey = board.statusKey();
        boardStatusCounter.putIfAbsent(boardKey, 1);
        boardStatusCounter.computeIfPresent(boardKey, (key, val) -> val++);
        return boardKey;
    }


    
}
