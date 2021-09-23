package by.diomov.newsportal.controller.impl;

import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.bean.Comment;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.service.CommentService;
import by.diomov.newsportal.service.ServiceProvider;
import by.diomov.newsportal.service.exception.ServiceException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class GoToReadCommentsPage implements Command {
	private static final Logger log = LogManager.getLogger(GoToReadCommentsPage.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final CommentService commentService = provider.getCommentService();

	private static final String PATH_TO_READ_COMMENTS_PAGE = "/WEB-INF/jsp/read_comments_page.jsp";
	private static final String PATH_TO_MAIN_PAGE_WITH_MESSAGE = "Controller?command=Go_To_Main_Page&message=%s";
	private static final String MESSAGE_TEMPORARY_PROBLEMS = "Sorry, we're having problems.Please try again later";
	private static final String COMMAND_WITH_PARAMETER = "Go_To_Read_Comments_Page&idNews=%s&pageNumber=%s";
	private static final String PATH_ATTRIBUTE = "path";
	private static final String COMMENT_ATTRIBUTE = "comments";
	private static final String ID_NEWS_ATTR_PARAM = "idNews";
	private static final String PAGE_ATTR_PARAM = "pageNumber";
	private static final int COMMENTS_ON_THE_PAGE = 2;
	

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				HttpSession session = request.getSession(false);

		try {
			int newsId = Integer.valueOf(request.getParameter(ID_NEWS_ATTR_PARAM));
			int pageNumber = Integer.valueOf(request.getParameter(PAGE_ATTR_PARAM));
			session.setAttribute(PATH_ATTRIBUTE, String.format(COMMAND_WITH_PARAMETER, newsId, pageNumber));

			int from;
			if (pageNumber == 0) {
				from = 0;
			} else {
				from = pageNumber * COMMENTS_ON_THE_PAGE;
			}

			int amount = COMMENTS_ON_THE_PAGE;
			List<Comment> comments = commentService.getLimitedListCommentsByNewsId(from, amount, newsId);

			if (comments.size() == 0) {
				pageNumber = 0;
				from = 0;
				comments = commentService.getLimitedListCommentsByNewsId(from, amount, newsId);
			}

			pageNumber++;
			request.setAttribute(PAGE_ATTR_PARAM, pageNumber);
			request.setAttribute(COMMENT_ATTRIBUTE, comments);
			request.setAttribute(ID_NEWS_ATTR_PARAM, newsId);

			RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_READ_COMMENTS_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			log.error("Error when trying to get list of comment from database.", e);
			response.sendRedirect(String.format(PATH_TO_MAIN_PAGE_WITH_MESSAGE, MESSAGE_TEMPORARY_PROBLEMS));
		}
	}
}