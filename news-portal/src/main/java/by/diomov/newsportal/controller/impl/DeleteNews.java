package by.diomov.newsportal.controller.impl;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.bean.Role;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.service.NewsService;
import by.diomov.newsportal.service.ServiceProvider;
import by.diomov.newsportal.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class DeleteNews implements Command {
	private static final Logger log = LogManager.getLogger(DeleteNews.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final NewsService newsService = provider.getNewsService();

	private static final String PATH_TO_AUTHORIZATION_PAGE = "Controller?command=Go_To_Authorization_Page";
	private static final String PATH_TO_MAIN_PAGE = "Controller?command=Go_To_Main_Page";
	private static final String PATH_TO_MAIN_PAGE_WITH_MESSAGE = "Controller?command=Go_To_Main_Page&message=%s";

	private static final String MESSAGE_TEMPORARY_PROBLEMS = "Sorry, we're having problems.Please try again later";
	private static final String COMMAND = "DELETE_NEWS";
	private static final String USER_ATTRIBUTE = "user";
	private static final String PATH_ATTRIBUTE = "path";
	public static final String ID_NEWS_PARAM = "idNews";

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

		int idNews = Integer.parseInt(request.getParameter(ID_NEWS_PARAM));
		System.out.println(idNews);
		try {
			newsService.delete(idNews);
			session.setAttribute(PATH_ATTRIBUTE, COMMAND);
			response.sendRedirect(PATH_TO_MAIN_PAGE);
		} catch (ServiceException e) {
			log.error("Error when trying to delete news  from database.", e);
			response.sendRedirect(String.format(PATH_TO_MAIN_PAGE_WITH_MESSAGE, MESSAGE_TEMPORARY_PROBLEMS));
		}
	}
}