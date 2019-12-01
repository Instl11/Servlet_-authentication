package ru.login.servlets;

import ru.login.dao.UserDAO;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            UserDAO.createUserTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
