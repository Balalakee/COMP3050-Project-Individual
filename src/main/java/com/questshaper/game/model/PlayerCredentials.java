package com.questshaper.game.model;

public class PlayerCredentials {

    private final String username;
    private final String encPassword;
    private final Player player;

    public PlayerCredentials(
            String username,
            String encPassword,
            Player player) {

        this.username = username;
        this.encPassword = encPassword;
        this.player = player;
    }

    public String getUsername() {
        return username;
    }

    public String getEncPassword() {
        return encPassword;
    }

    public Player getPlayer() {
        return player;
    }
}