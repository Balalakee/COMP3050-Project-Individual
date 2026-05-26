package com.questshaper.game.service;

import com.questshaper.game.model.Player;
import com.questshaper.game.model.PlayerCredentials;
import com.questshaper.game.model.tiles.Tile;
import com.questshaper.game.model.tiles.TileLayer;
import com.questshaper.game.model.tiles.TileStack;
import com.questshaper.game.model.tiles.TileType;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class GameService {

    private final Map<String, Player> sessions = new HashMap<>();
    private final MapService mapService;

    private final Map<String, PlayerCredentials> accounts =
        new HashMap<>();

private final Set<Character> usedAvatars =
        new HashSet<>();

    public GameService(MapService mapService) {
        this.mapService = mapService;
    }

    public String login(
        String username,
        String encPassword) {

    if (username == null ||
        encPassword == null ||
        username.isBlank() ||
        encPassword.isBlank()) {
        return null;
    }

    PlayerCredentials acc =
            accounts.get(username);

    // Existing user
    if (acc != null) {

        // Wrong password
        if (!acc.getEncPassword()
                .equals(encPassword)) {
            return null;
        }

        // Already logged in
        if (sessions.containsValue(
                acc.getPlayer())) {
            return null;
        }

        String session =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "");

        sessions.put(
                session,
                acc.getPlayer());

        return session;
    }

    // New account
    Character avatar =
            getNextAvatar();

    if (avatar == null) {
        return null;
    }

    Player player =
            new Player(
                    username,
                    5,
                    5,
                    avatar);

    PlayerCredentials newAcc =
            new PlayerCredentials(
                    username,
                    encPassword,
                    player);

    accounts.put(
            username,
            newAcc);

    String session =
            UUID.randomUUID()
                    .toString()
                    .replace("-", "");

    sessions.put(
            session,
            player);

    return session;
}

    public boolean isValidSession(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    public boolean logout(
        String sessionId) {

    Player player =
            sessions.remove(sessionId);

    return player != null;
}

    public Player getPlayer(String sessionId) {
        return sessions.get(sessionId);
    }

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

        if (mapService.isBlocked(newY,newX)
        || playerBlocking(
                newY,
                newX,
                player)) {

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

    private String[][] buildInfoWindow(
        int centerY,
        int centerX,
        int size) {

    String[][] window =
            mapService.getWindow(centerY, centerX, size);

    int half = size / 2;
    int width = mapService.getWidth();

    // Overlay active players
    for (Player p : sessions.values()) {

    int relY =
            p.getY() - (centerY - half);

    int relX =
            p.getX() - (centerX - half);

    while (relX < 0) {
        relX += width;
    }

    while (relX >= width) {
        relX -= width;
    }

    if (relY >= 0 &&
        relY < size &&
        relX >= 0 &&
        relX < size) {

        String tile =
                window[relY][relX];

        if (tile == null)
            tile = "g";

        tile =
                tile.replaceAll("[0-9]", "");

        window[relY][relX] =
                tile + p.getAvatar();
    }
}

    return window;
}

    public Map<String,Object> getInfo(
        String sessionId,
        int y,
        int x) {

    Player player =
            sessions.get(sessionId);

    if (player == null) {
        return null;
    }

    // Always return player's REAL location

    int size = 11;
    int half = 5;

    int top =
            player.getY() - half;

    int bottom =
            player.getY() + half;

    int left =
            player.getX() - half;

    int right =
            player.getX() + half;

    String[][] info =
            buildInfoWindow(
                    player.getY(),
                    player.getX(),
                    size);

    Map<String,Object> result =
            new HashMap<>();

    result.put("x", player.getX());
    result.put("y", player.getY());

    result.put("top", top);
    result.put("left", left);
    result.put("bottom", bottom);
    result.put("right", right);

    result.put("info", info);

    return result;
}

    public boolean take(String sessionId) {

    Player p = sessions.get(sessionId);

    if (p == null) return false;

    Tile item = mapService.getItem(p.getY(), p.getX());

    if (item == null) return false;

    if (!item.getType().moveable) return false;

    // swap same class
    for (int i = 0; i < p.getInventory().size(); i++) {

        TileType inv = p.getInventory().get(i);

        if (inv.itemClass != null &&
            inv.itemClass.equals(item.getType().itemClass)) {

            p.getInventory().set(i, item.getType());

            mapService.removeItem(p.getY(), p.getX());
            mapService.placeItem(
                p.getY(),
                p.getX(),
                new Tile(inv, TileLayer.ITEM)
            );

            return true;
        }
    }

    p.getInventory().add(item.getType());
    mapService.removeItem(p.getY(), p.getX());

    return true;
}

public boolean place(String sessionId) {

    Player p = sessions.get(sessionId);

    if (p == null) return false;

    if (p.getInventory().isEmpty()) {
        return false;
    }

    Tile existing = mapService.getItem(p.getY(), p.getX());

    if (existing != null) {
        return false;
    }

    TileType item = p.getInventory().remove(0);

    mapService.placeItem(
        p.getY(),
        p.getX(),
        new Tile(item, TileLayer.ITEM)
    );

    return true;
}

public boolean use(String sessionId, int dy, int dx) {

    Player p = sessions.get(sessionId);

    if (p == null) return false;

    if (Math.abs(dy) + Math.abs(dx) > 1) {
        return false;
    }

    int y = p.getY() + dy;
    int x = p.getX() + dx;

    TileStack stack = mapService.getStack(y, x);

    if (stack == null) return false;

    Tile structure =
        stack.getByLayer(TileLayer.STRUCTURE);

    if (structure == null) return false;

    if (structure.getType() == TileType.DOOR) {

        stack.add(
            new Tile(
                TileType.DOOR_OPEN,
                TileLayer.STRUCTURE
            )
        );

        return true;
    }

    if (structure.getType() == TileType.DOOR_OPEN) {

        stack.add(
            new Tile(
                TileType.DOOR,
                TileLayer.STRUCTURE
            )
        );

        return true;
    }

    return false;
}

private boolean playerBlocking(
        int y,
        int x,
        Player movingPlayer) {

    for (Player p : sessions.values()) {

        if (p == movingPlayer) {
            continue;
        }

        if (p.getY() == y &&
            p.getX() == x) {

            return true;
        }
    }

    return false;
}

private Character getNextAvatar() {

    for (char c='0'; c<='9'; c++) {

        if (!usedAvatars.contains(c)) {

            usedAvatars.add(c);
            return c;
        }
    }

    return null;
}

}