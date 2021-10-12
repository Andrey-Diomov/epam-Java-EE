package by.diomov.newsportal.service;

import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.service.impl.validator.ValidationException;
import by.diomov.newsportal.bean.RegistrationInfo;

public interface UserService {

	boolean registration(RegistrationInfo info) throws ServiceException, ValidationException;

	User authorization(String login, String password) throws ServiceException, ValidationException;

	void updatePassword(int userId, String password) throws ServiceException;

	RegistrationInfo getRegistrationInfo(int id) throws ServiceException;

	boolean isBlockedToComment(int id) throws ServiceException;

	void setAbilityToComment(boolean abilityToComment, int id) throws ServiceException;
}
