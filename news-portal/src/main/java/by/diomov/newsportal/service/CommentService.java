package by.diomov.newsportal.service;

import java.util.List;
import by.diomov.newsportal.bean.Comment;
import by.diomov.newsportal.service.exception.ServiceException;

public interface CommentService {
	
	List<Comment> getLimitedListCommentsByNewsId(int from, int amount, int id) throws ServiceException;

	void save(Comment comment) throws ServiceException;
}