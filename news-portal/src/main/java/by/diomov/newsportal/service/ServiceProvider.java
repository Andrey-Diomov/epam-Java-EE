package by.diomov.newsportal.service;

import by.diomov.newsportal.service.impl.CommentSeviceImpl;
import by.diomov.newsportal.service.impl.NewsServiceImpl;
import by.diomov.newsportal.service.impl.UserServiceImpl;

public final class ServiceProvider {
	private static final ServiceProvider instance = new ServiceProvider();

	private final UserService userService = new UserServiceImpl();
	private final NewsService newService = new NewsServiceImpl();
	private final CommentService commentService = new CommentSeviceImpl();

	private ServiceProvider() {
	}

	public static ServiceProvider getInstance() {
		return instance;
	}

	public UserService getUserService() {
		return userService;
	}

	public NewsService getNewsService() {
		return newService;
	}

	public CommentService getCommentService() {
		return commentService;
	}
}
