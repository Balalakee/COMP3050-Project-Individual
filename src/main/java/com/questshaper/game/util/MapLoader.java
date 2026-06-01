package com.questshaper.game.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {

    private static final String DEFAULT_MAP_RESOURCE =
            "map.txt";

    public static String[][] loadDefaultMap() {
        return loadMap(DEFAULT_MAP_RESOURCE);
    }

    public static String[][] loadMap(String resourcePath) {
        List<String[]> rows = new ArrayList<>();

        InputStream input =
                MapLoader.class
                        .getClassLoader()
                        .getResourceAsStream(resourcePath);

        if (input == null) {
            throw new RuntimeException(
                    "Could not find map resource: " + resourcePath
            );
        }

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     input,
                                     StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                rows.add(line.split("\\s+"));
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load map resource: " + resourcePath,
                    e
            );
        }

        if (rows.isEmpty()) {
            throw new RuntimeException(
                    "Map resource is empty: " + resourcePath
            );
        }

        return rows.toArray(new String[0][]);
    }
}