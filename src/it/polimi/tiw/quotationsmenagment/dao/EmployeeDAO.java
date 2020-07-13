package it.polimi.tiw.quotationsmenagment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.quotationsmenagment.beans.User;

public class EmployeeDAO {
private Connection connection;
	
	public EmployeeDAO(Connection connection) {
		this.connection = connection;
	}
	
	public User checkCredentials(String username, String password) throws SQLException {
		User userBean = null;
		
		String query = "SELECT * FROM db_quotation_management.employee WHERE username = ? AND password = ?;";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, username);
			pstatement.setString(2, password);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					userBean = new User();
					userBean.setIsClient(false);
					userBean.setID(result.getInt("ID"));
					userBean.setUsername(result.getString("username"));
				}	
			}
		}
		return userBean;
	}
}
