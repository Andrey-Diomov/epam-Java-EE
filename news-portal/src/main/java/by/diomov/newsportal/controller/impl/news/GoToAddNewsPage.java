package by.diomov.newsportal.controller.impl.news;

import java.io.IOException;
import by.diomov.newsportal.controller.Command;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class GoToAddNewsPage implements Command {
	private static final String PATH_TO_ADD_NEWS_PAGE = "/WEB-INF/jsp/add_news_page.jsp";
	
	private static final String COMMAND = "Go_To_Add_News_Page";
	private static final String PATH = "path";
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		session.setAttribute(PATH, COMMAND);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_ADD_NEWS_PAGE);
		requestDispatcher.forward(request, response);
	}
}
