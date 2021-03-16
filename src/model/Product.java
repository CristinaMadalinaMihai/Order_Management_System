package model;

/**
 * models the product table from the DataBase
 */
public class Product {
    /**
     * the id of the product
     */
    private int id;
    /**
     * the name of the product
     */
    private String name;
    /**
     * the current stock of the product
     */
    private int stock;
    /**
     * the price of the product
     */
    private float price;

    public Product(int id, String name, int stock, float price) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    /**
     * creates a product with the specified attributes
     * @param name the name of the product
     * @param stock the current stock of the product
     * @param price the current price of the product
     */
    public Product(String name, int stock, float price) {
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public Product() {
    }

    /**
     * gets the id of the product in the DataBase
     * @return an integer representing the id of the product in the DataBase
     */
    public int getId() {
        return id;
    }

    /**
     * sets the id of the product in the DataBase
     * @param id an integer containing the id of the product in the DataBase
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * gets the name of the product
     * @return a String representing the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of the product
     * @param name a String containing the name of the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets the current stock of the product
     * @return an integer representing the current stock of the product
     */
    public int getStock() {
        return stock;
    }

    /**
     * sets the current stock of the product
     * @param stock an integer containing the current stock of the product
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * gets the price of the product
     * @return a float representing the price of the product
     */
    public float getPrice() {
        return price;
    }

    /**
     * sets the price of the product
     * @param price a float containing the price of the product
     */
    public void setPrice(float price) {
        this.price = price;
    }
}
