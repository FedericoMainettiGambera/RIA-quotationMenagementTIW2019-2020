package it.polimi.tiw.quotationsmenagment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import it.polimi.tiw.quotationsmenagment.beans.User;

public class ClientDAO {
	private Connection connection;
	
	public ClientDAO(Connection connection) {
		this.connection = connection;
	}
	
	public User findByID(int clientID) throws SQLException {
		User clientBean = null;
		
		String query = "SELECT * FROM db_quotation_management.client WHERE ID = ?;";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, clientID);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					clientBean = new User();
					clientBean.setID(result.getInt("ID"));
					clientBean.setIsClient(true);
					clientBean.setUsername(result.getString("username"));
					clientBean.setEmail(result.getString("email"));
				}
			} 
		}
	
		return clientBean;  //could be null
	}
	
	public User findByUsername(String username) throws SQLException {
		User clientBean = null;
		
		String query = "SELECT * FROM db_quotation_management.client WHERE username = ?;";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, username);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					clientBean = new User();
					clientBean.setID(result.getInt("ID"));
					clientBean.setIsClient(true);
					clientBean.setUsername(result.getString("username"));
				}
			} 
		}
	
		return clientBean;  //could be null
	}
	
	public User checkCredentials(String username, String password) throws SQLException {		
		User userBean = null;
		
		String query = "SELECT * FROM db_quotation_management.client WHERE username = ? AND password = ?;";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, username);
			pstatement.setString(2, password);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					userBean = new User();
					userBean.setIsClient(true);
					userBean.setID(result.getInt("ID"));
					userBean.setUsername(result.getString("username"));
				}	
			}
		}
		return userBean; //might be null
	}
	
	public User createClient(String username, String email, String password) throws SQLException{
		//start transaction for database integrity
		connection.setAutoCommit(false); //not necessary
		User userBean = null;
		String query = "INSERT into db_quotation_management.client (username, password, email) VALUES (?, ?, ?);";
		try (PreparedStatement pstatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
			
			pstatement.setString(1, username);
			pstatement.setString(2, password);
			pstatement.setString(3, email);
			pstatement.executeUpdate();
			
			userBean = new User();
			userBean.setEmail(email);
			ResultSet rs = pstatement.getGeneratedKeys();
			if (rs.next()) {
			    int ID = rs.getInt(1);
				userBean.setID(ID);
			}
			userBean.setIsClient(true);
			userBean.setUsername(username);
			connection.commit();
		} 
		catch (SQLException e) {
			connection.rollback();
			throw e;
		} 
		finally {
			connection.setAutoCommit(true);
		}
		return userBean;
	}
}
