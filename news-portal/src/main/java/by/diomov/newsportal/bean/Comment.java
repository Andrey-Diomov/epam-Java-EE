package by.diomov.newsportal.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Comment implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String text;
	private Date created;
	private int userId;
	private int newsId;
	private User user;

	public Comment() {
	}

	public Comment(int id, String text, Date created, int userId, int newsId, User user) {
		this.id = id;
		this.text = text;
		this.created = created;
		this.userId = userId;
		this.newsId = newsId;
		this.user = user;
	}

	public Comment(String text, Date created, int userId, int newsId) {
		this(0, text, created, userId, newsId, null);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + id;
		result = prime * result + newsId;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + userId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (id != other.id)
			return false;
		if (newsId != other.newsId)
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}	
}
