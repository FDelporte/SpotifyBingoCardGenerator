package be.webtechie.spotifybingocardgenerator.model;

import java.util.List;

public record BingoCard(String forName, List<ImportedSong> songs) {
}
