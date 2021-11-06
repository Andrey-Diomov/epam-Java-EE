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
import by.diomov.newsportal.dao.CommentDAO;
import by.diomov.newsportal.dao.DAOException;
import by.diomov.newsportal.dao.impl.connection.ConnectionPoolException;
import by.diomov.newsportal.dao.impl.connection.ConnectionPool;

public class CommentDAOImpl implements CommentDAO {
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

	private static final String SQL_REQUEST_TO_INSERT = "INSERT INTO Comment(text, created, userId, newsId) VALUE(?,?,?,?)";
	private static final String SQL_REQUEST_TO_SELECT_LIMITED_AMOUNT_BY_NEWS_ID = "SELECT Comment.id, text, created, User.login FROM Comment JOIN User ON Comment.userId = User.id WHERE newsId = ? ORDER by comment.id DESC LIMIT ?, ? ";
	private static final String SQL_REQUEST_TO_SELECT_AMOUNT_BY_NEWS_ID = "SELECT COUNT(*) AS amount FROM Comment WHERE newsId = ?";
	private static final String SQL_REQUEST_TO_DELETE_COMMENTS_BY_NEWS_ID = "DELETE  FROM comment WHERE newsId = ?";

	private static final String ID = "id";
	private static final String TEXT_COMMENT = "text";
	private static final String CREATED_COMMENT = "created";
	private static final String AMOUNT = "amount";
	private static final String LOGIN_USER = "login";

	@Override
	public List<Comment> getLimitedAmountCommentsByNewsId(int start, int limit, int id) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_LIMITED_AMOUNT_BY_NEWS_ID)) {

			pr.setInt(1, id);
			pr.setInt(2, start);
			pr.setInt(3, limit);

			ResultSet rs = pr.executeQuery();

			List<Comment> list = new ArrayList<>();

			while (rs.next()) {
				Comment comment = new Comment(rs.getInt(ID), rs.getString(TEXT_COMMENT), rs.getDate(CREATED_COMMENT),
						rs.getString(LOGIN_USER));
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
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_INSERT);) {
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

	@Override
	public int getAmountCommentsByNewsId(int id) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_AMOUNT_BY_NEWS_ID)) {

			pr.setInt(1, id);
			ResultSet rs = pr.executeQuery();

			int amount = 0;
			if (rs.next()) {
				amount = rs.getInt(AMOUNT);
			}
			return amount;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error in getAmountCommentsByNewsId, CommentDAOImpl", e);
		}
	}
}
