package com.questshaper.game;


import com.questshaper.game.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private GameService gameService;

    @BeforeEach
    void setup() {
        gameService =
                new GameService(
                        new MapService()
                );
    }

    @Test
    void validLoginCreatesSession() {

        String session =
                gameService.login(
                        "Bob",
                        "hash123"
                );

        assertNotNull(session);
    }

    @Test
    void sessionExistsAfterLogin() {

        String session =
                gameService.login(
                        "Bob",
                        "hash123"
                );

        assertTrue(
                gameService.isValidSession(
                        session
                )
        );
    }

    @Test
    void logoutRemovesSession() {

        String session =
                gameService.login(
                        "Bob",
                        "hash123"
                );

        gameService.logout(session);

        assertFalse(
                gameService.isValidSession(
                        session
                )
        );
    }

    @Test
    void moveEastSucceeds() {

        String s =
                gameService.login(
                        "Bob",
                        "hash"
                );

        assertTrue(
                gameService.move(s,0,1)
        );
    }

    @Test
    void diagonalMoveFails() {

        String s =
                gameService.login(
                        "Bob",
                        "hash"
                );

        assertFalse(
                gameService.move(s,1,1)
        );
    }

    @Test
    void moveTooFarFails() {

        String s =
                gameService.login(
                        "Bob",
                        "hash"
                );

        assertFalse(
                gameService.move(s,2,0)
        );
    }

    @Test
    void blockedMoveFails() {

        String s =
                gameService.login(
                        "Bob",
                        "hash"
                );

        assertFalse(
                gameService.move(s,-1,0)
        );
    }

    @Test
    void invalidSessionMoveFails() {

        assertFalse(
                gameService.move(
                        "bad",
                        0,
                        1
                )
        );
    }

    @Test
    void infoInvalidSessionReturnsNull() {

        assertNull(
                gameService.getInfo(
                        "bad",
                        5,
                        5
                )
        );
    }

    @Test
    void infoCorrectSessionWorks() {

        String s =
                gameService.login(
                        "Bob",
                        "hash"
                );

        var info =
                gameService.getInfo(
                        s,
                        5,
                        5
                );

        assertNotNull(info);
    }


    @Test
void avatarAssigned() {

    String s =
            gameService.login(
                    "Bob",
                    "hash"
            );

    char avatar =
            gameService
                    .getPlayer(s)
                    .getAvatar();

    assertTrue(
            avatar >= '0'
            && avatar <= '9'
    );
}

@Test
void avatarsAreUnique() {

    String s1 =
            gameService.login(
                    "A",
                    "1"
            );

    String s2 =
            gameService.login(
                    "B",
                    "2"
            );

    char a1 =
            gameService.getPlayer(s1)
                    .getAvatar();

    char a2 =
            gameService.getPlayer(s2)
                    .getAvatar();

    assertNotEquals(a1,a2);
}

}