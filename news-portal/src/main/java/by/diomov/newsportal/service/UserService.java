package by.diomov.newsportal.service;

import by.diomov.newsportal.bean.User;
import java.util.List;
import by.diomov.newsportal.bean.RegistrationInfo;

public interface UserService {

	boolean registration(RegistrationInfo info) throws ServiceException;

	User authorization(String login, String password) throws ServiceException;

	boolean updatePassword(int userId, String password, String newPassword) throws ServiceException;

	RegistrationInfo getRegistrationInfo(int id) throws ServiceException;

	boolean isBlockedToComment(int id) throws ServiceException;

	void setAbilityToComment(boolean ability, int id) throws ServiceException;

	List<User> getLimitedAmountByAbilityToComment(boolean ability, int start, int limit) throws ServiceException;

	int getAmountByAbilityToComment(boolean ability) throws ServiceException;
}
