package by.diomov.newsportal.controller.impl;

import java.io.IOException;
import by.diomov.newsportal.controller.Command;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ChangeLocal implements Command {
	private static final String PATH_TO_LAST_COMMAND = "Controller?command=%s";
	private static final String LOCAL_ATTRIBUTE = "local";
	private static final String PATH_ATTRIBUTE = "path";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		session.setAttribute(LOCAL_ATTRIBUTE, request.getParameter(LOCAL_ATTRIBUTE));
		String lastCommand = (String) session.getAttribute(PATH_ATTRIBUTE);
		response.sendRedirect(String.format(PATH_TO_LAST_COMMAND, lastCommand));			
	}
}
