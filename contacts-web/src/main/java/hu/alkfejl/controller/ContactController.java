package hu.alkfejl.controller;


import hu.alkfejl.dao.ContactDAO;
import hu.alkfejl.dao.ContactDAOImpl;
import hu.alkfejl.dao.PhoneDAO;
import hu.alkfejl.dao.PhoneDAOImpl;
import hu.alkfejl.model.Contact;
import hu.alkfejl.model.Phone;
import hu.alkfejl.model.User;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/ContactController")
public class ContactController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ContactDAO dao = ContactDAOImpl.getInstance();
    private PhoneDAO phoneDAO = new PhoneDAOImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        int contactId = 0;
        try {
            contactId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException ex){
            ex.printStackTrace();
        }

        Contact c = dao.findById(contactId);

        // check if c exists -> if no then construct a new one (it's a save)
        if(c == null){
            c = new Contact();
        }

        try {
            c.setName(request.getParameter("name"));
            c.setEmail(request.getParameter("email"));
            c.setDateOfBirth(LocalDate.parse(request.getParameter("dateOfBirth")));
            c.setAddress(request.getParameter("address"));
            c.setCompany(request.getParameter("company"));
            c.setPosition(request.getParameter("position"));

            User currentUser = (User) request.getSession().getAttribute("currentUser");
            c.setUser(currentUser);

            String[] phoneValues = request.getParameterValues("phoneValues");
            String[] phoneTypes = request.getParameterValues("phoneTypes");
            String[] phoneIds = request.getParameterValues("phoneIds");

            List<Phone> phones = new ArrayList<>();
            for(int i = 0; i < phoneValues.length; i++){
                Phone p = null;
                if(phoneIds != null && phoneIds[i] != null && !phoneIds[i].isEmpty()){
                    p = phoneDAO.findById(Integer.parseInt(phoneIds[i]));
                }

                if(p == null){
                    p = new Phone();
                }

                p.setNumber(phoneValues[i]);
                final String phoneTypeString = phoneTypes[i];
                Optional<Phone.PhoneType> foundPhoneType = Arrays.stream(Phone.PhoneType.values()).filter(phoneType -> phoneType.getValue().equals(phoneTypeString)).findFirst();
                p.setPhoneType(foundPhoneType.orElse(Phone.PhoneType.UNKNOWN));
                phones.add(p);
            }
            c.setPhones(phones);
            dao.save(c);


            response.sendRedirect("pages/list-contact.jsp");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");
        List<Contact> all = dao.findAll(currentUser);
        req.setAttribute("contactList", all);
    }
}
