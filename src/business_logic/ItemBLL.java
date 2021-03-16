package business_logic;

import dao.ItemDAO;
import dao.ProductDAO;
import model.Item;
import model.Product;

/**
 * validates the Item fields and updates the product stock if valid
 */
public class ItemBLL {

    private Validator validator;

    /**
     * creates item business logic
     */
    public ItemBLL() {
        validator = new Validator();
    }

    /**
     * checks the given input for the item and builds the logic
     * @param item the possible item given as input
     * @return false if the stock is insufficient and true, otherwise
     */
    public boolean checkItem(Item item) {
        if (!validator.validateItemQuantity(item)) {
            throw new IllegalArgumentException("Item quantity invalid");
        }
        Product product = new ProductDAO().findById(item.getProductId());
        if (product.getStock() < item.getQuantity()) {
            return false; // then don't insert, nor update
        } else {
            product.setStock(product.getStock() - item.getQuantity());
            new ProductDAO().update(product, product.getId());
            return true; // then insert or update
        }
    }

}
