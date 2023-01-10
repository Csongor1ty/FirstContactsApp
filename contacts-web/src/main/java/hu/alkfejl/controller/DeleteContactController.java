package hu.alkfejl.controller;

import hu.alkfejl.dao.ContactDAO;
import hu.alkfejl.dao.ContactDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/DeleteContactController")
public class DeleteContactController extends HttpServlet {

    ContactDAO dao = ContactDAOImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int contactId = Integer.parseInt(req.getParameter("contactId"));
            dao.delete(contactId);
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }

        resp.sendRedirect("pages/list-contact.jsp");
    }
}
