<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Read_news_page</title>
<link rel="stylesheet" href="css/main_page.css">
<link rel="stylesheet" href="css/read_news.css">

<fmt:setLocale value="${sessionScope.local}" />
<fmt:setBundle basename="local" var="loc" />
<fmt:message bundle="${loc}" key="local.title_site" var="title_site" />
<fmt:message bundle="${loc}" key="local.ru_button" var="ru_button" />
<fmt:message bundle="${loc}" key="local.en_button" var="en_button" />
<fmt:message bundle="${loc}" key="local.to_main_page_button"
	var="main_page_button" />
<fmt:message bundle="${loc}"
	key="local.read_news_page.delete_news_button" var="delete_news_button" />
<fmt:message bundle="${loc}"
	key="local.read_news_page.update_news_button" var="update_news_button" />
<fmt:message bundle="${loc}"
	key="local.read_news_page.add_comment_button" var="add_comment_button" />
</head>
<fmt:message bundle="${loc}"
	key="local.read_news_page.read_comments_button"
	var="read_comments_button" />
<fmt:message bundle="${loc}"
	key="local.read_news_page.add_to_favourite_button"
	var="add_to_favourite_button" />
<fmt:message bundle="${loc}"
	key="local.read_news_page.delete_from_favourite_button"
	var="delete_from_favourite_button" />
	<fmt:message bundle="${loc}" key="local.read_comments_page.page"
	var="page" />
</head>

<body>
	<div class="header">
		<div class="title">
			<h1>
				<c:out value="${title_site}" />
			</h1>
		</div>
		<div class="controls">
			<div class="user-info">
				<h3>
					<c:out value="${user.login }" />
				</h3>
			</div>
			<div class="buttons-block">
				<form action="Controller" method="POST">
					<input type="hidden" name="command" value="CHANGE_LOCAL" /> <select
						id="locale-select" name="local" onchange="this.form.submit()">
						<option value="ru">${ru_button}</option>
						<option value="en">${en_button}</option>
					</select>
				</form>
				<form action="Controller" method="post">
				<input type="hidden" name="pageNumber" value="1" />
					<button class="main_button" type="submit" name="command"
						value="go_to_main_page">${main_page_button}</button>
				</form>
			</div>
		</div>
	</div>
	<div class="main-content">

		<h1 class="text-left">
			<c:out value="${news.title}" />
		</h1>
		
		<h2>
			<c:out value="${news.brief}" />
		</h2>
		
		<p>
			<c:out value="${news.content}" />
		</p>

		<form action="Controller" method="post">
			<input type="hidden" name="idNews" value="${news.id}" /> <input
				type="hidden" name="command" value="go_to_add_comment_page" /> <input
				type="submit" value="${add_comment_button}" />
		</form>

		<form action="Controller" method="post">
			<input type="hidden" name="idNews" value="${news.id}" /> <input
				type="hidden" name="pageNumber" value=1 /> <input type="hidden"
				name="command" value="go_to_read_comments_page" /> <input
				type="submit" value="${read_comments_button}" />
		</form>

		<c:if test="${!favourite}">
			<form action="Controller" method="post">
				<input type="hidden" name="idNews" value="${news.id}" /> <input
					type="hidden" name="command" value="add_news_to_favourite" /> <input
					type="submit" value="${add_to_favourite_button}" />
			</form>
		</c:if>

		<c:if test="${favourite}">
			<form action="Controller" method="post">			
				<input type="hidden" name="idNews" value="${news.id}" /> <input
					type="hidden" name="command" value="delete_news_from_favourite" />
				<input type="submit" value="${delete_from_favourite_button}" />
			</form>
		</c:if>

		<c:if test="${user.role == 'ADMIN'}">
			<form action="Controller" method="post">
				<input type="hidden" name="idNews" value="${news.id}" /> <input
					type="hidden" name="command" value="delete_news" /> <input
					type="submit" value="${delete_news_button}" />
			</form>

			<form action="Controller" method="post">
				<input type="hidden" name="idNews" value="${news.id}" /> <input
					type="hidden" name="command" value="go_to_update_news_page" /> <input
					type="submit" value="${update_news_button}" />
			</form>

		</c:if>

	</div>
	<script>
		document
				.addEventListener(
						'DOMContentLoaded',
						function() {
							var localeElement = document
									.getElementById("locale-select");
							localeElement.value = '${sessionScope.local}' != '' ? '${sessionScope.local}'
									: 'ru';
						})
	</script>
</body>
</html>