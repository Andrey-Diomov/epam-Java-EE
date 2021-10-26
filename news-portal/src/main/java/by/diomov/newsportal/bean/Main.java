package by.diomov.newsportal.bean;

import org.mindrot.jbcrypt.BCrypt;

public class Main {
	public static void main(String[] args) {

		String salt = BCrypt.gensalt();
		String hashpw = BCrypt.hashpw("Editor", salt);
		System.out.println("password = " + hashpw);

		if (BCrypt.checkpw("Editor", hashpw)) {
			System.out.println("It matches");
		} else {
			System.out.println("It does not match");
		}
		System.out.println("ghjk");
	}
}
