package by.diomov.newsportal.service.impl;

import java.util.List;

import by.diomov.newsportal.bean.RegistrationInfo;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.dao.DAOException;
import by.diomov.newsportal.dao.DAOProvider;
import by.diomov.newsportal.dao.UserDAO;
import by.diomov.newsportal.service.ServiceException;
import by.diomov.newsportal.service.UserService;

public class UserServiceImpl implements UserService {
	private static final DAOProvider provider = DAOProvider.getInstance();
	private final UserDAO userDAO = provider.getUserDAO();

	@Override
	public boolean registration(RegistrationInfo info) throws ServiceException {
		try {
			return userDAO.register(info);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public User authorization(String login, String password) throws ServiceException {
		try {
			return userDAO.authorize(login, password);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean updatePassword(int userId, String password, String newPassword) throws ServiceException {
		try {
			return userDAO.updatePassword(userId, password, newPassword);
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

	@Override
	public boolean isBlockedToComment(int id) throws ServiceException {
		try {
			return userDAO.isBlockedToComment(id);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void setAbilityToComment(boolean ability, int id) throws ServiceException {
		try {
			userDAO.setAbilityToComment(ability, id);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<User> getLimitedAmountByAbilityToComment(boolean ability, int start, int limit)
			throws ServiceException {
		try {
			return userDAO.getLimitedAmountByAbilityToComment(ability, start, limit);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int getAmountByAbilityToComment(boolean ability) throws ServiceException {
		try {
			return userDAO.getAmountByAbilityToComment(ability);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}
}
