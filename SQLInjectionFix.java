package com.fresco.t7challenge.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fresco.t7challenge.models.Users;

@Service
public class SqlService {
    public List<Users> getUser(String userId) {
        List<Users> users = new ArrayList<>();

        try (Connection sqlConnection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "")) {
            String query = "SELECT user_id, first_name, last_name, username, password, avatar FROM users WHERE user_id = ?";
            PreparedStatement st = sqlConnection.prepareStatement(query);
            st.setString(1, userId);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String avatar = rs.getString("avatar");

                Users user = new Users(id, firstName, lastName, username, password, avatar);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}
