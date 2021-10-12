package by.diomov.newsportal.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import by.diomov.newsportal.bean.Comment;
import by.diomov.newsportal.bean.News;
import by.diomov.newsportal.bean.Role;
import by.diomov.newsportal.bean.User;
import by.diomov.newsportal.dao.DAOException;
import by.diomov.newsportal.dao.NewsDAO;
import by.diomov.newsportal.dao.impl.connection.ConnectionPool;
import by.diomov.newsportal.dao.impl.connection.ConnectionPoolException;
import jakarta.servlet.jsp.jstl.sql.Result;

public class NewsDAOImpl implements NewsDAO {
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
	private static final String SQL_REQUEST_TO_INSERT_NEWS = "INSERT INTO news(title, brief, content, userId) VALUE(?,?,?,?)";
	private static final String SQL_REQUEST_TO_INSERT_NEWS_TO_FAVOURITE = "INSERT INTO favourite_news(userId, newsId) VALUE(?,?)";
	private static final String SQL_REQUEST_TO_DELETE_NEWS_FROM_FAVOURITE = "DELETE FROM favourite_news WHERE userId=? AND newsId=?";
	private static final String SQL_REQUEST_TO_UPDATE_NEWS_BY_ID = "UPDATE news SET title=?, brief =?, content= ?, userId =? WHERE id =?";
	private static final String SQL_REQUEST_TO_DELETE_NEWS_BY_ID = "DELETE FROM news WHERE id=?";
	private static final String SQL_REQUEST_TO_SELECT_ALL_NEWS = "SELECT * FROM news ORDER BY id DESC";
	private static final String SQL_REQUEST_TO_SELECT_NEWS_BY_WORD_IN_TITLE = "SELECT * FROM news WHERE title REGEXP ?";
	private static final String SQL_REQUEST_TO_SELECT_AMOUNT_FAVOURITES_NEWS = "SELECT COUNT(*) AS amount FROM favourite_news WHERE userId=?";
	private static final String SQL_REQUEST_TO_SELECT_AMOUNT_NEWS = "SELECT COUNT(*) AS amount FROM News";
	private static final String SQL_REQUEST_TO_SELECT_LIMITED_LIST = "SELECT * FROM news ORDER BY id DESC  LIMIT ?, ?";
	private static final String SQL_REQUEST_TO_SELECT_FAVOURITES_LIMITED_LIST_NEWS = "SELECT news.id,News.title, News.brief, News.content,News.userId FROM News JOIN Favourite_News ON News.id=Favourite_news.newsId WHERE Favourite_News.userId=? ORDER BY newsId DESC LIMIT ?, ?";
	private static final String SQL_REQUEST_TO_SELECT_NEWS_BY_ID = "SELECT * FROM news WHERE id=?";
	private static final String SQL_REQUEST_TO_SELECT_BY_USER_ID_AND_NEWS_ID = "SELECT * FROM favourite_news WHERE userId=? AND newsId=?";

	private static final String AMOUNT = "amount";

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

//	@Override
//	public List<News> getAll() throws DAOException {
//		try (Connection con = connectionPool.takeConnection();
//				Statement st = con.createStatement();
//				ResultSet rs = st.executeQuery(SQL_REQUEST_TO_SELECT_ALL_NEWS)) {
//
//			List<News> list = new ArrayList<>();
//
//			while (rs.next()) {
//				News news = new News(rs.getInt("id"), rs.getString("title"), rs.getString("brief"),
//						rs.getString("content"), rs.getInt("userId"));
//				list.add(news);
//			}
//			return list;
//
//		} catch (SQLException | ConnectionPoolException e) {
//			throw new DAOException("Error occurred while trying to get all news, NewsDAOImpl", e);
//		}
//	}

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

	@Override
	public void addToFavourite(int userId, int newsId) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement prCheck = con.prepareStatement(SQL_REQUEST_TO_SELECT_BY_USER_ID_AND_NEWS_ID);
				PreparedStatement prInsert = con.prepareStatement(SQL_REQUEST_TO_INSERT_NEWS_TO_FAVOURITE)) {

			prCheck.setInt(1, userId);
			prCheck.setInt(2, newsId);
			ResultSet rs = prCheck.executeQuery();

			if (!rs.next()) {
				prInsert.setInt(1, userId);
				prInsert.setInt(2, newsId);
				prInsert.executeUpdate();
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to add news to favourite, NewsDAOImpl", e);
		}
	}

//	@Override
//	public List<News> getFavourite(int userId) throws DAOException {
//		try (Connection con = connectionPool.takeConnection();
//				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_FAVOURITE_NEWS)) {
//
//			pr.setInt(1, userId);
//			ResultSet rs = pr.executeQuery();
//
//			List<News> list = new ArrayList<>();
//
//			while (rs.next()) {
//				News news = new News(rs.getInt("id"), rs.getString("title"), rs.getString("brief"),
//						rs.getString("content"), rs.getInt("userId"));
//				list.add(news);
//			}
//			return list;
//
//		} catch (SQLException | ConnectionPoolException e) {
//			throw new DAOException("Error occurred while trying to get favourite news, NewsDAOImpl", e);
//		}
//	}

	@Override
	public void deleteFromFavourite(int userId, int newsId) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_DELETE_NEWS_FROM_FAVOURITE)) {

			pr.setInt(1, userId);
			pr.setInt(2, newsId);
			pr.executeUpdate();

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to delete news from favourite, NewsDAOImpl", e);
		}
	}

	@Override
	public List<News> getLimitedList(int from, int amount) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_LIMITED_LIST)) {

			pr.setInt(1, from);
			pr.setInt(2, amount);

			ResultSet rs = pr.executeQuery();

			List<News> list = new ArrayList<>();

			while (rs.next()) {
				News news = new News(rs.getInt("id"), rs.getString("title"), rs.getString("brief"),
						rs.getString("content"), rs.getInt("userId"));
				list.add(news);
			}
			return list;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error in getLimitedList, NewsDAOImpl", e);
		}
	}

	@Override
	public int getAmountNews() throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_AMOUNT_NEWS)) {

			ResultSet rs = pr.executeQuery();

			int amount = 0;
			if (rs.next()) {
				amount = rs.getInt(AMOUNT);
			}
			return amount;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error in getAmountNews, NewsDAOImpl", e);
		}
	}

	@Override
	public List<News> getFavouritesLimitedList(int userId, int from, int amount) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_FAVOURITES_LIMITED_LIST_NEWS)) {

			pr.setInt(1, userId);
			pr.setInt(2, from);
			pr.setInt(3, amount);
			ResultSet rs = pr.executeQuery();

			List<News> list = new ArrayList<>();

			while (rs.next()) {
				News news = new News(rs.getInt("id"), rs.getString("title"), rs.getString("brief"),
						rs.getString("content"), rs.getInt("userId"));
				list.add(news);
			}
			return list;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to get favourites limited list news, NewsDAOImpl", e);
		}
	}

	@Override
	public int getAmountFavouritesNewsByUserId(int id) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_AMOUNT_FAVOURITES_NEWS)) {

			pr.setInt(1, id);
			ResultSet rs = pr.executeQuery();

			int amount = 0;
			if (rs.next()) {
				return rs.getInt(AMOUNT);
			}
			return amount;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to get favourite news, NewsDAOImpl", e);
		}
	}

	@Override
	public List<News> findNewsByWordInTitle(String word) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_NEWS_BY_WORD_IN_TITLE)) {

			pr.setString(1, word);
			ResultSet rs = pr.executeQuery();

			List<News> list = new ArrayList<>();

			while (rs.next()) {
				News news = new News(rs.getInt("id"), rs.getString("title"), rs.getString("brief"),
						rs.getString("content"), rs.getInt("userId"));
				list.add(news);
			}
			return list;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to search news by the word in the title, NewsDAOImpl",
					e);
		}
	}
}
