package it.polimi.tiw.quotationsmenagment.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import it.polimi.tiw.quotationsmenagment.beans.Quotation;
import it.polimi.tiw.quotationsmenagment.beans.User;
import it.polimi.tiw.quotationsmenagment.dao.QuotationDAO;
import it.polimi.tiw.quotationsmenagment.utils.ConnectionHandler;


@WebServlet("/getManagedQuotations")
public class getManagedQuotations extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	
    public getManagedQuotations() {
        super();
    }

    public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("getQuotations.doGet() just started.");
		QuotationDAO quotationDAO = new QuotationDAO(connection);
		
		try {
			User employeeBean = ((User)request.getSession().getAttribute("user"));
			if(employeeBean != null) {
				ArrayList<Quotation> quotations = quotationDAO.findAllByEmplyeeID(employeeBean.getID());
				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().println(new Gson().toJson(quotations));
			}
			else {  
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().println("Session not valid");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Internal server error, retry later.");
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
