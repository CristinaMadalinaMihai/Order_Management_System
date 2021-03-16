package dao;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import connection.ConnectionFactory;
import model.Client;
import model.Item;
import model.Order;
import model.Product;

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
 * - specific method for finding a section of the orders
 * - methods for generating reports and bills
 */
public class OrderDAO extends AbstractDAO<Order> {

    private int numberOfReport;
    public OrderDAO() {
        numberOfReport = 0;
    }

    /**
     * creates a specific query using the field name
     * obtains the connection to the DataBase
     * uses the resultSet to execute the statement
     * @param fieldName a String representing the required name of the field
     * @param id an integer representing the criteria with which the fields will be identified
     * @return the list of model objects of type Order with the specified id
     */
    public List<Order> findSection(String fieldName, int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery(fieldName);
        System.out.println(query);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            List<Order> table;
            table = createObjects(resultSet);
            if (!table.isEmpty())
                return table;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, Order.class + "DAO:findSection " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * creates and fills a PDF report in tabular form containing all the orders in the DataBase
     * @throws FileNotFoundException if file does not exist
     * @throws DocumentException if document cannot be created
     */
    public void generateOrderReport() throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("OrderReport" + numberOfReport + ".pdf"));
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
        Stream.of("Id", "Client", "Item", "Total price")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.MAGENTA);
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
        List<Order> orderList = findAll();
        for (Order order : orderList) {
            table.addCell(Integer.toString(order.getId()));
            Client client = new ClientDAO().findById(order.getClientId());
            table.addCell(client.getName());
            Item item = new ItemDAO().findById(order.getItemId());
            Product product = new ProductDAO().findById(item.getProductId());
            table.addCell(product.getName());
            table.addCell(Float.toString(order.getTotalPrice()));
        }
    }

    /**
     * creates and fills a PDF report containing an under-stock message
     * @param clientName a String representing the name of the client
     * @param productName a String representing the name of the product which is under-stocked
     * @throws FileNotFoundException if file does not exist
     * @throws DocumentException if document cannot be created
     */
    public void generateBill(String clientName, String productName) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(clientName + "Bill.pdf"));

        document.open();

        document.add(new Paragraph("Error message: insufficient stock of " + productName + " to process order of " + clientName));
        document.close();
    }

    /**
     * creates and fills a PDF report in tabular form containing all the orders of a given client
     * @param clientName a String representing the name of the client who requested an order
     * @throws FileNotFoundException if file does not exist
     * @throws DocumentException if document cannot be created
     */
    public void generateOrderBill(String clientName) throws FileNotFoundException, DocumentException {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("OrderReport" + clientName + numberOfReport +".pdf"));
            numberOfReport++;
            document.open();

            PdfPTable table = new PdfPTable(4);
            addOrderTableHeader(table);
            addOrderRows(table, clientName);

            document.add(table);
            document.close();

    }

    /**
     * populates and customises the header of the table
     * @param table the table which will be operated on
     */
    private void addOrderTableHeader(PdfPTable table) {
        Stream.of("Client name", "Ordered item", "Quantity", "Total price")
                .forEach(columnTitle ->{
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.BLUE);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    /**
     * builds and populates the rows of the table
     * @param table the table which will be operated on
     * @param clientName a String representing the name of the client who requested an order
     */
    private void addOrderRows(PdfPTable table, String clientName) {
        Client client = new ClientDAO().findByName(clientName);
        List<Order> orderList = findSection("clientId", client.getId());
        for (Order order : orderList) {
            table.addCell(clientName);
            String productName = new ProductDAO().findById(new ItemDAO().findById(order.getItemId()).getProductId()).getName();
            table.addCell(productName);
            int quantity = new ItemDAO().findById(order.getItemId()).getQuantity();
            table.addCell(String.valueOf(quantity));
            table.addCell(String.valueOf(order.getTotalPrice()));
        }
    }

}
