<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>User_profile_page</title>
<link rel="stylesheet" href="css/main_page.css">

<fmt:setLocale value="${sessionScope.local}" />
<fmt:setBundle basename="local" var="loc" />
<fmt:message bundle="${loc}" key="local.title_site" var="title_site" />
<fmt:message bundle="${loc}" key="local.ru_button" var="ru_button" />
<fmt:message bundle="${loc}" key="local.en_button" var="en_button" />
<fmt:message bundle="${loc}" key="local.user_profile_page.name"
	var="name" />
<fmt:message bundle="${loc}" key="local.user_profile_page.surname"
	var="surname" />
<fmt:message bundle="${loc}" key="local.user_profile_page.eMail"
	var="eMail" />
<fmt:message bundle="${loc}" key="local.user_profile_page.login"
	var="login" />
<fmt:message bundle="${loc}" key="local.user_profile_page.profile"
	var="profile" />
<fmt:message bundle="${loc}" key="local.to_main_page_button"
	var="main_page_button" />
<fmt:message bundle="${loc}"
	key="local.user_profile_page.change_password_button"
	var="change_password_button" />
</head>
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
					<button class="main_button" type="submit" name="command"
						value="go_to_main_page">${main_page_button}</button>
				</form>
			</div>
		</div>
	</div>
	<div class="main-content">
		<c:if test="${user.role != 'GUEST'}">
			<br />
			<h1>${profile}</h1>


			<h3>${name}</h3>
			<c:out value="${info.name}" />

			<h3>${surname}</h3>
			<c:out value="${info.surname}" />

			<h3>${eMail}</h3>
			<c:out value="${info.eMail}" />

			<h3>${login}</h3>
			<c:out value="${info.login}" />
			<br />
			<br />
			<form action="Controller" method="post">
				<input type="hidden" name="command" value="UPDATE_USER_PROFILE" />
				<input type="submit" value="${change_password_button}" />
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









