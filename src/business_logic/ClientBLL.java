package business_logic;

import dao.ClientDAO;
import model.Client;

/**
 * validates the Client fields and checks whether the given client already exists or not.
 */
public class ClientBLL {

    private Validator validator;

    /**
     * creates client business logic
     */
    public ClientBLL() {
        validator = new Validator();
    }

    /**
     * checks the given input for the client and builds the logic
     * @param client is the possible client given as input
     * @return false in case of a new client and true, otherwise
     */
    public boolean checkClient(Client client) {
        if ((!validator.validateClientName(client)) || (!validator.validateClientAddress(client))) {
            throw new IllegalArgumentException("Client fields are invalid");
        }
        Client sameClient = new ClientDAO().findByName(client.getName());
        if (sameClient == null) {
            return false; // then client can be inserted
        } else {
            return true; // then client can be deleted
        }
    }

}
