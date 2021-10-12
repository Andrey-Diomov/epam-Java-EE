package by.diomov.newsportal.dao;

import java.util.List;
import by.diomov.newsportal.bean.News;
import by.diomov.newsportal.service.ServiceException;

public interface NewsDAO {

	void save(News news) throws DAOException;

	void update(News news) throws DAOException;

	void delete(int id) throws DAOException;

	void deleteFromFavourite(int userId, int newsId) throws DAOException;

	News get(int id) throws DAOException;

	// List<News> getAll() throws DAOException;

	void addToFavourite(int userId, int newsId) throws DAOException;

	List<News> getFavouritesLimitedList(int userId, int from, int amount) throws DAOException;

	List<News> getLimitedList(int from, int amount) throws DAOException;

	int getAmountNews() throws DAOException;

	int getAmountFavouritesNewsByUserId(int id) throws DAOException;

	List<News> findNewsByWordInTitle(String word) throws DAOException;
}
