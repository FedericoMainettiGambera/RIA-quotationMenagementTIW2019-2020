package it.polimi.tiw.quotationsmenagment.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import it.polimi.tiw.quotationsmenagment.beans.User;

@WebServlet("/getClientInfo")
public class getClientInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public getClientInfo() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("getClientInfo.doGet() just started.");
		
		Object client = null;
		try {
			client = request.getSession().getAttribute("user");
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Invalid session");
			return;
		}
		if(client != null) {
			User clientBean = ((User)client);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8"); 
			response.getWriter().println(new Gson().toJson(clientBean));
		}
		else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Session not valid");
			return;
		}
	}
	
}
