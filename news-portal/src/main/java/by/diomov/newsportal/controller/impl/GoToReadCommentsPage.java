package by.diomov.newsportal.controller.impl;

import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.bean.Comment;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.service.CommentService;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.ServiceProvider;
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
	private static final String PATH = "path";
	private static final String COMMENT = "comments";
	private static final String ID_NEWS = "idNews";
	private static final String PAGE_NUMBER = "pageNumber";
	private static final int NUMBER_COMMENTS_ON_THE_PAGE = 2;
	private static final String AMOUNT_PAGE = "amountPage";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		try {
			int newsId = Integer.valueOf(request.getParameter(ID_NEWS));
			int pageNumber = Integer.valueOf(request.getParameter(PAGE_NUMBER));

			int from = (pageNumber - 1) * NUMBER_COMMENTS_ON_THE_PAGE;
			List<Comment> comments = commentService.getLimitedListCommentsByNewsId(from, NUMBER_COMMENTS_ON_THE_PAGE, newsId);

			int amountComments = commentService.getAmountCommentsByNewsId(newsId);
			int amountPage = amountComments / NUMBER_COMMENTS_ON_THE_PAGE;
			if (amountComments % NUMBER_COMMENTS_ON_THE_PAGE != 0) {
				amountPage++;
			}

			session.setAttribute(PATH, String.format(COMMAND_WITH_PARAMETER, newsId, pageNumber));
			request.setAttribute(COMMENT, comments);			
			request.setAttribute(AMOUNT_PAGE, amountPage);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_READ_COMMENTS_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			log.error("Error when trying to get list of comment from database.", e);
			response.sendRedirect(String.format(PATH_TO_MAIN_PAGE_WITH_MESSAGE, MESSAGE_TEMPORARY_PROBLEMS));
		}
	}
}

//@Override
//public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//			HttpSession session = request.getSession(false);
//
//	try {
//		int newsId = Integer.valueOf(request.getParameter(ID_NEWS_ATTR_PARAM));
//		int pageNumber = Integer.valueOf(request.getParameter(PAGE_ATTR_PARAM));
//		session.setAttribute(PATH_ATTRIBUTE, String.format(COMMAND_WITH_PARAMETER, newsId, pageNumber));
//
//		int from;
//		if (pageNumber == 0) {
//			from = 0;
//		} else {
//			from = pageNumber * COMMENTS_ON_THE_PAGE;
//		}
//
//		int amount = COMMENTS_ON_THE_PAGE;
//		List<Comment> comments = commentService.getLimitedListCommentsByNewsId(from, amount, newsId);
//
//		if (comments.size() == 0) {
//			pageNumber = 0;
//			from = 0;
//			comments = commentService.getLimitedListCommentsByNewsId(from, amount, newsId);
//		}
//
//		pageNumber++;
//		request.setAttribute(PAGE_ATTR_PARAM, pageNumber);
//		request.setAttribute(COMMENT_ATTRIBUTE, comments);
//		request.setAttribute(ID_NEWS_ATTR_PARAM, newsId);
//
//		RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_READ_COMMENTS_PAGE);
//		requestDispatcher.forward(request, response);
//	} catch (ServiceException e) {
//		log.error("Error when trying to get list of comment from database.", e);
//		response.sendRedirect(String.format(PATH_TO_MAIN_PAGE_WITH_MESSAGE, MESSAGE_TEMPORARY_PROBLEMS));
//	}
//}