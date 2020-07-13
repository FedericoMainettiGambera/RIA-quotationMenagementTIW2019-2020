package it.polimi.tiw.quotationsmenagment.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.quotationsmenagment.beans.Option;
import it.polimi.tiw.quotationsmenagment.beans.User;
import it.polimi.tiw.quotationsmenagment.dao.ProductDAO;
import it.polimi.tiw.quotationsmenagment.dao.QuotationDAO;
import it.polimi.tiw.quotationsmenagment.utils.ConnectionHandler;

@WebServlet("/CreateQuotation")
@MultipartConfig
public class createQuotation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

    public createQuotation() {
        super();
    }

    public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("CreateQuotation.doGot() just started.");
		
		Integer productID = null;
		try {
			productID = Integer.parseInt(request.getParameter("productID"));
		} catch (NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Invalid parameters");
			return;
		}
		
		//extracting available options for the product
		ProductDAO productDAO = new ProductDAO(connection);
		ArrayList<Option> availableOptions= null;
		try {
			availableOptions = productDAO.findAvailableOptionsForProduct(productID);
		} catch (SQLException e1) {
			e1.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Invalid parameters");
			return;
		}
		ArrayList<Integer> availableOptionsID = new ArrayList<Integer>();
		for(int i = 0; i< availableOptions.size(); i++) {
			availableOptionsID.add(availableOptions.get(i).getID());
		}
		
		Map<String, String[]> parametersMap = request.getParameterMap();
		
		ArrayList<Integer> optionsID = new ArrayList<Integer>();
		
		for (Map.Entry<String, String[]> entry : parametersMap.entrySet()) {
		    if(entry.getKey().startsWith("option")) {
		    	Integer currentOptionSelectedID = null;
				try {
					currentOptionSelectedID = Integer.parseInt(entry.getValue()[0]);
					//checking if option is one of the available
					if(!availableOptionsID.contains(currentOptionSelectedID)) {
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						response.getWriter().println("Invalid parameters");
						return;
					}
				} catch (NumberFormatException | NullPointerException e) {
					e.printStackTrace();
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().println("Invalid parameters");
					return;
				}
		    	optionsID.add(currentOptionSelectedID);
		    }
		}
		
		if(optionsID.size() > 0) {
			QuotationDAO quotationDAO = new QuotationDAO(connection);
			try {
				quotationDAO.createQuotation(productID, ((User)request.getSession().getAttribute("user")).getID(), optionsID);
			} catch (SQLException e) {
				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("Internal server error, retry later.");
				return;
			}
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Invalid parameters, at least one option selected");
			return;
		}
		
	}

	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
