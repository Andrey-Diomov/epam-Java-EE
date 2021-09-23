
package by.diomov.newsportal.controller.impl;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.service.ServiceProvider;
import by.diomov.newsportal.service.UserService;
import by.diomov.newsportal.service.exception.ServiceException;
import by.diomov.newsportal.service.exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SignIn implements Command {
	private static final Logger log = LogManager.getLogger(SignIn.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final UserService userService = provider.getUserService();

	private static final String PATH_TO_MAIN_PAGE = "Controller?command=Go_To_Main_Page";
	private static final String PATH_TO_AUTHIRIZATION_WITH_MESSAGE = "Controller?command=Authorization&message=%s";
	private static final String PATH_TO_MAIN_PAGE_WITH_MESSAGE = "Controller?command=Go_To_Main_Page&message=%s";

	private static final String MESSAGE_TEMPORARY_PROBLEMS = "Sorry, we're having problems.Please try again later";
	private static final String MESSAGE_TO_FILL_IN_FIELDS = "Please, Fill in all the fields!";
	private static final String MESSAGE_ABOUT_INVALID_DATA = "The entered login and (or) password does not exist. Try again!";

	private static final String USER_ATTRIBUTE = "user";
	private static final String LOGIN = "login";
	private static final String PASSWORD = "password";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		String login = request.getParameter(LOGIN);
		String password = request.getParameter(PASSWORD);

		if (!checkForValue(login, password)) {
			response.sendRedirect(String.format(PATH_TO_AUTHIRIZATION_WITH_MESSAGE, MESSAGE_TO_FILL_IN_FIELDS));
			return;
		}

		try {
			User user = userService.authorization(login, password);
			if (user == null) {
				response.sendRedirect(String.format(PATH_TO_AUTHIRIZATION_WITH_MESSAGE, MESSAGE_ABOUT_INVALID_DATA));
				return;
			}
			session.setAttribute(USER_ATTRIBUTE, user);
			response.sendRedirect(PATH_TO_MAIN_PAGE);

		} catch (ValidationException e) {
			log.error("Error occurred while trying to authorizate user, SignIn", e);
			response.sendRedirect(String.format(PATH_TO_AUTHIRIZATION_WITH_MESSAGE, e.getMessage()));
		} catch (ServiceException e) {
			log.error("Error occurred while trying to authorizate user, SignIn", e);
			response.sendRedirect(String.format(PATH_TO_MAIN_PAGE_WITH_MESSAGE, MESSAGE_TEMPORARY_PROBLEMS));
		}
	}

	private boolean checkForValue(String login, String password) {
		if (login == null || login.isEmpty() || login.isBlank()) {
			return false;
		}
		if (password == null || password.isEmpty() || password.isBlank()) {
			return false;
		}
		return true;
	}
}
