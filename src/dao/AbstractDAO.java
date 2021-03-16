package dao;

import connection.ConnectionFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author criss
 * Generic class that defines the common operations for accessing a table:
 * - findAll
 * - findById
 * - findByName
 * - insert
 * - update
 * - delete
 * - resetAutoincrement
 * @param <T> any Java Model Class that is mapped to the DataBase,
 *           and has the same name as the table and the same instance
 *           variables and data types as the table fields
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    /**
     * obtains the class of the generic type T
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    /**
     * builds a generic select query with one field
     * @param field a String representing the name of the field
     * @return a String containing the select query
     */
    public String createSelectQuery(String field) {
        // String query = "SELECT * FROM warehouse.tableName WHERE field =?"
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM warehouse.");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    /**
     * builds a generic select query with two fields
     * @param field1 a String representing the name of the first field
     * @param field2 a String representing the name of the second field
     * @return a String containing the select query
     */
    public String createSelectQuery2(String field1, String field2) {
        // String query = "SELECT * FROM warehouse.tableName WHERE field1 =? AND field2 =?"
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field1 + " =? AND " + field2 + " =?");
        return sb.toString();
    }

    /**
     * builds a generic insert query
     * @param t the model object of type T which will be inserted
     * @return a String containing the insert query
     */
    private String createInsertQuery(T t) {
        // String query = "INSERT INTO warehouse.tableName (field1, field2, field3, field4)" + "values(?,?,?,?)"
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO warehouse." + type.getSimpleName() + " (");
        query.append(type.getDeclaredFields()[1].getName() + ", " + type.getDeclaredFields()[2].getName());
        if (type.getDeclaredFields().length == 4) {
            query.append(", " + type.getDeclaredFields()[3].getName() + ") values (");
        } else {
            query.append(") values (");
        }
        try {
            Field[] field = type.getDeclaredFields();
            for (int i = 1; i < field.length; i++) {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field[i].getName(), type);
                Method method = propertyDescriptor.getReadMethod();
                if (field[i].getType().equals(String.class)) {
                    query.append("\'" + method.invoke(t) + "\' ,");
                } else {
                    query.append(method.invoke(t) + ",");
                }
            }
            query.deleteCharAt(query.length() - 1);
            query.append(")");
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(query.toString());
        return query.toString();
    }

    /**
     * builds a generic delete query
     * @param id the model object of type T which will be inserted
     * @return a String containing the delete query
     */
    private String createDeleteQuery(int id) {
        // String query = "DELETE FROM warehouse.tableName WHERE id = ?"
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM warehouse." + type.getSimpleName() + " WHERE id = " + id);
        /*try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(type.getDeclaredFields()[0].getName(), type);
            Method method = propertyDescriptor.getReadMethod();
            query.append(method.invoke(t));
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }*/
        System.out.println(query.toString());
        return query.toString();
    }

    /**
     * builds a generic update query
     * @param t the model object of type T which will be inserted
     * @param id the id of the field where the udpate will occur
     * @return a String containing the update query
     */
    private String createUpdateQuery(T t, int id) {
        // String query = "UPDATE warehouse.tableName SET field2 = ?, field3 = ?, field4 = ? WHERE id = ?"
        StringBuilder query = new StringBuilder();
        query.append("UPDATE warehouse." + type.getSimpleName() + " SET ");
        try {
            for (int i = 1; i < type.getDeclaredFields().length; i++) {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(type.getDeclaredFields()[i].getName(), type);
                Method method = propertyDescriptor.getReadMethod();

                if (type.getDeclaredFields()[i].getType().equals(String.class)) {
                    query.append(type.getDeclaredFields()[i].getName() + " = '" + method.invoke(t) + "',");
                } else {
                    query.append(type.getDeclaredFields()[i].getName() + " = '" + method.invoke(t) + "',");
                }
            }

            query.deleteCharAt(query.length() - 1);

        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        query.append(" WHERE id = " + id);
        System.out.println(query);
        return query.toString();
    }

    /**
     * builds the generic query,
     * obtains the connection to the DataBase,
     * uses the resultSet to execute the statement
     * @return the list of model objects of type T
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        // String query = "SELECT * FROM warehouse.tableName"
        String query = "SELECT * FROM warehouse." + type.getSimpleName();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

    /**
     * builds the generic query using the id field name
     * obtains the connection to the DataBase
     * uses the resultSet to execute the statement
     * @param id an integer representing the PK of an entry
     * @return the object of type T representing the entry with the specified id
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            List<T> table;
            table = createObjects(resultSet);
            if (!table.isEmpty())
                return table.get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * builds the generic query using the name field name
     * obtains the connection to the DataBase
     * uses the resultSet to execute the statement
     * @param name a String representing the unique content of an entry
     * @return the object of type T representing the entry with the specified name
     */
    public T findByName(String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("name");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            resultSet = statement.executeQuery();

            List<T> table;
            table = createObjects(resultSet);
            if (!table.isEmpty())
                return table.get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findByName " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * retrieves from the current resultSet the value of the fields
     * @param resultSet the entries of a query
     * @return the list of model objects of type T
     */
    public List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();

        try {
            while (resultSet.next()) {
                T instance = type.getDeclaredConstructor().newInstance();
                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * builds the generic query for inserting an object of type T
     * obtains the connection to the DataBase
     * @param t the object of type T which is required to be inserted
     */
    public void insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createInsertQuery(t);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

    }

    /**
     * builds the generic query for updating an object of type T
     * obtains the connection to the DataBase
     * @param t the object of type T which is required to be updated
     * @param id an integer representing the criteria for which the entry will be identified
     */
    public void update(T t, int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createUpdateQuery(t, id);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * builds the generic query for deleting an object of type T
     * obtains the connection to the DataBase
     * @param id an integer representing the criteria for which the entry will be identified
     */
    public void delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createDeleteQuery(id);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * builds the query for resetting the id after a deletion is done
     * @param tableName a String representing the name of the table
     */
    public void resetAutoincrement(String tableName) {
        // ALTER TABLE tableName AUTO_INCREMENT = 1
        Connection connection = null;
        PreparedStatement statement = null;
        String query = " ALTER TABLE warehouse." + tableName + " AUTO_INCREMENT = 1";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:resetAutoincrement " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
}
