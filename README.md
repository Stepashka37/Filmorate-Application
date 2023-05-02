# java-filmorate
Template repository for Filmorate project.

## Функциональность «Отзывы» (Бобровский Виктор)

`POST /reviews`

Добавление нового отзыва.

`PUT /reviews`

Редактирование уже имеющегося отзыва.

`DELETE /reviews/{id}`

Удаление уже имеющегося отзыва.

`GET /reviews/{id}`

Получение отзыва по идентификатору. 

`GET /reviews?filmId={filmId}&count={count}`
Получение всех отзывов по идентификатору фильма, если фильм не указан то все. Если кол-во не указано то 10.

- `PUT /reviews/{id}/like/{userId}`  — пользователь ставит лайк отзыву.
- `PUT /reviews/{id}/dislike/{userId}`  — пользователь ставит дизлайк отзыву.
- `DELETE /reviews/{id}/like/{userId}`  — пользователь удаляет лайк/дизлайк отзыву.
- `DELETE /reviews/{id}/dislike/{userId}`  — пользователь удаляет дизлайк отзыву.

## Функциональность «Поиск» (Максимов Дмитрий)

`GET /fimls/search`

Возвращает список фильмов, отсортированных по популярности.

**Параметры строки запроса**

`query` — текст для поиска

`by` — может принимать значения `director` (поиск по режиссёру), `title` (поиск по названию), либо оба значения через запятую при поиске одновременно и по режиссеру и по названию.

## Функциональность «Общие фильмы» (Бобровский Виктор)

`GET /films/common?userId={userId}&friendId={friendId}`
Возвращает список фильмов, отсортированных по популярности.

**Параметры**

`userId` — идентификатор пользователя, запрашивающего информацию;

`friendId` — идентификатор пользователя, с которым необходимо сравнить список фильмов.

## Функциональность «Рекомендации» (Корюкова Надежда)

`GET /users/{id}/recommendations`

Возвращает рекомендации по фильмам для просмотра.

## Функциональность «Лента событий» (Пьянков Владимир)

`GET /users/{id}/feed`

Возвращает ленту событий пользователя.

## Функциональность «Удаление фильмов и пользователей» (Максимов Дмитрий)

`DELETE /users/{userId}` 

Удаляет пользователя по идентификатору. 

`DELETE /films/{filmId}` 

Удаляет фильм по идентификатору.

## Функциональность «Добавление режиссёров в фильмы» (Жолтиков Александр)

`GET /films/director/{directorId}?sortBy=[year,likes]` 

Возвращает список фильмов режиссера отсортированных по количеству лайков или году выпуска. 

`GET /directors` - Список всех режиссёров

`GET /directors/{id}`- Получение режиссёра по id

`POST /directors` - Создание режиссёра

`PUT /directors` - Изменение режиссёра

`DELETE /directors/{id}` - Удаление режиссёра

## Функциональность «Вывод самых популярных фильмов по жанру и годам» (Бобровский Виктор)

`GET /films/popular?count={limit}&genreId={genreId}&year={year}`

Возвращает список самых популярных фильмов указанного жанра за нужный год.

# Схема для базы данных: 

<p align="left">
<a href="https://miro.com/app/board/uXjVMbPpfag=/?moveToWidget=3458764549540578678&cot=10" target="_blank" rel="noreferrer"><img src="https://github.com/Stepashka37/java-filmorate/blob/add-friends-likes/My First Board (1).jpg" width="2000" height="900" alt="Java" /></a>

  </p>

Примеры запросов для основных операций вашего приложения:
1) Получить список всех фильмов
   
   SELECT * 
   FROM Film;
   
2) Получить фильм c конкретным id:
   
   SELECT * 
   FROM Film
   WHERE film_id = <your_id>;
   
3) Получить список самых популярных фильмов:
   
   SELECT f.name as name
          COUNT(fl.user_id) as likes_number
   FROM Film as f
   LEFT JOIN FIlm_likes AS fl ON fl.film_id = f.film_id
   GROUP BY f.name 
   ORDER BY likes_number DESC
   LIMIT <your_limit>;
   
4) Получить список всех юзеров
   
   SELECT * 
   FROM User;
   
5) Получить юзера с конкретным id
   
   SELECT * 
   FROM User
   WHERE user_id = <your_id>;
   
6) Получить друзей юзера с конкретным id

   SELECT u.* AS user,
      f.* AS friend
FROM User AS u
LEFT JOIN User AS f ON u.friend_id = f.user_id
WHERE u.user_id = <your_id>;

7) Получить общих друзей юзера c id = 1 и id = 2
SELECT user1_friends.user_id
FROM (SELECT u.* AS user,
      f.* AS friend
      FROM User AS u
      LEFT JOIN User AS f ON u.friend_id = f.user_id
      WHERE u.user_id = <your_id>) AS user1_friends
INNER JOIN (SELECT u.* AS user,
      f.* AS friend
      FROM User AS u
      LEFT JOIN User AS f ON u.friend_id = f.user_id
      WHERE u.user_id = <your_id>) AS user2_friends 
ON user1_friends.friend_id = user2_friends.frind_id;

  
  SELECT * FROM EMP JOIN DEPT ON EMP.DEPTNO = DEPT.DEPTNO;
