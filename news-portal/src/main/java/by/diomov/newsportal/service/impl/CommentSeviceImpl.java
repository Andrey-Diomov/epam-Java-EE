package by.diomov.newsportal.service.impl;

import java.util.List;
import by.diomov.newsportal.bean.Comment;
import by.diomov.newsportal.dao.CommentDAO;
import by.diomov.newsportal.dao.DAOException;
import by.diomov.newsportal.dao.DAOProvider;
import by.diomov.newsportal.service.CommentService;
import by.diomov.newsportal.service.exception.ServiceException;

public class CommentSeviceImpl implements CommentService {
	private static final DAOProvider provider = DAOProvider.getInstance();
	private final CommentDAO commentDAO = provider.getCommentDAO();
	
	@Override
	public List<Comment> getLimitedListCommentsByNewsId(int from, int amount, int id) throws ServiceException {
		try {
			return commentDAO.getLimitedListCommentsByNewsId(from, amount, id);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void save(Comment comment) throws ServiceException {
		try {
			commentDAO.save(comment);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}
}