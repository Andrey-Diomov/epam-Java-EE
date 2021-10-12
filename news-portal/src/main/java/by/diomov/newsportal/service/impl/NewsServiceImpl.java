
package by.diomov.newsportal.service.impl;

import java.util.List;
import by.diomov.newsportal.bean.News;
import by.diomov.newsportal.dao.CommentDAO;
import by.diomov.newsportal.dao.DAOException;
import by.diomov.newsportal.dao.DAOProvider;
import by.diomov.newsportal.dao.NewsDAO;
import by.diomov.newsportal.service.NewsService;
import by.diomov.newsportal.service.ServiceException;

public class NewsServiceImpl implements NewsService {
	private static final DAOProvider provider = DAOProvider.getInstance();
	private final NewsDAO newsDAO = provider.getNewsDAO();
	private final CommentDAO commentDAO = provider.getCommentDAO();

	@Override
	public void save(News news) throws ServiceException {
		try {
			newsDAO.save(news);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void update(News news) throws ServiceException {
		try {
			newsDAO.update(news);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void delete(int id) throws ServiceException {
		try {
			commentDAO.deleteAllByNewsId(id);
			newsDAO.delete(id);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

//	@Override
//	public List<News> getAll() throws ServiceException {
//		try {
//			return newsDAO.getAll();
//		} catch (DAOException e) {
//			throw new ServiceException(e);
//		}
//	}

	@Override
	public News get(int id) throws ServiceException {
		try {
			return newsDAO.get(id);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void addToFavourite(int userId, int newsId) throws ServiceException {
		try {
			newsDAO.addToFavourite(userId, newsId);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<News> getFavouritesLimitedList(int userId, int from, int amount) throws ServiceException {
		try {
			return newsDAO.getFavouritesLimitedList(userId, from, amount);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void deleteFromFavourite(int userId, int newsId) throws ServiceException {
		try {
			newsDAO.deleteFromFavourite(userId, newsId);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<News> getLimitedList(int from, int amount) throws ServiceException {
		try {
			return newsDAO.getLimitedList(from, amount);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int getAmountNews() throws ServiceException {
		try {
			return newsDAO.getAmountNews();
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int getAmountFavouritesNewsByUserId(int id) throws ServiceException {
		try {
			return newsDAO.getAmountFavouritesNewsByUserId(id);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<News> findNewsByWordInTitle(String word) throws ServiceException {
		try {
			return newsDAO.findNewsByWordInTitle(word);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}
}
