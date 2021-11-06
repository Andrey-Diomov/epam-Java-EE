<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Registration_page</title>
<link rel="stylesheet" href="css/registration_page.css">
<link rel="stylesheet" href="css/main_page.css">

<fmt:setLocale value="${sessionScope.local}" />
<fmt:setBundle basename="local" var="loc" />
<fmt:message bundle="${loc}" key="local.ru_button" var="ru_button" />
<fmt:message bundle="${loc}" key="local.en_button" var="en_button" />
<fmt:message bundle="${loc}" key="local.title_site" var="title_site" />
<fmt:message bundle="${loc}" key="local.registration_page.table_header"
	var="table_header" />
<fmt:message bundle="${loc}"
	key="local.registration_page.password_placeholder" var="password" />
<fmt:message bundle="${loc}"
	key="local.registration_page.login_placeholder" var="login" />
<fmt:message bundle="${loc}"
	key="local.registration_page.name_placeholder" var="name" />
<fmt:message bundle="${loc}"
	key="local.registration_page.surname_placeholder" var="surname" />
<fmt:message bundle="${loc}"
	key="local.registration_page.email_placeholder" var="email" />
<fmt:message bundle="${loc}" key="local.registration_page.send_button"
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
					<input type="hidden" name="pageNumber" value="1" />
					<button class="main_button" type="submit" name="command"
						value="GO_TO_MAIN_PAGE">${main_page_button}</button>
				</form>
			</div>
		</div>
	</div>
	<br>
	<br>
	<br>
	<br>

	<div class="registration_form">

		<c:if test="${param.message != null}">
			<fmt:message bundle="${loc}" key="${param.message}" var="message" />
			<c:out value="${message}"></c:out>
		</c:if>

		<c:if test="${param.list_messages == 'registration'}">

			<c:forEach var="message" items="${messages}">

				<fmt:message bundle="${loc}" key="${message}" var="message" />
				<c:out value="${message}"></c:out>
				<br>
				<br>
			</c:forEach>

		</c:if>
		
		<form action="Controller" method="POST">
			<h3 class="headline">${table_header}</h3>
			<input type="hidden" name="command" value="REGISTRATION_NEW_USER" />
			${name}<br /> <input type="text" name="name"  value="" /><br />
			${surname}<br /> <input type="text" name="surname" value="" /><br />
			${email}<br /> <input type="text" name="eMail" value="" /><br />
			${login}<br /> <input type="text" name="login" value="" /><br />
			${password}<br /> <input type="password" name="password" value="" /><br />
			<input type="submit" value="${send_button}" /><br />
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