package business_logic;

import dao.ProductDAO;
import model.Product;

/**
 * validates the Product fields and checks whether the given product already exists or not.
 */
public class ProductBLL {

    private Validator validator;

    /**
     * creates product business logic
     */
    public ProductBLL() {
        validator = new Validator();
    }

    /**
     * checks the given input for the product and builds the logic
     * @param product is the possible product given as input
     * @return false in case of a new product and true, otherwise
     */
    public boolean checkProduct(Product product) {
        if ((!validator.validateProductName(product)) || (!validator.validateProductPrice(product)) || (!validator.validateProductStock(product))) {
            throw new IllegalArgumentException("Product fields are invalid");
        }
        Product sameProduct = new ProductDAO().findByName(product.getName());
        if (sameProduct == null) {
            return false;
        }
        if (sameProduct.getName() == null) {
            return false; // then insert
        } else {
            return true; // then delete or update
        }
    }
}
