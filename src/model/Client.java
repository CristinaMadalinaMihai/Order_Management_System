package model;

/**
 * models the client table from the DataBase
 */
public class Client {
    /**
     * the client's id in the DataBase
     */
    private int id;
    /**
     * the client's full name
     */
    private String name;
    /**
     * the client's address
     */
    private String address;

    public Client(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    /**
     * creates a client with the specified attributes
     * @param name the client's full name
     * @param address the client's address
     */
    public Client(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Client() {
    }

    /**
     * gets the client's id in the DataBase
     * @return an integer representing the client's id in the DataBase
     */
    public int getId() {
        return id;
    }

    /**
     * sets the client's id in the DataBase
     * @param id an integer containing the client's id in the DataBase
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * gets the client's full name
     * @return a String representing the client's full name
     */
    public String getName() {
        return name;
    }

    /**
     * sets the client's full name
     * @param name a String containing the client's full name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets the client's address
     * @return a String representing the client's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * sets the client's address
     * @param address a String containing the client's address
     */
    public void setAddress(String address) {
        this.address = address;
    }

}
