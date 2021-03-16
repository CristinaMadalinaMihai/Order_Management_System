package dao;

import connection.ConnectionFactory;
import model.Client;
import model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

/**
 * Uses:
 * - basic CRUD methods from superclass
 * - specific method for finding an item
 */
public class ItemDAO extends AbstractDAO<Item> {

    public ItemDAO() {
    }

    /**
     * creates a specific query using the product id field name
     * obtains the connection to the DataBase
     * uses the resultSet to execute the statement
     * @param productId an integer representing the unique content of the entry in the product table
     * @return the object of type Item representing the entry with the specified productId
     */
    public Item findItemByProductId(int productId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("productId");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, Integer.toString(productId));
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

}
