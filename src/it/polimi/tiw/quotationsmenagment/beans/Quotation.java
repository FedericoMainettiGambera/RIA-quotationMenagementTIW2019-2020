package it.polimi.tiw.quotationsmenagment.beans;

import it.polimi.tiw.quotationsmenagment.utils.Money;

public class Quotation {
	private int ID;
	//negative price means no price; also price is considered in cents in Java and in DB, 
	//only in the view is trasformed in a decimal value.
	private Money price; 
	private String employeeUsername;
	private String clientUsername;
	private String email;
	private Product product;
	
	public Quotation() {
		this.price = null;
	}
	
	public Quotation( String productName, byte[] img, Money price, String clientUsername ) {
		this.setProduct(new Product(productName, img));
		this.price = price;
		this.clientUsername = clientUsername;
	}
	
	public Quotation( Product product, Money price, String clientUsername) {
		this.setProduct(product);
		this.price = price;
		this.clientUsername = clientUsername;
	}
	
	public void setPrice(Money price) {
		this.price = price;
	}
	public Money getPrice() {
		return this.price;
	}
	
	public void setClientUsername(String clientUsername) {
		this.clientUsername = clientUsername;
	}
	public String getClientUsername() {
		return this.clientUsername;
	}

	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	public String getEmployeeUsername() {
		return employeeUsername;
	}

	public void setEmployeeUsername(String employeeUsername) {
		this.employeeUsername = employeeUsername;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public String toString() {
		return "Quotation: [ID: " + this.ID + ", clientUsername: " + this.clientUsername + ", employeeUsername:" 
									  + this.employeeUsername + ", price: " + this.price + "]"
						+ "\n -" + this.product.toString();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
