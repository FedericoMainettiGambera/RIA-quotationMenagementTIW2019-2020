package it.polimi.tiw.quotationsmenagment.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import it.polimi.tiw.quotationsmenagment.beans.User;

@WebServlet("/getEmployeeInfo")
@MultipartConfig
public class getEmployeeInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public getEmployeeInfo() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("getEmployeeInfo.doGet() just started.");
		Object employee = null;
		try {
			employee = request.getSession().getAttribute("user");
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Invalid session");
			return;
		}
		if(employee != null) {
			User employeeBean = ((User)employee);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8"); 
			response.getWriter().println(new Gson().toJson(employeeBean));
		}
		else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Session not valid");
			return;
		}
	}
}