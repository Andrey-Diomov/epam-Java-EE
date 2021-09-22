package by.diomov.newsportal.controller.impl;

import java.io.IOException;
import by.diomov.newsportal.controller.Command;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LogOut implements Command {
	private static final String PATH_TO_MAIN_PAGE = "Controller?command=Go_To_Main_Page";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession(false).invalidate();
		response.sendRedirect(PATH_TO_MAIN_PAGE);
	}
}
