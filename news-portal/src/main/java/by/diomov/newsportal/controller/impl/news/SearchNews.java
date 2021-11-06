package by.diomov.newsportal.controller.impl.news;

import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.bean.News;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.controller.impl.message.LocalMessage;
import by.diomov.newsportal.service.NewsService;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.ServiceProvider;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SearchNews implements Command {

	private static final Logger log = LogManager.getLogger(SearchNews.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final NewsService newsService = provider.getNewsService();

	private static final String PATH_TO_SEARCH_NEWS_PAGE = "/WEB-INF/jsp/search_news_page.jsp";
	private static final String PATH_TO_ERROR_PAGE_WITH_MESSAGE = "Controller?command=Unknown_Command&message=%s";

	private static final String NEWS = "newsList";
	private static final String SEARCH_WORD = "search_word";
	private static final String PATH_TO_SEARCH_NEWS_PAGE_WITH_PARAMETR = "Search_News&search_word=%s";
	private static final String PATH = "path";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		String searchWord = request.getParameter(SEARCH_WORD);

		try {
			List<News> newsList = newsService.findByWordInTitle(searchWord);

			session.setAttribute(PATH, String.format(PATH_TO_SEARCH_NEWS_PAGE_WITH_PARAMETR, searchWord));
			request.setAttribute(NEWS, newsList);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_SEARCH_NEWS_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			log.error("Error when trying to update news in the database.", e);
			response.sendRedirect(String.format(PATH_TO_ERROR_PAGE_WITH_MESSAGE, LocalMessage.TEMPORARY_PROBLEMS));
		}
	}
}
