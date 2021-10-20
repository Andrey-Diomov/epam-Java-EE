<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Favourite_news_page</title>
<link rel="stylesheet" href="css/main_page.css">

<fmt:setLocale value="${sessionScope.local}" />
<fmt:setBundle basename="local" var="loc" />
<fmt:message bundle="${loc}" key="local.title_site" var="title_site" />
<fmt:message bundle="${loc}" key="local.ru_button" var="ru_button" />
<fmt:message bundle="${loc}" key="local.en_button" var="en_button" />
<fmt:message bundle="${loc}" key="local.page" var="page" />
<fmt:message bundle="${loc}" key="local.to_main_page_button"
	var="main_page_button" />
<fmt:message bundle="${loc}" key="local.list_users_page.block_button"
	var="block_button" />
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

		<div align="center">
			<p>
				<c:out value="${page}" />
				<c:out value="${param.pageNumber}" />
			</p>
		</div>
		<br />
		<c:forEach var="user" items="${users}">

			<form action="Controller" method="post">

				<b> <c:out value="${user.login }" />
				</b> <br> <input type="hidden" name="command"
					value="set_ability_to_comment" /> <input type="hidden"
					name="ability" value="false" />

				<div>
					<input type="checkbox" name="userId" value="${user.id}" required>
					<input type="submit" value="${block_button}">
				</div>
			</form>


			<br>

		</c:forEach>

		<div align="center">
			<c:forEach begin="1" end="${amountPage}" step="1" varStatus="i">
				<c:url value="Controller?command=Go_To_List_Users_Page" var="url">
					<c:param name="pageNumber" value="${i.index}" />
					<c:param name="ability" value="${param.ability}" />
				</c:url>
				<a href="${url}">${i.index}</a>
			</c:forEach>
		</div>
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