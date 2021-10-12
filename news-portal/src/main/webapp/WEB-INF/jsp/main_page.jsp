<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Main_page</title>
<link rel="stylesheet" href="css/main_page.css">

<fmt:setLocale value="${sessionScope.local}" />
<fmt:setBundle basename="local" var="loc" />
<fmt:message bundle="${loc}" key="local.title_site" var="title_site" />
<fmt:message bundle="${loc}" key="local.ru_button" var="ru_button" />
<fmt:message bundle="${loc}" key="local.en_button" var="en_button" />
<fmt:message bundle="${loc}" key="local.main_page.registration"
	var="registration_button" />
<fmt:message bundle="${loc}" key="local.main_page.authorization"
	var="authorization_button" />
<fmt:message bundle="${loc}" key="local.main_page.log_out_button"
	var="log_out_button" />
<fmt:message bundle="${loc}" key="local.main_page.read_button"
	var="read_button" />
<fmt:message bundle="${loc}" key="local.main_page.add_news_button"
	var="add_button" />
<fmt:message bundle="${loc}" key="local.main_page.user_profile"
	var="user_profile" />
<fmt:message bundle="${loc}" key="local.main_page.favourite_news_button"
	var="favourite_news_button" />
<fmt:message bundle="${loc}" key="local.page" var="page" />
<fmt:message bundle="${loc}" key="local.main_page.search_news_button"
	var="search_news_button" />
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

				<c:if test="${user.role =='GUEST'}">
					<div class="registration_button">
						<form action="Controller" method="post">
							<input type="hidden" name="command" value="registration" /> <input
								type="submit" name="command" value="${registration_button}" />
						</form>
					</div>
					<div class="authorization_button">
						<form action="Controller" method="post">
							<input type="hidden" name="command" value="authorization" /> <input
								type="submit" name="command" value="${authorization_button}" />
						</form>
					</div>
				</c:if>

				<c:if test="${user.role == 'ADMIN'}">
					<div class="add_button">
						<form action="Controller" method="post">
							<input type="hidden" name="command" value="go_to_add_news_page" />
							<input type="submit" name="command" value="${add_button}" />
						</form>
					</div>
				</c:if>

				<c:if test="${user.role !='GUEST'}">
					<div class="log_out_button">
						<form action="Controller" method="post">
							<input type=hidden name="command" value="LOG_OUT" /> <input
								type="submit" name="command" value="${log_out_button}" />
						</form>
					</div>

					<div class="view_profile">
						<form action="Controller" method="post">
							<input type=hidden name="command" value="GO_TO_USER_PROFILE" />
							<input type="submit" name="command" value="${user_profile}" />
						</form>
					</div>

					<div class="favourite_news">
						<form action="Controller" method="post">
							<input type="hidden" name="pageNumber" value="1" /> <input
								type=hidden name="command" value="GO_TO_FAVOURITE_NEWS_PAGE" />
							<input type="submit" name="command"
								value="${favourite_news_button}" />
						</form>
					</div>
					<div class="search_news">
						<form action="Controller" method="post">
							<input type=hidden name="command" value="GO_TO_SEARCH_NEWS_PAGE" />
							<input type="submit" name="command" value="${search_news_button}" />
						</form>
					</div>

				</c:if>
			</div>
		</div>
	</div>

	<h1>
		<c:out value="${param.message}" />
	</h1>
	<br>
	<br>
	<div class="main-content">

		<div align="center">
			<p>
				<c:out value="${page}" />
				<c:out value="${param.pageNumber}" />
			</p>
		</div>
		<br />
		<c:forEach var="news" items="${newsList}">
			<h2>
				<c:out value="${news.title}" />
			</h2>

			<h3>
				<c:out value="${news.brief}" />
			</h3>
			<c:if test="${user.role !='GUEST'}">
				<form action="Controller" method="post">
					<input type="hidden" name="idNews" value="${news.id}" /> <input
						type="hidden" name="command" value="go_to_read_news_page" /> <input
						type="submit" value="${read_button}" />
				</form>
			</c:if>
			<hr>
		</c:forEach>
	</div>

	<div align="center">
		<c:forEach begin="1" end="${amountPage}" step="1" varStatus="i">
			<c:url value="Controller?command=GO_TO_MAIN_PAGE" var="url">
				<c:param name="pageNumber" value="${i.index}" />
			</c:url>
			<a href="${url}">${i.index}</a>
		</c:forEach>
	</div>
</body>
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
</html>