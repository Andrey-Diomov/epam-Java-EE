package by.diomov.newsportal.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import by.diomov.newsportal.bean.News;
import by.diomov.newsportal.dao.DAOException;
import by.diomov.newsportal.dao.NewsDAO;
import by.diomov.newsportal.dao.impl.connection.ConnectionPool;
import by.diomov.newsportal.dao.impl.connection.ConnectionPoolException;

public class NewsDAOImpl implements NewsDAO {
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
	private static final String SQL_REQUEST_TO_INSERT = "INSERT INTO News(title, brief, content, userId) VALUE(?,?,?,?)";
	private static final String SQL_REQUEST_TO_INSERT_TO_FAVOURITE = "INSERT INTO Favourite_news(userId, newsId) VALUE(?,?)";
	private static final String SQL_REQUEST_TO_DELETE_FROM_FAVOURITE = "DELETE FROM Favourite_news WHERE userId = ? AND newsId = ?";
	private static final String SQL_REQUEST_TO_UPDATE_BY_ID = "UPDATE News SET title = ?, brief = ?, content = ?, userId = ? WHERE id = ?";
	private static final String SQL_REQUEST_TO_DELETE_BY_ID = "DELETE FROM News WHERE id=?";
	private static final String SQL_REQUEST_TO_SELECT_BY_WORD_IN_TITLE = "SELECT * FROM News WHERE title REGEXP ?";
	private static final String SQL_REQUEST_TO_SELECT_AMOUNT_FAVOURITES_NEWS = "SELECT COUNT(*) AS amount FROM Favourite_news WHERE userId = ?";
	private static final String SQL_REQUEST_TO_SELECT_AMOUNT_NEWS = "SELECT COUNT(*) AS amount FROM News";
	private static final String SQL_REQUEST_TO_SELECT_LIMITED_LIST = "SELECT * FROM news ORDER BY id DESC  LIMIT ?, ?";
	private static final String SQL_REQUEST_TO_SELECT_FAVOURITES_LIMITED_AMOUNT_NEWS = "SELECT News.id, News.title, News.brief, News.content, News.userId FROM News JOIN Favourite_News ON News.id = Favourite_news.newsId WHERE Favourite_News.userId = ? ORDER BY newsId DESC LIMIT ?, ?";
	private static final String SQL_REQUEST_TO_SELECT_NEWS_BY_ID = "SELECT * FROM News WHERE id = ?";
	private static final String SQL_REQUEST_TO_SELECT_BY_USER_ID_AND_NEWS_ID = "SELECT * FROM Favourite_news WHERE userId = ? AND newsId = ?";
	private static final String SQL_REQUEST_TO_CHECK_IS_FAVOURITE = "SELECT COUNT(1) as result FROM Favourite_news WHERE userId = ? AND newsId = ?";

	private static final String ID = "id";
	private static final String TITLE = "title";
	private static final String BRIEF = "brief";
	private static final String CONTENT = "content";
	private static final String USER_ID = "userId";
	private static final String AMOUNT = "amount";
	private static final String RESULT = "result";

	@Override
	public void save(News news) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_INSERT);) {
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
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_UPDATE_BY_ID)) {
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
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_DELETE_BY_ID)) {
			pr.setInt(1, id);
			pr.executeUpdate();

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to delete news, NewsDAOImpl", e);
		}
	}

	@Override
	public News get(int id) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_NEWS_BY_ID)) {
			pr.setInt(1, id);
			ResultSet rs = pr.executeQuery();
			
			News news = null;

			if (rs.next()) {
				news = new News(rs.getInt(ID), rs.getString(TITLE), rs.getString(BRIEF), rs.getString(CONTENT),
						rs.getInt(USER_ID));
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
				PreparedStatement prInsert = con.prepareStatement(SQL_REQUEST_TO_INSERT_TO_FAVOURITE)) {

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

	@Override
	public void deleteFromFavourite(int userId, int newsId) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_DELETE_FROM_FAVOURITE)) {

			pr.setInt(1, userId);
			pr.setInt(2, newsId);
			pr.executeUpdate();

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to delete news from favourite, NewsDAOImpl", e);
		}
	}

	@Override
	public List<News> getLimitedAmount(int start, int limit) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_LIMITED_LIST)) {
			pr.setInt(1, start);
			pr.setInt(2, limit);

			ResultSet rs = pr.executeQuery();

			List<News> list = new ArrayList<>();

			while (rs.next()) {
				News news = new News(rs.getInt(ID), rs.getString(TITLE), rs.getString(BRIEF), rs.getString(CONTENT),
						rs.getInt(USER_ID));
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
	public List<News> getFavouritesLimitedAmount(int userId, int start, int limit) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_FAVOURITES_LIMITED_AMOUNT_NEWS)) {
			pr.setInt(1, userId);
			pr.setInt(2, start);
			pr.setInt(3, limit);
			ResultSet rs = pr.executeQuery();

			List<News> list = new ArrayList<>();

			while (rs.next()) {
				News news = new News(rs.getInt(ID), rs.getString(TITLE), rs.getString(BRIEF), rs.getString(CONTENT),
						rs.getInt(USER_ID));
				list.add(news);
			}
			return list;
		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to get favourites limited list news, NewsDAOImpl", e);
		}
	}

	@Override
	public int getAmountFavouritesByUserId(int id) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_AMOUNT_FAVOURITES_NEWS)) {
			pr.setInt(1, id);
			ResultSet rs = pr.executeQuery();

			rs.next();
			return rs.getInt(AMOUNT);

		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to get favourite news, NewsDAOImpl", e);
		}
	}

	@Override
	public List<News> findByWordInTitle(String word) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_SELECT_BY_WORD_IN_TITLE)) {
			pr.setString(1, word);
			ResultSet rs = pr.executeQuery();

			List<News> list = new ArrayList<>();

			while (rs.next()) {
				News news = new News(rs.getInt(ID), rs.getString(TITLE), rs.getString(BRIEF), rs.getString(CONTENT),
						rs.getInt(USER_ID));
				list.add(news);
			}
			return list;
		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to search news by the word in the title, NewsDAOImpl",
					e);
		}
	}

	@Override
	public boolean isFavourite(int userId, int newsId) throws DAOException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement pr = con.prepareStatement(SQL_REQUEST_TO_CHECK_IS_FAVOURITE)) {
			pr.setInt(1, userId);
			pr.setInt(2, newsId);
			ResultSet rs = pr.executeQuery();

			rs.next();
			if (rs.getInt(RESULT) == 1) {
				return true;
			}
			return false;
		} catch (SQLException | ConnectionPoolException e) {
			throw new DAOException("Error occurred while trying to check if news is favourite, NewsDAOImpl", e);
		}
	}
}
