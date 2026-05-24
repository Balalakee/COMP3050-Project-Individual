package com.questshaper.game.service;

import com.questshaper.game.model.Player;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class GameService {

    private final Map<String, Player> sessions = new HashMap<>();
    private final MapService mapService;

    public GameService(MapService mapService) {
        this.mapService = mapService;
    }

    // -------------------------
    // SESSION HELPERS
    // -------------------------

    public String createSession(String username) {

        String sessionId =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "");

        char avatar =
                (char) ('0' +
                        (Math.abs(username.hashCode()) % 10));

        Player player =
                new Player(
                        username,
                        5,
                        5,
                        avatar
                );

        sessions.put(sessionId, player);

        System.out.println(
                "LOGIN " + username +
                " avatar=" + avatar +
                " session=" + sessionId
        );

        return sessionId;
    }

    public boolean isValidSession(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    public void logout(String sessionId) {
        sessions.remove(sessionId);
    }

    public Player getPlayer(String sessionId) {
        return sessions.get(sessionId);
    }

    // -------------------------
    // MOVE
    // -------------------------

    public boolean move(String sessionId, int dy, int dx) {

        Player player = sessions.get(sessionId);

        if (player == null) {
            return false;
        }

        // only cardinal movement

        if (Math.abs(dy) + Math.abs(dx) != 1) {
            return false;
        }

        int newY = player.getY() + dy;
        int newX = player.getX() + dx;

        int width = mapService.getWidth();

        // wrap X

        if (newX < 0) {
            newX = width - 1;
        }

        if (newX >= width) {
            newX = 0;
        }

        // clamp Y

        if (newY < 0 || newY >= mapService.getHeight()) {
            return false;
        }

        // terrain collision

        if (mapService.isBlocked(newY, newX)) {
            return false;
        }

        // player collision

        for (Player other : sessions.values()) {

            if (other == player) {
                continue;
            }

            if (other.getY() == newY &&
                other.getX() == newX) {

                return false;
            }
        }

        player.setPosition(newY, newX);

        System.out.println(
                "MOVE " + player.getUsername() +
                " -> (" + newY + "," + newX + ")"
        );

        return true;
    }

    // -------------------------
    // INFO WINDOW
    // -------------------------

    private String[][] buildInfoWindow(Player viewer) {

        String[][] window =
                mapService.getWindow(
                        viewer.getY(),
                        viewer.getX(),
                        11
                );

        int top = viewer.getY() - 5;
        int left = viewer.getX() - 5;
        int width = mapService.getWidth();

        // overlay players

        for (Player p : sessions.values()) {

            int relY = p.getY() - top;
            int relX = p.getX() - left;

            // X wraps

            relX =
                    ((relX % width) + width)
                            % width;

            if (relY >= 0 &&
                relY < 11 &&
                relX >= 0 &&
                relX < 11) {
                    //System.out.println(
   // "BEFORE PLAYER: " +
    //window[relY][relX]
//);
                window[relY][relX] +=
                        p.getAvatar();
                       // System.out.println(
    //"AFTER PLAYER: " +
    //window[relY][relX]
//);
            }
        }

        return window;
    }

    // -------------------------
    // INFO
    // -------------------------

    public Map<String, Object> getInfo(
            String sessionId,
            int y,
            int x
    ) {

        Player player = sessions.get(sessionId);

        if (player == null) {
            return null;
        }

        // spec:
        // request must match player location

        if (player.getY() != y ||
            player.getX() != x) {

            return null;
        }

        int top = y - 5;
        int bottom = y + 5;
        int left = x - 5;
        int right = x + 5;

        String[][] mapData =
                buildInfoWindow(player);

        Map<String, Object> result =
                new HashMap<>();

        result.put("x", player.getX());
        result.put("y", player.getY());

        result.put("top", top);
        result.put("left", left);

        result.put("bottom", bottom);
        result.put("right", right);

        result.put("info", mapData);

        return result;
    }
}