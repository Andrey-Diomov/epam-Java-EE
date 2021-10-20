package by.diomov.newsportal.dao;

import java.util.List;
import by.diomov.newsportal.bean.News;

public interface NewsDAO {

	void save(News news) throws DAOException;

	void update(News news) throws DAOException;

	void delete(int id) throws DAOException;

	void deleteFromFavourite(int userId, int newsId) throws DAOException;

	News get(int id) throws DAOException;

	void addToFavourite(int userId, int newsId) throws DAOException;

	List<News> getFavouritesLimitedAmount(int userId, int start, int limit) throws DAOException;

	List<News> getLimitedAmount(int start, int limit) throws DAOException;

	int getAmountNews() throws DAOException;

	int getAmountFavouritesByUserId(int id) throws DAOException;

	List<News> findByWordInTitle(String word) throws DAOException;

	boolean isFavourite(int userId, int newsId) throws DAOException;
}
