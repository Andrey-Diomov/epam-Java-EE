package by.diomov.newsportal.controller.impl;

import java.io.IOException;
import by.diomov.newsportal.bean.Role;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.controller.Command;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class GoToAddCommentPage implements Command {
	private static final String PATH_TO_ADD_COMMENT_PAGE = "/WEB-INF/jsp/add_comment_page.jsp";
	private static final String PATH_TO_AUTHORIZATION_PAGE = "Controller?command=Go_To_Authorization_Page";

	private static final String COMMAND_WITH_PARAMETER = "Go_To_Add_Comment_Page&idNews=%s";
	private static final String PATH_ATTRIBUTE = "path";
	private static final String USER_ATTRIBUTE = "user";
	private static final String ID_NEWS_PARAM = "idNews";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		User user = (User) session.getAttribute(USER_ATTRIBUTE);

//		if (Role.GUEST.equals(user.getRole())) {
//			response.sendRedirect(PATH_TO_AUTHORIZATION_PAGE);
//			return;
//		}

		String newsId = request.getParameter(ID_NEWS_PARAM);
		request.setAttribute(ID_NEWS_PARAM, newsId);
		session.setAttribute(PATH_ATTRIBUTE, String.format(COMMAND_WITH_PARAMETER, newsId));
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_ADD_COMMENT_PAGE);
		requestDispatcher.forward(request, response);
	}
}