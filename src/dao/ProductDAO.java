package dao;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.Client;
import model.Product;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Stream;

/**
 * Uses:
 * - basic CRUD methods from superclass
 * - methods for generating a report of all current products
 */
public class ProductDAO extends AbstractDAO<Product> {

    private int numberOfReport;
    public ProductDAO() {
        numberOfReport = 0;
    }

    /**
     * creates and fills a PDF report in tabular form containing all the products in the DataBase
     * @throws FileNotFoundException if file does not exist
     * @throws DocumentException if document cannot be created
     */
    public void generateProductReport() throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("ProductReport" + numberOfReport + ".pdf"));
        numberOfReport++;
        document.open();

        PdfPTable table = new PdfPTable(4);
        addTableHeader(table);
        addRows(table);

        document.add(table);
        document.close();
    }

    /**
     * populates and customises the header of the table
     * @param table the table which will be operated on
     */
    private void addTableHeader(PdfPTable table) {
        Stream.of("Id", "Name", "Stock", "Price")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    /**
     * populates and customises the rows of the table
     * @param table the table which will be operated on
     */
    private void addRows(PdfPTable table) {
        List<Product> productList = findAll();
        for (Product product : productList) {
            table.addCell(Integer.toString(product.getId()));
            table.addCell(product.getName());
            table.addCell(Integer.toString(product.getStock()));
            table.addCell(Float.toString(product.getPrice()));
        }
    }
}
