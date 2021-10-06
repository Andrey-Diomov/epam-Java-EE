package by.diomov.newsportal.filter;

import java.io.IOException;

import by.diomov.newsportal.bean.Role;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.controller.CommandName;
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
	private static final String PATH_TO_AUTHORIZATION_PAGE = "Controller?command=Go_To_Authorization_Page";
	private static final String PATH_TO_ERROR_PAGE = "Controller?command=Unknown_Command";
	private static final String PATH_TO_MAIN_PAGE = "Controller?command=Go_To_Main_Page";
	private static final String USER_ATTRIBUTE = "user";	
	private static final String COMMAND_PARAMETER = "command";

	public void init(FilterConfig fConfig) throws ServletException {
	}

	@SuppressWarnings("incomplete-switch")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

		User user;

		if (session == null) {
			session = req.getSession(true);
			user = new User();
			user.setRole(Role.GUEST);
			session.setAttribute(USER_ATTRIBUTE, user);
			resp.sendRedirect(PATH_TO_MAIN_PAGE);
			return;
		}

		user = (User) session.getAttribute(USER_ATTRIBUTE);

		if (user == null) {
			user = new User();
			user.setRole(Role.GUEST);
			session.setAttribute(USER_ATTRIBUTE, user);
			resp.sendRedirect(PATH_TO_MAIN_PAGE);
			return;
		}

		String name = request.getParameter(COMMAND_PARAMETER);

		CommandName commandName;

		try {
			commandName = CommandName.valueOf(name.toUpperCase());

			switch (commandName) {
			case GO_TO_READ_NEWS_PAGE:
			case GO_TO_ADD_COMMENT_PAGE:
			case ADD_COMMENT:
			case GO_TO_READ_COMMENTS_PAGE:
			case GO_TO_USER_PROFILE:
			case ADD_NEWS_TO_FAVOURITE:
			case GO_TO_FAVOURITE_NEWS_PAGE:
			case DELETE_NEWS_FROM_FAVOURITE:

				if (Role.GUEST.equals(user.getRole())) {
					resp.sendRedirect(PATH_TO_AUTHORIZATION_PAGE);
					return;
				}
				break;

			case ADD_NEWS:
			case GO_TO_ADD_NEWS_PAGE:
			case UPDATE_NEWS:
			case GO_TO_UPDATE_NEWS_PAGE:
			case DELETE_NEWS:

				if (!Role.ADMIN.equals(user.getRole())) {
					user.setRole(Role.GUEST);
					session.setAttribute(USER_ATTRIBUTE, user);
					resp.sendRedirect(PATH_TO_AUTHORIZATION_PAGE);
					return;
				}
				break;

			}
		} catch (IllegalArgumentException e) {
			resp.sendRedirect(PATH_TO_ERROR_PAGE);
			return;
		}

		chain.doFilter(request, response);
	}

	public void destroy() {
	}
}

//switch (commandName) {
//case READ_NEWS, ADD_TO_FAVORITES, DELETE_FROM_FAVORITES, READ_ALL_NEWS, OPEN_PROFILE, VIEW_FAVORITE_NEWS, ADD_COMMENT, OFFER_NEWS, GO_TO_OFFER_NEWS_PAGE, VIEW_USER_OFFERED_NEWS, GO_TO_CHANGE_PASSWORD_PAGE, CHANGE_PASSWORD: {
//
//
//	if (ROLE_GUEST.equals(user.getRole())) {
//		response.sendRedirect(PATH_USER_GUEST);
//		return;
//	}
//
//	break;
//}
//case ADD_NEWS, GO_TO_ADD_NEWS_PAGE, DELETE_NEWS, EDIT_NEWS, GO_TO_EDIT_NEWS_PAGE, VIEW_OFFERED_NEWS, DENY_TO_PUBLISH, GO_TO_CHECK_OFFERED_NEWS_PAGE, APPROVE_PUBLICATION:
//
//	if (ROLE_GUEST.equals(user.getRole())) {
//		response.sendRedirect(PATH_USER_GUEST);
//		return;
//	}
//
//	if (!ROLE_ADMIN.equals(user.getRole())) {
//		response.sendRedirect(PATH_USER_NOT_ADMIN);
//		return;
//	}
//
//	break;
//
//default:
//
//{
//
//}
//
//}
//
//chain.doFilter(arg0, arg1);
//
//}

//
//package by.diomov.newsportal.filter;
//
//import java.io.IOException;
//
//import by.diomov.newsportal.bean.Role;
//import by.diomov.newsportal.bean.User;
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//public class SecurityFilter implements Filter {
//	private static final String USER_ATTRIBUTE = "user";
//	private static final String PATH_TO_INDEX_PAGE = "index.jsp";
//
//	public void init(FilterConfig fConfig) throws ServletException {
//	}
//
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse resp = (HttpServletResponse) response;
//		HttpSession session = req.getSession(false);
//
//		if (session == null) {
//			session = req.getSession(true);
//			resp.sendRedirect(PATH_TO_INDEX_PAGE);
//			return;
//		}
//
//		User user = (User) session.getAttribute(USER_ATTRIBUTE);
//
//		if (user == null) {						
//			user = new User();
//			user.setRole(Role.GUEST);
//			
//			session.setAttribute(USER_ATTRIBUTE, user);
//			resp.sendRedirect(PATH_TO_INDEX_PAGE);
//			return;
//		}
//
//		chain.doFilter(request, response);
//	}
//
//	public void destroy() {
//	}
//}