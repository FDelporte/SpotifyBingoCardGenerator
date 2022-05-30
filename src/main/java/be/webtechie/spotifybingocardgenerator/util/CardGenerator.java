package be.webtechie.spotifybingocardgenerator.util;

import be.webtechie.spotifybingocardgenerator.model.BingoCard;
import be.webtechie.spotifybingocardgenerator.model.ImportedSong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CardGenerator {

    private static final Logger logger = LogManager.getLogger(CardGenerator.class.getName());

    private static final Random RANDOM = new Random();
    private static final int SONGS_PER_CARD = 16;

    private CardGenerator() {
        // Hide constructor
    }

    public static List<BingoCard> generate(List<String> persons, List<ImportedSong> songs) {
        var list = new ArrayList<BingoCard>();
        for (String person : persons) {
            list.add(generate(person, songs));
        }
        return list;
    }

    private static BingoCard generate(String person, List<ImportedSong> songs) {
        var songsForPerson = new ArrayList<ImportedSong>();

        logger.info("Songs for {}", person);

        // Add songs selected by this person
        for (ImportedSong song : songs) {
            if (song.selectedBy().equals(person)) {
                songsForPerson.add(song);
                logger.info(song);
            }
        }

        // Add random other songs till list has requested size
        while (songsForPerson.size() < SONGS_PER_CARD) {
            var randomSong = songs.get(RANDOM.nextInt(songs.size()));
            if (!songsForPerson.contains(randomSong)) {
                songsForPerson.add(randomSong);
                logger.info(randomSong);
            }
        }

        // Randomize to avoid the own songs being first in the list
        Collections.shuffle(songsForPerson);

        return new BingoCard(person, songsForPerson);
    }
}
