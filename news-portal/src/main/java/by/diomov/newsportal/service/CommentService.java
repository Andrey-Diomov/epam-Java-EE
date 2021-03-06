package by.diomov.newsportal.service;

import java.util.List;
import by.diomov.newsportal.bean.Comment;

public interface CommentService {

	List<Comment> getLimitedAmountCommentsByNewsId(int start, int limit, int id) throws ServiceException;

	void save(Comment comment) throws ServiceException;

	int getAmountCommentsByNewsId(int id) throws ServiceException;
}
