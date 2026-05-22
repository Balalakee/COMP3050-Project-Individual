package com.questshaper.game.service;

import com.questshaper.game.model.Player;
import org.springframework.stereotype.Service;
import com.questshaper.game.model.GameMap;
import com.questshaper.game.util.TileEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class GameService {

    private final MapService mapService;

    // username -> encrypted password
    private final Map<String, String> users = new HashMap<>();

    // sessionId -> player
    private final Map<String, Player> sessions = new HashMap<>();

    public GameService(MapService mapService) {
        this.mapService = mapService;
    }

    public String login(String name, String encpswrd) {

        // Create user if first login
        users.putIfAbsent(name, encpswrd);

        String storedPassword = users.get(name);

        // Wrong password
        if (!storedPassword.equals(encpswrd)) {
            return null;
        }

        // Create session
        String sessionId = UUID.randomUUID().toString();

        sessions.put(sessionId, new Player(5, 5));

        System.out.println("LOGIN SUCCESS: " + name);
        System.out.println("SESSION: " + sessionId);

        return sessionId;
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);

        System.out.println("LOGOUT: " + sessionId);
    }

    public boolean move(String sessionId, int dy, int dx) {

        Player player = sessions.get(sessionId);

        if (player == null) {
            return false;
        }

        
        int newY = player.getY() + dy;
        int newX = player.getX() + dx;

        // WRAP X ONLY
        int width = mapService.getWidth();

        if (newX < 0) {
            newX = width - 1;
        }

        if (newX >= width) {
            newX = 0;
        }

        // Y DOES NOT WRAP
        if (newY < 0 || newY >= mapService.getHeight()) {
            return false;
        }


        // Collision
        if (mapService.isBlocked(newY, newX)) {
            return false;
        }

        player.setPosition(newY, newX);

        System.out.println("MOVE: " + sessionId +
                " -> (" + newY + ", " + newX + ")");

        return true;
    }

    public Map<String, Object> getInfo(String sessionId, int y, int x) {

    Player player = sessions.get(sessionId);

    if (player == null) {
        return null;
    }

    if (player.getY() != y || player.getX() != x) {
        return null;
    }

        int size = 11;

        int top = y - 5;
        int bottom = y + 5;
        int left = x - 5;
        int right = x + 5;

        

    String[][] mapData = mapService.getWindow(y, x, size);

    Map<String, Object> result = new HashMap<>();

    result.put("x", player.getX());
    result.put("y", player.getY());

    result.put("top", top);
    result.put("left", left);

    result.put("bottom", bottom);
    result.put("right", right);

    result.put("info", mapData);

    return result;
}

    public void createSession(String sessionId) {
        sessions.put(sessionId, new Player(5, 5));
    }
}