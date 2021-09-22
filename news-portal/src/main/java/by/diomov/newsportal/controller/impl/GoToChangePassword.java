package by.diomov.newsportal.controller.impl;

import java.io.IOException;
import by.diomov.newsportal.bean.Role;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.controller.Command;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class GoToChangePassword implements Command {
	private static final String PATH_TO_AUTHORIZATION_PAGE = "Controller?command=Go_To_Authorization_Page";
	private static final String PATH_TO_CHANGE_PASSWORD_PAGE = "/WEB-INF/jsp/change_password_page.jsp";
	
	private static final String PATH_ATTRIBUTE = "path";
	private static final String COMMAND = "Go_To_Change_Password";
	private static final String USER_ATTRIBUTE = "user";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		if (session == null) {
			response.sendRedirect(PATH_TO_AUTHORIZATION_PAGE);
			return;
		}

		User user = (User) session.getAttribute(USER_ATTRIBUTE);

		if (user == null) {
			response.sendRedirect(PATH_TO_AUTHORIZATION_PAGE);
			return;
		}

		if (Role.GUEST.equals(user.getRole())) {
			session.removeAttribute(USER_ATTRIBUTE);
			response.sendRedirect(PATH_TO_AUTHORIZATION_PAGE);
			return;
		}
		session.setAttribute(PATH_ATTRIBUTE, COMMAND);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_CHANGE_PASSWORD_PAGE);
		requestDispatcher.forward(request, response);
	}
}
