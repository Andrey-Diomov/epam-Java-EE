package by.diomov.newsportal.service;

import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.service.exception.ServiceException;
import by.diomov.newsportal.service.exception.ValidationException;
import by.diomov.newsportal.bean.RegistrationInfo;

public interface UserService {

	boolean registration(RegistrationInfo info) throws ServiceException, ValidationException;

	User authorization(String login, String password) throws ServiceException, ValidationException;

	void updateLogin(String login) throws ServiceException;

	void updatePassword(int userId, String password) throws ServiceException;

	RegistrationInfo getRegistrationInfo(int id) throws ServiceException;
}
