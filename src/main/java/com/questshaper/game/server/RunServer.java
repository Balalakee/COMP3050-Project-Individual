package com.questshaper.game.server;

import com.questshaper.game.server.handlers.*;
import com.questshaper.game.service.GameService;
import com.sun.net.httpserver.HttpServer;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.io.InputStream;

// ...



import java.net.InetSocketAddress;

public class RunServer {

    public static void main(String[] args) throws Exception {
    GameService game = new GameService();
    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/login", new LoginHandler(game));
        server.createContext("/logout", new LogoutHandler(game));
        server.createContext("/move", new MoveHandler(game));
        server.createContext("/info", new InfoHandler(game));
        server.createContext("/take", new TakeHandler(game));
        server.createContext("/place", new PlaceHandler(game));
        server.createContext("/use", new UseHandler(game));

        server.setExecutor(null);
        server.start();

        System.out.println("QuestShaper server running on port 8000");

        
    }
}
