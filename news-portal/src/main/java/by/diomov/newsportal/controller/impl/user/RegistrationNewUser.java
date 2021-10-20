
package by.diomov.newsportal.controller.impl.user;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.bean.RegistrationInfo;
import by.diomov.newsportal.bean.Role;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.controller.impl.message.LocalMessage;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.ServiceProvider;
import by.diomov.newsportal.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegistrationNewUser implements Command {
	private static final Logger log = LogManager.getLogger(RegistrationNewUser.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final UserService userService = provider.getUserService();

	private static final String PATH_TO_ERROR_PAGE_WITH_MESSAGE = "Controller?command=Unknown_Command&message=%s";
	private static final String PATH_TO_REGISTRATION_WITH_MESSAGE = "Controller?command=Registration&message=%s";
	private static final String PATH_TO_AUTHORIZATION = "Controller?command=Authorization";

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

		try {
			RegistrationInfo info = new RegistrationInfo(name, surname, eMail, login, password, Role.USER);
			boolean success = userService.registration(info);
			if (success) {
				response.sendRedirect(PATH_TO_AUTHORIZATION);
				return;
			}
			response.sendRedirect(String.format(PATH_TO_REGISTRATION_WITH_MESSAGE, LocalMessage.USER_EXISTS));

		} catch (ServiceException e) {
			log.error("Error occurred while trying to registrate user, RegistrationNewUser.", e);
			response.sendRedirect(String.format(PATH_TO_ERROR_PAGE_WITH_MESSAGE, LocalMessage.TEMPORARY_PROBLEMS));
		}
	}
}
