package com.fresco.t7challenge.repo;
​
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
​
import org.springframework.stereotype.Service;
​
import com.fresco.t7challenge.models.Users;
​
@Service
public class SqlService {
	public List<Users> getUser(String userId) {
		List<Users> users = new ArrayList<Users>();
		try (Connection sqlConnection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "")) {
			Statement st = sqlConnection.createStatement();
			ResultSet rs = st.executeQuery(
					"SELECT user_id, first_name, last_name, user, password, avatar FROM users WHERE user_id = "
							+ userId);
			while (rs.next()) {
				users.add(new Users(rs.getInt(1), rs.getString(1), rs.getString(1), rs.getString(1), rs.getString(1),
						rs.getString(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
​
		return users;
	}
}