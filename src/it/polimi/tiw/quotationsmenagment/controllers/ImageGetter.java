package it.polimi.tiw.quotationsmenagment.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.tiw.quotationsmenagment.dao.ProductDAO;
import it.polimi.tiw.quotationsmenagment.utils.ConnectionHandler;


@WebServlet("/Image")
public class ImageGetter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;

	
    public ImageGetter() {
        super();
    }

    public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ImageGetter.doGet() just started.");
		
		Integer productID = null;
		try {
			productID = Integer.parseInt(request.getParameter("productID"));
		} catch (NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			response.sendError(505, "Invalid parameters");
			return;
		}
		
		ProductDAO productDAO = new ProductDAO(connection);
		byte[] content = null;
		try {
			content = productDAO.findProductImage(productID);
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(505, "Internal server error");
			return;
		}
		if(content == null) {
			System.out.println("Image not found.");
			//TODO - maybe should just send back a "sample" image indicating image is missing...
			response.sendError(505, "Internal server error");
			return;
		}
		response.setContentType("image/jpeg");
        response.setContentLength(content.length);
        response.getOutputStream().write(content);
	}
	
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
