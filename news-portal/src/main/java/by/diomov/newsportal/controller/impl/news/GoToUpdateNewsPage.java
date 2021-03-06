package by.diomov.newsportal.controller.impl.news;

import java.io.IOException;
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

public class GoToUpdateNewsPage implements Command {
	private static final Logger log = LogManager.getLogger(GoToUpdateNewsPage.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final NewsService newsService = provider.getNewsService();

	private static final String PATH_TO_UPDATE_NEWS_PAGE = "/WEB-INF/jsp/update_news_page.jsp";
	private static final String PATH_TO_ERROR_PAGE_WITH_MESSAGE = "Controller?command=Unknown_Command&message=%s";
	private static final String COMMAND_WITH_PARAMETER = "Go_To_Update_News_Page&idNews=%s";

	private static final String PATH = "path";
	private static final String NEWS = "news";
	private static final String ID_NEWS = "idNews";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		int idNews = Integer.parseInt(request.getParameter(ID_NEWS));

		try {
			News news = newsService.get(idNews);
			request.setAttribute(NEWS, news);
			session.setAttribute(PATH, String.format(COMMAND_WITH_PARAMETER, idNews));
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_UPDATE_NEWS_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			log.error("Error when trying to get news from database.", e);
			response.sendRedirect(String.format(PATH_TO_ERROR_PAGE_WITH_MESSAGE, LocalMessage.TEMPORARY_PROBLEMS));
		}
	}
}
