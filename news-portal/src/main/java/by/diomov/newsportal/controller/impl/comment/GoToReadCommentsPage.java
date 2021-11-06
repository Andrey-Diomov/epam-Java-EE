package by.diomov.newsportal.controller.impl.comment;

import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.bean.Comment;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.controller.impl.message.LocalMessage;
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

	private static final String PATH_TO_ERROR_PAGE_WITH_MESSAGE = "Controller?command=Unknown_Command&message=%s";
	private static final String PATH_TO_READ_COMMENTS_PAGE = "/WEB-INF/jsp/read_comments_page.jsp";
	private static final String COMMAND_WITH_PARAMETER = "Go_To_Read_Comments_Page&idNews=%s&pageNumber=%s";

	private static final String PATH = "path";
	private static final String COMMENT = "comments";
	private static final String ID_NEWS = "idNews";
	private static final String PAGE_NUMBER = "pageNumber";
	private static final int COMMENTS_ON_THE_PAGE = 2;
	private static final String AMOUNT_PAGE = "amountPage";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		try {
			int newsId = Integer.valueOf(request.getParameter(ID_NEWS));
			int pageNumber = Integer.valueOf(request.getParameter(PAGE_NUMBER));

			int start = (pageNumber - 1) * COMMENTS_ON_THE_PAGE;
			List<Comment> comments = commentService.getLimitedAmountCommentsByNewsId(start, COMMENTS_ON_THE_PAGE,
					newsId);

			int amountComments = commentService.getAmountCommentsByNewsId(newsId);
			int amountPage = amountComments / COMMENTS_ON_THE_PAGE;
			if (amountComments % COMMENTS_ON_THE_PAGE != 0) {
				amountPage++;
			}
			session.setAttribute(PATH, String.format(COMMAND_WITH_PARAMETER, newsId, pageNumber));
			request.setAttribute(COMMENT, comments);
			request.setAttribute(AMOUNT_PAGE, amountPage);			
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_READ_COMMENTS_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			log.error("Error when trying to get list of comment from database.", e);
			response.sendRedirect(String.format(PATH_TO_ERROR_PAGE_WITH_MESSAGE, LocalMessage.TEMPORARY_PROBLEMS));
		}
	}
}
