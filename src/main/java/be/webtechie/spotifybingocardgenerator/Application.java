package be.webtechie.spotifybingocardgenerator;

import be.webtechie.spotifybingocardgenerator.model.ImportedSong;
import be.webtechie.spotifybingocardgenerator.util.CardGenerator;
import be.webtechie.spotifybingocardgenerator.util.CreateCardPdf;
import be.webtechie.spotifybingocardgenerator.util.SongLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;

public class Application {

    private static final Logger logger = LogManager.getLogger(Application.class.getName());

    private static final String SOURCE_FILE = "viks_muziekbingo.csv";

    public static void main(String[] args) {
        var songs = SongLoader.loadFromFile(SOURCE_FILE);
        logger.info("Number of songs loaded: {}", songs.size());

        var persons = songs.stream()
                .map(ImportedSong::selectedBy)
                .filter(s -> !s.isEmpty())
                .distinct()
                .sorted(Comparator.naturalOrder())
                .toList();
        logger.info("{} names found: {}", persons.size(), String.join(", ", persons));

        var cards = CardGenerator.generate(persons, songs);

        CreateCardPdf.createPdfWithBingoCards(cards);
    }
}
