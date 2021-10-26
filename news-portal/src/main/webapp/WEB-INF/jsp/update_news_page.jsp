<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Update_news_page</title>
<link rel="stylesheet" href="css/main_page.css">
<link rel="stylesheet" href="css/update_news_page.css">

<fmt:setLocale value="${sessionScope.local}" />
<fmt:setBundle basename="local" var="loc" />
<fmt:message bundle="${loc}" key="local.title_site" var="title_site" />
<fmt:message bundle="${loc}" key="local.ru_button" var="ru_button" />
<fmt:message bundle="${loc}" key="local.en_button" var="en_button" />
<fmt:message bundle="${loc}" key="local.to_main_page_button"
	var="main_button" />
<fmt:message bundle="${loc}"
	key="local.update_news_page.update_news_button" var="update_button" />
<fmt:message bundle="${loc}" key="local.update_news_page.title"
	var="title" />
<fmt:message bundle="${loc}" key="local.update_news_page.brief"
	var="brief" />
<fmt:message bundle="${loc}" key="local.update_news_page.content"
	var="content" />
	<fmt:message bundle="${loc}" key="local.update_news_page.to_read_news_page_button"
	var="back_to_news_button" />

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
						value="GO_TO_MAIN_PAGE">${main_button}</button>
				</form>
			</div>
		</div>
	</div>

	<div class="main-content">		
			<form action="Controller" method="post">
				<input type="hidden" name="command" value="UPDATE_NEWS" /> <input
					type="hidden" name="idNews" value="${news.id}" /> <br />
				<c:out value="${title}" />
				<br /> <input type="text" name="title" size="45"
					value="${news.title}" /><br />
				<c:out value="${brief}" />
				<br />
				<textarea rows="4" cols="50" name="brief">${news.brief}</textarea>
				<br />
				<c:out value="${content}" />
				<br />
				<textarea rows="4" cols="50" name="content">${news.content}</textarea>
				<br />
				<div>
					<input type="submit" value="${update_button}" />
				</div>				
			</form>

			<form action="Controller" method="post">
				<input type="hidden" name="idNews" value="${news.id}" /> <input
					type="hidden" name="command" value="GO_TO_READ_NEWS_PAGE" /> <input
					type="submit" value="${back_to_news_button}" />
			</form>
	
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