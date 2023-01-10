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

@WebServlet("/RegisterController")
public class RegisterController extends HttpServlet {

    UserDAO dao = UserDAOImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        User user = new User();
        user.setUsername(req.getParameter("username"));
        user.setEmail(req.getParameter("email"));
        user.setPassword(req.getParameter("password"));

        dao.save(user);

        resp.sendRedirect("pages/login.jsp");
    }
}
