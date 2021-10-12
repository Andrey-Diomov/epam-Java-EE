package by.diomov.newsportal.service;

import java.util.List;

import by.diomov.newsportal.bean.News;

public interface NewsService {

	void save(News news) throws ServiceException;

	void update(News news) throws ServiceException;

	void delete(int id) throws ServiceException;

	News get(int id) throws ServiceException;

	// List<News> getAll() throws ServiceException;

	void addToFavourite(int userId, int newsId) throws ServiceException;

	void deleteFromFavourite(int userId, int newsId) throws ServiceException;

	List<News> getFavouritesLimitedList(int userId, int from, int amount) throws ServiceException;

	List<News> getLimitedList(int from, int amount) throws ServiceException;

	int getAmountNews() throws ServiceException;

	int getAmountFavouritesNewsByUserId(int id) throws ServiceException;

	List<News> findNewsByWordInTitle(String word) throws ServiceException;
}
