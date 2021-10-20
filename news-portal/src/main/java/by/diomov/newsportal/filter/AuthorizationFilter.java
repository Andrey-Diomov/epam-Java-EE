package by.diomov.newsportal.filter;

import java.io.IOException;
import by.diomov.newsportal.bean.Role;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.controller.CommandName;
import by.diomov.newsportal.controller.impl.message.LocalMessage;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthorizationFilter implements Filter {
	private static final String PATH_TO_AUTHORIZATION_PAGE = "Controller?command=Go_To_Authorization_Page";
	private static final String PATH_TO_ERROR_PAGE_WITH_MESSAGE = "Controller?command=Unknown_Command&message=%s";
	private static final String PATH_TO_MAIN_PAGE_WITH_PARAMETR = "Controller?command=Go_To_Main_Page&pageNumber=1";

	private static final String USER = "user";
	private static final String COMMAND = "command";

	public void init(FilterConfig fConfig) throws ServletException {
	}

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
			session.setAttribute(USER, user);
			resp.sendRedirect(PATH_TO_MAIN_PAGE_WITH_PARAMETR);
			return;
		}

		user = (User) session.getAttribute(USER);

		if (user == null) {
			user = new User();
			user.setRole(Role.GUEST);
			session.setAttribute(USER, user);
			resp.sendRedirect(PATH_TO_MAIN_PAGE_WITH_PARAMETR);
			return;
		}

		String name = request.getParameter(COMMAND);

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
			case GO_TO_SEARCH_NEWS_PAGE:
			case SEARCH_NEWS:

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

				if (!Role.EDITOR.equals(user.getRole())) {
					user.setRole(Role.GUEST);
					session.setAttribute(USER, user);
					resp.sendRedirect(PATH_TO_AUTHORIZATION_PAGE);
					return;
				}
				break;
				
			default:
				break;
			}
		} catch (IllegalArgumentException e) {
			resp.sendRedirect(String.format(PATH_TO_ERROR_PAGE_WITH_MESSAGE, LocalMessage.TEMPORARY_PROBLEMS));
			return;
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
	}
}
