package by.diomov.newsportal.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import by.diomov.newsportal.bean.News;
import by.diomov.newsportal.dao.DAOException;
import by.diomov.newsportal.dao.NewsDAO;
import by.diomov.newsportal.dao.impl.connection.ConnectionPool;
import by.diomov.newsportal.dao.impl.connection.ConnectionPoolException;

public class NewsDAOImpl implements NewsDAO {
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
	private static final String SQL_REQUEST_TO_INSERT_NEWS = "INSERT INTO news(title, brief, content, userId) VALUE(?,?,?,?)";
	private static final String SQL_REQUEST_TO_UPDATE_NEWS_BY_ID = "UPDATE news SET title=?, brief =?, content= ?, userId =? WHERE id =?";
	private static final String SQL_REQUEST_TO_DELETE_NEWS_BY_ID = "DELETE FROM news WHERE id=?";
	private static final String SQL_REQUEST_TO_SELECT_ALL_NEWS = "SELECT * FROM news";
	private static final String SQL_REQUEST_TO_SELECT_NEWS_BY_ID = "SELECT * FROM news WHERE id=?";

	@Override
	public void save(News news) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_INSERT_NEWS);) {
			pr.setString(1, news.getTitle());
			pr.setString(2, news.getBrief());
			pr.setString(3, news.getContent());
			pr.setInt(4, news.getUserId());

			pr.executeUpdate();
		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to save news, NewsDAOImpl", e);
		}
	}

	@Override
	public void update(News news) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_UPDATE_NEWS_BY_ID)) {
			pr.setString(1, news.getTitle());
			pr.setString(2, news.getBrief());
			pr.setString(3, news.getContent());
			pr.setInt(4, news.getUserId());
			pr.setInt(5, news.getId());
			pr.executeUpdate();

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to update news, NewsDAOImpl", e);
		}
	}

	@Override
	public void delete(int id) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_DELETE_NEWS_BY_ID)) {

			pr.setInt(1, id);
			pr.executeUpdate();

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to delete news, NewsDAOImpl", e);
		}
	}

	@Override
	public List<News> getAll() throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(SQL_REQUEST_TO_SELECT_ALL_NEWS)) {

			List<News> list = new ArrayList<>();

			while (rs.next()) {
				News news = new News(rs.getInt("id"), rs.getString("title"), rs.getString("brief"),
						rs.getString("content"), rs.getInt("userId"));			
				list.add(news);
			}
			return list;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to get all news, NewsDAOImpl", e);
		}
	}

	@Override
	public News get(int id) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_NEWS_BY_ID)) {

			pr.setInt(1, id);
			ResultSet rs = pr.executeQuery();
			News news = null;

			while (rs.next()) {
				news = new News(rs.getInt("id"), rs.getString("title"), rs.getString("brief"), rs.getString("content"),
						rs.getInt("userId"));
			}
			return news;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to get news by id, NewsDAOImpl", e);
		}
	}
}
