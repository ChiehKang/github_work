package com.chieh.dream.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "securityFilter", urlPatterns = { "/*" })
public class SecurityFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");
		
		
		String requestUri = request.getRequestURI().toString().replace("/dream/", "");
		System.out.println("requestUrl: "+request.getRequestURL().toString());
		
		if (request.getSession(false) != null) {
			filterChain.doFilter(req, res);	
		}
//		else if(requestUri.contains("app")) {
//			filterChain.doFilter(req, res);
//		}
//		else if(requestUri.contains("assets")) {
//			filterChain.doFilter(req, res);
//		}
//		else if(requestUri.equals("user/login")) {
//			filterChain.doFilter(req, res);
//		}
//		else if(requestUri.equals("index.html")) {
//			filterChain.doFilter(req, res);
//		}
		else {
//			System.out.println("requestUri: "+request.getRequestURI().toString());
			filterChain.doFilter(req, res);
//			response.sendRedirect("/dream/index.html");
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
