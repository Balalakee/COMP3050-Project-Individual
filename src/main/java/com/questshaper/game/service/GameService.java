package com.questshaper.game.service;

import com.questshaper.game.model.Account;
import com.questshaper.game.model.Player;
import com.questshaper.game.model.tiles.Tile;
import com.questshaper.game.model.tiles.TileType;
import com.questshaper.game.util.JsonUtil;
import com.questshaper.game.util.EnvUserLoader;

import java.io.IOException;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class GameService {

    private static final int VIEW_SIZE = 11;
    private static final int VIEW_RADIUS = VIEW_SIZE / 2;

    private final MapService mapService;

    // username -> account loaded from GAME_USERS
    private final Map<String, Account> accounts =
            new LinkedHashMap<>();

    // session token -> player
    private final Map<String, Player> sessions =
            new LinkedHashMap<>();

    public GameService() throws IOException {
        this.mapService = new MapService();

        this.accounts.putAll(
                EnvUserLoader.loadUsers()
        );

        System.out.println(
                "Loaded " + accounts.size() +
                " account(s) from GAME_USERS"
        );
    }

    public MapService getMapService() {
        return mapService;
    }

    public static boolean isValidPlayerName(String name) {
        return name != null && name.matches("[A-Za-z-]+");
    }

    public static String hashPassword(String name, String password) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            (name + ";" + password)
                                    .getBytes(StandardCharsets.UTF_8)
                    );

            StringBuilder out =
                    new StringBuilder();

            for (byte b : hash) {
                out.append(
                        String.format("%02x", b)
                );
            }

            return out.toString();

        } catch (Exception e) {
            return "";
        }
    }

    public synchronized String login(
            String name,
            String encpswrd) {

        if (!isValidPlayerName(name) ||
            encpswrd == null ||
            encpswrd.isBlank()) {

            return null;
        }

        Account account =
                accounts.get(name);

        if (account == null) {
            return null;
        }

        if (!account.getEncryptedPassword()
                .equals(encpswrd)) {

            return null;
        }

        // Do not allow same account online twice.
        if (sessions.containsValue(
                account.getPlayer())) {

            return null;
        }

        String token =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "");

        sessions.put(
                token,
                account.getPlayer()
        );

        System.out.println(
                "LOGIN success: " +
                name +
                " avatar=" +
                account.getPlayer().getAvatar()
        );

        return token;
    }

    public synchronized boolean logout(
            String token) {

        if (token == null || token.isBlank()) {
            return false;
        }

        Player removed =
                sessions.remove(token);

        if (removed != null) {
            System.out.println(
                    "LOGOUT success: " +
                    removed.getUsername()
            );
        }

        return removed != null;
    }

    public synchronized Player getPlayer(
            String token) {

        if (token == null) {
            return null;
        }

        return sessions.get(token);
    }

    public synchronized boolean move(
            Player player,
            int dy,
            int dx) {

        if (player == null) {
            return false;
        }

        if (!isValidRelativeStep(dy, dx)) {
            return false;
        }

        if (dy == 0 && dx == 0) {
            return true;
        }

        int newY =
                player.getY() + dy;

        int newX =
                mapService.wrapX(
                        player.getX() + dx
                );

        if (mapService.isBlocked(newY, newX)) {
            return false;
        }

        if (isPlayerAt(newY, newX, player)) {
            return false;
        }

        player.setPosition(newY, newX);

        return true;
    }

    public synchronized String infoJson(
            Player player,
            int requestedY,
            int requestedX) {

        if (player == null) {
            return null;
        }

        // Server is source of truth.
        // This also resyncs the client if it moved before login/logout.
        int y = player.getY();
        int x = player.getX();

        int top =
                y - VIEW_RADIUS;

        int left =
                x - VIEW_RADIUS;

        int bottom =
                y + VIEW_RADIUS;

        int right =
                x + VIEW_RADIUS;

        String[][] info =
                buildInfoWindow(y, x);

        StringBuilder json =
                new StringBuilder();

        json.append("{");
        json.append("\"y\":").append(y).append(",");
        json.append("\"x\":").append(x).append(",");
        json.append("\"top\":").append(top).append(",");
        json.append("\"left\":").append(left).append(",");
        json.append("\"bottom\":").append(bottom).append(",");
        json.append("\"right\":").append(right).append(",");
        json.append("\"info\":[");

        for (int row = 0; row < info.length; row++) {

            json.append("[");

            for (int col = 0; col < info[row].length; col++) {

                json.append(
                        JsonUtil.quote(
                                info[row][col]
                        )
                );

                if (col < info[row].length - 1) {
                    json.append(",");
                }
            }

            json.append("]");

            if (row < info.length - 1) {
                json.append(",");
            }
        }

        json.append("]}");

        return json.toString();
    }

    public synchronized boolean take(
            Player player) {

        if (player == null) {
            return false;
        }

        Tile item =
                mapService.getItem(
                        player.getY(),
                        player.getX()
                );

        if (item == null ||
            !isMovableItem(
                    item.getType())) {

            return false;
        }

        TileType previous =
                player.takeItem(
                        item.getType()
                );

        mapService.removeItem(
                player.getY(),
                player.getX()
        );

        if (previous != null) {
            mapService.placeItem(
                    player.getY(),
                    player.getX(),
                    previous
            );
        }

        return true;
    }

    public synchronized boolean place(
            Player player) {

        if (player == null) {
            return false;
        }

        TileType item =
                player.itemToPlace();

        if (item == null) {
            return false;
        }

        if (!mapService.placeItem(
                player.getY(),
                player.getX(),
                item)) {

            return false;
        }

        player.removeItem(item);

        return true;
    }

    public synchronized boolean use(
            Player player,
            int dy,
            int dx) {

        if (player == null) {
            return false;
        }

        if (!isValidRelativeStep(dy, dx)) {
            return false;
        }

        int y =
                player.getY() + dy;

        int x =
                mapService.wrapX(
                        player.getX() + dx
                );

        if (!mapService.isYInBounds(y)) {
            return false;
        }

        return mapService.toggleDoor(y, x);
    }

    public synchronized boolean isBlockingForTests(
            int y,
            int x) {

        return mapService.isBlocked(y, x);
    }

    private String[][] buildInfoWindow(
            int centerY,
            int centerX) {

        String[][] window =
                mapService.getWindow(
                        centerY,
                        centerX,
                        VIEW_SIZE
                );

        int top =
                centerY - VIEW_RADIUS;

        int left =
                centerX - VIEW_RADIUS;

        for (Player p : sessions.values()) {

            int relY =
                    p.getY() - top;

            int relX =
                    p.getX() - left;

            while (relX < 0) {
                relX += mapService.getWidth();
            }

            while (relX >= mapService.getWidth()) {
                relX -= mapService.getWidth();
            }

            if (relY >= 0 &&
                relY < VIEW_SIZE &&
                relX >= 0 &&
                relX < VIEW_SIZE) {

                String tile =
                        window[relY][relX];

                if (tile == null || tile.isBlank()) {
                    tile = "g";
                }

                window[relY][relX] =
                        tile.replaceAll("[0-9]", "")
                                + p.getAvatar();
            }
        }

        return window;
    }

    private boolean isPlayerAt(
            int y,
            int x,
            Player excluded) {

        for (Player p : sessions.values()) {

            if (p == excluded) {
                continue;
            }

            if (p.getY() == y &&
                p.getX() == x) {

                return true;
            }
        }

        return false;
    }

    private boolean isMovableItem(
            TileType type) {

        return switch (type) {
            case AXE,
                 CYAN_POTION,
                 HEART_POTION,
                 KEY -> true;
            default -> false;
        };
    }

    private boolean isValidRelativeStep(
            int dy,
            int dx) {

        return Math.abs(dy) <= 1 &&
               Math.abs(dx) <= 1 &&
               Math.abs(dy) + Math.abs(dx) <= 1;
    }
}