Приложение представляет собой новостной портал.

При написании использованы следующие технологии:

Servlets, Java Server Pages, Java Server Pages Standard Tag Library, The Java Database Connectivity API (JDBC).

Действия, которые можно выполнять на сайте, зависят от роли пользователя (гость, пользователь, редактор, администратор). Авторизация происходит в соответствующем фильтре.

Реализован следующий функционал в зависимости от уровня доступа :

гость : просмотр заглавия новости и краткое содержание, регистрация, авторизация;

пользователь : просмотр новости целиком, добавление/удаление новостей в/из избранные для отдельного просмотра, поиск новостей по слову в заглавии, комментирование новости, просмотр комментариев, просмотр профиля, изменение пароля;

админ : возможности пользователя + блокирование юзеров для возможности комментирования;

редактор : возможности пользователя + создание, удаление, редактирование новости;

PS : применена пагинация новостей/избранных новостей, комментариев, пользователей на соответствующих страницах;

локализация, хеширование паролей.
