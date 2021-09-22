package by.diomov.newsportal.controller.impl;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.bean.News;
import by.diomov.newsportal.bean.Role;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.service.NewsService;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.ServiceProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AddNews implements Command {
	private static final Logger log = LogManager.getLogger(AddNews.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final NewsService newsService = provider.getNewsService();

	private static final String PATH_TO_AUTHORIZATION_PAGE = "Controller?command=Go_To_Authorization_Page";
	private static final String PATH_TO_MAIN_PAGE = "Controller?command=Go_To_Main_Page";
	private static final String PATH_TO_MAIN_PAGE_WITH_MESSAGE = "Controller?command=Go_To_Main_Page&message=%s";

	private static final String MESSAGE_TEMPORARY_PROBLEMS = "Sorry, we're having problems.Please try again later";
	private static final String USER_ATTRIBUTE = "user";
	private static final String TITLE_PARAMETER = "title";
	private static final String BRIEF_PARAMETER = "brief";
	private static final String CONTENT_PARAMETER = "content";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		User user = (User) session.getAttribute(USER_ATTRIBUTE);

		if (!Role.ADMIN.equals(user.getRole())) {
			user.setRole(Role.GUEST);
			session.setAttribute(USER_ATTRIBUTE, user);
			response.sendRedirect(PATH_TO_AUTHORIZATION_PAGE);
			return;
		}

		String title = request.getParameter(TITLE_PARAMETER);
		String brief = request.getParameter(BRIEF_PARAMETER);
		String content = request.getParameter(CONTENT_PARAMETER);

		try {
			News news = new News(title, brief, content, user.getId());
			newsService.save(news);
			response.sendRedirect(PATH_TO_MAIN_PAGE);
		} catch (ServiceException e) {
			log.error("Error when trying to add news to the database.", e);
			response.sendRedirect(String.format(PATH_TO_MAIN_PAGE_WITH_MESSAGE, MESSAGE_TEMPORARY_PROBLEMS));
		}
	}
}
