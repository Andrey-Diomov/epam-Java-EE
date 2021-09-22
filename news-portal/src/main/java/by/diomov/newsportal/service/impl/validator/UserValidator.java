package by.diomov.newsportal.service.impl.validator;

import by.diomov.newsportal.bean.RegistrationInfo;

public final class UserValidator {
	private static final String NAME_REGULAR_EXPRESSION = "^[A-Za-z]\\w{2,29}$";
	private static final String SURNAME_REGULAR_EXPRESSION = "^[A-Za-z]\\w{2,29}$";
	private static final String EMAIL_REGULAR_EXPRESSION = "([A-Za-z0-9._%+-]+)@(\\w+\\.)([a-z]{2,4})";
	private static final String LOGIN_REGULAR_EXPRESSION = "^[A-Za-z]\\w{5,19}$";
	private static final String PASSWORD_REGULAR_EXPRESSION = "\\w{5,}";

	private static final String REQUIREMENTS_TO_NAME = "The name must be composed of alphanumeric characters and an underscore character, from 3 to 30 characters inclusive. The first character of the username must be alphabetic.";
	private static final String REQUIREMENTS_TO_SURNAME = "The userame must be composed of alphanumeric characters and an underscore character, from 3 to 30 characters inclusive. The first character of the username must be alphabetic.";
	private static final String REQUIREMENTS_TO_EMAIL = "Incorrect email";
	private static final String REQUIREMENTS_TO_LOGIN = "The login must be composed of alphanumeric characters and an underscore character, from 6 to 20 characters inclusive. The first character of the username must be alphabetic.";
	private static final String REQUIREMENTS_TO_PASSWORD = "The password must be at least 5 alphanumeric characters and an underscore character.";

	private UserValidator() {
	};

	public static String validateRegistrationInfo(RegistrationInfo info) {
		StringBuilder errors = new StringBuilder();

		if (!info.getName().matches(NAME_REGULAR_EXPRESSION)) {
			errors.append(REQUIREMENTS_TO_NAME);
		}

		if (!info.getSurname().matches(SURNAME_REGULAR_EXPRESSION)) {
			errors.append(REQUIREMENTS_TO_SURNAME);
		}

		if (!info.geteMail().matches(EMAIL_REGULAR_EXPRESSION)) {
			errors.append(REQUIREMENTS_TO_EMAIL);
		}

		String loginAndPasswordErrors = validateLoginAndPassword(info.getLogin(), info.getPassword());

		if (!loginAndPasswordErrors.isEmpty()) {
			errors.append(loginAndPasswordErrors);
		}
		return errors.toString();
	}

	public static String validateLoginAndPassword(String login, String password) {
		StringBuilder errors = new StringBuilder();

		if (!login.matches(LOGIN_REGULAR_EXPRESSION)) {
			errors.append(REQUIREMENTS_TO_LOGIN);
		}

		if (!password.matches(PASSWORD_REGULAR_EXPRESSION)) {
			errors.append(REQUIREMENTS_TO_PASSWORD);
		}
		return errors.toString();
	}
}
