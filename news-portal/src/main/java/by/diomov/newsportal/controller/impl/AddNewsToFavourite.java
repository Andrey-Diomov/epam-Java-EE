package by.diomov.newsportal.controller.impl;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.service.NewsService;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.ServiceProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AddNewsToFavourite implements Command {
	private static final Logger log = LogManager.getLogger(AddNewsToFavourite.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final NewsService newsService = provider.getNewsService();

	private static final String PATH_TO_READ_NEWS_PAGE_WITH_PARAMETER = "Controller?command=Go_To_Read_News_Page&idNews=%s";		
	private static final String PATH_TO_MAIN_PAGE_WITH_MESSAGE = "Controller?command=Go_To_Main_Page&message=%s";
	private static final String MESSAGE_TEMPORARY_PROBLEMS = "Sorry, we're having problems.Please try again later";
	private static final String USER = "user";
	private static final String ID_NEWS = "idNews";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		User user = (User) session.getAttribute(USER);

		int userId = user.getId();
		int newsId = Integer.valueOf(request.getParameter(ID_NEWS));

		try {
			newsService.addToFavourite(userId, newsId);
			response.sendRedirect(String.format(PATH_TO_READ_NEWS_PAGE_WITH_PARAMETER, newsId));			
		} catch (ServiceException e) {
			log.error("Error when trying to add news to favourite.", e);
			response.sendRedirect(String.format(PATH_TO_MAIN_PAGE_WITH_MESSAGE, MESSAGE_TEMPORARY_PROBLEMS));
		}
	}
}
