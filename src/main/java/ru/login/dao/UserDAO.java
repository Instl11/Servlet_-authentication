package ru.login.dao;

import ru.login.model.User;

import java.sql.*;

import static ru.login.model.User.*;

public class UserDAO {

    private static Connection connection = getConnection();

    private static Connection getConnection() {
        String username = "postgres";
        String password = "11111";
        String url = "jdbc:postgresql://localhost:5433/demo";
        connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Some problems with connection to Database");
        }
        return connection;
    }

    public static void createUserTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS My_Users(" +
                "username text not null, " +
                "password text not null, " +
                "role text not null, " +
                "PRIMARY KEY (username))");
    }

    public static void registerUser(String username, String password, Role role) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO My_Users VALUES (?, ?, ?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean userExists(String username) {
        String name = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT username FROM My_Users WHERE username = ?");
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                name = resultSet.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (name == null) {
            return false;
        }
        return name.equals(username);
    }

    public static Role getRole(String username) {
        Role role = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT role FROM My_Users WHERE username = ?");
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            role = Role.valueOf(resultSet.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

    public static boolean usernameAndPassValidation(String username, String password) {
        Connection connection = getConnection();
        String name = null;
        String pass = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM My_Users WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                name = resultSet.getString(1);
                pass = resultSet.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (name == null || pass == null) {
            return false;
        }
        return username.equals(name) && password.equals(pass);
    }
}
