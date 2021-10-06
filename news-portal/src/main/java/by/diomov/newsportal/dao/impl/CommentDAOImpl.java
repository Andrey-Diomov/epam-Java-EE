package by.diomov.newsportal.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import by.diomov.newsportal.bean.Comment;
import by.diomov.newsportal.bean.Role;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.dao.CommentDAO;
import by.diomov.newsportal.dao.DAOException;
import by.diomov.newsportal.dao.impl.connection.ConnectionPoolException;
import by.diomov.newsportal.dao.impl.connection.ConnectionPool;

public class CommentDAOImpl implements CommentDAO {
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	private static final String SQL_REQUEST_TO_INSERT_COMMENT = "INSERT INTO comment(text, created, userId, newsId) VALUE(?,?,?,?)";
	private static final String SQL_REQUEST_TO_SELECT_LIMITED_LIST_COMMENTS_BY_NEWS_ID = "SELECT Comment.id, text, created, userId, newsId, User.id, login, role  FROM comment JOIN User ON comment.userId=User.id WHERE newsId=? LIMIT ?, ?";
	private static final String SQL_REQUEST_TO_DELETE_COMMENTS_BY_NEWS_ID = "DELETE  FROM comment WHERE newsId=?";

	private static final String ID = "id";
	private static final String TEXT_COMMENT = "text";
	private static final String CREATED_COMMENT = "created"; 
	private static final String ID_USER = "userId";
	private static final String ID_NEWS = "newsId";

	private static final String LOGIN_USER = "login";
	private static final String ROLE_USER = "role";

	@Override
	public List<Comment> getLimitedListCommentsByNewsId(int from, int amount, int id) throws DAOException {

		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_LIMITED_LIST_COMMENTS_BY_NEWS_ID)) {

			pr.setInt(1, id);
			pr.setInt(2, from);
			pr.setInt(3, amount);

			ResultSet rs = pr.executeQuery();

			List<Comment> list = new ArrayList<>();

			while (rs.next()) {
				Comment comment = new Comment(rs.getInt(ID), rs.getString(TEXT_COMMENT), rs.getDate(CREATED_COMMENT),
						rs.getInt(ID_USER), rs.getInt(ID_NEWS), new User(rs.getInt(ID), rs.getString(LOGIN_USER),
								Role.valueOf(rs.getString(ROLE_USER).toUpperCase())));
				list.add(comment);
			}
			return list;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error in getAllByNewsId, CommentDAOImpl", e);
		}
	}

	@Override
	public void save(Comment comment) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_INSERT_COMMENT);) {
			pr.setString(1, comment.getText());
			pr.setDate(2, Date.valueOf(LocalDate.now()));
			pr.setInt(3, comment.getUserId());
			pr.setInt(4, comment.getNewsId());

			pr.executeUpdate();
		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to save comment, CommentDAOImpl", e);
		}
	}

	@Override
	public void deleteAllByNewsId(int id) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_DELETE_COMMENTS_BY_NEWS_ID)) {

			pr.setInt(1, id);
			pr.executeUpdate();

		} catch (SQLException | ConnectionPoolException e) {
			new DAOException("Error in deleteAllByNewsId, CommentDAOImpl", e);
		}
	}
}
