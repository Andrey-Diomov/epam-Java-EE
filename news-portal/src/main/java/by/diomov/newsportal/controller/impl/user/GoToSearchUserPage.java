package by.diomov.newsportal.controller.impl.user;

import java.io.IOException;

import by.diomov.newsportal.controller.Command;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class GoToSearchUserPage implements Command {
	private static final String PATH_TO_SEARCH_USER_PAGE = "/WEB-INF/jsp/search_user_page.jsp";
	private static final String COMMAND = "Go_To_Search_User_Page";
	private static final String PATH = "path";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		session.setAttribute(PATH, COMMAND);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_SEARCH_USER_PAGE);
		requestDispatcher.forward(request, response);
	}
}
