package model;

/**
 * models the order table from the DataBase
 */
public class Order {
    /**
     * the id of the order in the DataBase
     */
    private int id;
    /**
     * the id by which the client will be identified
     */
    private int clientId;
    /**
     * the id by which the item will be identified
     */
    private int itemId;
    /**
     * the price of the item
     */
    private float totalPrice;

    public Order(int id, int clientId, int itemId, float totalPrice) {
        this.id = id;
        this.clientId = clientId;
        this.itemId = itemId;
        this.totalPrice = totalPrice;
    }

    /**
     * creates an order with the specified attributes
     * @param clientId the client's id
     * @param itemId the id of the item
     * @param totalPrice the price of the order
     */
    public Order(int clientId, int itemId, float totalPrice) {
        this.clientId = clientId;
        this.itemId = itemId;
        this.totalPrice = totalPrice;
    }

    public Order() {
    }

    /**
     * gets the id of the order in the DataBase
     * @return an integer representing the id of the order in the DataBase
     */
    public int getId() {
        return id;
    }

    /**
     * sets the id of the order in the DataBase
     * @param id an integer containing the id of the order in the DataBase
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * gets the client's id in the order
     * @return an integer representing the client's id in the order
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * sets the client's id in the order
     * @param clientId an integer containing the client's id in the order
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * gets the id of the item in the order
     * @return an integer containing the id of the item in the order
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * sets the id of the item in the order
     * @param itemId an integer containing the id of the item in the order
     */
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /**
     * gets the total price of the order
     * @return a float representing the total price of the order
     */
    public float getTotalPrice() {
        return totalPrice;
    }

    /**
     * sets the total price of the order
     * @param totalPrice a float containing the total price of the order
     */
    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
