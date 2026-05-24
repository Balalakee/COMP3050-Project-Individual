package com.questshaper.game.util;

import java.io.*;
import java.util.*;

public class MapLoader {

    public static String[][] loadMap(String filename) {

        List<String[]> rows = new ArrayList<>();

        try (InputStream is = MapLoader.class.getClassLoader().getResourceAsStream(filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String line;

            while ((line = br.readLine()) != null) {
                rows.add(line.trim().split("\\s+"));
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load map", e);
        }

        return rows.toArray(new String[0][]);
    }
}