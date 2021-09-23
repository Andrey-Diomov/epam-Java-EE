package by.diomov.newsportal.service;

import java.util.List;
import by.diomov.newsportal.bean.News;
import by.diomov.newsportal.service.exception.ServiceException;

public interface NewsService {

	void save(News news) throws ServiceException;

	void update(News news) throws ServiceException;

	void delete(int id) throws ServiceException;
	
	News get(int id) throws ServiceException;

	List<News> getAll() throws ServiceException;
}
