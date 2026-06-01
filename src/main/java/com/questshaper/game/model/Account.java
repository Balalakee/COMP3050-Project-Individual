package com.questshaper.game.model;

public class Account {

    private final String username;
    private final String encryptedPassword;
    private final Player player;

    public Account(String username, String encryptedPassword, Player player) {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.player = player;
    }

    public String getUsername() {
        return username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public Player getPlayer() {
        return player;
    }
}