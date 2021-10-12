package by.diomov.newsportal.controller.impl;

import java.io.IOException;
import by.diomov.newsportal.controller.Command;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ChangeLocal implements Command {
	private static final String PATH_TO_LAST_COMMAND = "Controller?command=%s";
	private static final String LOCAL = "local";
	private static final String PATH = "path";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		session.setAttribute(LOCAL, request.getParameter(LOCAL));
		String lastCommand = (String) session.getAttribute(PATH);
		response.sendRedirect(String.format(PATH_TO_LAST_COMMAND, lastCommand));			
	}
}
