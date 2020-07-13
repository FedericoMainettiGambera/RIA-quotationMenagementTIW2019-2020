package it.polimi.tiw.quotationsmenagment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import it.polimi.tiw.quotationsmenagment.beans.Product;
import it.polimi.tiw.quotationsmenagment.beans.Option;

public class ProductDAO {
private Connection connection;
	
	public ProductDAO(Connection connection) {
		this.connection = connection;
	}
	
	public ArrayList<Product> findAllWithOptions() throws SQLException{
		//returned object
		ArrayList<Product> products = new ArrayList<Product>();
		
		String query = "SELECT * FROM db_quotation_management.product;";
		// Query result structure:
		// ID | image | name
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			try (ResultSet result = pstatement.executeQuery();) {
				Product product;
				while (result.next()) {
					product = new Product(
							result.getString("name"),
							result.getBytes("image")
							);
					product.setID(result.getInt("ID"));
					product.setOptions(this.findAvailableOptionsForProduct(result.getInt("ID")));
					products.add(product);
				}
			}
		}
		return products; //could be empty
	}
	
	public ArrayList<Product> findAllWithoutOptions() throws SQLException{
		//returned object
		ArrayList<Product> products = new ArrayList<Product>();
		
		String query = "SELECT * FROM db_quotation_management.product;";
		// Query result structure:
		// ID | image | name
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			try (ResultSet result = pstatement.executeQuery();) {
				Product product;
				while (result.next()) {
					product = new Product(
							result.getString("name"),
							result.getBytes("image")
							);
					product.setID(result.getInt("ID"));
					products.add(product);
				}
			}
		}
		return products; //could be empty
	}
	
	public ArrayList<Option> findAvailableOptionsForProduct(int productID) throws SQLException{
		//returned object
		ArrayList<Option> availableOptions = new ArrayList<Option>();
		
		String query = "SELECT " + 
		        "    O.ID, " + 
				"	 O.type, " + 
				"    O.name " + 
				"    FROM db_quotation_management.availableoption AO " + 
				"    INNER JOIN db_quotation_management.option O " + 
				"		ON O.ID = AO.optionID " + 
				"	 WHERE productID = ?; ";
		// Query result structure:
		// ID | type | name
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, productID);
			try (ResultSet result = pstatement.executeQuery();) {
				Option option;
				while (result.next()) {
					option = new Option(
							result.getString("type"),
							result.getString("name")
							);
					option.setID(result.getInt("ID"));
					availableOptions.add(option);
				}
			}
		}
	
		return availableOptions; //could be empty
	}
	
	public byte[] findProductImage(int productID) throws SQLException {
		//returned object
		byte[] image = null;
		
		String query = "SELECT image FROM db_quotation_management.product WHERE ID = ?;";
		// Query result structure:
		// image
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, productID);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					image = result.getBytes("image");
				}
			}
		}
		return image; //could be null
	}
}
