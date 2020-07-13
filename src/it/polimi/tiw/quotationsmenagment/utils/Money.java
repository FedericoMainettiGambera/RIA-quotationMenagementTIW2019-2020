package it.polimi.tiw.quotationsmenagment.utils;

public class Money {
	private int wholePart;
	private int decimalPart;
	
	public Money(int wholePart, int decimalPart) {
		this.setWholePart(wholePart);
		this.setDecimalPart(decimalPart);
	}

	public int getWholePart() {
		return wholePart;
	}

	public void setWholePart(int wholePart) {
		this.wholePart = wholePart;
	}

	public int getDecimalPart() {
		return decimalPart;
	}

	public void setDecimalPart(int decimalPart) {
		this.decimalPart = decimalPart;
	}
	
	public String toString() {
		return wholePart + "." + decimalPart;
	}
}
