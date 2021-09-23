
package by.diomov.newsportal.controller.impl;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.diomov.newsportal.bean.RegistrationInfo;
import by.diomov.newsportal.bean.Role;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.service.ServiceProvider;
import by.diomov.newsportal.service.UserService;
import by.diomov.newsportal.service.exception.ServiceException;
import by.diomov.newsportal.service.exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegistrationNewUser implements Command {
	private static final Logger log = LogManager.getLogger(RegistrationNewUser.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final UserService userService = provider.getUserService();

	private static final String PATH_TO_REGISTRATION_WITH_MESSAGE = "Controller?command=Registration&message=%s";
	private static final String PATH_TO_AUTHORIZATION = "Controller?command=Authorization";

	private static final String MESSAGE_TO_FILL_IN_FIELDS = "Please, Fill in all the fields!";
	private static final String MESSAGE_TEMPORARY_PROBLEMS = "Sorry, we're having problems. Please try again later";
	private static final String MESSAGE_USER_EXISTS = "User with such a login or mail exists";

	private static final String NAME = "name";
	private static final String SURNAME = "surname";
	private static final String EMAIL = "eMail";
	private static final String LOGIN = "login";
	private static final String PASSWORD = "password";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String name = request.getParameter(NAME);
		String surname = request.getParameter(SURNAME);
		String eMail = request.getParameter(EMAIL);
		String login = request.getParameter(LOGIN);
		String password = request.getParameter(PASSWORD);

		if (!checkForValue(name, surname, eMail, login, password)) {
			response.sendRedirect(String.format(PATH_TO_REGISTRATION_WITH_MESSAGE, MESSAGE_TO_FILL_IN_FIELDS));
			return;
		}

		try {
			RegistrationInfo info = new RegistrationInfo(name, surname, eMail, login, password, Role.USER);
			boolean isRegistered = userService.registration(info);
			if (isRegistered) {
				response.sendRedirect(PATH_TO_AUTHORIZATION);
			} else {
				response.sendRedirect(String.format(PATH_TO_REGISTRATION_WITH_MESSAGE, MESSAGE_USER_EXISTS));
			}

		} catch (ValidationException e) {
			log.error("Error occurred while trying to registrate user, RegistrationNewUser", e);
			response.sendRedirect(String.format(PATH_TO_REGISTRATION_WITH_MESSAGE, e.getMessage()));
		} catch (ServiceException e) {
			log.error("Error occurred while trying to registrate user, RegistrationNewUser.", e);
			response.sendRedirect(String.format(PATH_TO_REGISTRATION_WITH_MESSAGE, MESSAGE_TEMPORARY_PROBLEMS));
		}
	}

	private boolean checkForValue(String name, String surname, String eMail, String login, String password) {
		if (name == null || name.isEmpty() || name.isBlank()) {
			return false;
		}

		if (surname == null || surname.isEmpty() || surname.isBlank()) {
			return false;
		}

		if (eMail == null || eMail.isEmpty() || eMail.isBlank()) {
			return false;
		}

		if (login == null || login.isEmpty() || login.isBlank()) {
			return false;
		}

		if (password == null || password.isEmpty() || password.isBlank()) {
			return false;
		}

		return true;
	}
}
