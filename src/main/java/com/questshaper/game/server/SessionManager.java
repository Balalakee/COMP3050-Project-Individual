package com.questshaper.game.server;

import com.questshaper.game.model.*;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final String TOKEN_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // nosemgrep
    private static final int TOKEN_LENGTH = 32;

    private final SecureRandom random = new SecureRandom();
    private final Map<String, Player> sessions = new ConcurrentHashMap<>();

    public String createSession(Player player) {
        String token;
        do {
            token = randomToken();
        } while (sessions.containsKey(token));

        sessions.put(token, player);
        return token;
    }

    public Player getPlayer(String token) {
        if (token == null) {
            return null;
        }
        return sessions.get(token);
    }

    public Player invalidate(String token) {
        if (token == null) {
            return null;
        }
        return sessions.remove(token);
    }

    private String randomToken() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(TOKEN_CHARS.charAt(random.nextInt(TOKEN_CHARS.length())));
        }
        return token.toString();
    }
}
