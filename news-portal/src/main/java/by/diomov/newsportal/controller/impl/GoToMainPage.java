package by.diomov.newsportal.controller.impl;

import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.bean.News;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.service.NewsService;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.ServiceProvider;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class GoToMainPage implements Command {
	private static final Logger log = LogManager.getLogger(GoToMainPage.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final NewsService newsService = provider.getNewsService();

	private static final String PATH_TO_MAIN_PAGE = "/WEB-INF/jsp/main_page.jsp";
	private static final String PATH_TO_ERROR_PAGE_ = "/WEB-INF/jsp/error_page.jsp";

	private static final String COMMAND = "Go_To_Main_Page";
	private static final String PATH_ATTRIBUTE = "path";
	private static final String NEWS_ATTRIBUTE = "newsList";
	private static final String NEWS_FAVOURITE_ATTRIBUTE = "favourite";
	private static final boolean FAVOURITE = false;

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		try {
			List<News> newsList = newsService.getAll();
			request.setAttribute(NEWS_ATTRIBUTE, newsList);

			session.setAttribute(PATH_ATTRIBUTE, COMMAND);
			session.setAttribute(NEWS_FAVOURITE_ATTRIBUTE, FAVOURITE);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_MAIN_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			log.error("Error when trying to get list of news from database.", e);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_ERROR_PAGE_);
			requestDispatcher.forward(request, response);
		}
	}
}