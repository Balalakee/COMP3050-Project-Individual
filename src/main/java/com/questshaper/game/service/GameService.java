package com.questshaper.game.service;

import com.questshaper.game.model.Player;
import com.questshaper.game.model.tiles.TileStack;
import com.questshaper.game.util.TileEncoder;

import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class GameService {

    private final MapService mapService;

    // session token -> player
    private final Map<String, Player> sessions = new HashMap<>();

    public GameService(MapService mapService) {
        this.mapService = mapService;
    }

    public String login(String username, String encpswrd) {

        // basic validation
        if (username == null || username.isBlank()) {
            return null;
        }

        if (encpswrd == null || encpswrd.isBlank()) {
            return null;
        }

        // create player in middle-ish spawn
        Player player = new Player(5, 5);

        String session = UUID.randomUUID().toString();

        sessions.put(session, player);

        return session;
    }

    public boolean logout(String session) {

        if (session == null) {
            return false;
        }

        return sessions.remove(session) != null;
    }

    public Player getPlayer(String session) {
        return sessions.get(session);
    }

    public boolean movePlayer(String session, int dy, int dx) {
        Player player = sessions.get(session);

        if (player == null) {
            return false;
        }

        int newY = player.getY() + dy;
        int newX = player.getX() + dx;

        // Wrap x around horizontally
        int width = mapService.getWidth();

        if (newX < 0) {
            newX = width - 1;
        }

        if (newX >= width) {
            newX = 0;
        }

        // Doesn't wrap y
        if (newY < 0 || newY >= mapService.getHeight()) {
            return false;
        }

        // blocked terrain
        if (mapService.isBlocked(newY, newX)) {
            return false;
        }

        player.setPosition(newY, newX);

        return true;
    }

    public Map<String, Object> getInfo(String session, int y, int x) {

    Player player = sessions.get(session);
    if (player == null) return null;

    if (player.getY() != y || player.getX() != x) {
        return null;
    }

    int top = y - 5;
    int left = x - 5;

    List<List<String>> info = new ArrayList<>();

    for (int row = 0; row < 11; row++) {

        List<String> rowData = new ArrayList<>();

        int actualY = top + row;

        for (int col = 0; col < 11; col++) {

            int actualX = left + col;

            TileStack stack = mapService.getStack(actualY, actualX);

            rowData.add(TileEncoder.encode(stack));
            System.out.println("STACK at " + actualY + "," + actualX + " = " + stack);
System.out.println("ENCODED = " + TileEncoder.encode(stack));
        }
        
        info.add(rowData);
    }

    Map<String, Object> response = new HashMap<>();
    response.put("y", y);
    response.put("x", x);
    response.put("top", top);
    response.put("left", left);
    response.put("bottom", top + 10);
    response.put("right", left + 10);
    response.put("info", info);
    
    return response;
}
}