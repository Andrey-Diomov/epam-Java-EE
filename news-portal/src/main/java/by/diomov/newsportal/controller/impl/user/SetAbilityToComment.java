package by.diomov.newsportal.controller.impl.user;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.controller.impl.message.LocalMessage;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.ServiceProvider;
import by.diomov.newsportal.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SetAbilityToComment implements Command {
	private static final Logger log = LogManager.getLogger(SetAbilityToComment.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final UserService userService = provider.getUserService();

	private static final String PATH_TO_SEARCH_USER_PAGE_WITH_PARAMETR = "Controller?command=Search_User&pageNumber=%s&ability=%s";
	private static final String PATH_TO_ERROR_PAGE_WITH_MESSAGE = "Controller?command=Unknown_Command&message=%s";

	public static final String USER_ID = "userId";
	public static final String ABILITY = "ability";
	private static final String PAGE_NUMBER = "pageNumber";
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int userId = Integer.parseInt(request.getParameter(USER_ID));
		boolean ability = Boolean.parseBoolean(request.getParameter(ABILITY));
		int pageNumber = Integer.valueOf(request.getParameter(PAGE_NUMBER));
		
		try {
			userService.setAbilityToComment(ability, userId);

			if (ability) {
				response.sendRedirect(String.format(PATH_TO_SEARCH_USER_PAGE_WITH_PARAMETR, pageNumber, false));
				return;
			}
			response.sendRedirect(String.format(PATH_TO_SEARCH_USER_PAGE_WITH_PARAMETR, pageNumber, true));
		} catch (ServiceException e) {
			log.error("Error when trying to delete news  from database.", e);
			response.sendRedirect(String.format(PATH_TO_ERROR_PAGE_WITH_MESSAGE, LocalMessage.TEMPORARY_PROBLEMS));
		}
	}
}
