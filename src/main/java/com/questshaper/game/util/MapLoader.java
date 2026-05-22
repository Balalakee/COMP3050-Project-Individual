package com.questshaper.game.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {

    public static String[][] loadMap(String filename) {
        List<String[]> rows = new ArrayList<>();

        try {
            InputStream is = MapLoader.class.getClassLoader().getResourceAsStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                // split by one or more spaces (safer than single space)
                String[] tokens = line.trim().split("\\s+");
                rows.add(tokens);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows.toArray(new String[0][]);
    }
}