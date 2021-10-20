package by.diomov.newsportal.controller.impl.news;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.bean.News;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.controller.impl.message.LocalMessage;
import by.diomov.newsportal.service.NewsService;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.ServiceProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UpdateNews implements Command {
	private static final Logger log = LogManager.getLogger(UpdateNews.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final NewsService newsService = provider.getNewsService();

	private static final String PATH_TO_READ_NEWS_PAGE_WITH_MESSAGE = "Controller?command=Go_To_Read_News_Page&idNews=%s";
	private static final String PATH_TO_UPDATE_PAGE_WITH_PARAMETER_ID_NEWS = "Controller?command=Go_To_Update_News_Page&idNews=%s";
	private static final String PATH_TO_ERROR_PAGE_WITH_MESSAGE = "Controller?command=Unknown_Command&message=%s";

	private static final String USER = "user";
	public static final String ID_NEWS = "idNews";
	private static final String TITLE = "title";
	private static final String BRIEF = "brief";
	private static final String CONTENT = "content";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		User user = (User) session.getAttribute(USER);

		int newsId = Integer.valueOf(request.getParameter(ID_NEWS));
		String title = request.getParameter(TITLE);
		String brief = request.getParameter(BRIEF);
		String content = request.getParameter(CONTENT);
		int userId = user.getId();

		if (!checkValuesForValidity(title, brief, content)) {
			response.sendRedirect(String.format(PATH_TO_UPDATE_PAGE_WITH_PARAMETER_ID_NEWS, newsId));
			return;
		}

		News news = new News(newsId, title, brief, content, userId);

		try {
			newsService.update(news);
			response.sendRedirect(String.format(PATH_TO_READ_NEWS_PAGE_WITH_MESSAGE, newsId));
		} catch (ServiceException e) {
			log.error("Error when trying to update news in the database.", e);
			response.sendRedirect(String.format(PATH_TO_ERROR_PAGE_WITH_MESSAGE, LocalMessage.TEMPORARY_PROBLEMS));
		}
	}

	private boolean checkValuesForValidity(String title, String brief, String content) {
		if (title == null || title.isEmpty() || title.isBlank()) {
			return false;
		}
		if (brief == null || brief.isEmpty() || brief.isBlank()) {
			return false;
		}
		if (content == null || content.isEmpty() || content.isBlank()) {
			return false;
		}
		return true;
	}
}