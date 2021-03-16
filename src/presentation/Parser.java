package presentation;

import business_logic.ClientBLL;
import business_logic.ItemBLL;
import business_logic.ProductBLL;
import com.itextpdf.text.DocumentException;
import dao.ClientDAO;
import dao.ItemDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import model.Client;
import model.Item;
import model.Order;
import model.Product;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * It reads commands from a "commands.txt" and parses them
 */
public class Parser {

    public static void main(String[] args) {
        ClientDAO clientDAO = new ClientDAO();
        OrderDAO orderDAO = new OrderDAO();
        ProductDAO productDAO = new ProductDAO();
        ItemDAO itemDAO = new ItemDAO();
        clientDAO.resetAutoincrement("client");
        orderDAO.resetAutoincrement("order");
        productDAO.resetAutoincrement("product");
        itemDAO.resetAutoincrement("item");
        try {
            File inputFile = new File(args[0]);
            Scanner scan = new Scanner(inputFile);

            while (scan.hasNextLine()) {

                String[] commandLine = scan.nextLine().split(": ", 2);
                if(commandLine[0].equals("Report client")) {
                    clientDAO.generateClientReport();
                } else if (commandLine[0].equals("Report order")) {
                    orderDAO.generateOrderReport();
                } else if (commandLine[0].equals("Report product")) {
                    productDAO.generateProductReport();
                } else  if (commandLine[0].equals("Insert client")) {
                    String[] clientInfo = commandLine[1].split(", ", 2);
                    Client newClientToInsert = new Client(clientInfo[0], clientInfo[1]);
                    ClientBLL clientBLL = new ClientBLL();
                    if (clientBLL.checkClient(newClientToInsert)) {
                        System.out.println("Client " + clientInfo[0] + " already exists.");
                    } else {
                        clientDAO.insert(newClientToInsert);
                    }
                } else if (commandLine[0].equals("Delete client")) {
                    String[] clientInfo = commandLine[1].split(", ", 2);
                    Client newClientToDelete = new Client(clientInfo[0], clientInfo[1]);
                    ClientBLL clientBLL = new ClientBLL();
                    if (!clientBLL.checkClient(newClientToDelete)) {
                        System.out.println("Client " + clientInfo[0] + "does not exists.");
                    } else {
                        clientDAO.deleteClient(newClientToDelete.getName(), newClientToDelete.getAddress());
                        clientDAO.resetAutoincrement("client");
                    }
                } else if (commandLine[0].equals("Insert product")) {
                    String[] productInfo = commandLine[1].split(", ", 3);
                    Product newProductToInsert = new Product(productInfo[0], Integer.parseInt(productInfo[1]), Float.parseFloat(productInfo[2]));
                    ProductBLL productBLL = new ProductBLL();
                    if (!productBLL.checkProduct(newProductToInsert)) {
                        productDAO.insert(newProductToInsert);
                    } else {
                        Product product = productDAO.findByName(newProductToInsert.getName());
                        int id = product.getId();
                        int newStock = product.getStock() + newProductToInsert.getStock();
                        newProductToInsert.setStock(newStock);
                        productDAO.update(newProductToInsert, id);
                    }
                } else if (commandLine[0].equals("Delete Product")) {
                    Product newProductToDelete = productDAO.findByName(commandLine[1]);
                    ProductBLL productBLL = new ProductBLL();
                    if (productBLL.checkProduct(newProductToDelete)) {
                        productDAO.delete(newProductToDelete.getId());
                        productDAO.resetAutoincrement("product");
                    } else {
                        System.out.println("Product not found.");
                    }
                } else if (commandLine[0].equals("Order")) {
                    String[] orderInfo = commandLine[1].split(", ", 3);
                    Product product = productDAO.findByName(orderInfo[1]);
                    Item newItem = new Item(product.getId(), Integer.parseInt(orderInfo[2]));
                    ItemBLL itemBLL = new ItemBLL();
                    if (!itemBLL.checkItem(newItem)) {
                        System.out.println("Under-stock");
                        orderDAO.generateBill(orderInfo[0], orderInfo[1]);
                    } else {
                        itemDAO.insert(newItem);
                        float totalPrice = product.getPrice() * newItem.getQuantity();
                        Order newOrderToInsert = new Order(clientDAO.findByName(orderInfo[0]).getId(), itemDAO.findItemByProductId(newItem.getProductId()).getId(), totalPrice);
                        orderDAO.insert(newOrderToInsert);
                        orderDAO.generateOrderBill(orderInfo[0]);
                    }
                }
            }
        } catch (FileNotFoundException | DocumentException e) {
            System.out.println("\nopen file failed");
            e.printStackTrace();
        }
    }

}
