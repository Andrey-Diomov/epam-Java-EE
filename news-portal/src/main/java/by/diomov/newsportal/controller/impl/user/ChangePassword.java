package by.diomov.newsportal.controller.impl.user;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.diomov.newsportal.bean.News;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.controller.Command;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.ServiceProvider;
import by.diomov.newsportal.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ChangePassword implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
	private static final Logger log = LogManager.getLogger(ChangePassword.class);

	private static final ServiceProvider provider = ServiceProvider.getInstance();
	private static final UserService newsService = provider.getUserService();

	private static final String PATH_TO_MAIN_PAGE = "Controller?command=Go_To_Main_Page";
	private static final String PATH_TO_READ_NEWS_PAGE_WITH_MESSAGE = "Controller?command=Go_To_Read_News_Page&idNews=%s";
	private static final String PATH_TO_CHANGE_PASSWORD_PAGE = "Controller?command=Go_To_Change_Password_Page&message=%s";

	
	public static final String LOGIN = "login";
	private static final String PASSWORD = "password";
	private static final String NEW_PASSWORD = "new_password";
	

//	@Override
//	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		HttpSession session = request.getSession(false);
//		
//		String login = request.getParameter(LOGIN);
//		String password = request.getParameter(PASSWORD);
//		String newPassword = request.getParameter(NEW_PASSWORD);
//		
//		if (!checkForValue(login, password, newPassword)) {
//			response.sendRedirect(PATH_TO_CHANGE_PASSWORD_PAGE);
//			return;
//		}
//
//		try {
//			newsService.update(news);
//			response.sendRedirect(String.format(PATH_TO_READ_NEWS_PAGE_WITH_MESSAGE, newsId));
//		} catch (ServiceException e) {
//			log.error("Error when trying to update news in the database.", e);
//			response.sendRedirect(PATH_TO_MAIN_PAGE);
//		}
//	}
//
//	private boolean checkForValue(String login, String password, String newPassword) {
//		if (login == null || login.isEmpty() || login.isBlank()) {
//			return false;
//		}
//		if (password == null || password.isEmpty() || password.isBlank()) {
//			return false;
//		}
//		if (newPassword == null || newPassword.isEmpty() || newPassword.isBlank()) {
//			return false;
//		}
//		return true;
//	}
}
