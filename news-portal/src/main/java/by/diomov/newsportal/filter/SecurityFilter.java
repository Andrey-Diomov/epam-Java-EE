package by.diomov.newsportal.filter;

import java.io.IOException;

import by.diomov.newsportal.bean.Role;
import by.diomov.newsportal.bean.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SecurityFilter implements Filter {
	private static final String USER_ATTRIBUTE = "user";
	private static final String PATH_TO_INDEX_PAGE = "index.jsp";

	public void init(FilterConfig fConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

		if (session == null) {
			session = req.getSession(true);
			resp.sendRedirect(PATH_TO_INDEX_PAGE);
			return;
		}

		User user = (User) session.getAttribute(USER_ATTRIBUTE);

		if (user == null) {						
			user = new User();
			user.setRole(Role.GUEST);
			
			session.setAttribute(USER_ATTRIBUTE, user);
			resp.sendRedirect(PATH_TO_INDEX_PAGE);
			return;
		}

		chain.doFilter(request, response);
	}

	public void destroy() {
	}
}