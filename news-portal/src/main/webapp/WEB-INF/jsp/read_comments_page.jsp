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
<fmt:message bundle="${loc}" key="local.page" var="page" />
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
				<form action="Controller" method="GET">
					<input type="hidden" name="command" value="CHANGE_LOCAL" /> <select
						id="locale-select" name="local" onchange="this.form.submit()">
						<option value="ru">${ru_button}</option>
						<option value="en">${en_button}</option>
					</select>
				</form>
				<form action="Controller" method="GET">
					<input type="hidden" name="pageNumber" value="1" />
					<button class="main_button" type="submit" name="command"
						value="GO_TO_MAIN_PAGE">${main_button}</button>
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
		<c:forEach var="comment" items="${comments}">
			<p>
				<b> <c:out value="${comment.userLogin}" /> <c:out
						value="${comment.created}" />
				</b>
			</p>

			<p>
				<c:out value="${comment.text}" />
			</p>
			<hr>
		</c:forEach>

		<form action="Controller" method="GET">
			<input type="hidden" name="idNews" value="${param.idNews}" /> <input
				type="hidden" name="command" value="GO_TO_READ_NEWS_PAGE" /> <input
				type="submit" value="${back_to_news_button}" />
		</form>

		<div align="center">
			<c:forEach begin="1" end="${amountPage}" step="1" varStatus="i">
				<c:url value="Controller?command=GO_TO_READ_COMMENTS_PAGE" var="url">
					<c:param name="pageNumber" value="${i.index}" />
					<c:param name="idNews" value="${param.idNews}" />
				</c:url>
				<a href="${url}">${i.index}</a>
			</c:forEach>
		</div>

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