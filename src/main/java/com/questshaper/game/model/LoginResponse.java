package com.questshaper.game.model;

public class LoginResponse {

    private String session;
    private int x;
    private int y;

    public LoginResponse(
            String session,
            int x,
            int y) {

        this.session = session;
        this.x = x;
        this.y = y;
    }

    public String getSession() {
        return session;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}