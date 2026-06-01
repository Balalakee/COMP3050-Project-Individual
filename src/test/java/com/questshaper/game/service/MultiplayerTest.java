// src/test/java/com/questshaper/game/service/MultiplayerTest.java

package com.questshaper.game.service;

import com.questshaper.game.model.Player;
import org.junit.jupiter.api.*;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MultiplayerTest {

    private static final String ADMIN_HASH =
            "abc26589a512186b8947f4c14bb18906a588a9d44b4da1e1bd1fe1a04d5023c4";

    private static final String WILLOW_HASH =
            "37f8fd23338f899ed3d0a4a443fb8bf50369ca762440cb6b2313c17b6a7be16b";

    private static final String ENV_CONTENT =
            "GAME_USERS=admin:" + ADMIN_HASH + ":1:5:5;" +
            "willow:" + WILLOW_HASH + ":2:5:6";

    private String previousEnvFile;
    private boolean hadEnvFile;

    private GameService gameService;

    @BeforeEach
    void setup() throws Exception {
        backupAndWriteEnvFile();
        gameService = new GameService();
    }

    @AfterEach
    void cleanup() throws Exception {
        restoreEnvFile();
    }

    @Test
    void twoPlayersCanLoginWithDifferentSessions() {
        String adminSession = gameService.login("admin", ADMIN_HASH);
        String willowSession = gameService.login("willow", WILLOW_HASH);

        assertNotNull(adminSession);
        assertNotNull(willowSession);
        assertNotEquals(adminSession, willowSession);
    }

    @Test
    void duplicateLoginForSameAccountFailsWhileOnline() {
        String firstSession = gameService.login("admin", ADMIN_HASH);
        String secondSession = gameService.login("admin", ADMIN_HASH);

        assertNotNull(firstSession);
        assertNull(secondSession);
    }

    @Test
    void sameAccountCanLoginAgainAfterLogout() {
        String firstSession = gameService.login("admin", ADMIN_HASH);

        assertTrue(gameService.logout(firstSession));

        String secondSession = gameService.login("admin", ADMIN_HASH);

        assertNotNull(secondSession);
    }

    @Test
    void playersHaveDifferentAvatars() {
        String adminSession = gameService.login("admin", ADMIN_HASH);
        String willowSession = gameService.login("willow", WILLOW_HASH);

        Player admin = gameService.getPlayer(adminSession);
        Player willow = gameService.getPlayer(willowSession);

        assertNotEquals(admin.getAvatar(), willow.getAvatar());
    }

    @Test
    void infoJsonIncludesBothOnlinePlayersWhenVisible() {
        String adminSession = gameService.login("admin", ADMIN_HASH);
        String willowSession = gameService.login("willow", WILLOW_HASH);

        Player admin = gameService.getPlayer(adminSession);
        Player willow = gameService.getPlayer(willowSession);

        String json = gameService.infoJson(admin, admin.getY(), admin.getX());

        assertTrue(json.contains(String.valueOf(admin.getAvatar())));
        assertTrue(json.contains(String.valueOf(willow.getAvatar())));
    }

    @Test
    void onlinePlayerBlocksMovement() {
        String adminSession = gameService.login("admin", ADMIN_HASH);
        String willowSession = gameService.login("willow", WILLOW_HASH);

        Player admin = gameService.getPlayer(adminSession);
        Player willow = gameService.getPlayer(willowSession);

        willow.setPosition(admin.getY(), admin.getX() + 1);

        assertFalse(gameService.move(admin, 0, 1));
    }

    private void backupAndWriteEnvFile() throws Exception {
        Path envPath = Path.of(".env");

        hadEnvFile = Files.exists(envPath);
        previousEnvFile = hadEnvFile ? Files.readString(envPath) : null;

        Files.writeString(envPath, ENV_CONTENT);
    }

    private void restoreEnvFile() throws Exception {
        Path envPath = Path.of(".env");

        if (hadEnvFile) {
            Files.writeString(envPath, previousEnvFile);
        } else {
            Files.deleteIfExists(envPath);
        }
    }
}