package by.diomov.newsportal.service.impl;

import by.diomov.newsportal.bean.RegistrationInfo;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.dao.DAOException;
import by.diomov.newsportal.dao.DAOProvider;
import by.diomov.newsportal.dao.UserDAO;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.UserService;
import by.diomov.newsportal.service.impl.validator.UserValidator;
import by.diomov.newsportal.service.impl.validator.ValidationException;

public class UserServiceImpl implements UserService {
	private static final DAOProvider provider = DAOProvider.getInstance();
	private final UserDAO userDAO = provider.getUserDAO();

	@Override
	public boolean registration(RegistrationInfo info) throws ServiceException, ValidationException {
		String error = UserValidator.validateRegistrationInfo(info);

		if (!error.isEmpty()) {
			throw new ValidationException(error);
		}

		try {
			return userDAO.register(info);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public User authorization(String login, String password) throws ServiceException, ValidationException {

		String error = UserValidator.validateLoginAndPassword(login, password);
		if (!error.isEmpty()) {
			throw new ValidationException(error);
		}

		try {
			return userDAO.authorize(login, password);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateLogin(String login) throws ServiceException {
		try {
			userDAO.updateLogin(login);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updatePassword(int userId, String password) throws ServiceException {
		try {
			userDAO.updatePassword(userId, password);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public RegistrationInfo getRegistrationInfo(int id) throws ServiceException {
		try {
			return userDAO.getRegistrationInfo(id);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}
}