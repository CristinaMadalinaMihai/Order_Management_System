package dao;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import connection.ConnectionFactory;
import model.Client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Stream;

/**
 * Uses:
 * - basic CRUD methods from superclass
 * - specific method for finding a client
 * - methods for generating a report of all current clients
 */
public class ClientDAO extends AbstractDAO<Client> {

    /**
     * an integer representing the number of reports
     */
    private int numberOfReport;
    public ClientDAO() {
        numberOfReport = 0;
    }

    /**
     * creates a specific query using the name and address field name
     * obtains the connection to the DataBase
     * uses the resultSet to execute the statement
     * @param name a String representing the unique content of an entry
     * @param address a String representing the unique content of an entry
     * @return the object of type Client representing the entry with the specified name and address
     */
    public Client findByNameAndAddress(String name, String address) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery2("name", "address");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, address);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, Client.class + "DAO:findByNameAndAddress " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * deletes a specific object of type Client from the DataBase identified by name and address
     * @param name a String representing the name of the Client
     * @param address a String representing the address of the Client
     */
    public void deleteClient(String name, String address) {
        Client client = findByNameAndAddress(name, address);
        int id = client.getId();
        delete(id);
    }

    /**
     * creates and fills a PDF report in tabular form containing all the clients in the DataBase
     * @throws FileNotFoundException if file does not exist
     * @throws DocumentException if document cannot be created
     */
    public void generateClientReport() throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("ClientReport" + numberOfReport + ".pdf"));
        numberOfReport++;
        document.open();

        PdfPTable table = new PdfPTable(3);
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
        Stream.of("Id", "Full name", "Address")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.GREEN);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    /**
     * builds and populates the rows of the table
     * @param table the table which will be operated on
     */
    private void addRows(PdfPTable table) {
        List<Client> clientList = findAll();
        for (Client client : clientList) {
            table.addCell(Integer.toString(client.getId()));
            table.addCell(client.getName());
            table.addCell(client.getAddress());
        }
    }

}
