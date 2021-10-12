package by.diomov.newsportal.controller.impl;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.diomov.newsportal.bean.News;
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

	private static final String PATH_TO_FAVOURITE_NEWS_PAGE = "/WEB-INF/jsp/favourite_news_page.jsp";
	private static final String PATH_TO_MAIN_PAGE_WITH_MESSAGE = "Controller?command=Go_To_Main_Page&message=%s";
	private static final String PATH_TO_FAVOURITE_NEWS_PAGE_WITH_PARAMETER = "Go_To_Favourite_News_Page&pageNumber=%s";
	private static final String MESSAGE_TEMPORARY_PROBLEMS = "Sorry, we're having problems.Please try again later";

	private static final String PATH = "path";
	private static final String USER = "user";
	private static final String NEWS = "newsList";
	private static final String NEWS_FAVOURITE = "favourite";
	private static final String PAGE_NUMBER = "pageNumber";
	private static final int NUMBER_NEWS_ON_THE_PAGE = 2;
	private static final boolean FAVOURITE = true;
	private static final String AMOUNT_PAGE = "amountPage";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		User user = (User) session.getAttribute(USER);

		try {

			int pageNumber = Integer.valueOf(request.getParameter(PAGE_NUMBER));
			int from = (pageNumber - 1) * NUMBER_NEWS_ON_THE_PAGE;

			List<News> newsList = newsService.getFavouritesLimitedList(user.getId(), from, NUMBER_NEWS_ON_THE_PAGE);

			int amountNews = newsService.getAmountFavouritesNewsByUserId(user.getId());
			int amountPage = amountNews / NUMBER_NEWS_ON_THE_PAGE;
			if (amountNews % NUMBER_NEWS_ON_THE_PAGE != 0) {
				amountPage++;
			}

			session.setAttribute(NEWS_FAVOURITE, FAVOURITE);
			session.setAttribute(PATH, String.format(PATH_TO_FAVOURITE_NEWS_PAGE_WITH_PARAMETER, pageNumber));
			request.setAttribute(NEWS, newsList);
			request.setAttribute(NEWS, newsList);
			request.setAttribute(AMOUNT_PAGE, amountPage);

			RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_FAVOURITE_NEWS_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			log.error("Error when trying to get favourite news from database.", e);
			response.sendRedirect(String.format(PATH_TO_MAIN_PAGE_WITH_MESSAGE, MESSAGE_TEMPORARY_PROBLEMS));
		}
	}
}
