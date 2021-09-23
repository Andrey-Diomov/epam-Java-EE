package by.diomov.newsportal.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import by.diomov.newsportal.bean.News;
import by.diomov.newsportal.bean.Role;
import by.diomov.newsportal.dao.DAOException;
import by.diomov.newsportal.dao.DAOProvider;
import by.diomov.newsportal.dao.NewsDAO;
import by.diomov.newsportal.dao.impl.connection.ConnectionPool;
import by.diomov.newsportal.dao.impl.connection.ConnectionPoolException;

public class NewsDAOImplTest {
	private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
	private static final DAOProvider provider = DAOProvider.getInstance();
	private final NewsDAO newsDAO = provider.getNewsDAO();

	private static final String SQL_REQUEST_TO_INSERT_USER = "INSERT INTO user(name, surname, email, login, password, role) VALUES(?,?,?,?,?,?)";
	private static final String SQL_REQUEST_TO_INSERT_NEWS = "INSERT INTO news(title, brief, content, userId) VALUE(?,?,?,?)";

	@BeforeEach
	public void reCreateDB() throws SQLException, ConnectionPoolException, DAOException {
		try (Connection con = connectionPool.takeConnection(); Statement st = con.createStatement()) {
			st.execute("USE mynewsTest");
			st.execute("DROP TABLE IF EXISTS News");
			st.execute("DROP TABLE  IF EXISTS User");
			st.execute(
					"CREATE TABLE IF NOT EXISTS User(id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT, name VARCHAR(50) NOT NULL,"
							+ " surname VARCHAR(50) NOT NULL, eMail VARCHAR(50) NOT NULL UNIQUE, login VARCHAR(50) NOT NULL UNIQUE,"
							+ " password VARCHAR(50) NOT NULL , role VARCHAR(50))");
			st.execute(
					"CREATE TABLE IF NOT EXISTS News(id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT, title VARCHAR(100) NOT NULL, "
							+ "brief VARCHAR(150)NOT NULL, content VARCHAR(2000)NOT NULL, userId INT UNSIGNED NOT NULL,"
							+ " FOREIGN KEY (userId) REFERENCES user(id))");
		}
	}

	@Test
	public void testGetById() throws SQLException, ConnectionPoolException, DAOException {
		int userId = insertUser("Andrey", "Diomov", "andrey-diomov@mail.ru", "Karapyz", "Sirena-25", Role.USER);

		insertNews("First_Title", "First_Brief", "First_Content", userId);
		int newsId = insertNews("Second_Title", "Second_Brief", "Second_Content", userId);

		News gotNews = newsDAO.get(newsId);

		assertEquals("Second_Title", gotNews.getTitle());
		assertEquals("Second_Brief", gotNews.getBrief());
		assertEquals("Second_Content", gotNews.getContent());
		assertEquals(userId, gotNews.getUserId());
	}

	@Test
	public void testSave() throws SQLException, ConnectionPoolException, DAOException {
		int userId = insertUser("Andrey", "Diomov", "andrey-diomov@mail.ru", "Karapyz", "Sirena-25", Role.USER);

		News news = new News("Economy", "Economy of our country", "The economy must be economical!", userId);

		newsDAO.save(news);

		try (Connection con = connectionPool.takeConnection();
				Statement stFirst = con.createStatement();
				Statement stSecond = con.createStatement();
				ResultSet rsFirst = stFirst.executeQuery("SELECT * FROM  User  ORDER BY ID DESC LIMIT 1")) {

			rsFirst.next();
			int newsId = rsFirst.getInt("id");

			ResultSet rsSecond = stSecond.executeQuery("SELECT * FROM News WHERE ID = " + newsId);// TODO

			if (rsSecond.next()) {
				assertEquals(news.getTitle(), rsSecond.getString("title"));
				assertEquals(news.getBrief(), rsSecond.getString("brief"));
				assertEquals(news.getContent(), rsSecond.getString("content"));
				assertEquals(news.getUserId(), rsSecond.getInt("userId"));
			}
		}
	}

	@Test
	public void testDeleteById() throws SQLException, ConnectionPoolException, DAOException {

		int userId = insertUser("Andrey", "Diomov", "andrey-diomov@mail.ru", "Karapyz", "Sirena-25", Role.USER);

		int newsId = insertNews("Economy", "Economy of our country", "The economy must be economical!", userId);

		try (Connection con = connectionPool.takeConnection();
				Statement stFirst = con.createStatement();
				Statement stSecond = con.createStatement();
				ResultSet rsFirst = stFirst.executeQuery("SELECT Count(ID) FROM News WHERE ID = " + newsId)) {

			rsFirst.next();
			int countNews = rsFirst.getInt(1);
			assertEquals(1, countNews);

			newsDAO.delete(newsId);

			ResultSet rsSecond = stSecond.executeQuery("SELECT Count(ID) FROM News WHERE ID = " + newsId);

			rsSecond.next();
			countNews = rsSecond.getInt(1);
			assertEquals(0, countNews);
		}
	}

	@Test
	public void testGetAll() throws DAOException, SQLException, ConnectionPoolException {
		List<News> listOfNews = newsDAO.getAll();
		assertEquals(0, listOfNews.size());

		int userId = insertUser("Andrey", "Diomov", "andrey-diomov@mail.ru", "Karapyz", "Sirena-25", Role.USER);

		insertNews("Economy", "Economy of our country", "The economy must be economical!", userId);
		insertNews("Politics", "Politics of our country", "Politics may be a controversial topic!", userId);

		listOfNews = newsDAO.getAll();
		assertEquals(2, listOfNews.size());
	}

	private static int insertUser(String name, String surname, String eMail, String login, String password, Role role)
			throws SQLException, ConnectionPoolException {
		try (Connection con = connectionPool.takeConnection();
				PreparedStatement ps = con.prepareStatement(SQL_REQUEST_TO_INSERT_USER,
						Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, name);
			ps.setString(2, surname);
			ps.setString(3, eMail);
			ps.setString(4, login);
			ps.setString(5, password);
			ps.setString(6, role.toString());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();

			return rs.getInt(1);
		}
	}

	private static int insertNews(String title, String brief, String content, int userId)
			throws SQLException, ConnectionPoolException {

		try (Connection con = connectionPool.takeConnection();
				PreparedStatement ps = con.prepareStatement(SQL_REQUEST_TO_INSERT_NEWS,
						Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, title);
			ps.setString(2, brief);
			ps.setString(3, content);
			ps.setInt(4, userId);
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();

			return rs.getInt(1);
		}
	}

	private static News getNewsById(int id) throws SQLException, ConnectionPoolException {
		try (Connection con = connectionPool.takeConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM Item WHERE ID = " + id)) {
			if (rs.next()) {
				return new News(rs.getInt("id"), rs.getString("title"), rs.getString("brief"), rs.getString("content"),
						rs.getInt("userId"));
			} else {
				return null;
			}
		}
	}

// private static void assertNews(News news, Connection connection, int id)
// throws SQLException {
//		Statement statement = connection.createStatement();
//		ResultSet rs = statement.executeQuery("SELECT * FROM Item WHERE ID = " + id);
//
//		rs.next();
//		assertEquals(item.getName(), rs.getString("name"));
//		assertEquals(item.getCreated(), rs.getDate("created"));
//		assertEquals(item.getCategoryId(), rs.getLong("categoryId"));
//	}
}