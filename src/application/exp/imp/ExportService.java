package application.exp.imp;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.datatable.DataTable;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



public class ExportService {
	
	public enum Orientation {
        PORTRAIT, LANDSCAPE
    };
    
	public static boolean exportToPdf(List<List> list, File saveLoc, Orientation orientation ) {
		try {
            if (saveLoc == null) {
                return false;
            }
            if (!saveLoc.getName().endsWith(".pdf")) {
                saveLoc = new File(saveLoc.getAbsolutePath() + ".pdf");
            }
            //Initialize Document
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            //Create a landscape page
            if (orientation == Orientation.LANDSCAPE) {
                page.setMediaBox(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
            } else {
                page.setMediaBox(new PDRectangle(PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight()));
            }

            doc.addPage(page);
            //Initialize table
            float margin = 10;
            float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
            float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
            float yStart = yStartNewPage;
            float bottomMargin = 0;

            BaseTable dataTable = new BaseTable(yStart, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, true);
            DataTable t = new DataTable(dataTable, page);
            t.getDataCellTemplateEven().setFillColor(Color.WHITE);
            t.getDataCellTemplateOdd().setFillColor(Color.WHITE);
            t.getHeaderCellTemplate().setFillColor(Color.WHITE);
            t.addListToTable(list, DataTable.HASHEADER);
            dataTable.draw();
            doc.save(saveLoc);
            doc.close();

            return true;
        } catch (IOException ex) {
        }
        return false;
    }
	
	
	public static boolean initPDFExport(Stage stage, List<List> data) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as PDF");
        fileChooser.setInitialFileName("LMS");
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File saveLoc = fileChooser.showSaveDialog(stage);
        return exportToPdf(data, saveLoc, ExportService.Orientation.LANDSCAPE);

    }
	
	

}
