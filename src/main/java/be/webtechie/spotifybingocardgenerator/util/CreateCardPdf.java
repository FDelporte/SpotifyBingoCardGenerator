package be.webtechie.spotifybingocardgenerator.util;

import be.webtechie.spotifybingocardgenerator.model.BingoCard;
import be.webtechie.spotifybingocardgenerator.model.ImportedSong;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.List;

/**
 * Based on <a href="https://www.vogella.com/tutorials/JavaPDF/article.html">this article</a>
 */
public class CreateCardPdf {

    private static final Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font FONT_SMALL = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

    private static final int NUMBER_OF_COLUMNS = 4;

    private CreateCardPdf() {
        // Hide constructor
    }

    public static void createPdfWithBingoCards(List<BingoCard> cards) {
        try {
            var desktop = System.getProperty("user.home") + "/Desktop";
            var pdfFile = Paths.get(desktop, "bingo_" + System.currentTimeMillis() + ".pdf").toFile();
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            for (BingoCard card : cards) {
                addBingoCard(document, card);
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addBingoCard(Document document, BingoCard bingoCard) throws DocumentException {
        // Start a new page
        document.newPage();

        Paragraph paragraph = new Paragraph();
        paragraph.setAlignment(Element.ALIGN_CENTER);

        // We add one empty line
        addEmptyLine(paragraph, 1);

        // Name of the person for this card
        var personName =new Paragraph(bingoCard.forName(), FONT_TITLE);
        personName.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(personName);

        // We add two empty lines
        addEmptyLine(paragraph, 2);

        // Add a table with the songs
        PdfPTable table = new PdfPTable(NUMBER_OF_COLUMNS);

        for (ImportedSong song : bingoCard.songs()) {
            PdfPCell tableCell = new PdfPCell(new Phrase(song.title()
                    + System.lineSeparator()
                    + System.lineSeparator()
                    + song.artist()
                    + System.lineSeparator()
                    + System.lineSeparator()
                    + (song.selectedBy().equals(bingoCard.forName()) ? "" : song.selectedBy()), FONT_SMALL));
            tableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableCell.setVerticalAlignment(Element.ALIGN_TOP);
            tableCell.setMinimumHeight(120);
            if (song.selectedBy().equals(bingoCard.forName())) {
                tableCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            }
            table.addCell(tableCell);
        }

        paragraph.add(table);

        // Add paragraph to document
        document.add(paragraph);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
