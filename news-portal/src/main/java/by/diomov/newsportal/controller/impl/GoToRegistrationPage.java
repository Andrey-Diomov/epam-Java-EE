package by.diomov.newsportal.controller.impl;

import java.io.IOException;
import by.diomov.newsportal.controller.Command;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GoToRegistrationPage implements Command {
	private static final String PATH_TO_REGISTRATION_PAGE = "/WEB-INF/jsp/registration_page.jsp";
	private static final String COMMAND = "Registration";
	private static final String PATH = "path";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession(false).setAttribute(PATH, COMMAND);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_REGISTRATION_PAGE);
		requestDispatcher.forward(request, response);
	}
}





	

