<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Change_password_page</title>
<link rel="stylesheet" href="css/main_page.css">
<link rel="stylesheet" href="css/change_password_page.css">

<fmt:setLocale value="${sessionScope.local}" />
<fmt:setBundle basename="local" var="loc" />
<fmt:message bundle="${loc}" key="local.title_site" var="title_site" />
<fmt:message bundle="${loc}" key="local.ru_button" var="ru_button" />
<fmt:message bundle="${loc}" key="local.en_button" var="en_button" />
<fmt:message bundle="${loc}" key="local.to_main_page_button"
	var="main_button" />

<fmt:message bundle="${loc}"
	key="local.change_password_page.send_button" var="send_button" />

<fmt:message bundle="${loc}" key="local.change_password_page.password"
	var="password" />

<fmt:message bundle="${loc}"
	key="local.change_password_page.new_password" var="new_password" />
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

	<div class="change_password_form">
		<br>
		<c:if test="${param.message != null}">
			<fmt:message bundle="${loc}" key="${param.message}" var="message" />
			<c:out value="${message}"></c:out>
		</c:if>
		<br><br>
		<form action="Controller" method="POST">
			<input type="hidden" name="command" value="CHANGE_PASSWORD" />
			${password}<br /> <input type="password" name="password" value="" /><br />
			${new_password}<br /> <input type="password" name="new_password"
				value="" /><br /> <br> <input type="submit"
				value="${send_button}" /><br />
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