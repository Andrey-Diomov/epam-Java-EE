package by.diomov.newsportal.controller.impl.user;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.diomov.newsportal.bean.RegistrationInfo;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.controller.impl.message.LocalMessage;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.ServiceProvider;
import by.diomov.newsportal.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class GoToUserProfile implements Command {
	private static final Logger log = LogManager.getLogger(GoToUserProfile.class);
	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final UserService userService = provider.getUserService();

	private static final String PATH_TO_USER_PROFILE_PAGE = "/WEB-INF/jsp/user_profile_page.jsp";
	private static final String PATH_TO_ERROR_PAGE_WITH_MESSAGE = "Controller?command=Unknown_Command&message=%s";

	private static final String COMMAND = "Go_To_User_Profile";
	private static final String PATH = "path";
	private static final String USER = "user";
	private static final String INFO = "info";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		User user = (User) session.getAttribute(USER);

		try {
			RegistrationInfo info = userService.getRegistrationInfo(user.getId());

			session.setAttribute(PATH, COMMAND);
			request.setAttribute(INFO, info);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_USER_PROFILE_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			log.error("Error occurred while trying to get user registration data, GoToUserProfile", e);
			response.sendRedirect(String.format(PATH_TO_ERROR_PAGE_WITH_MESSAGE, LocalMessage.TEMPORARY_PROBLEMS));
		}
	}
}