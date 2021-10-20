package by.diomov.newsportal.dao;

import java.util.List;
import by.diomov.newsportal.bean.Comment;

public interface CommentDAO {

	void save(Comment comment) throws DAOException;

	List<Comment> getLimitedAmountCommentsByNewsId(int start, int limit, int id) throws DAOException;

	void deleteAllByNewsId(int id) throws DAOException;

	int getAmountCommentsByNewsId(int id) throws DAOException;
}
