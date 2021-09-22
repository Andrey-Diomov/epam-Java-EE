package by.diomov.newsportal.listener;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		System.out.println("Session Created : id=" + sessionEvent.getSession().getId());
	}

	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		System.out.println("Session Destroyed : ID=" + sessionEvent.getSession().getId());
	}
}
