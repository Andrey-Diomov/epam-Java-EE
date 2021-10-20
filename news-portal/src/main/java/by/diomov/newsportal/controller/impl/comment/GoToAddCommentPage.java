package by.diomov.newsportal.controller.impl.comment;

import java.io.IOException;
import by.diomov.newsportal.controller.Command;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class GoToAddCommentPage implements Command {	
	private static final String PATH_TO_ADD_COMMENT_PAGE = "/WEB-INF/jsp/add_comment_page.jsp";	
	private static final String PATH_TO_ADD_COMMENT_PAGE_WITH_PARAMETER = "Go_To_Add_Comment_Page&idNews=%s";
	private static final String PATH = "path";	
	private static final String ID_NEWS = "idNews";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		String newsId = request.getParameter(ID_NEWS);
		request.setAttribute(ID_NEWS, newsId);
		session.setAttribute(PATH, String.format(PATH_TO_ADD_COMMENT_PAGE_WITH_PARAMETER, newsId));
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_ADD_COMMENT_PAGE);
		requestDispatcher.forward(request, response);
	}
}