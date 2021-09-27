package by.diomov.newsportal.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import by.diomov.newsportal.bean.RegistrationInfo;
import by.diomov.newsportal.bean.Role;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.dao.DAOException;
import by.diomov.newsportal.dao.UserDAO;
import by.diomov.newsportal.dao.impl.connection.ConnectionPoolException;
import by.diomov.newsportal.dao.impl.connection.ConnectionPool;

public class UserDAOImpl implements UserDAO {
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
	private static final ReentrantLock lock = new ReentrantLock();

	private static final String SQL_REQUEST_TO_SELECT_BY_LOGIN_AND_PASSWORD = "SELECT * FROM user WHERE login= ? AND password = ?";
	private static final String SQL_REQUEST_TO_SELECT_BY_LOGIN_OR_EMAIL = "SELECT * FROM user WHERE login= ? OR eMail = ?";
	private static final String SQL_REQUEST_TO_SELECT_USER_BY_ID = "SELECT * FROM user WHERE id= ?";
	private static final String SQL_REQUEST_TO_INSERT_INTO_USER = "INSERT INTO user(name, surname, email, login, password, role) VALUES(?,?,?,?,?,?)";
	private static final String SQL_REQUEST_TO_UPDATE_PASSWORD_BY_ID = "UPDATE user SET password =? WHERE id=?";

	private static final String ID_USER = "id";
	private static final String NAME_USER = "name";
	private static final String SURNAME_USER = "surname";
	private static final String EMAIL_USER = "eMail";
	private static final String LOGIN_USER = "login";
	private static final String ROLE_USER = "role";

	@Override
	public User authorize(String login, String password) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_BY_LOGIN_AND_PASSWORD)) {

			User user = null;

			pr.setString(1, login);
			pr.setString(2, password);
			ResultSet rs = pr.executeQuery();

			if (!rs.next()) {
				return user;
			}

			user = new User(rs.getInt(ID_USER), rs.getString(LOGIN_USER),
					Role.valueOf(rs.getString(ROLE_USER).toUpperCase()));
			return user;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to authorize a user, UserDAOImpl", e);
		}
	}

	@Override
	public boolean register(RegistrationInfo info) throws DAOException {

		try (Connection con = connectionPool.takeConnection();
				PreparedStatement psByLoginOrEmail = con.prepareStatement(SQL_REQUEST_TO_SELECT_BY_LOGIN_OR_EMAIL);
				PreparedStatement psInsertUser = con.prepareStatement(SQL_REQUEST_TO_INSERT_INTO_USER);) {

			lock.lock();
			psByLoginOrEmail.setString(1, info.getLogin());
			psByLoginOrEmail.setString(2, info.geteMail());
			ResultSet rsByLoginOrEmail = psByLoginOrEmail.executeQuery();

			if (rsByLoginOrEmail.next()) {
				return false;
			}

			psInsertUser.setString(1, info.getName());
			psInsertUser.setString(2, info.getSurname());
			psInsertUser.setString(3, info.geteMail());
			psInsertUser.setString(4, info.getLogin());
			psInsertUser.setString(5, info.getPassword());
			psInsertUser.setString(6, info.getRole().toString());
			psInsertUser.executeUpdate();
			return true;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to register a user, UserDAOImpl", e);

		} finally {
			lock.unlock();
		}
	}

	@Override
	public RegistrationInfo getRegistrationInfo(int id) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_USER_BY_ID)) {

			RegistrationInfo info = null;

			pr.setInt(1, id);
			ResultSet rs = pr.executeQuery();

			if (!rs.next()) {
				return info;
			}

			info = new RegistrationInfo(rs.getString(NAME_USER), rs.getString(SURNAME_USER), rs.getString(EMAIL_USER),
					rs.getString(LOGIN_USER), Role.valueOf(rs.getString(ROLE_USER).toUpperCase()));
			return info;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("An error occurred while trying to get user registration data, UserDAOImpl", e);
		}
	}
	

	@Override
	public void updatePassword(int userId, String password) throws DAOException {
		try (Connection con = ConnectionPool.getInstance().takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_UPDATE_PASSWORD_BY_ID)) {
			pr.setString(1, password);
			pr.setInt(2, userId);
			pr.executeUpdate();
		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to update password, UserDAOImpl", e);
		}
	}
}
