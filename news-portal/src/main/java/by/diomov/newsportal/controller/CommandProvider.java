package by.diomov.newsportal.controller;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.diomov.newsportal.controller.impl.AddComment;
import by.diomov.newsportal.controller.impl.AddNews;
import by.diomov.newsportal.controller.impl.AddNewsToFavourite;
import by.diomov.newsportal.controller.impl.ChangeLocal;
import by.diomov.newsportal.controller.impl.DeleteNews;
import by.diomov.newsportal.controller.impl.DeleteNewsFromFavourite;
import by.diomov.newsportal.controller.impl.GoToAddCommentPage;
import by.diomov.newsportal.controller.impl.GoToAddNewsPage;
import by.diomov.newsportal.controller.impl.GoToAuthorizationPage;
import by.diomov.newsportal.controller.impl.GoToFavouriteNewsPage;
import by.diomov.newsportal.controller.impl.GoToMainPage;
import by.diomov.newsportal.controller.impl.GoToReadCommentsPage;
import by.diomov.newsportal.controller.impl.GoToReadNewsPage;
import by.diomov.newsportal.controller.impl.GoToRegistrationPage;
import by.diomov.newsportal.controller.impl.GoToUpdateNewsPage;
import by.diomov.newsportal.controller.impl.GoToUserProfile;
import by.diomov.newsportal.controller.impl.LogOut;
import by.diomov.newsportal.controller.impl.RegistrationNewUser;
import by.diomov.newsportal.controller.impl.SignIn;
import by.diomov.newsportal.controller.impl.UnknownCommand;
import by.diomov.newsportal.controller.impl.UpdateNews;

public class CommandProvider {
	private final static Logger log = LogManager.getLogger(CommandProvider.class);
	private Map<CommandName, Command> commands = new HashMap<>();

	public CommandProvider() {
		commands.put(CommandName.REGISTRATION, new GoToRegistrationPage());
		commands.put(CommandName.REGISTRATION_NEW_USER, new RegistrationNewUser());
		commands.put(CommandName.AUTHORIZATION, new GoToAuthorizationPage());
		commands.put(CommandName.SIGN_IN, new SignIn());
		commands.put(CommandName.GO_TO_MAIN_PAGE, new GoToMainPage());
		commands.put(CommandName.CHANGE_LOCAL, new ChangeLocal());
		commands.put(CommandName.LOG_OUT, new LogOut());
		commands.put(CommandName.GO_TO_READ_NEWS_PAGE, new GoToReadNewsPage());
		commands.put(CommandName.ADD_NEWS, new AddNews());
		commands.put(CommandName.GO_TO_ADD_NEWS_PAGE, new GoToAddNewsPage());
		commands.put(CommandName.UPDATE_NEWS, new UpdateNews());
		commands.put(CommandName.GO_TO_UPDATE_NEWS_PAGE, new GoToUpdateNewsPage());
		commands.put(CommandName.DELETE_NEWS, new DeleteNews());
		commands.put(CommandName.GO_TO_ADD_COMMENT_PAGE, new GoToAddCommentPage());
		commands.put(CommandName.ADD_COMMENT, new AddComment());
		commands.put(CommandName.GO_TO_READ_COMMENTS_PAGE, new GoToReadCommentsPage());
		commands.put(CommandName.GO_TO_USER_PROFILE, new GoToUserProfile());
		commands.put(CommandName.ADD_NEWS_TO_FAVOURITE, new AddNewsToFavourite());
		commands.put(CommandName.GO_TO_FAVOURITE_NEWS_PAGE, new GoToFavouriteNewsPage());
		commands.put(CommandName.DELETE_NEWS_FROM_FAVOURITE, new DeleteNewsFromFavourite());

		commands.put(CommandName.UNKNOWN_COMMAND, new UnknownCommand());
	}

	public Command findCommand(String name) {
//		if (name == null || name.isEmpty() || name.isBlank()) {
//			name = CommandName.UNKNOWN_COMMAND.toString();
//		}

		CommandName commandName= CommandName.valueOf(name.toUpperCase());
//		try {
//			commandName = CommandName.valueOf(name.toUpperCase());
//		} catch (IllegalArgumentException e) {
//			log.error(e);
//			commandName = CommandName.UNKNOWN_COMMAND;
//		}

		Command command = commands.get(commandName);
		return command;
	}
}
