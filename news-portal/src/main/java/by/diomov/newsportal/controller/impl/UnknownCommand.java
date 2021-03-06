package by.diomov.newsportal.controller.impl;

import java.io.IOException;
import by.diomov.newsportal.controller.Command;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UnknownCommand implements Command {
	private static final String ERROR_PAGE = "/WEB-INF/jsp/error_page.jsp";
	private static final String PATH = "path";
	private static final String COMMAND_WITH_PARAMETR = "Unknown_Command&message=%s";
	private static final String MESSAGE = "message";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		String message = request.getParameter(MESSAGE);

		session.setAttribute(PATH, String.format(COMMAND_WITH_PARAMETR, message));
		request.setAttribute(MESSAGE, message);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(ERROR_PAGE);
		requestDispatcher.forward(request, response);
	}
}
