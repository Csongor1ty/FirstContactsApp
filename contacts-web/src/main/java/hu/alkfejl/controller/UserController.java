package hu.alkfejl.controller;

import hu.alkfejl.dao.UserDAO;
import hu.alkfejl.dao.UserDAOImpl;
import hu.alkfejl.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/UserController")
public class UserController extends HttpServlet {

    private UserDAO userDAO = UserDAOImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("currentUser");

        if(req.getParameter("name") != null && !req.getParameter("name").isEmpty()){
            user.setUsername(req.getParameter("name"));
        }

        if(req.getParameter("email") != null && !req.getParameter("email").isEmpty()){
            user.setEmail(req.getParameter("email"));
        }

        if(req.getParameter("profilePic") != null && !req.getParameter("profilePic").isEmpty()){
            user.setProfilePic(req.getParameter("profilePic"));
        }

        if(req.getParameter("description") != null && !req.getParameter("description").isEmpty()){
            user.setDescription(req.getParameter("description"));
        }

        user = userDAO.save(user);

        req.getSession().setAttribute("currentUser", user);
        resp.sendRedirect("pages/profile.jsp");
    }
}
