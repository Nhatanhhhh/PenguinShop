/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAOs.CheckoutDAO;
import Models.Cart;
import Models.Customer;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * @author PC
 */
public class Checkout extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String customerIDStr = (String) session.getAttribute("customerID");
        if (customerIDStr == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        int customerId = Integer.parseInt(customerIDStr);

        CheckoutDAO checkoutDAO = new CheckoutDAO();

        // Lấy thông tin khách hàng
        Customer customer = checkoutDAO.getCustomerInfo(customerId);

        // Lấy danh sách sản phẩm trong giỏ hàng
        List<Cart> cartItems = checkoutDAO.getCartItems(customerId);

        // Tính tổng giá trị đơn hàng
        double subtotal = checkoutDAO.calculateSubtotal(customerId);
        double shippingFee = 10.0;
        double total = subtotal + shippingFee;

        // Gửi dữ liệu đến JSP
        request.setAttribute("customer", customer);
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("shippingFee", shippingFee);
        request.setAttribute("total", total);

        request.getRequestDispatcher("Checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
