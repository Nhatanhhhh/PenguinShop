/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Loc_LM
 */
package DAOs;

import Models.Cart;
import Models.Customer;
import Models.Product;
import Models.ProductVariants;
import DB.DBContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckoutDAO {

    private DBContext dbContext = new DBContext();

    public Customer getCustomerInfo(int customerId) {
        Customer customer = null;
        String sql = "SELECT id, fullName, email, address, zip, state FROM Customer WHERE id = ?";

        try ( ResultSet rs = dbContext.execSelectQuery(sql, new Object[]{customerId})) {
            if (rs.next()) {
                customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("zip"),
                        rs.getString("state")
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(CheckoutDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return customer;
    }

    public List<Cart> getCartItems(int customerId) {
        List<Cart> cartItems = new ArrayList<>();
        String sql = "SELECT id, quantity, productID FROM Cart WHERE customerID = ?";

        try ( ResultSet rs = dbContext.execSelectQuery(sql, new Object[]{customerId})) {
            while (rs.next()) {
                int productID = rs.getInt("productID");
                Product product = getProductDetails(productID); // Gọi hàm riêng
                cartItems.add(new Cart(rs.getInt("id"), rs.getInt("quantity"), product));
            }
        } catch (SQLException e) {
            Logger.getLogger(CheckoutDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return cartItems;
    }

    private Product getProductDetails(int productID) {
        String sql = "SELECT productName, price FROM Product WHERE id = ?";
        try ( ResultSet rs = dbContext.execSelectQuery(sql, new Object[]{productID})) {
            if (rs.next()) {
                return new Product(rs.getString("productName"), rs.getDouble("price"));
            }
        } catch (SQLException e) {
            Logger.getLogger(CheckoutDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public double calculateSubtotal(List<Cart> cartItems) {
        double subtotal = 0;
        for (Cart cart : cartItems) {
            subtotal += cart.getQuantity() * cart.getProduct().getPrice();
        }
        return subtotal;
    }
}
