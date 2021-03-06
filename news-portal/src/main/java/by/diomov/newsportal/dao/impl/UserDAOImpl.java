package by.diomov.newsportal.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

	private static final String SQL_REQUEST_TO_INSERT = "INSERT INTO User(name, surname, email, login, password, role, ability_to_comment) VALUES(?,?,?,?,?,?,?)";
	private static final String SQL_REQUEST_TO_SELECT_BY_LOGIN = "SELECT * FROM user WHERE login = ?";
	private static final String SQL_REQUEST_TO_SELECT_BY_LOGIN_OR_EMAIL = "SELECT * FROM User WHERE login = ? OR eMail = ?";
	private static final String SQL_REQUEST_TO_SELECT_BY_ID = "SELECT * FROM User WHERE id = ?";
	private static final String SQL_REQUEST_TO_CHECK_ABILITY_TO_COMMENT_BY_ID = "SELECT ability_to_comment FROM User WHERE id = ?";
	private static final String SQL_REQUEST_TO_UPDATE_PASSWORD_BY_ID = "UPDATE user SET password = ? WHERE id = ?";
	private static final String SQL_REQUEST_TO_SELECT_LIMITED_AMOUNT_BY_ABILITY_TO_COMMENT = "SELECT * FROM User WHERE ability_to_comment = ? ORDER BY login LIMIT ?, ?";
	private static final String SQL_REQUEST_TO_UPDATE_ABILITY_TO_COMMENT_BY_ID = "UPDATE User SET ability_to_comment = ? WHERE id = ?";
	private static final String SQL_REQUEST_TO_GET_AMOUNT_BY_ABILITY_TO_COMMENT = "SELECT COUNT(*) AS amount  FROM User WHERE ability_to_comment = ?";

	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String SURNAME = "surname";
	private static final String EMAIL = "eMail";
	private static final String LOGIN = "login";
	private static final String PASSWORD = "password";
	private static final String ROLE = "role";
	private static final String AMOUNT = "amount";
	private static final String ABILITY_TO_COMMENT = "ability_to_comment";

	@Override
	public User authorize(String login, String password) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_BY_LOGIN)) {

			User user = null;

			pr.setString(1, login);
			ResultSet rs = pr.executeQuery();

			if (!rs.next()) {
				return user;
			}
			String hashedPassword = rs.getString(PASSWORD);

			if (BCrypt.checkpw(password, hashedPassword)) {
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
			psInsertUser.setString(6, info.getRole().toString());
			psInsertUser.setBoolean(7, true);
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

			pr.setInt(1, id);
			ResultSet rs = pr.executeQuery();

			rs.next();
			RegistrationInfo info = new RegistrationInfo(rs.getString(NAME), rs.getString(SURNAME), rs.getString(EMAIL),
					rs.getString(LOGIN), Role.valueOf(rs.getString(ROLE).toUpperCase()));
			return info;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("An error occurred while trying to get user registration data, UserDAOImpl", e);
		}
	}

	@Override
	public boolean updatePassword(int userId, String password, String newPassword) throws DAOException {
		try (Connection con = ConnectionPool.getInstance().takeConnection();
				PreparedStatement prGetByLogin = con.prepareStatement(SQL_REQUEST_TO_SELECT_BY_ID);
				PreparedStatement prSetNewPassword = con.prepareStatement(SQL_REQUEST_TO_UPDATE_PASSWORD_BY_ID)) {

			prGetByLogin.setInt(1, userId);
			ResultSet rs = prGetByLogin.executeQuery();

			rs.next();
			String hashedPasswordFromDB = rs.getString(PASSWORD);

			if (BCrypt.checkpw(password, hashedPasswordFromDB)) {
				String salt = BCrypt.gensalt();
				String hashedNewpassword = BCrypt.hashpw(newPassword, salt);

				prSetNewPassword.setString(1, hashedNewpassword);
				prSetNewPassword.setInt(2, userId);
				prSetNewPassword.executeUpdate();

				return true;
			}
			return false;
		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to update password, UserDAOImpl", e);
		}
	}

	@Override
	public boolean isBlockedToComment(int id) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_CHECK_ABILITY_TO_COMMENT_BY_ID)) {

			pr.setInt(1, id);
			ResultSet rs = pr.executeQuery();

			rs.next();
			return rs.getBoolean(ABILITY_TO_COMMENT);
		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("An error occurred while trying to get user  ability to comment , UserDAOImpl", e);
		}
	}

	@Override
	public void setAbilityToComment(boolean ability, int id) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_UPDATE_ABILITY_TO_COMMENT_BY_ID)) {

			pr.setBoolean(1, ability);
			pr.setInt(2, id);
			pr.executeUpdate();

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("An error occurred while trying to get user  ability to comment , UserDAOImpl", e);
		}
	}

	@Override
	public List<User> getLimitedAmountByAbilityToComment(boolean ability, int start, int limit) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con
						.prepareStatement(SQL_REQUEST_TO_SELECT_LIMITED_AMOUNT_BY_ABILITY_TO_COMMENT)) {

			pr.setBoolean(1, ability);
			pr.setInt(2, start);
			pr.setInt(3, limit);

			ResultSet rs = pr.executeQuery();

			List<User> list = new ArrayList<>();

			while (rs.next()) {
				User user = new User(rs.getInt(ID), rs.getString(LOGIN),
						Role.valueOf(rs.getString(ROLE).toUpperCase()));
				list.add(user);
			}
			return list;
		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error in getLimitedAmountByAbilityToComment, UserDAOImpl", e);
		}
	}

	@Override
	public int getAmountByAbilityToComment(boolean ability) throws DAOException {

		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_GET_AMOUNT_BY_ABILITY_TO_COMMENT)) {
			pr.setBoolean(1, ability);
			ResultSet rs = pr.executeQuery();

			rs.next();
			return rs.getInt(AMOUNT);
		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to get favourite news, NewsDAOImpl", e);
		}
	}
}
