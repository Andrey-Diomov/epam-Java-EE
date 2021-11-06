package by.diomov.newsportal.controller.impl.user;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.diomov.newsportal.bean.News;
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

public class ChangePassword implements Command {

	private static final Logger log = LogManager.getLogger(ChangePassword.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final UserService userService = provider.getUserService();

	private static final String PATH_TO_USER_PROFILE_PAGE = "Controller?command=Go_To_User_Profile";
	private static final String PATH_TO_CHANGE_PASSWORD_PAGE_WITH_MESSAGE = "Controller?command=Go_To_Change_Password_Page&message=%s";	
	private static final String PATH_TO_ERROR_PAGE_WITH_MESSAGE = "Controller?command=Unknown_Command&message=%s";

	private static final String PASSWORD = "password";
	private static final String NEW_PASSWORD = "new_password";
	private static final String USER = "user";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		User user = (User) session.getAttribute(USER);
		int userId = user.getId();
		String password = request.getParameter(PASSWORD);
		String newPassword = request.getParameter(NEW_PASSWORD);

		try {
			boolean result = userService.updatePassword(userId, password, newPassword);
			if (result) {
				response.sendRedirect(PATH_TO_USER_PROFILE_PAGE);
				return;
			}
			response.sendRedirect(String.format(PATH_TO_CHANGE_PASSWORD_PAGE_WITH_MESSAGE, LocalMessage.INVALID_PASSWORD));
		} catch (ServiceException e) {
			log.error("Error when trying to update news in the database.", e);
			response.sendRedirect(String.format(PATH_TO_ERROR_PAGE_WITH_MESSAGE, LocalMessage.TEMPORARY_PROBLEMS));
		}
	}
}
