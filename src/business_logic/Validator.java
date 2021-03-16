package business_logic;

import model.Client;
import model.Item;
import model.Product;

import java.util.regex.Pattern;

/**
 * It validates the spelling of:
 * - the name and address of the client
 * - the name, stock and price of the product
 * - the quantity of the item
 * given as input
 */
public class Validator {

    public Validator() {
    }

    /**
     * validates the spelling of the client name given as input
     * @param client the given input
     * @return true in case client name is of form "Name Surname" and false, otherwise
     */
    public boolean validateClientName(Client client) {
        Pattern pattern = Pattern.compile("^[A-Za-z]+([\\ A-Za-z]+)*$");
        if(!pattern.matcher(client.getName()).matches()) {
            return false;
        }
        return true;
    }

    /**
     * validated the spelling of the client address given as input
     * @param client the given input
     * @return true in case client address is of form "City"/"City-Town"/"City Town" and false, otherwise
     */
    public boolean validateClientAddress(Client client) {
        Pattern pattern = Pattern.compile("^([A-Za-z]+([\\-A-Za-z]+)*([\\ A-Za-z]+)*)|([A-Za-z]+)$");
        if(!pattern.matcher(client.getAddress()).matches()) {
            return false;
        }
        return true;
    }

    /**
     * validates the spelling of the product name given as input
     * @param product the given input
     * @return true in case product name is of form "name" and false, otherwise
     */
    public boolean validateProductName(Product product) {
        Pattern pattern = Pattern.compile("[A-Za-z]+");
        if(!pattern.matcher(product.getName()).matches()) {
            return false;
        }
        return true;
    }

    /**
     * validates the spelling of the product stock given as input
     * @param product the given input
     * @return true in case product stock has an integer form and false, otherwise
     */
    public boolean validateProductStock(Product product) {
        Pattern pattern = Pattern.compile("^\\d+$");
        String stock = Integer.toString(product.getStock());
        if (!pattern.matcher(stock).matches()) {
            return false;
        }
        return true;
    }

    /**
     * validates the spelling of the item quantity given as input
     * @param item the given input
     * @return true in case item quantity has an integer form and false, otherwise
     */
    public boolean validateItemQuantity(Item item) {
        Pattern pattern = Pattern.compile("^\\d+$");
        String quantity = Integer.toString(item.getQuantity());
        if (!pattern.matcher(quantity).matches()) {
            return false;
        }
        return true;
    }

    /**
     * validates the spelling of the product price given as input
     * @param product the given input
     * @return true in case item quantity has an integer or float form and false, otherwise
     */
    public boolean validateProductPrice(Product product) {
        Pattern pattern = Pattern.compile("^([0-9]*[.])?[0-9]+$");
        String price = Float.toString(product.getPrice());
        if (!pattern.matcher(price).matches()) {
            return false;
        }
        return true;
    }
}
