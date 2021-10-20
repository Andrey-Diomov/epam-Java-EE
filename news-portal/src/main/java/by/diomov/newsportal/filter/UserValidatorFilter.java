package by.diomov.newsportal.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import by.diomov.newsportal.controller.CommandName;
import by.diomov.newsportal.controller.impl.message.LocalMessage;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UserValidatorFilter implements Filter {
	private static final String NAME_SURNAME_REGULAR_EXPRESSION = "^[A-Za-z]\\w{2,29}$";
	private static final String EMAIL_REGULAR_EXPRESSION = "([A-Za-z0-9._%+-]+)@(\\w+\\.)([a-z]{2,4})";
	private static final String LOGIN_REGULAR_EXPRESSION = "^[A-Za-z]\\w{5,19}$";
	private static final String PASSWORD_REGULAR_EXPRESSION = "\\w{5,}";

	private static final String PATH_TO_AUTHIRIZATION_WITH_PARAMETER_LIST_MESSAGES = "Controller?command=Authorization&list_messages=authorization";
	private static final String PATH_TO_AUTHIRIZATION_WITH_MESSAGE = "Controller?command=Authorization&message=%s";

	private static final String PATH_TO_REGISTRATION_WITH_PARAMETER_LIST_MESSAGES = "Controller?command=Registration&list_messages=registration";
	private static final String PATH_TO_REGISTRATION_WITH_MESSAGE = "Controller?command=Registration&message=%s";

	private static final String PATH_TO_ERROR_PAGE_WITH_MESSAGE = "Controller?command=Unknown_Command&message=%s";

	private static final String COMMAND = "command";
	private static final String NAME = "name";
	private static final String SURNAME = "surname";
	private static final String EMAIL = "eMail";
	private static final String LOGIN = "login";
	private static final String PASSWORD = "password";
	private static final String MESSAGES = "messages";

	public void init(FilterConfig fConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

		String command = req.getParameter(COMMAND);

		CommandName commandName;

		try {
			commandName = CommandName.valueOf(command.toUpperCase());

			switch (commandName) {
			case SIGN_IN:
				String login = req.getParameter(LOGIN);
				String password = req.getParameter(PASSWORD);

				if (!checkForValue(login, password)) {
					resp.sendRedirect(String.format(PATH_TO_AUTHIRIZATION_WITH_MESSAGE, LocalMessage.EMPTY_FIELDS));
					return;
				}

				List<String> messages = validateLoginAndPassword(login, password);

				if (!messages.isEmpty()) {
					session.setAttribute(MESSAGES, messages);
					resp.sendRedirect(PATH_TO_AUTHIRIZATION_WITH_PARAMETER_LIST_MESSAGES);
					return;
				}
				break;

			case REGISTRATION_NEW_USER:
				String name = req.getParameter(NAME);
				String surname = req.getParameter(SURNAME);
				String eMail = req.getParameter(EMAIL);
				login = req.getParameter(LOGIN);
				password = req.getParameter(PASSWORD);

				if (!checkForValue(name, surname, eMail, login, password)) {
					resp.sendRedirect(String.format(PATH_TO_REGISTRATION_WITH_MESSAGE, LocalMessage.EMPTY_FIELDS));
					return;
				}

				messages = validateRegistrationInfo(name, surname, eMail, login, password);

				if (!messages.isEmpty()) {
					session.setAttribute(MESSAGES, messages);
					resp.sendRedirect(PATH_TO_REGISTRATION_WITH_PARAMETER_LIST_MESSAGES);
					return;
				}
				break;
			default:
				break;

			}
		} catch (IllegalArgumentException e) {
			resp.sendRedirect(String.format(PATH_TO_ERROR_PAGE_WITH_MESSAGE, LocalMessage.TEMPORARY_PROBLEMS));
			return;
		}

		chain.doFilter(request, response);
	}

	public void destroy() {
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

	private boolean checkForValue(String name, String surname, String eMail, String login, String password) {
		if (!checkForValue(login, password)) {
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

	private static List<String> validateLoginAndPassword(String login, String password) {
		List<String> messages = new ArrayList<>();

		if (!login.matches(LOGIN_REGULAR_EXPRESSION)) {
			messages.add(LocalMessage.LOGIN_INVALID);
		}

		if (!password.matches(PASSWORD_REGULAR_EXPRESSION)) {
			messages.add(LocalMessage.PASSWORD_INVALID);
		}
		return messages;
	}

	private static List<String> validateRegistrationInfo(String name, String surname, String eMail, String login,
			String password) {
		List<String> messages = new ArrayList<>();

		if (!(name.matches(NAME_SURNAME_REGULAR_EXPRESSION) || (surname.matches(NAME_SURNAME_REGULAR_EXPRESSION)))) {
			messages.add(LocalMessage.INVALID_NAME_SURNAME);
		}

		if (!eMail.matches(EMAIL_REGULAR_EXPRESSION)) {
			messages.add(LocalMessage.INVALID_EMAIL);
		}

		List<String> loginAndPasswordErrors = validateLoginAndPassword(login, password);

		if (!loginAndPasswordErrors.isEmpty()) {
			messages.addAll(loginAndPasswordErrors);
		}
		return messages;
	}
}
