package com.questshaper.game.service;

import com.questshaper.game.model.Player;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class GameService {

    private final MapService mapService;

    private final Map<String, Player> sessions = new HashMap<>();
    private Map<String, String> users = new HashMap<>();
    public GameService(MapService mapService) {
        this.mapService = mapService;
    }

    public void createSession(String sessionId) {
        sessions.put(sessionId, new Player(5, 5));
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    private Player getPlayer(String sessionId) {
        return sessions.get(sessionId);
    }

    public boolean move(String sessionId, int dy, int dx) {
        Player player = getPlayer(sessionId);
        if (player == null) return false;

        int newY = player.getY() + dy;
        int newX = player.getX() + dx;

        if (mapService.isBlocked(newY, newX)) return false;

        player.setPosition(newY, newX);
        return true;
    }

    public Map<String, Object> getInfo(String sessionId, int y, int x) {
        Player player = getPlayer(sessionId);
        if (player == null) return null;

        int viewSize = 11;
        int half = viewSize / 2;

        int top = player.getY() - half;
        int left = player.getX() - half;

        char[][] window = mapService.getWindow(top, left, viewSize);

        Map<String, Object> result = new HashMap<>();
        result.put("x", player.getX());
        result.put("y", player.getY());
        result.put("top", top);
        result.put("left", left);
        result.put("bottom", top + viewSize - 1);
        result.put("right", left + viewSize - 1);
        result.put("info", window);

        return result;
    }

    public String login(String username, String password) {

    // simple user store (for now)
    users.putIfAbsent(username, hash(password));

    String storedHash = users.get(username);

    if (!storedHash.equals(hash(password))) {
        return null;
    }

    String sessionId = UUID.randomUUID().toString();
    sessions.put(sessionId, new Player(5, 5));

    return sessionId;
}

private String hash(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());

        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
}