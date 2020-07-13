package it.polimi.tiw.quotationsmenagment.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.regex.*;

import it.polimi.tiw.quotationsmenagment.beans.User;
import it.polimi.tiw.quotationsmenagment.dao.ClientDAO;
import it.polimi.tiw.quotationsmenagment.utils.ConnectionHandler;

@WebServlet("/SignUp")
@MultipartConfig
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
   
    public SignUp() {
        super();
    }
    
    public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("SignUp.doPost() just started.");
		String email = null;
		String usrn = null;
		String pwd = null;
		String repeatpwd = null;
		try {
			usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("password"));
			email = StringEscapeUtils.escapeJava(request.getParameter("email"));
			repeatpwd = StringEscapeUtils.escapeJava(request.getParameter("repeatPassword"));
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			response.sendError(505, "Invalid parameters");
			return;
		}
		//parameters not null or empty
		if(email == null || email.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("mailError:email must be not null");
			return;
		}
		if( usrn == null || usrn.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("usernameError:Username must be not null");
			return;
		}
		if( pwd == null || pwd.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("passwordError:Password must be not null");
			return;
		}
		if( repeatpwd == null || repeatpwd.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("repeatPasswordError:Repeat password must be not null");
			return;
		}

		//email vlaidation
		String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		if(!matcher.matches()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("mailError:email format not accepted.");
			return;
		}
		if(email.length() > 100) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("mailError:no more than 100 characters");
			return;
		}
		
		if(usrn.length() > 40) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("usernameError:no more than 40 characters");
			return;
		}
		
		//password e repeat password validation
		if(!pwd.equals(repeatpwd)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("repeatPasswordError:Password and repeat password don't match.");
			return;
		}
		if(pwd.length() > 40) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("repeatPasswordError:no more than 40 characters");
			return;
		}
		if(repeatpwd.length() > 40) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("repeatPasswordError:no more than 40 characters");
			return;
		}
		
		//checking uniqueness of username
		ClientDAO clientDAO = new ClientDAO(connection);
		try {
			if(clientDAO.findByUsername(usrn) != null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("usernameError:Selected username is already taken.");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Internal server error, retry later.");
			return;
		}
		
		//all validation successful, creating new account
		User userBean = null;
		try {
			userBean = clientDAO.createClient(usrn, email, pwd);
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Internal server error, retry later.");
			return;
		}
		
		request.getSession().setAttribute("user", userBean);
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
