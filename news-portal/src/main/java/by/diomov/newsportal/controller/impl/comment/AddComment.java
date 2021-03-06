package by.diomov.newsportal.controller.impl.comment;

import java.io.IOException;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.bean.Comment;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.controller.impl.message.LocalMessage;
import by.diomov.newsportal.service.CommentService;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.ServiceProvider;
import by.diomov.newsportal.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AddComment implements Command {
	private static final Logger log = LogManager.getLogger(AddComment.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final CommentService commentService = provider.getCommentService();
	private static final UserService userService = provider.getUserService();

	private static final String PATH_TO_READ_NEWS_PAGE_WITH_PARAMETER = "Controller?command=Go_To_Read_News_Page&idNews=%s";
	private static final String PATH_TO_ADD_COMMENT_PAGE_WITH_PARAMETER = "Controller?command=Go_To_Add_comment_Page&idNews=%s&message=%s";
	private static final String PATH_TO_ERROR_PAGE_WITH_MESSAGE = "Controller?command=Unknown_Command&message=%s";

	private static final String USER = "user";
	private static final String TEXT = "text";
	private static final String ID_NEWS = "idNews";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		User user = (User) session.getAttribute(USER);
		int userId = user.getId();
		int newsId = Integer.valueOf(request.getParameter(ID_NEWS));

		try {
			boolean isUserBlockedToComment = userService.isBlockedToComment(userId);

			if (!isUserBlockedToComment) {
				response.sendRedirect(
						String.format(PATH_TO_ADD_COMMENT_PAGE_WITH_PARAMETER, newsId, LocalMessage.USER_BLOCKED));
				return;
			}
			String text = request.getParameter(TEXT);			
			Comment comment = new Comment(text, new Date(), user.getId(), newsId);
			
			commentService.save(comment);
			response.sendRedirect(String.format(PATH_TO_READ_NEWS_PAGE_WITH_PARAMETER, newsId));
		} catch (ServiceException e) {
			log.error("Error when trying to add comment to the database.", e);
			response.sendRedirect(String.format(PATH_TO_ERROR_PAGE_WITH_MESSAGE, LocalMessage.TEMPORARY_PROBLEMS));
		}
	}
}
