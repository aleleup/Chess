package chess;
import io.javalin.Javalin;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// Front-end messages: 
//          [MOVE]: calls move, [PAWN_UPGRADE]: calls moveAndUpgradePawn, [CASTLE]: calls castleKing
// DELETE PREVIOUS STATUS HASH MAP WHENEVER THE FRONT-END MESSAGE IS [TRY_UPGRADE_PAWN] and the method return true. 

// [TODO]: REFACTOR SERVICE. Create Desk entity that handles the communication with the board. Interpretation of user messages and 
// creation of server messages. (Research if it doesn't cause any concurrency issue)

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

    private static record BrodcastMessage(
        boolean wasLegalMove, int playerTurn, GameOverData gameOverData, int[] previousPos,
         int[] newPos, String pawnUpgrade, CastelingData castelingData, String prevTypeOfMove
    ){};

    private static record GameOverData(boolean isTie, int winnerId, String reasson){};

    private static record ConnectionMessage(boolean success, int id, String message){};

    private static record CastelingData(int[] kingPos, int[] rookPos){};
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
                if (board == null) {
                    board = new Board();
                    boardStatusCounter = new HashMap<String, Integer>(); 
                    System.out.println("New board created: \n" + board.toString());
                }
                String successMesage = mapper.writeValueAsString(
                    new ConnectionMessage(true, players.size(), "Connected")
                );
                String rejectedMesage = mapper.writeValueAsString(
                    new ConnectionMessage(false, -1, "Board Full")
                );

                keepConnectionAlive(context);
                keep2Connections(context, successMesage, rejectedMesage);
                
            });
            ws.onMessage(context -> {
                String message = context.message();
                System.out.println("Client mesage: " + message);
                try {
                
                    BodyMessage playerMessage = mapper.readValue(message, BodyMessage.class);
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

    private static void keep2Connections(WsContext ctx, String successMessage, String rejectedMesage) {
        if (players.size() > 1) {
            ctx.send(rejectedMesage); 
            ctx.closeSession();
        }
        else {
            players.add(ctx);
            ctx.send(successMessage); 

        }
    }

    private static void brodcast(String message) {
        for (WsContext ctx : players){
            ctx.send(message);
        }
    }

    private static BrodcastMessage movesHandle(BodyMessage mssg) {
        boolean wasLegalMove = false;
        GameOverData gameOverData = null;
        CastelingData caselingData = null;
        if (mssg.typeOfMove().equals("MOVE")) {
            wasLegalMove = board.move(mssg.currentPos(), mssg.newPos());
        }
        else if (mssg.typeOfMove().equals("CASTLE")) {
            wasLegalMove = board.castleKing(mssg.currentPos(), mssg.newPos());
            if (wasLegalMove) { 
                int[][] kingAndRookPos = board.getCasteledPiecesPos();
                caselingData = new CastelingData(kingAndRookPos[0], kingAndRookPos[1]);
                
            }
        }
        else if (mssg.typeOfMove().equals("PAWN_UPGRADE")) {
            wasLegalMove = board.moveAndUpgradePawn(mssg.currentPos(), mssg.newPos(), mssg.pawnUpgrade);
            if (wasLegalMove) {boardStatusCounter.clear();}
        }


        if (wasLegalMove) {
            String boardKey = updateBoardStatusCounter();
            gameOverData = checkIsGameOver(mssg.playerId(), mssg.timeStamp(), boardKey);
            playerTurn = (mssg.playerId + 1) % 2;
        } 

        System.out.println(board.toString());

        return new BrodcastMessage(
            wasLegalMove, playerTurn, gameOverData, mssg.currentPos(), mssg.newPos, mssg.pawnUpgrade, caselingData, mssg.typeOfMove()
        );
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
            if (board.arePiecesEnough(contrincantId)) return new GameOverData(false, contrincantId, "TIMEOUT");
            else return new GameOverData(true, -1, "TIMEOUT_WITHOUT_MATERIAL");
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

        if (!(board.arePiecesEnough(playerId) || board.arePiecesEnough(contrincantId))) return new GameOverData(true, -1, "LACK_MATERIALS");
        return null;
    }

    private static String updateBoardStatusCounter() {
        String boardKey = board.statusKey();
        boardStatusCounter.putIfAbsent(boardKey, 1);
        boardStatusCounter.computeIfPresent(boardKey, (key, val) -> val++);
        return boardKey;
    }


    
}
