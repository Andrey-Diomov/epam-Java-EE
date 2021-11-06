package by.diomov.newsportal.controller.impl.news;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.controller.impl.message.LocalMessage;
import by.diomov.newsportal.service.NewsService;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.ServiceProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteNews implements Command {
	private static final Logger log = LogManager.getLogger(DeleteNews.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final NewsService newsService = provider.getNewsService();

	private static final String PATH_TO_MAIN_PAGE_WITH_PARAMETR = "Controller?command=Go_To_Main_Page&pageNumber=1";
	private static final String PATH_TO_ERROR_PAGE_WITH_MESSAGE = "Controller?command=Unknown_Command&message=%s";

	public static final String ID_NEWS = "idNews";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int idNews = Integer.parseInt(request.getParameter(ID_NEWS));
		
		try {
			newsService.delete(idNews);
			response.sendRedirect(PATH_TO_MAIN_PAGE_WITH_PARAMETR);
		} catch (ServiceException e) {
			log.error("Error when trying to delete news  from database.", e);
			response.sendRedirect(String.format(PATH_TO_ERROR_PAGE_WITH_MESSAGE, LocalMessage.TEMPORARY_PROBLEMS));
		}
	}
}