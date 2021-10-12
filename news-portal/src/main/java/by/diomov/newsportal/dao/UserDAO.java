package by.diomov.newsportal.dao;

import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.bean.RegistrationInfo;

public interface UserDAO {
	User authorize(String login, String password) throws DAOException;

	boolean register(RegistrationInfo info) throws DAOException;

	void updatePassword(int id, String password) throws DAOException;

	RegistrationInfo getRegistrationInfo(int id) throws DAOException;

	boolean isBlockedToComment(int id) throws DAOException;

	void setAbilityToComment(boolean abilityToComment, int id) throws DAOException;
}
