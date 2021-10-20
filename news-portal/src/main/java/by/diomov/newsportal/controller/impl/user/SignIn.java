
package by.diomov.newsportal.controller.impl.user;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.controller.impl.message.LocalMessage;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.ServiceProvider;
import by.diomov.newsportal.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SignIn implements Command {
	private static final Logger log = LogManager.getLogger(SignIn.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final UserService userService = provider.getUserService();

	private static final String PATH_TO_MAIN_PAGE_WITH_PARAMETR = "Controller?command=Go_To_Main_Page&pageNumber=1";
	private static final String PATH_TO_AUTHIRIZATION_PAGE_WITH_MESSAGE = "Controller?command=Authorization&message=%s";
	private static final String PATH_TO_ERROR_PAGE_WITH_MESSAGE = "Controller?command=Unknown_Command&message=%s";

	private static final String LOGIN = "login";
	private static final String PASSWORD = "password";
	private static final String USER = "user";
	private static final String MESSAGES = "messages";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		String login = request.getParameter(LOGIN);
		String password = request.getParameter(PASSWORD);

		try {
			User user = userService.authorization(login, password);
			if (user == null) {
				response.sendRedirect(
						String.format(PATH_TO_AUTHIRIZATION_PAGE_WITH_MESSAGE, LocalMessage.INVALID_LOGIN_OR_PASSWORD));
				return;
			}
			session.removeAttribute(MESSAGES);
			session.setAttribute(USER, user);
			response.sendRedirect(PATH_TO_MAIN_PAGE_WITH_PARAMETR);
		} catch (ServiceException e) {
			log.error("Error occurred while trying to authorizate user, SignIn", e);
			response.sendRedirect(String.format(PATH_TO_ERROR_PAGE_WITH_MESSAGE, LocalMessage.TEMPORARY_PROBLEMS));
		}
	}
}
