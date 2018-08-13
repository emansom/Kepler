package org.alexdev.kepler.game.games;

import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import groovy.transform.Synchronized;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.triggers.GameTrigger;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.messages.outgoing.rooms.games.ITEMMSG;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameChess extends GamehallGame {
    private static class GameToken {
        private char token;

        private GameToken(char token) {
            this.token = token;
        }

        private char getToken() {
            return token;
        }
    }

    private Board board;
    private GameToken[] gameTokens;

    private List<Player> playersInGame;
    private HashMap<Player, GameToken> playerSides;

    public GameChess(int roomId, List<int[]> chairs) {
        super(roomId, chairs);
    }

    @Override
    public void gameStart() {
        this.playersInGame =  new ArrayList<>();
        this.playerSides = new HashMap<>();
        this.restartMap();
    }

    @Override
    public void gameStop() {
        this.playersInGame.clear();
        this.playerSides.clear();
        this.board = null;
    }

    @Override
    public void handleCommand(Player player, Room room, Item item, String command, String[] args) {
        GameTrigger trigger = (GameTrigger) item.getItemTrigger();

        if (command.equals("CLOSE")) {
            trigger.onEntityLeave(player, player.getRoomUser(), item);
            return;
        }

        if (command.equals("CHOOSETYPE")) {
            char sideChosen = args[0].charAt(0);

            if (this.getToken(sideChosen) == null) {
                return;
            }

            if (this.getPlayerBySide(sideChosen) != null) {
                player.send(new ITEMMSG(new String[]{this.getGameId(), "TYPERESERVED"}));
                return;
            }

            player.send(new ITEMMSG(new String[]{this.getGameId(), "SELECTTYPE " + String.valueOf(sideChosen)}));

            GameToken token = this.getToken(sideChosen);

            this.playersInGame.add(player);
            this.playerSides.put(player, token);

            // Select the other side for the player
            GameToken otherToken = null;

            for (GameToken other : this.gameTokens) {
                if (other.getToken() != sideChosen) {
                    otherToken = other;
                    break;
                }
            }

            if (otherToken != null) {
                for (Player otherPlayer : this.getPlayers()) {
                    if (otherPlayer != player) {
                        otherPlayer.send(new ITEMMSG(new String[]{this.getGameId(), "SELECTTYPE " + String.valueOf(otherToken.getToken())}));
                        this.playersInGame.add(otherPlayer);
                        this.playerSides.put(otherPlayer, otherToken);
                        break;
                    }
                }
            }

            this.broadcastMap();
        }

        if (command.equals("MOVEPIECE")) {
            Square fromSquare = Square.valueOf(args[0].toUpperCase());
            Square toSquare = Square.valueOf(args[1].toUpperCase());

            if (fromSquare == toSquare) {
                return;
            }

            Move move = new Move(fromSquare, toSquare);
            boolean isLegalMove = false;

            try {
                var moveList = MoveGenerator.generateLegalMoves(this.board);
                isLegalMove = moveList.contains(move);

            } catch (MoveGeneratorException e) { }

            if (isLegalMove) {
                this.board.doMove(move, true);
            }

            this.broadcastMap();
        }

        if (command.equals("RESTART")) {
            this.restartMap();
            this.broadcastMap();
            return;
        }
    }

    /**
     * Send the game map to the opponents.
     */
    private void broadcastMap() {
        StringBuilder boardData = new StringBuilder();

        for (Square square : Square.values()) {
            Piece piece = this.board.getPiece(square);

            if (piece == null) {
                continue;
            }

            if (piece.getPieceType() == PieceType.NONE || piece.getPieceSide() == null) {
                continue;
            }

            String side = piece.getPieceSide() == Side.BLACK ? "B" : "W";
            String chessPiece = this.getChessPiece(piece.getPieceType());

            boardData.append(side);
            boardData.append(chessPiece);
            boardData.append(square.value().toLowerCase());
            boardData.append("\r");

        }

        String[] playerNames = this.getPlayerNames();
        this.sendToEveryone(new ITEMMSG(new String[]{this.getGameId(), "PIECEDATA", playerNames[0], playerNames[1], boardData.toString()}));
    }

    /**
     * Get the CCT type of chess piece by the piece type supplied.
     *
     * @param pieceType the piece type instance
     * @return the CCT type, else it defaults to Rook
     */
    public String getChessPiece(PieceType pieceType) {
        if (pieceType == PieceType.BISHOP) {
            return "cr";
        }

        if (pieceType == PieceType.KNIGHT) {
            return "hr";
        }

        if (pieceType == PieceType.KING) {
            return "kg";
        }

        if (pieceType == PieceType.QUEEN) {
            return "qu";
        }

        if (pieceType == PieceType.ROOK) {
            return "tw";
        }

        return "sd"; // Pawn
    }

    /**
     * Get the names of the people currently playing, always returns an array with
     * a length of two, if the name is blank there's no player.
     *
     * @return the player names
     */
    private String[] getPlayerNames() {
        String[] playerNames = new String[]{"", ""};

        for (int i = 0; i < this.playersInGame.size(); i++) {
            Player player = this.playersInGame.get(i);
            playerNames[i] = Character.toUpperCase(this.playerSides.get(player).getToken()) + " " + player.getDetails().getName();
        }

        return playerNames;
    }

    /**
     * Reset the game map.
     */
    private void restartMap() {
        this.gameTokens = new GameToken[]{
                new GameToken('w'),
                new GameToken('b')
        };

        this.board = new Board();
    }

    /**
     * Get token instance by character.
     *
     * @param side the character to compare against
     * @return the instance, if successful
     */
    private GameToken getToken(char side) {
        GameToken token = null;

        for (GameToken t : gameTokens) {
            if (t.getToken() == side) {
                token = t;
                break;
            }
        }

        return token;
    }

    /**
     * Locate a player instance by the side they're playing.
     *
     * @param side the side used
     * @return the player instance, if successful
     */
    private Player getPlayerBySide(char side) {
        for (var kvp : this.playerSides.entrySet()) {
            if (kvp.getValue().getToken() == side) {
                return kvp.getKey();
            }
        }

        return null;
    }

    @Override
    public int getMaximumPeopleRequired() {
        return 2;
    }

    @Override
    public int getMinimumPeopleRequired() {
        return 2;
    }

    @Override
    public String getGameFuseType() {
        return "Chess";
    }
}
