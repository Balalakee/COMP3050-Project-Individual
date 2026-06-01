package com.questshaper.game.service;

import com.questshaper.game.model.Player;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MultiplayerTest {

    @Test
    void twoPlayersCanLoginWithDifferentSessions() throws Exception {

        GameService gameService =
                new GameService();

        List<TestUser> users =
                configuredUsers();

        TestUser user1 =
                users.get(0);

        TestUser user2 =
                users.get(1);

        String session1 =
                gameService.login(
                        user1.username(),
                        user1.hash()
                );

        String session2 =
                gameService.login(
                        user2.username(),
                        user2.hash()
                );

        assertNotNull(session1);
        assertNotNull(session2);

        assertNotEquals(
                session1,
                session2
        );
    }

    @Test
    void duplicateLoginForSameAccountFailsWhileOnline() throws Exception {

        GameService gameService =
                new GameService();

        TestUser user =
                configuredUsers().get(0);

        String firstSession =
                gameService.login(
                        user.username(),
                        user.hash()
                );

        String secondSession =
                gameService.login(
                        user.username(),
                        user.hash()
                );

        assertNotNull(firstSession);

        assertNull(secondSession);
    }

    @Test
    void sameAccountCanLoginAgainAfterLogout() throws Exception {

        GameService gameService =
                new GameService();

        TestUser user =
                configuredUsers().get(0);

        String firstSession =
                gameService.login(
                        user.username(),
                        user.hash()
                );

        assertNotNull(firstSession);

        assertTrue(
                gameService.logout(firstSession)
        );

        String secondSession =
                gameService.login(
                        user.username(),
                        user.hash()
                );

        assertNotNull(secondSession);
    }

    @Test
    void playersHaveDifferentAvatars() throws Exception {

        GameService gameService =
                new GameService();

        List<TestUser> users =
                configuredUsers();

        TestUser user1 =
                users.get(0);

        TestUser user2 =
                users.get(1);

        String session1 =
                gameService.login(
                        user1.username(),
                        user1.hash()
                );

        String session2 =
                gameService.login(
                        user2.username(),
                        user2.hash()
                );

        Player player1 =
                gameService.getPlayer(session1);

        Player player2 =
                gameService.getPlayer(session2);

        assertNotNull(player1);
        assertNotNull(player2);

        assertNotEquals(
                player1.getAvatar(),
                player2.getAvatar()
        );
    }

    @Test
    void infoJsonIncludesBothOnlinePlayersWhenVisible() throws Exception {

        GameService gameService =
                new GameService();

        List<TestUser> users =
                configuredUsers();

        TestUser user1 =
                users.get(0);

        TestUser user2 =
                users.get(1);

        String session1 =
                gameService.login(
                        user1.username(),
                        user1.hash()
                );

        String session2 =
                gameService.login(
                        user2.username(),
                        user2.hash()
                );

        Player player1 =
                gameService.getPlayer(session1);

        Player player2 =
                gameService.getPlayer(session2);

        player2.setPosition(
                player1.getY(),
                player1.getX() + 1
        );

        String json =
                gameService.infoJson(
                        player1,
                        player1.getY(),
                        player1.getX()
                );

        assertTrue(
                json.contains(
                        String.valueOf(
                                player1.getAvatar()
                        )
                )
        );

        assertTrue(
                json.contains(
                        String.valueOf(
                                player2.getAvatar()
                        )
                )
        );
    }

    @Test
    void onlinePlayerBlocksMovement() throws Exception {

        GameService gameService =
                new GameService();

        List<TestUser> users =
                configuredUsers();

        TestUser user1 =
                users.get(0);

        TestUser user2 =
                users.get(1);

        String session1 =
                gameService.login(
                        user1.username(),
                        user1.hash()
                );

        String session2 =
                gameService.login(
                        user2.username(),
                        user2.hash()
                );

        Player player1 =
                gameService.getPlayer(session1);

        Player player2 =
                gameService.getPlayer(session2);

        player2.setPosition(
                player1.getY(),
                player1.getX() + 1
        );

        assertFalse(
                gameService.move(
                        player1,
                        0,
                        1
                )
        );
    }

    private List<TestUser> configuredUsers() {

        String users =
                loadGameUsers();

        String[] entries =
                users.split(";");

        List<TestUser> result =
                new ArrayList<>();

        for (String entry : entries) {

            if (entry == null ||
                entry.isBlank()) {
                continue;
            }

            String[] parts =
                    entry.split(":");

            if (parts.length != 5) {
                fail(
                        "Invalid GAME_USERS entry: "
                        + entry
                );
            }

            result.add(
                    new TestUser(
                            parts[0],
                            parts[1]
                    )
            );
        }

        if (result.size() < 2) {

            fail(
                    "At least two users are required in GAME_USERS"
            );
        }

        return result;
    }

    private String loadGameUsers() {

        String users =
                System.getenv(
                        "GAME_USERS"
                );

        if (users != null &&
            !users.isBlank()) {

            return users;
        }

        try {

            users =
                    Files.lines(
                            Path.of(".env")
                    )
                    .filter(
                            line ->
                                    line.startsWith(
                                            "GAME_USERS="
                                    )
                    )
                    .map(
                            line ->
                                    line.substring(
                                            "GAME_USERS=".length()
                                    )
                    )
                    .findFirst()
                    .orElse("");

        } catch (Exception e) {

            users = "";
        }

        if (users.isBlank()) {

            fail(
                    "GAME_USERS must be set in environment or .env for tests"
            );
        }

        return users;
    }

    private record TestUser(
            String username,
            String hash
    ) {
    }
}
