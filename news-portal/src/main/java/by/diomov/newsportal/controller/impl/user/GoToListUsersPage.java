package by.diomov.newsportal.controller.impl.user;

import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

public class GoToListUsersPage implements Command {
	private static final Logger log = LogManager.getLogger(GoToListUsersPage.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final UserService userService = provider.getUserService();

	private static final String PATH_TO_LIST_USERS_PAGE = "/WEB-INF/jsp/list_users_page.jsp";
	private static final String PATH_TO_LIST_USERS_PAGE_WITH_PARAMETER = "Go_To_List_Users_Page&pageNumber=%s&ability=%s";
	private static final String PATH_TO_ERROR_PAGE_WITH_MESSAGE = "Controller?command=Unknown_Command&message=%s";

	private static final String PATH = "path";
	private static final String USERS = "users";
	private static final String PAGE_NUMBER = "pageNumber";
	private static final int USERS_ON_THE_PAGE = 2;
	private static final String AMOUNT_PAGE = "amountPage";
	private static final String ABILITY = "ability";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		try {
			int pageNumber = Integer.valueOf(request.getParameter(PAGE_NUMBER));
			int start = (pageNumber - 1) * USERS_ON_THE_PAGE;
			boolean ability = Boolean.parseBoolean(request.getParameter(ABILITY));

			List<User> users = userService.getLimitedAmountByAbilityToComment(ability, start, USERS_ON_THE_PAGE);

			int amountUsers = userService.getAmountByAbilityToComment(ability);
			int amountPage = amountUsers / USERS_ON_THE_PAGE;
			if (amountUsers % USERS_ON_THE_PAGE != 0) {
				amountPage++;
			}

			session.setAttribute(PATH, String.format(PATH_TO_LIST_USERS_PAGE_WITH_PARAMETER, pageNumber, ability));
			request.setAttribute(USERS, users);
			request.setAttribute(ABILITY, ability);
			request.setAttribute(AMOUNT_PAGE, amountPage);

			RequestDispatcher requestDispatcher = request.getRequestDispatcher(PATH_TO_LIST_USERS_PAGE);
			requestDispatcher.forward(request, response);
		} catch (ServiceException e) {
			log.error("Error when trying to get list users from database.", e);
			response.sendRedirect(String.format(PATH_TO_ERROR_PAGE_WITH_MESSAGE, LocalMessage.TEMPORARY_PROBLEMS));
		}
	}
}
