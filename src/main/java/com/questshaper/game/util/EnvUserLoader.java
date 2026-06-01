package com.questshaper.game.util;

import com.questshaper.game.model.Account;
import com.questshaper.game.model.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnvUserLoader {

    public static Map<String, Account> loadUsers() {
        Map<String, Account> accounts = new LinkedHashMap<>();

        String users = getGameUsersEnv();

        if (users == null || users.isBlank()) {
            return accounts;
        }

        String[] entries = users.split(";");

        for (String entry : entries) {
            if (entry == null || entry.isBlank()) {
                continue;
            }

            String[] parts = entry.split(":");

            if (parts.length != 5) {
                System.err.println("Skipping invalid GAME_USERS entry: " + entry);
                continue;
            }

            try {
                String username = parts[0];
                String encryptedPassword = parts[1];
                char avatar = parts[2].charAt(0);
                int y = Integer.parseInt(parts[3]);
                int x = Integer.parseInt(parts[4]);

                Player player =
                        new Player(
                                username,
                                y,
                                x,
                                avatar
                        );

                Account account =
                        new Account(
                                username,
                                encryptedPassword,
                                player
                        );

                accounts.put(username, account);

            } catch (Exception e) {
                System.err.println("Skipping invalid GAME_USERS entry: " + entry);
            }
        }

        return accounts;
    }

    private static String getGameUsersEnv() {

    String users = System.getenv("GAME_USERS");

    if (users != null && !users.isBlank()) {
        return users;
    }

    Path envPath = Paths.get(".env");

    if (!Files.exists(envPath)) {
        return null;
    }

    try {
        List<String> lines = Files.readAllLines(envPath);

        for (String line : lines) {

            line = line.trim();

            if (line.startsWith("GAME_USERS=")) {
                return line.substring("GAME_USERS=".length());
            }
        }

    } catch (IOException e) {
        e.printStackTrace();
    }

    return null;
}
}