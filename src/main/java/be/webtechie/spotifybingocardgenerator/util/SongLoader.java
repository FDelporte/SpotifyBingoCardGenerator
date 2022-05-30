package be.webtechie.spotifybingocardgenerator.util;

import be.webtechie.spotifybingocardgenerator.model.ImportedSong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SongLoader {

    private static final Logger logger = LogManager.getLogger(SongLoader.class.getName());

    private SongLoader() {
        // Hide constructor
    }

    public static List<ImportedSong> loadFromFile(String fileName) {
        List<ImportedSong> list = new ArrayList<>();

        ClassLoader classLoader = SongLoader.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("File " + fileName + " not found!");
        }

        try {
            File f = new File(resource.getFile());
            try (BufferedReader b = new BufferedReader(new FileReader(f, StandardCharsets.UTF_8))) {
                String line;
                while ((line = b.readLine()) != null) {
                    var song = processLine(line);
                    if (song != null) {
                        list.add(song);
                        logger.info("Added {}", song);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Error while importing song list: {}", ex.getMessage());
        }
        return list;
    }

    private static ImportedSong processLine(String data) {
        if (data == null || data.isEmpty()) {
            logger.warn("Data is empty");
            return null;
        }
        String[] csvLine = data.split(",");
        return new ImportedSong(csvLine[0], csvLine[1], csvLine.length >= 3 ? csvLine[2] : "");
    }
}
