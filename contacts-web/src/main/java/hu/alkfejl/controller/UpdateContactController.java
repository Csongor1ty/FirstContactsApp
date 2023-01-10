package hu.alkfejl.controller;

import hu.alkfejl.dao.ContactDAO;
import hu.alkfejl.dao.ContactDAOImpl;
import hu.alkfejl.model.Contact;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/UpdateContactController")
public class UpdateContactController extends HttpServlet {
    private ContactDAO dao = ContactDAOImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contactIdStr = req.getParameter("contactId");

        if(contactIdStr != null && !contactIdStr.isEmpty()){
            int contactId = Integer.parseInt(contactIdStr);
            Contact contact = dao.findById(contactId);
            req.setAttribute("contact", contact);
        }

        req.getRequestDispatcher("pages/add-contact.jsp").forward(req, resp);
    }
}
