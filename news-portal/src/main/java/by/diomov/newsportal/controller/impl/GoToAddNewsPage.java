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

public class GoToAddNewsPage implements Command {
	private static final String PATH_TO_ADD_NEWS_PAGE = "/WEB-INF/jsp/add_news_page.jsp";
	private static final String PATH_TO_AUTHORIZATION_PAGE = "Controller?command=Go_To_Authorization_Page";

	private static final String COMMAND = "Go_To_Add_News_Page";
	private static final String PATH_ATTRIBUTE = "path";
	private static final String USER_ATTRIBUTE = "user";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		User user = (User) session.getAttribute(USER_ATTRIBUTE);

//		if (!Role.ADMIN.equals(user.getRole())) {
//			user.setRole(Role.GUEST);
//			session.setAttribute(USER_ATTRIBUTE, user);
//			response.sendRedirect(PATH_TO_AUTHORIZATION_PAGE);
//			return;
//		}
		session.setAttribute(PATH_ATTRIBUTE, COMMAND);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_ADD_NEWS_PAGE);
		requestDispatcher.forward(request, response);
	}
}
