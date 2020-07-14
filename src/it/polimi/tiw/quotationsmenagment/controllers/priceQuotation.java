package it.polimi.tiw.quotationsmenagment.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.quotationsmenagment.beans.Quotation;
import it.polimi.tiw.quotationsmenagment.beans.User;
import it.polimi.tiw.quotationsmenagment.dao.QuotationDAO;
import it.polimi.tiw.quotationsmenagment.utils.ConnectionHandler;
import it.polimi.tiw.quotationsmenagment.utils.Money;

@WebServlet("/PriceQuotation")
public class priceQuotation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

    public priceQuotation() {
        super();
    }

    public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}
    

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("PriceQuotation.doGet() just started.");
		
		Integer wholePart;
		Integer decimalPart;
		Integer quotationID;
		try {
			quotationID = Integer.parseInt(request.getParameter("quotationID"));			
			
			wholePart = Integer.parseInt(request.getParameter("wholePart"));
			
			String toParse = request.getParameter("decimalPart");
			if(toParse.length()>2) {
				toParse = "" + toParse.charAt(0) + toParse.charAt(1);
			}
			
			if(toParse.length()==1) {
				toParse = toParse + "0";
			}
			decimalPart = Integer.parseInt(toParse);
		}
		catch (NumberFormatException | NullPointerException e){
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Invalid parameters");
			return;
		}
		
		if(decimalPart >= 100 || decimalPart < 0 || wholePart <= 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Invalid parameters");
			return;
		}
		
		Money price = new Money(wholePart,decimalPart);
		
		QuotationDAO quotationDAO = new QuotationDAO(connection);
		Quotation quotation = null;
		try {
			quotation = quotationDAO.findByID(quotationID);
		} catch (SQLException e1) {
			e1.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Internal server error, retry later.");
			return;
		}
		if(quotation == null || quotation.getPrice() != null) { //checking if quotations exists and is not already priced
			//TODO maybe should redirect to the priceQuotationPage... or to the home..
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Invalid parameters");
			return;
		}
		try {
			quotationDAO.priceQuotation(quotationID, price, ((User)request.getSession().getAttribute("user")).getID());
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Internal server error.");
			return;
		}
		
		response.setStatus(HttpServletResponse.SC_OK);
		
	}

	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
