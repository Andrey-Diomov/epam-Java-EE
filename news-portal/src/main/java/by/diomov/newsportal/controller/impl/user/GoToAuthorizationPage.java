
package by.diomov.newsportal.controller.impl.user;

import java.io.IOException;
import by.diomov.newsportal.controller.Command;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GoToAuthorizationPage implements Command {
	private static final String PATH_TO_AUTHORIZATION_PAGE = "/WEB-INF/jsp/authorization_page.jsp";
	private static final String COMMAND = "Authorization";
	private static final String PATH = "path";
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		request.getSession(false).setAttribute(PATH, COMMAND);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_AUTHORIZATION_PAGE);
		requestDispatcher.forward(request, response);
	}
}
