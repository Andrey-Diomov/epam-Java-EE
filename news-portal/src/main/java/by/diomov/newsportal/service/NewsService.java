package by.diomov.newsportal.service;

import java.util.List;

import by.diomov.newsportal.bean.News;

public interface NewsService {

	void save(News news) throws ServiceException;

	void update(News news) throws ServiceException;

	void delete(int id) throws ServiceException;

	News get(int id) throws ServiceException;	

	void addToFavourite(int userId, int newsId) throws ServiceException;

	void deleteFromFavourite(int userId, int newsId) throws ServiceException;

	List<News> getFavouritesLimitedAmount(int userId, int from, int amount) throws ServiceException;

	List<News> getLimitedAmount(int start, int limit) throws ServiceException;

	int getAmountNews() throws ServiceException;

	int getAmountFavouritesByUserId(int id) throws ServiceException;

	List<News> findByWordInTitle(String word) throws ServiceException;

	boolean isFavourite(int userId, int newsId) throws ServiceException;
}
