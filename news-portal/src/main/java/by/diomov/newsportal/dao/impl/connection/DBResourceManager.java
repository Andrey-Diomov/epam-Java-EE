package by.diomov.newsportal.dao.impl.connection;

import java.util.ResourceBundle;

public class DBResourceManager {
	private final static  String RESOURCES = "db_news";
	private final static  DBResourceManager INSTANCE = new DBResourceManager();
	private ResourceBundle bundle = ResourceBundle.getBundle(RESOURCES);

	private DBResourceManager() {
	}

	public static DBResourceManager getInstance() {
		return INSTANCE;
	}

	public String getValue(String key) {
		return bundle.getString(key);
	}
}
