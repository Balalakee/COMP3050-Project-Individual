package com.questshaper.game.service;

import com.questshaper.game.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Test
    void validLoginCreatesSession() throws Exception {
        GameService gameService = newGameService();

        String session = loginFirstConfiguredUser(gameService);

        assertNotNull(session);
        assertFalse(session.isBlank());
        assertNotNull(gameService.getPlayer(session));
    }

    @Test
    void invalidPasswordFailsLogin() throws Exception {
        GameService gameService = newGameService();

        TestUser user = firstConfiguredUser();

        String session = gameService.login(
                user.username(),
                "wrong-password"
        );

        assertNull(session);
    }

    @Test
    void logoutRemovesSession() throws Exception {
        GameService gameService = newGameService();

        String session = loginFirstConfiguredUser(gameService);

        assertTrue(gameService.logout(session));
        assertNull(gameService.getPlayer(session));
    }

    @Test
    void moveEastUpdatesPlayerPosition() throws Exception {
        GameService gameService = newGameService();

        String session = loginFirstConfiguredUser(gameService);
        Player player = gameService.getPlayer(session);

        int startX = player.getX();

        assertTrue(gameService.move(player, 0, 1));
        assertEquals(startX + 1, player.getX());
    }

    @Test
    void diagonalMovementFails() throws Exception {
        GameService gameService = newGameService();

        String session = loginFirstConfiguredUser(gameService);
        Player player = gameService.getPlayer(session);

        assertFalse(gameService.move(player, 1, 1));
    }

    @Test
    void longMovementFails() throws Exception {
        GameService gameService = newGameService();

        String session = loginFirstConfiguredUser(gameService);
        Player player = gameService.getPlayer(session);

        assertFalse(gameService.move(player, 0, 2));
    }

    @Test
    void moveDoesNotWrapVertically() throws Exception {
        GameService gameService = newGameService();

        String session = loginFirstConfiguredUser(gameService);
        Player player = gameService.getPlayer(session);

        player.setPosition(0, 0);

        assertFalse(gameService.move(player, -1, 0));
        assertEquals(0, player.getY());
        assertEquals(0, player.getX());
    }

    @Test
    void moveWrapsHorizontally() throws Exception {
        GameService gameService = newGameService();

        String session = loginFirstConfiguredUser(gameService);
        Player player = gameService.getPlayer(session);

        player.setPosition(7, 0);

        assertTrue(gameService.move(player, 0, -1));
        assertEquals(7, player.getY());
        assertEquals(gameService.getMapService().getWidth() - 1, player.getX());
    }

    @Test
    void infoJsonReturnsPlayerPositionAndWindow() throws Exception {
        GameService gameService = newGameService();

        String session = loginFirstConfiguredUser(gameService);
        Player player = gameService.getPlayer(session);

        String json = gameService.infoJson(player, player.getY(), player.getX());

        assertNotNull(json);
        assertTrue(json.contains("\"y\":"));
        assertTrue(json.contains("\"x\":"));
        assertTrue(json.contains("\"top\":"));
        assertTrue(json.contains("\"left\":"));
        assertTrue(json.contains("\"info\""));
    }

    @Test
    void infoJsonUsesServerPositionEvenIfClientCoordinatesAreWrong() throws Exception {
        GameService gameService = newGameService();

        String session = loginFirstConfiguredUser(gameService);
        Player player = gameService.getPlayer(session);

        String json = gameService.infoJson(player, 0, 0);

        assertNotNull(json);
        assertTrue(json.contains("\"y\":" + player.getY()));
        assertTrue(json.contains("\"x\":" + player.getX()));
    }

    @Test
    void takeItemWorksFromKnownItemTile() throws Exception {
        GameService gameService = newGameService();

        String session = loginFirstConfiguredUser(gameService);
        Player player = gameService.getPlayer(session);

        player.setPosition(0, 14);

        boolean success = gameService.take(player);

        assertTrue(success);
    }

    @Test
    void placeItemWorksAfterTakingItem() throws Exception {
        GameService gameService = newGameService();

        String session = loginFirstConfiguredUser(gameService);
        Player player = gameService.getPlayer(session);

        player.setPosition(0, 14);

        assertTrue(gameService.take(player));
        assertTrue(gameService.place(player));
    }

    @Test
    void useDoorTogglesDoor() throws Exception {
        GameService gameService = newGameService();

        String session = loginFirstConfiguredUser(gameService);
        Player player = gameService.getPlayer(session);

        player.setPosition(4, 2);

        String before = gameService.getMapService()
                .getEncodedTileForView(4, 3);

        assertTrue(gameService.use(player, 0, 1));

        String after = gameService.getMapService()
                .getEncodedTileForView(4, 3);

        assertNotEquals(before, after);
    }

    private GameService newGameService() throws Exception {
        return new GameService();
    }

    private String loginFirstConfiguredUser(GameService gameService) {
        TestUser user = firstConfiguredUser();

        String session = gameService.login(
                user.username(),
                user.hash()
        );

        assertNotNull(session);

        return session;
    }

    private TestUser firstConfiguredUser() {
        String users = loadGameUsers();

        String firstUser = users.split(";")[0];
        String[] parts = firstUser.split(":");

        if (parts.length != 5) {
            fail("Invalid GAME_USERS test entry");
        }

        return new TestUser(parts[0], parts[1]);
    }

    private String loadGameUsers() {
        String users = System.getenv("GAME_USERS");

        if (users != null && !users.isBlank()) {
            return users;
        }

        try {
            users = java.nio.file.Files.lines(java.nio.file.Path.of(".env"))
                    .filter(line -> line.startsWith("GAME_USERS="))
                    .map(line -> line.substring("GAME_USERS=".length()))
                    .findFirst()
                    .orElse("");
        } catch (Exception e) {
            users = "";
        }

        if (users.isBlank()) {
            fail("GAME_USERS must be set in environment or .env for tests");
        }

        return users;
    }

    private record TestUser(String username, String hash) {}
}