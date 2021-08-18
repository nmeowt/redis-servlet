package org.nmt.controller;

import org.nmt.model.User;
import org.nmt.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/user")
public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users =  userService.list();
        String context = "";
        for (User user: users) {
            String data = "{" +
                    "\"id\": \""+ user.getId() +"\"," +
                    "\"name\": \""+ user.getName() +"\"," +
                    "\"country\": \""+ user.getAddress() +"\"," +
                    "}";
            context+= data + ",";
        }
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print("[" + context + "]");
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        User user = new User();
        user.setName(name);
        user.setAddress(address);
        userService.addUser(user);
    }
}
