package by.diomov.newsportal.dao;

import by.diomov.newsportal.dao.impl.CommentDAOImpl;
import by.diomov.newsportal.dao.impl.NewsDAOImpl;
import by.diomov.newsportal.dao.impl.UserDAOImpl;

public final class DAOProvider {
	private static final DAOProvider instance = new DAOProvider();

	private final UserDAO userDAO = new UserDAOImpl();
	private final NewsDAO newsDAO = new NewsDAOImpl();
	private final CommentDAO commentDAO = new CommentDAOImpl();

	private DAOProvider() {
	}

	public static DAOProvider getInstance() {
		return instance;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public NewsDAO getNewsDAO() {
		return newsDAO;
	}

	public CommentDAO getCommentDAO() {
		return commentDAO;
	}
}
