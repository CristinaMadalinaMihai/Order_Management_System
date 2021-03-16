package model;

/**
 * models the item table from the DataBase
 */
public class Item {
    /**
     * the id of the item in the DataBase
     */
    private int id;
    /**
     * the id by which the product will be identified
     */
    private int productId;
    /**
     * the quantity required for the current item
     */
    private int quantity;

    public Item(int id, int productId, int quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    /**
     * creates an item with the specified attributes
     * @param productId the product id of the item
     * @param quantity the quantity of the item
     */
    public Item(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Item() {
    }

    /**
     * gets the id of the item in the DataBase
     * @return an integer representing the id of the item in the DataBase
     */
    public int getId() {
        return id;
    }

    /**
     * sets the id of the item in the DataBase
     * @param id an integer containing the id of the item in the DataBase
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * gets the product id of the item
     * @return an integer representing the product id of the item
     */
    public int getProductId() {
        return productId;
    }

    /**
     * sets the product id of the item
     * @param productId an integer containing the product id of the item
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * gets the quantity of the item
     * @return an integer representing the quantity of the item
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * sets the quantity of the item
     * @param quantity an integer containing the quantity of the item
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
