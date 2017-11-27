package service;

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
import javax.servlet.http.HttpSession;

@WebFilter("/rest/*")
public class SessionFilter implements Filter {

	private static final String FACADE_ATTRIBE_NAME = "facade";

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		String requestURI = request.getRequestURI();
		
		if (requestURI.endsWith("/")) {
			requestURI = requestURI.substring(0, requestURI.length()-1);
		}
		
		if (requestURI.equals(request.getContextPath())) {
			chain.doFilter(req, res);
			return;
		}
		
		HttpSession session = request.getSession();

		String prefix = request.getContextPath() + "/rest/";

		Object facade = session.getAttribute(FACADE_ATTRIBE_NAME);
		if (facade == null) {
			if (requestURI.equals(prefix + "admin/login")) {
				session.setAttribute(FACADE_ATTRIBE_NAME, "admin");
			} else if (requestURI.equals(prefix + "companies/login")) {
				session.setAttribute(FACADE_ATTRIBE_NAME, "companies");
			} else if (requestURI.equals(prefix + "customers/login")) {
				session.setAttribute(FACADE_ATTRIBE_NAME, "customers");
			}
		}
		chain.doFilter(req, res);
	}
}
