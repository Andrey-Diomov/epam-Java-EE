package by.diomov.newsportal.dao;

import by.diomov.newsportal.bean.User;
import java.util.List;

import by.diomov.newsportal.bean.RegistrationInfo;

public interface UserDAO {
	User authorize(String login, String password) throws DAOException;

	boolean register(RegistrationInfo info) throws DAOException;

	boolean updatePassword(String login, String oldPassword, String newPassword) throws DAOException;

	RegistrationInfo getRegistrationInfo(int id) throws DAOException;

	boolean isBlockedToComment(int id) throws DAOException;

	void setAbilityToComment(boolean ability, int id) throws DAOException;

	List<User> getLimitedAmountByAbilityToComment(boolean ability, int start, int limit) throws DAOException;

	int getAmountByAbilityToComment(boolean ability) throws DAOException;
}
