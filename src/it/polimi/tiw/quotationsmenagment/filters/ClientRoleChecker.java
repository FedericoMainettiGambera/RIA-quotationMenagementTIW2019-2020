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

import it.polimi.tiw.quotationsmenagment.beans.User;

@WebFilter("/ClientRoleChecker")
public class ClientRoleChecker implements Filter {

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		System.out.println("ClientRoleChecker.doFilter() just started. [must be executed after LoginChecker filter]");
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
				
		if(((User)request.getSession().getAttribute("user")).isClient()) {
			chain.doFilter(req, res);
			return;
		}
		else {
			String path = "/RIA-quotationMenagementTIW2019-2020/LogIn.html"; //TODO should move project root to Tomcat root
			response.sendRedirect(path);
			return;
		}
	}

}
