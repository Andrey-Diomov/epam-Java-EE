package by.diomov.newsportal.controller.impl;

import java.io.IOException;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.bean.Comment;
import by.diomov.newsportal.bean.Role;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.service.CommentService;
import by.diomov.newsportal.service.ServiceProvider;
import by.diomov.newsportal.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AddComment implements Command {
	private static final Logger log = LogManager.getLogger(AddComment.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final CommentService commentService = provider.getCommentService();

	private static final String PATH_TO_AUTHORIZATION_PAGE = "Controller?command=Go_To_Authorization_Page";
	private static final String PATH_TO_READ_NEWS_PAGE_WITH_PARAMETER = "Controller?command=Go_To_Read_News_Page&idNews=%s";
	private static final String PATH_TO_MAIN_PAGE_WITH_MESSAGE = "Controller?command=Go_To_Main_Page&message=%s";

	private static final String MESSAGE_TEMPORARY_PROBLEM = "Sorry, we're having problems.Please try again later";
	private static final String USER_ATTRIBUTE = "user";
	private static final String TEXT_PARAMETER = "text";
	private static final String ID_NEWS_PARAM = "idNews";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		User user = (User) session.getAttribute(USER_ATTRIBUTE);

		if (Role.GUEST.equals(user.getRole())) {
			response.sendRedirect(PATH_TO_AUTHORIZATION_PAGE);
			return;
		}

		String text = request.getParameter(TEXT_PARAMETER);
		int newsId = Integer.valueOf(request.getParameter(ID_NEWS_PARAM));

		try {
			Comment comment = new Comment(text, new Date(), user.getId(), newsId);
			commentService.save(comment);
			response.sendRedirect(String.format(PATH_TO_READ_NEWS_PAGE_WITH_PARAMETER, newsId));
		} catch (ServiceException e) {
			log.error("Error when trying to add comment to the database.", e);
			response.sendRedirect(String.format(PATH_TO_MAIN_PAGE_WITH_MESSAGE, MESSAGE_TEMPORARY_PROBLEM));
		}
	}
}
