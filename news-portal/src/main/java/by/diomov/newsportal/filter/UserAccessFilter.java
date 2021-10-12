//package by.diomov.newsportal.filter;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import by.diomov.newsportal.bean.Role;
//import by.diomov.newsportal.bean.User;
//import by.diomov.newsportal.controller.CommandName;
//import by.diomov.newsportal.controller.impl.GoToMainPage;
//import by.diomov.newsportal.dao.DAOException;
//import by.diomov.newsportal.dao.impl.connection.ConnectionPool;
//import by.diomov.newsportal.dao.impl.connection.ConnectionPoolException;
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//public class UserAccessFilter implements Filter {
//	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
//
//	private static final String SQL_REQUEST_TO_SELECT_USER_BY_ID = "SELECT status FROM user WHERE id= ?";
//	private static final String PATH_TO_ERROR_PAGE = "Controller?command=Unknown_Command";
//	private static final String COMMAND_PARAMETER = "command";
//	private static final String USER_ATTRIBUTE = "user";
//	private static final String STATUS_PARAMETER = "status";
//	private static final String LOCKED_USER_STATUS = "locked";
//
//	public void init(FilterConfig fConfig) throws ServletException {
//	}
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse resp = (HttpServletResponse) response;
//
//		HttpSession session = req.getSession(false);
//
//		User user = (User) session.getAttribute(USER_ATTRIBUTE);
//
//		String name = request.getParameter(COMMAND_PARAMETER);
//
//		CommandName commandName;
//
//		try {
//			commandName = CommandName.valueOf(name.toUpperCase());
//
//			if (CommandName.AUTHORIZATION.equals(commandName)) {
//				String login = req.getParameter(name);
//
//			}
//
//			if (!CommandName.GO_TO_MAIN_PAGE.equals(commandName) || !CommandName.AUTHORIZATION.equals(commandName)
//					|| !CommandName.REGISTRATION.equals(commandName)) {
//
//				String userLogin = user.getLogin();
//				String userEMail = user.geteMail();
//
//			}
//
//		} catch (IllegalArgumentException e) {
//			resp.sendRedirect(PATH_TO_ERROR_PAGE);
//			return;
//		}
//
//		try (Connection con = connectionPool.takeConnection();
//				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_USER_BY_ID)) {
//
//			pr.setInt(1, userId);
//			ResultSet rs = pr.executeQuery();
//
//			String status = null;
//			if (!rs.next()) {
//				status = rs.getString(STATUS_PARAMETER);
//			}
//
//			if (LOCKED_USER_STATUS.equals(status)) {
//				resp.sendRedirect(PATH_TO_ERROR_PAGE);
//				return;
//			}
//
//		} catch (SQLException | ConnectionPoolException e) {
//			// TODO
//		}
//		chain.doFilter(request, response);
//	}
//
//	public void destroy() {
//	}
//}
