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
<link rel="stylesheet" href="css/read_comment_page.css">

<fmt:setLocale value="${sessionScope.local}" />
<fmt:setBundle basename="local" var="loc" />
<fmt:message bundle="${loc}" key="local.title_site" var="title_site" />
<fmt:message bundle="${loc}" key="local.ru_button" var="ru_button" />
<fmt:message bundle="${loc}" key="local.en_button" var="en_button" />
<fmt:message bundle="${loc}" key="local.to_main_page_button"
	var="main_button" />
<fmt:message bundle="${loc}" key="local.read_comments_page.next_button"
	var="next_button" />
<fmt:message bundle="${loc}" key="local.read_comments_page.page"
	var="page" />
<fmt:message bundle="${loc}"
	key="local.read_comments_page.to_read_news_page_button"
	var="back_to_news_button" />
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
					<button class="main_button" type="submit" name="command"
						value="go_to_main_page">${main_button}</button>
				</form>
			</div>
		</div>
	</div>
	<div class="main-content">

		<c:if test="${user.role != 'GUEST'}">
			<div align="center">
				<p>${page}${pageNumber}</p>
			</div>
			<br />
			<c:forEach var="comment" items="${comments}">
				<p>
					<b>${comment.user.login}</b> ${comment.created}
				</p>

				<p>${comment.text}</p>
				<hr>
			</c:forEach>
		</c:if>

		<form action="Controller" method="post">
			<input type="hidden" name="idNews" value="${idNews}" /> <input
				type="hidden" name="pageNumber" value="${pageNumber}" /> <input
				type="hidden" name="command" value="Go_To_Read_Comments_Page" />
			<div>
				<input type="submit" value="${next_button}" />
			</div>
		</form>
		<form action="Controller" method="post">
			<input type="hidden" name="idNews" value="${idNews}" /> <input
				type="hidden" name="command" value="go_to_read_news_page" /> <input
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