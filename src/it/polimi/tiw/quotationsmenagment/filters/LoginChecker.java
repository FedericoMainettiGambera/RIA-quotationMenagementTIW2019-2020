package it.polimi.tiw.quotationsmenagment.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.quotationsmenagment.beans.User;

@WebFilter("/LoginChecker")
public class LoginChecker implements Filter {
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		System.out.println("LoginChecker.doFilter() just started.");
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		//System.out.println("Servlet path is " + request.getServletPath());
		HttpSession session = request.getSession(true);
		if(session.isNew() || session.getAttribute("user") == null) { //user not logged in
			//System.out.println("User has no active session or hasn't logged in.");
			if(request.getServletPath().contains("LogIn.html")) { //TODOOOOOOOOOOOO CHANGE CHANGE CHANGE CONTAINS TO IS EQUAL !!!
				//System.out.println("User is trying to access log in page: access granted.");
				chain.doFilter(req, res);
				return;
			}
			else {
				if(request.getServletPath().contains("CheckLogin")) { //TODOOOOOOOOOOOO CHANGE CHANGE CHANGE CONTAINS TO IS EQUAL !!!
					//System.out.println("User is trying to log in using servlet CheckLogin: acces granted");
					chain.doFilter(req, res);
					return;
				}
				else {
					//System.out.println("User is trying to access something different from log in page or CheckLogin servlet: access denied.");
					String path = "/RIA-quotationMenagementTIW2019-2020/LogIn.html";
					response.sendRedirect(path);
					return;
				}
			}
		}
		else { //user already logged in
			//System.out.println("User has an active session and is logged in.");
			//System.out.println("Checking if user is trying to log in twice.");
			if(request.getServletPath().contains("/LogIn.html") || request.getServletPath().contains("/CheckLogin")) {
				//System.out.println("User is trying to access LoginPage and has an active session: " + ((User)session.getAttribute("user")).toString());
				if (((User)session.getAttribute("user")).isClient()) { //checking role
					//System.out.println("User is a client, redirecting to GoToClientHomePage");
					String path = "/RIA-quotationMenagementTIW2019-2020/ClientHome.html"; //TODO should move project root to Tomcat root
					response.sendRedirect(path);
					return;
				}
				else {
					//System.out.println("User is an employee, redirecting to GoToEmployeeHomePage");
					String path = "/RIA-quotationMenagementTIW2019-2020/EmployeeHome.html"; //TODO should move project root to Tomcat root
					response.sendRedirect(path);
					return;
				}
			}
			else {
				//System.out.println("User is not trying to log in twice.");
				chain.doFilter(req, res);
				return;
			}
		}

	}
}
