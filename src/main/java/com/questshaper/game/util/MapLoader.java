package com.questshaper.game.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {

    public static char[][] loadMap(String filename) {
        List<char[]> rows = new ArrayList<>();

        try {
            InputStream is = MapLoader.class.getClassLoader().getResourceAsStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                rows.add(line.toCharArray());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows.toArray(new char[0][]);
    }
}