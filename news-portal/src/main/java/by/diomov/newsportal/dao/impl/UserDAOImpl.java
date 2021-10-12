package by.diomov.newsportal.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import org.mindrot.jbcrypt.BCrypt;

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

	private static final String SQL_REQUEST_TO_INSERT = "INSERT INTO user(name, surname, email, login, password, password_salt, role) VALUES(?,?,?,?,?,?)";
	private static final String SQL_REQUEST_TO_SELECT_BY_LOGIN_AND_PASSWORD = "SELECT * FROM user WHERE login = ?";
	private static final String SQL_REQUEST_TO_SELECT_BY_LOGIN_OR_EMAIL = "SELECT * FROM user WHERE login = ? OR eMail = ?";
	private static final String SQL_REQUEST_TO_SELECT_BY_ID = "SELECT * FROM user WHERE id = ?";
	private static final String SQL_REQUEST_TO_SELECT_ABILITY_TO_COMMENT_BY_ID = "SELECT ability_to_comment FROM user WHERE id = ?";
	private static final String SQL_REQUEST_TO_UPDATE_PASSWORD_BY_ID = "UPDATE user SET password = ? WHERE id = ?";
	private static final String SQL_REQUEST_TO_UPDATE_ABILITY_TO_COMMENT_BY_ID = "UPDATE user SET ability_to_comment = ? WHERE id = ?";

	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String SURNAME = "surname";
	private static final String EMAIL = "eMail";
	private static final String LOGIN = "login";
	private static final String PASSWORD_SALT = "password_salt";
	private static final String PASSWORD = "password";
	private static final String ROLE = "role";
	private static final String ABILITY_TO_COMMENT = "ability_to_comment";

	@Override
	public User authorize(String login, String password) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_BY_LOGIN_AND_PASSWORD)) {

			User user = null;

			pr.setString(1, login);
			ResultSet rs = pr.executeQuery();

			if (!rs.next()) {
				return user;
			}

			String saltFromDB = rs.getString(PASSWORD_SALT);
			String passwordFromDB = rs.getString(PASSWORD);

			String hashEnteredPassword = BCrypt.hashpw(password, saltFromDB);
			if (hashEnteredPassword.equals(passwordFromDB)) {
				user = new User(rs.getInt(ID), rs.getString(LOGIN), Role.valueOf(rs.getString(ROLE).toUpperCase()));
			}

			return user;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to authorize a user, UserDAOImpl", e);
		}
	}

	@Override
	public boolean register(RegistrationInfo info) throws DAOException {

		try (Connection con = connectionPool.takeConnection();
				PreparedStatement psByLoginOrEmail = con.prepareStatement(SQL_REQUEST_TO_SELECT_BY_LOGIN_OR_EMAIL);
				PreparedStatement psInsertUser = con.prepareStatement(SQL_REQUEST_TO_INSERT);) {

			lock.lock();
			psByLoginOrEmail.setString(1, info.getLogin());
			psByLoginOrEmail.setString(2, info.geteMail());
			ResultSet rsByLoginOrEmail = psByLoginOrEmail.executeQuery();

			if (rsByLoginOrEmail.next()) {
				return false;
			}

			String salt = BCrypt.gensalt();
			String hashpw = BCrypt.hashpw(info.getPassword(), salt);

			psInsertUser.setString(1, info.getName());
			psInsertUser.setString(2, info.getSurname());
			psInsertUser.setString(3, info.geteMail());
			psInsertUser.setString(4, info.getLogin());
			psInsertUser.setString(5, hashpw);
			psInsertUser.setString(6, salt);
			psInsertUser.setString(7, info.getRole().toString());
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
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_BY_ID)) {

			RegistrationInfo info = null;

			pr.setInt(1, id);
			ResultSet rs = pr.executeQuery();

			if (!rs.next()) {
				return info;
			}

			info = new RegistrationInfo(rs.getString(NAME), rs.getString(SURNAME), rs.getString(EMAIL),
					rs.getString(LOGIN), Role.valueOf(rs.getString(ROLE).toUpperCase()));
			return info;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("An error occurred while trying to get user registration data, UserDAOImpl", e);
		}
	}

	@Override
	public void updatePassword(int id, String password) throws DAOException {
		try (Connection con = ConnectionPool.getInstance().takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_UPDATE_PASSWORD_BY_ID)) {
			pr.setString(1, password);
			pr.setInt(2, id);
			pr.executeUpdate();
		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to update password, UserDAOImpl", e);
		}
	}

	@Override
	public boolean isBlockedToComment(int id) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_ABILITY_TO_COMMENT_BY_ID)) {

			pr.setInt(1, id);
			ResultSet rs = pr.executeQuery();
			rs.next();

			return rs.getBoolean(ABILITY_TO_COMMENT);

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("An error occurred while trying to get user  ability to comment , UserDAOImpl", e);
		}
	}

	@Override
	public void setAbilityToComment(boolean abilityToComment, int id) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_UPDATE_ABILITY_TO_COMMENT_BY_ID)) {

			pr.setBoolean(1, abilityToComment);
			pr.setInt(2, id);
			pr.executeUpdate();

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("An error occurred while trying to get user  ability to comment , UserDAOImpl", e);
		}
	}
}
