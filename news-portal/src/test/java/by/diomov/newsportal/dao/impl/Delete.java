package by.diomov.newsportal.dao.impl;
//package by.diomov.newsportal.dao.impl;
//
//	import static org.junit.jupiter.api.Assertions.assertEquals;
//
//	import java.io.IOException;
//	import java.sql.Connection;
//	import java.sql.Date;
//	import java.sql.PreparedStatement;
//	import java.sql.ResultSet;
//	import java.sql.SQLException;
//	import java.sql.Statement;
//	import java.text.SimpleDateFormat;
//	import java.util.List;
//	import org.junit.jupiter.api.BeforeEach;
//	import org.junit.jupiter.api.Test;
//	import by.diomov.categorizeditem.dao.impl.ConnectConfigProporties;
//	import by.diomov.categorizeditem.dao.impl.DBconnection;
//	import by.diomov.categorizeditem.dao.ItemDAO;
//	import by.diomov.categorizeditem.model.impl.Item;
//
//	public class ItemDAOImplTest {
//		private DBconnection dbConnection;
//
//		@BeforeEach
//		public void reCreateDB() throws SQLException, IOException {
//			dbConnection = new DBconnection(new ConnectConfigProporties());
//			TestDBUtil.reCreateBD(dbConnection.getConnection());
//		}
//
//		@Test
//		public void testGetAll() throws Exception {
//			Connection connection = dbConnection.getConnection();
//			ItemDAO itemDAO = new ItemDAOImpl(dbConnection);
//
//			List<Item> items = itemDAO.getAll();
//			assertEquals(0, items.size());
//
//			long categoryId = insertCategory(connection, "FirstCategory", 10);
//
//			insertItem(connection, "FirstItem", "2020-12-22", categoryId);
//			insertItem(connection, "SecondItem", "2020-10-22", categoryId);
//
//			items = itemDAO.getAll();
//			assertEquals(2, items.size());
//		}
//
//		@Test
//		public void testGetById() throws Exception {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//			Connection connection = dbConnection.getConnection();
//			ItemDAO itemDAO = new ItemDAOImpl(dbConnection);
//
//			long categoryId = insertCategory(connection, "FirstCategory", 10);
//
//			insertItem(connection, "FirstItem", "2020-12-22", categoryId);
//			long itemId = insertItem(connection, "SecondItem", "2020-10-22", categoryId);
//
//			Item gotItem = itemDAO.get(itemId);
//			/* equals true */
//			assertEquals("SecondItem", gotItem.getName());
//			assertEquals(sdf.parse("2020-10-22"), gotItem.getCreated());
//			assertEquals(categoryId, gotItem.getCategoryId());
//		}
//
//		@Test
//		public void testSave() throws Exception {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//			Connection connection = dbConnection.getConnection();
//			ItemDAO itemDAO = new ItemDAOImpl(dbConnection);
//
//			long categoryId = insertCategory(connection, "FirstCategory", 10);
//
//			Item item = new Item("Item-1", sdf.parse("2020-10-10"), categoryId);
//
//			itemDAO.save(item);			
//
//			Statement statement = connection.createStatement();
//			ResultSet rs = statement.executeQuery("SELECT * FROM  Item  ORDER BY ID DESC LIMIT 1");
//
//			rs.next();
//			long itemId = rs.getLong(1);
//
//			rs = statement.executeQuery("SELECT * FROM Item WHERE ID = " + itemId);
//
//			if (rs.next()) {
//				assertEquals(item.getName(), rs.getString("name"));
//				assertEquals(item.getCreated(), rs.getDate("created"));
//				assertEquals(item.getCategoryId(), rs.getLong("categoryId"));
//			}
//		}
//
//		@Test
//		public void testDeleteById() throws Exception {
//
//			Connection connection = dbConnection.getConnection();
//			ItemDAO itemDAO = new ItemDAOImpl(dbConnection);
//
//			long categoryId = insertCategory(connection, "FirstCategory", 10);
//
//			long itemId = insertItem(connection, "FirstItem", "2020-12-22", categoryId);
//
//			Statement statement = connection.createStatement();
//
//			ResultSet rs = statement.executeQuery("SELECT Count(ID) FROM Item WHERE ID = " + itemId);
//
//			rs.next();
//			long countItem = rs.getLong(1);
//			assertEquals(1, countItem);
//
//			itemDAO.delete(itemId);
//
//			rs = statement.executeQuery("SELECT Count(ID) FROM Item WHERE ID = " + itemId);
//
//			rs.next();
//			countItem = rs.getLong(1);
//			assertEquals(0, countItem);
//		}
//
//		@Test
//		public void testUpdate() throws Exception {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//			Connection connection = dbConnection.getConnection();
//			ItemDAO itemDAO = new ItemDAOImpl(dbConnection);
//
//			long categoryId = insertCategory(connection, "FirstCategory", 10);
//
//			long itemId = insertItem(connection, "FirstItem", "2012-12-12", categoryId);
//
//			Item item = getItemById(connection, itemId);
//
//			item.setName("UpdatedItem");
//			//item.setCreated(sdf.parse("2021-01-01"));
//
//			itemDAO.update(item);
//
//			assertItem(item, connection, itemId);
//		}
//
//		private static void assertItem(Item item, Connection connection, long id) throws SQLException {
//			Statement statement = connection.createStatement();
//			ResultSet rs = statement.executeQuery("SELECT * FROM Item WHERE ID = " + id);
//
//			rs.next();
//			assertEquals(item.getName(), rs.getString("name"));
//			assertEquals(item.getCreated(), rs.getDate("created"));
//			assertEquals(item.getCategoryId(), rs.getLong("categoryId"));
//		}
//
//		private static long insertCategory(Connection connection, String name, int color) throws SQLException {
//
//			String queryToInsertCategory = "INSERT Category(name , color) VALUES (?,?);";
//
//			PreparedStatement preparedStatement = connection.prepareStatement(queryToInsertCategory,
//					Statement.RETURN_GENERATED_KEYS);
//
//			preparedStatement.setString(1, name);
//			preparedStatement.setInt(2, color);
//			preparedStatement.executeUpdate();
//
//			ResultSet rs = preparedStatement.getGeneratedKeys();
//			rs.next();
//			return rs.getLong(1);
//		}
//
//		private static long insertItem(Connection connection, String name, String created, long categoryId)
//				throws SQLException {
//			String queryToInsertItem = "INSERT Item(name , created, categoryId) VALUES (?,?,?);";
//			PreparedStatement preparedStatement = connection.prepareStatement(queryToInsertItem,
//					Statement.RETURN_GENERATED_KEYS);
//
//			preparedStatement.setString(1, name);
//			preparedStatement.setDate(2, Date.valueOf(created));
//			preparedStatement.setLong(3, categoryId);
//			preparedStatement.executeUpdate();
//
//			ResultSet rs = preparedStatement.getGeneratedKeys();
//
//			rs.next();
//			return rs.getLong(1);
//		}
//
//		private static Item getItemById(Connection connection, long id) throws SQLException {
//			Statement statement = connection.createStatement();
//			ResultSet rs = statement.executeQuery("SELECT * FROM Item WHERE ID = " + id);
//			if (rs.next()) {
//				return new Item(rs.getLong("id"), rs.getString("name"), rs.getDate("created"), rs.getLong("categoryId"));
//			} else {
//				return null;
//			}
//		}
//	}
//
