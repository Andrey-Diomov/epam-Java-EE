<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Authorization Page</title>
<link rel="stylesheet" href="css/authorization_page.css">
<link rel="stylesheet" href="css/main_page.css">

<fmt:setLocale value="${sessionScope.local}" />
<fmt:setBundle basename="local" var="loc" />
<fmt:message bundle="${loc}" key="local.ru_button" var="ru_button" />
<fmt:message bundle="${loc}" key="local.en_button" var="en_button" />
<fmt:message bundle="${loc}" key="local.title_site" var="title_site" />
<fmt:message bundle="${loc}" key="local.authorization_page.table_header"
	var="table_header" />
<fmt:message bundle="${loc}"
	key="local.authorization_page.password_placeholder" var="password" />
<fmt:message bundle="${loc}"
	key="local.authorization_page.login_placeholder" var="login" />
<fmt:message bundle="${loc}" key="local.authorization_page.send_button"
	var="send_button" />
<fmt:message bundle="${loc}" key="local.to_main_page_button"
	var="main_page_button" />

</head>
<body>
	<div class="header">

		<div class="title">
			<h1>
				<c:out value="${title_site}" />
			</h1>
		</div>

		<div class="controls">
			<div class="buttons-block">
				<form action="Controller" method="GET">
					<input type="hidden" name="command" value="CHANGE_LOCAL" /> <select
						id="locale-select" name="local" onchange="this.form.submit()">
						<option value="ru">${ru_button}</option>
						<option value="en">${en_button}</option>
					</select>
				</form>

				<form action="Controller" method="GET">
					<input type="hidden" name="pageNumber" value="1" /> <input
						type="hidden" name="messages" value="" />
					<button class="main_button" type="submit" name="command"
						value="GO_TO_MAIN_PAGE">${main_page_button}</button>
				</form>
			</div>
		</div>

	</div>

	<br>

	<div class="authorization_form">

		<c:if test="${param.message != null}">
			<fmt:message bundle="${loc}" key="${param.message}" var="message" />
			<c:out value="${message}"></c:out>
		</c:if>

		<c:if test="${param.list_messages == 'authorization'}">

			<c:forEach var="message" items="${messages}">

				<fmt:message bundle="${loc}" key="${message}" var="message" />
				<c:out value="${message}"></c:out>
				<br><br>
			</c:forEach>

		</c:if>

		<form action="Controller" method="POST">
			<h3 class="headline">
				<c:out value="${table_header}" />
			</h3>
			<input type="hidden" name="command" value="SIGN_IN" /> ${login} <br />
			<input type="text" name="login" value="" /><br /> ${password} <br />
			<input type="password" name="password" value="" /><br /> <input
				type="submit" value="${send_button}" /><br />
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
