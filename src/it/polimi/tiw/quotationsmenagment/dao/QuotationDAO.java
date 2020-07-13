package it.polimi.tiw.quotationsmenagment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import it.polimi.tiw.quotationsmenagment.beans.Option;
import it.polimi.tiw.quotationsmenagment.beans.Product;
import it.polimi.tiw.quotationsmenagment.beans.Quotation;
import it.polimi.tiw.quotationsmenagment.utils.Money;

public class QuotationDAO {
	private Connection connection;
	
	public QuotationDAO(Connection connection) {
		this.connection = connection;
	}
	
	public ArrayList<Quotation> findAllByClientID(int clientID) throws SQLException {
		//returned object
		ArrayList<Quotation> quotations = new ArrayList<Quotation>();
		
		String query = "SELECT " +
				"   Q.ID AS quotationID, " +
				"   Q.wholePart, " +
				"   Q.decimalPart, " +
				"	C.username AS clientusername, " + 
				"	P.name AS productname, " +
				"   P.ID AS productID" +
				"		FROM db_quotation_management.quotation Q " + 
				"		INNER JOIN db_quotation_management.client C " + 
				"			ON Q.clientID = C.ID " + 
				"		INNER JOIN db_quotation_management.product P " + 
				"			ON Q.productID = P.ID " +
				"		WHERE Q.clientID = ?; ";
		
		// Query result structure:
		// quotationID | wholePart | decimalPart | clientusername | productname | productID 
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, clientID);
			try (ResultSet result = pstatement.executeQuery();) {
				
				Quotation quotationBean;
				
				//parsing the result
				while (result.next()) { //loops on all the different quotations made by the client
					quotationBean = new Quotation();
					//quotation ID
					quotationBean.setID(result.getInt("quotationID"));
					//price
					Money price = new Money (result.getInt("wholePart"), result.getInt("decimalPart"));
					if(!result.wasNull()) {
						quotationBean.setPrice(price);
					}
					//client username
					quotationBean.setClientUsername(result.getString("clientusername"));
					//product selected
					quotationBean.setProduct(new Product(
							result.getInt("productID"),
							result.getString("productname")
							));
					//options selected for the product
					quotationBean.getProduct().setOptions(this.getOptionsSelected(result.getInt("quotationID")));
					
					quotations.add(quotationBean);
				}
			}
		}
		
		return quotations; //might be empty
		
		/*OLD VERSION - single query, the returned value is the exact same, but the process is a little bit harder and complex:
		-----------------------------------------------------------------------------------------------------------------------
		//returned object
		ArrayList<Quotation> quotations = new ArrayList<Quotation>();
		
		String query = "SELECT " +
				"   Q.ID AS quotationID, " +
				"   Q.price, " + 
				"	C.username AS clientusername, " + 
				"	P.name AS productname, " + 
				"	P.image, " + 
				"   O.name AS optionname, " + 
				"   O.type " + 
				"		FROM db_quotation_management.quotation Q " + 
				"		INNER JOIN db_quotation_management.client C " + 
				"			ON Q.clientID = C.ID " + 
				"		INNER JOIN db_quotation_management.product P " + 
				"			ON Q.productID = P.ID " +
				"		WHERE Q.clientID = ?; ";
		
		// Query result structure:
		// quotationID | price | clientusername | productname | image | optionname | type
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, clientID);
			try (ResultSet result = pstatement.executeQuery();) {
				
				Quotation quotationBean;
				ArrayList<Option> options;
				
				//parsing the result
				while (result.next()) { //loops on all the different quotations made by the client
					quotationBean = new Quotation();
					//price
					int price = result.getInt("price");
					if(!result.wasNull()) {
						quotationBean.setPrice(price);
					}
					//client username
					quotationBean.setClientUsername(result.getString("username"));
					//product selected
					quotationBean.setProduct(new Product(
							result.getString("productname"),
							result.getBytes("image")
							));
					//options selected for the product
					options = new ArrayList<Option>();
					//first option
					options.add(new Option(
							result.getString("type"),
							result.getString("optionname")
							));
					int currentQuotationID = result.getInt("quotationID");
					while(result.next() && (result.getInt("quotationID") == currentQuotationID)) {//loops on all the options for the current quotation
						options.add(new Option(
								result.getString("type"),
								result.getString("optionname")
								));
					}
					quotationBean.setOptions(options);
					
					quotations.add(quotationBean);
					
					if(result.isAfterLast()) {
						break;
					}
				}
			}
		}
		
		return quotations; //might be empty
		-----------------------------------------------------------------------------------------------------------------------
		*/
	}
	
	public ArrayList<Quotation> findAllByEmplyeeID(int emplyeeID) throws SQLException {		
		//returned object
		ArrayList<Quotation> quotations = new ArrayList<Quotation>();
		
		String query = "SELECT" + 
				"	Q.ID AS quotationID, " + 
				"	Q.wholePart, " +
				"   Q.decimalPart, " +
				"	C.username AS clientusername, " + 
				"	P.name AS productname, " + 
				"   P.ID AS productID, " +
				"   E.username AS employeeusername " +
				"		FROM db_quotation_management.quotation Q " + 
				"        INNER JOIN db_quotation_management.client C " + 
				"			ON Q.clientID = C.ID " + 
				"        INNER JOIN db_quotation_management.product P " + 
				"			ON Q.productID = P.ID " + 
				"		INNER JOIN (db_quotation_management.management M "
				+ "                        INNER JOIN db_quotation_management.employee E "
				+ "                               ON M.employeeID = E.ID) " + 
				"			ON Q.ID = M.quotationID " + 
				"		WHERE E.ID = ?;";
		
		// Query result structure:
		// quotationID | employeeusername | wholePart | decimalPart | clientusername | productID | productname 
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, emplyeeID);
			try (ResultSet result = pstatement.executeQuery();) {
				
				Quotation quotationBean;
				
				//parsing the result
				while (result.next()) { //loops on all the different quotations managed by the employee
					quotationBean = new Quotation();
					//quotation ID
					quotationBean.setID(result.getInt("quotationID"));
					//employee username
					quotationBean.setEmployeeUsername(result.getString("employeeusername"));
					//price
					quotationBean.setPrice(new Money (result.getInt("wholePart"), result.getInt("decimalPart")));
					//client username
					quotationBean.setClientUsername(result.getString("clientusername"));
					//product selected
					quotationBean.setProduct(new Product(
							result.getInt("productID"),
							result.getString("productname")
							));
					//options selected for the product
					quotationBean.getProduct().setOptions(this.getOptionsSelected(result.getInt("quotationID")));
					
					quotations.add(quotationBean);
					
					if(result.isAfterLast()) {
						break;
					}
				}
			}
		}
		
		return quotations; //might be empty
		
		/*OLD VERSION - single query, the returned value is the exact same, but the process is a little bit harder and complex:
		-----------------------------------------------------------------------------------------------------------------------
		//returned object
		ArrayList<Quotation> quotations = new ArrayList<Quotation>();
		
		String query = "SELECT" + 
				"	Q.ID AS quotationID, " + 
				"	Q.price, " + 
				"	C.username AS clientusername, " + 
				"	P.name AS productname, " + 
				"	P.image, " + 
				"   O.name as optionname, " + 
				"   O.type " + 
				"   E.username AS employeeusername " +
				"		FROM db_quotation_management.quotation Q " + 
				"        INNER JOIN db_quotation_management.client C " + 
				"			ON Q.clientID = C.ID " + 
				"        INNER JOIN db_quotation_management.product P " + 
				"			ON Q.productID = P.ID " + 
				"		INNER JOIN (db_quotation_management.selectedoption SO INNER JOIN db_quotation_management.option O ON SO.optionID = O.ID) " + 
				"			ON Q.ID = SO.quotationID " + 
				"		INNER JOIN (db_quotation_management.management M INNER JOIN db_quotation_management.employee E ON M.employeeID = E.ID) " + 
				"			ON Q.ID = M.quotationID " + 
				"		WHERE E.ID = ?;";
		
		// Query result structure:
		// quotationID | emplyeeusername | price | clientusername | productname | image | optionname | type
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, emplyeeID);
			try (ResultSet result = pstatement.executeQuery();) {
				
				Quotation quotationBean;
				ArrayList<Option> options;
				
				//parsing the result
				while (result.next()) { //loops on all the different quotations managed by the emplyee
					quotationBean = new Quotation();
					//employee username
					quotationBean.setEmplyeeUsername(result.getString("emplyeeusername"));
					//price
					quotationBean.setPrice(result.getInt("price"));
					//client username
					quotationBean.setClientUsername(result.getString("username"));
					//product selected
					quotationBean.setProduct(new Product(
							result.getString("productname"),
							result.getBytes("image")
							));
					//options selected for the product
					options = new ArrayList<Option>();
					//first option
					options.add(new Option(
							result.getString("type"),
							result.getString("optionname")
							));
					int currentQuotationID = result.getInt("quotationID");
					while(result.next() && (result.getInt("quotationID") == currentQuotationID)) {//loops on all the options for the current quotation
						options.add(new Option(
								result.getString("type"),
								result.getString("optionname")
								));
					}
					quotationBean.setOptions(options);
					
					quotations.add(quotationBean);
					
					if(result.isAfterLast()) {
						break;
					}
				}
			}
		}
		
		return quotations; //might be empty
		-----------------------------------------------------------------------------------------------------------------------
		*/
	}
	
	public void createQuotation(int productID, int clientID, ArrayList<Integer> optionsID ) throws SQLException {
		//start transaction for database integrity
		connection.setAutoCommit(false);
		
		String query = "INSERT into db_quotation_management.quotation (productID, clientID) VALUES (?, ?);";
		try (PreparedStatement pstatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
			
			pstatement.setInt(1, productID);
			pstatement.setInt(2, clientID);
			pstatement.executeUpdate();
			
			query = "INSERT into db_quotation_management.selectedoption (optionID, quotationID) VALUES (?, LAST_INSERT_ID());";
			try (PreparedStatement pstatement1 = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
				for(int i = 0; i < optionsID.size(); i++) {
					pstatement1.setInt(1, optionsID.get(i));
					pstatement1.executeUpdate();
				}
			}
			connection.commit();
		} 
		catch (SQLException e) {
			connection.rollback();
			throw e;
		} 
		finally {
			connection.setAutoCommit(true);
		}
		
		return;
	}
	
	
	public void priceQuotation(int quotationID, Money price, int employeeID) throws SQLException {
		//start transaction for database integrity
		connection.setAutoCommit(false); 	
		String query = "UPDATE db_quotation_management.quotation SET wholePart = ?, decimalPart = ? WHERE (ID = ? and decimalPart IS NULL);";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, price.getWholePart());
			pstatement.setInt(2, price.getDecimalPart());
			pstatement.setInt(3, quotationID);
			int matchedRows = pstatement.executeUpdate();
			if(matchedRows == 0){
				//quotation has already been priced, so you can't reprice it.
				throw new SQLException("quotation has already been priced");
			}
			
			query = "INSERT into db_quotation_management.management (employeeID, quotationID) VALUES (?, ?);";
			try (PreparedStatement pstatement1 = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
				pstatement1.setInt(1, employeeID);
				pstatement1.setInt(2, quotationID);
				pstatement1.executeUpdate();
			}
			connection.commit();
		} 
		catch (SQLException e) {
			connection.rollback();
			throw e;
		} 
		finally {
			connection.setAutoCommit(true);
		}
	}
	
	public ArrayList<Quotation> findAllNotManaged() throws SQLException{		
		//returned object
		ArrayList<Quotation> quotations = new ArrayList<Quotation>();
			
		//extract all quotations where price is NULL
		String query = "SELECT" + 
				"	Q.ID AS quotationID, " +
				"	C.username AS clientusername, " + 
				"   C.email AS email," +
				"   P.ID AS productID, " +
				"	P.name AS productname " +  
				"		FROM db_quotation_management.quotation Q " + 
				"		INNER JOIN db_quotation_management.client C " + 
				"			ON Q.clientID = C.ID " + 
				"		INNER JOIN db_quotation_management.product P" + 
				"			ON Q.productID = P.ID " + 
				"		WHERE Q.wholePart IS NULL; ";
		
		// Query result structure:
		// quotationID | clientusername | email | productID | productname
				
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			try (ResultSet result = pstatement.executeQuery();) {
				
				Quotation quotationBean;
				
				//parsing the result
				while (result.next()) { //loops on all the quotations where price is NULL
					quotationBean = new Quotation();
					//quotation ID
					quotationBean.setID(result.getInt("quotationID"));
					//client username
					quotationBean.setClientUsername(result.getString("clientusername"));
					//client email
					quotationBean.setEmail(result.getString("email"));
					//product selected
					quotationBean.setProduct(new Product(
							result.getInt("productID"),
							result.getString("productname")
							));
										
					quotationBean.getProduct().setOptions(this.getOptionsSelected(result.getInt("quotationID")));
					
					quotations.add(quotationBean);
				}
			}
		}
		return quotations; //could be empty
	}
	
	public Quotation findByID(int quotationID) throws SQLException {
		Quotation quotation = null;
		
		String query = "SELECT " + 
				"	Q.ID AS quotationID, " + 
				"   Q.wholePart, " +
				"   Q.decimalPart, " +
				"   C.username AS clientusername, " + 
				"   P.ID AS productID, " +
				"   P.name AS productname " + 
				"   FROM db_quotation_management.quotation Q " + 
				"	INNER JOIN db_quotation_management.client C " + 
				"		ON Q.clientID = C.ID " + 
				"	INNER JOIN db_quotation_management.product P " + 
				"		ON Q.productID = P.ID " + 
				"	WHERE Q.ID = ?;";
		// Query result structure:
		// quotationID | wholePart | decimalPart | clientusername | productID | productname
						
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1,quotationID);
			try (ResultSet result = pstatement.executeQuery();) {
				
				pstatement.setInt(1, quotationID);
				
				//parsing the result
				if (result.next()) {
					quotation = new Quotation();
					//quotation ID
					quotation.setID(result.getInt("quotationID"));
					//price
					Money price = new Money(result.getInt("wholePart"), result.getInt("decimalPart"));
					if(!result.wasNull()) {
						quotation.setPrice(price);
					}
					else {
						quotation.setPrice(null);
					}
					//client username
					quotation.setClientUsername(result.getString("clientusername"));
					//product selected
					quotation.setProduct(new Product(
							result.getInt("productID"),
							result.getString("productname")
							));
										
					quotation.getProduct().setOptions(this.getOptionsSelected(quotationID));
				}
			}
		}
		return quotation; //could be null
	}
	
	public ArrayList<Option> getOptionsSelected(int quotationID) throws SQLException {
		//returned object
		ArrayList<Option> options = new ArrayList<Option>();
		
		String query = "SELECT " + 
				"	O.name, " + 
				"    O.type " + 
				"    FROM db_quotation_management.option AS O " + 
				"    INNER JOIN db_quotation_management.selectedoption SO " + 
				"		ON O.ID = SO.optionID " + 
				"	WHERE SO.quotationID = ?;";
		
		// Query result structure:
		// name | type
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			
			pstatement.setInt(1, quotationID);
			
			try (ResultSet result = pstatement.executeQuery();) {
				
				Option option;
				
				//parsing the result
				while (result.next()) { //loops on all the options found, must enter at least one times because of how the database is structured
					option = new Option();
					
					//option name
					option.setName(result.getString("name"));
					//option type
					option.setType(result.getString("type"));
					
					options.add(option);
				}
			}
		}
		
		return options; //must contain at least one option, because of how the database is structured
	}
	
}
