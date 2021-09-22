//package by.diomov.newsportal.controller.impl;
//
//import java.io.IOException;
//import by.diomov.newsportal.bean.Role;
//import by.diomov.newsportal.bean.User;
//import by.diomov.newsportal.controller.Command;
//import by.diomov.newsportal.service.ServiceProvider;
//import by.diomov.newsportal.service.UserService;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//public class ChangePassword implements Command {
//	private static final ServiceProvider provider = ServiceProvider.getInstance();
//	private static final UserService userService = provider.getUserService();
//
//	private static final String PATH_TO_AUTHORIZATION = "Controller?command=Authorization";
//	private static final String USER_ATTRIBUTE = "user";
//
//	@Override
//	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		HttpSession session = request.getSession(false);
//
//		User user = (User) session.getAttribute(USER_ATTRIBUTE);
//
//		if (Role.GUEST.equals(user.getRole())) {			
//			response.sendRedirect(PATH_TO_AUTHORIZATION);
//			return;
//		}
//
//	}
//}
