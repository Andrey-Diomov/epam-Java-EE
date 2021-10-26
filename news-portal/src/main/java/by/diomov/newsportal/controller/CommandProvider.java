package by.diomov.newsportal.controller;

import java.util.HashMap;
import java.util.Map;
import by.diomov.newsportal.controller.impl.ChangeLocal;
import by.diomov.newsportal.controller.impl.LogOut;
import by.diomov.newsportal.controller.impl.UnknownCommand;
import by.diomov.newsportal.controller.impl.comment.AddComment;
import by.diomov.newsportal.controller.impl.comment.GoToAddCommentPage;
import by.diomov.newsportal.controller.impl.comment.GoToReadCommentsPage;
import by.diomov.newsportal.controller.impl.news.AddNews;
import by.diomov.newsportal.controller.impl.news.AddNewsToFavourite;
import by.diomov.newsportal.controller.impl.news.DeleteNews;
import by.diomov.newsportal.controller.impl.news.DeleteNewsFromFavourite;
import by.diomov.newsportal.controller.impl.news.GoToAddNewsPage;
import by.diomov.newsportal.controller.impl.news.GoToFavouriteNewsPage;
import by.diomov.newsportal.controller.impl.news.GoToMainPage;
import by.diomov.newsportal.controller.impl.news.GoToReadNewsPage;
import by.diomov.newsportal.controller.impl.news.GoToSearchNewsPage;
import by.diomov.newsportal.controller.impl.news.GoToUpdateNewsPage;
import by.diomov.newsportal.controller.impl.news.SearchNews;
import by.diomov.newsportal.controller.impl.news.UpdateNews;
import by.diomov.newsportal.controller.impl.user.ChangePassword;
import by.diomov.newsportal.controller.impl.user.GoToAuthorizationPage;
import by.diomov.newsportal.controller.impl.user.GoToChangePassword;
import by.diomov.newsportal.controller.impl.user.GoToRegistrationPage;
import by.diomov.newsportal.controller.impl.user.GoToSearchUserPage;
import by.diomov.newsportal.controller.impl.user.GoToUserProfile;
import by.diomov.newsportal.controller.impl.user.RegistrationNewUser;
import by.diomov.newsportal.controller.impl.user.SearchUser;
import by.diomov.newsportal.controller.impl.user.SetAbilityToComment;
import by.diomov.newsportal.controller.impl.user.SignIn;

public class CommandProvider {
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
		commands.put(CommandName.GO_TO_SEARCH_NEWS_PAGE, new GoToSearchNewsPage());
		commands.put(CommandName.SEARCH_NEWS, new SearchNews());
		commands.put(CommandName.CHANGE_PASSWORD, new ChangePassword());
		commands.put(CommandName.GO_TO_CHANGE_PASSWORD_PAGE, new GoToChangePassword());
		commands.put(CommandName.GO_TO_SEARCH_USER_PAGE, new GoToSearchUserPage());
		commands.put(CommandName.SEARCH_USER, new SearchUser());
		commands.put(CommandName.SET_ABILITY_TO_COMMENT, new SetAbilityToComment());

		commands.put(CommandName.UNKNOWN_COMMAND, new UnknownCommand());
	}

	public Command findCommand(String name) {
		CommandName commandName = CommandName.valueOf(name.toUpperCase());
		Command command = commands.get(commandName);
		return command;
	}
}
