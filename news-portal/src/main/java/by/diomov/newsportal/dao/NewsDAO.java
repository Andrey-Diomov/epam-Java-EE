package by.diomov.newsportal.dao;

import java.util.List;
import by.diomov.newsportal.bean.News;

public interface NewsDAO {

	void save(News news) throws DAOException;

	void update(News news) throws DAOException;

	void delete(int id) throws DAOException;

	News get(int id) throws DAOException;

	List<News> getAll() throws DAOException;

	
}
