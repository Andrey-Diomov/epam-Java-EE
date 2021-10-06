package by.diomov.newsportal.controller.impl;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.diomov.newsportal.bean.News;
import by.diomov.newsportal.bean.Role;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.service.NewsService;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.ServiceProvider;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class GoToFavouriteNewsPage implements Command {
	private static final Logger log = LogManager.getLogger(GoToFavouriteNewsPage.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final NewsService newsService = provider.getNewsService();

	private static final String PATH_TO_AUTHORIZATION_PAGE = "Controller?command=Go_To_Authorization_Page";
	private static final String PATH_TO_FAVOURITE_NEWS_PAGE = "/WEB-INF/jsp/favourite_news_page.jsp";
	private static final String PATH_TO_MAIN_PAGE_WITH_MESSAGE = "Controller?command=Go_To_Main_Page&message=%s";

	private static final String MESSAGE_TEMPORARY_PROBLEMS = "Sorry, we're having problems.Please try again later";
	private static final String COMMAND = "Go_To_Favourite_News_Page";
	private static final String PATH_ATTRIBUTE = "path";
	private static final String USER_ATTRIBUTE = "user";
	private static final String NEWS_ATTRIBUTE = "newsList";
	private static final String NEWS_FAVOURITE_ATTRIBUTE = "favourite";
	private static final boolean FAVOURITE = true;

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		User user = (User) session.getAttribute(USER_ATTRIBUTE);

//		if (Role.GUEST.equals(user.getRole())) {
//			response.sendRedirect(PATH_TO_AUTHORIZATION_PAGE);
//			return;
//		}
		
		try {			
			List<News> newsList = newsService.getFavourite(user.getId());			
			request.setAttribute(NEWS_ATTRIBUTE, newsList);
			
			
			session.setAttribute(PATH_ATTRIBUTE, COMMAND);
			session.setAttribute(NEWS_FAVOURITE_ATTRIBUTE, FAVOURITE);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_FAVOURITE_NEWS_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			log.error("Error when trying to get favourite news from database.", e);
			response.sendRedirect(String.format(PATH_TO_MAIN_PAGE_WITH_MESSAGE, MESSAGE_TEMPORARY_PROBLEMS));
		}
	}
}
