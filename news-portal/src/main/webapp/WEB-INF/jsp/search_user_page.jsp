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
<link rel="stylesheet" href="css/search_user_page.css">

<fmt:setLocale value="${sessionScope.local}" />
<fmt:setBundle basename="local" var="loc" />
<fmt:message bundle="${loc}" key="local.title_site" var="title_site" />
<fmt:message bundle="${loc}" key="local.ru_button" var="ru_button" />
<fmt:message bundle="${loc}" key="local.en_button" var="en_button" />
<fmt:message bundle="${loc}" key="local.page" var="page" />
<fmt:message bundle="${loc}" key="local.to_main_page_button"
	var="main_page_button" />
<fmt:message bundle="${loc}" key="local.search_user_page.block_button"
	var="block_button" />
<fmt:message bundle="${loc}" key="local.search_user_page.unblock_button"
	var="unblock_button" />
<fmt:message bundle="${loc}"
	key="local.search_user_page.search_blocked_user_button"
	var="blocked_users_button" />
<fmt:message bundle="${loc}"
	key="local.search_user_page.search_unblocked_user_button"
	var="unblocked_users_button" />
<fmt:message bundle="${loc}"
	key="local.search_user_page.search_blocked_user_header"
	var="blocked_users_header" />
<fmt:message bundle="${loc}"
	key="local.search_user_page.search_unblocked_user_header"
	var="unblocked_users_header" />

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
					<input type="hidden" name="pageNumber" value="1" /> <input
						type="hidden" name="ability" value="false" />
					<button class="main_button" type="submit" name="command"
						value="SEARCH_USER">${blocked_users_button}</button>
				</form>

				<form action="Controller" method="post">
					<input type="hidden" name="pageNumber" value="1" /> <input
						type="hidden" name="ability" value="true" />
					<button class="main_button" type="submit" name="command"
						value="SEARCH_USER">${unblocked_users_button}</button>
				</form>

				<form action="Controller" method="post">
					<input type="hidden" name="pageNumber" value="1" />
					<button class="main_button" type="submit" name="command"
						value="GO_TO_MAIN_PAGE">${main_page_button}</button>
				</form>

			</div>
		</div>
	</div>

	<div class="main-content">

		<c:if test="${users!=null}">
			<br />
			<c:choose>
				<c:when test="${ability}">
					<b><c:out value="${unblocked_users_header}" /></b>
				</c:when>
				<c:when test="${!ability}">
					<b><c:out value="${blocked_users_header}" /></b>
				</c:when>
			</c:choose>
			<br />
			<div align="center">
				<p>
					<c:out value="${page}" />
					<c:out value="${param.pageNumber}" />
				</p>
			</div>
		</c:if>
		<br />

		<c:forEach var="user" items="${users}">

			<form action="Controller" method="post">

				<input type="hidden" name="pageNumber" value="${pageNumber}" /> <input
					type="hidden" name="command" value="SET_ABILITY_TO_COMMENT" />
				<c:if test="${ability}">
					<input type="hidden" name="ability" value="false" />
					<input type="submit" value="${block_button}">
				</c:if>
				<c:if test="${!ability}">
					<input type="hidden" name="ability" value="true" />
					<input type="submit" value="${unblock_button}">
				</c:if>
				<input type="checkbox" name="userId" value="${user.id}" required>
				<b> <c:out value="${user.login }" /></b>
			</form>

			<br>

		</c:forEach>

		<div align="center">
			<c:forEach begin="1" end="${amountPage}" step="1" varStatus="i">
				<c:url value="Controller?command=SEARCH_USER" var="url">
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