# Filmorate Application 🎥
## Description
Filmorate - проект социальной сети, которая поможет выбрать кино на основе того, какие фильмы вы иваши друзья смотрите и какие оценки им ставите. Пользователи могут добавляться в друзья, добавлять  редактировать фильмы, оставлять отзывы и оценки, осуществлять поиск фильмов, получать рекомендации и выборки самых популярных фильмов по году выпуска и режиссеру.

## Tech Stack 🔧
[![Java](https://img.shields.io/badge/Java%2011-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/) [![Spring](https://img.shields.io/badge/Spring%20Boot%202.7.9-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-framework) [![JDBC](https://img.shields.io/badge/JDBC-FF5733?style=for-the-badge&logo=JUnit&logoColor=white)](https://docs.oracle.com/javase/tutorial/jdbc/overview/index.html) [![H2 Database](https://img.shields.io/badge/H2-0000FF?style=for-the-badge&logo=H2&logoColor=white)](https://www.h2database.com/html/main.html) [![JUnit](https://img.shields.io/badge/JUnit%205-9F2B68?style=for-the-badge&logo=JUnit&logoColor=white)](https://junit.org/junit5/docs/current/user-guide/)
[![Maven](https://img.shields.io/badge/Maven-00008B?style=for-the-badge&logo=Maven&logoColor=white)](https://maven.apache.org/)

## How to set up the project ▶️

### 1) Склонируйте репозиторий и перейдите в него 
```
git clone https://github.com/Stepashka37/java-filmorate.git
```
### 2) Запустите проект в выбранной IDE

### 3) Перейдите по адресу 
```
http://localhost:8080
```
### 4) Можно работать с проектом


# Функциональности проекта:

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


# Database schema: 
![](https://github.com/Stepashka37/java-filmorate/blob/main/DB%20scheme.jpg?raw=true)

# Team 🎯:

- [Бобровский Виктор](https://github.com/VictorBobrovskiy) - добавление отзывы, поиск общие фильмы, выдача популярных фильмов
- [Корюкова Надежда](https://github.com/Salaia) - реализация алгоритма общих рекомендаций
- [Жолтиков Александр](https://github.com/Zholtikov-A) - добавление режиссеров, получение фильмов режиссера
- [Пьянков Владимир](https://github.com/Antroverden) - реализация ленты событий
- [Дмитрий Максимов](https://github.com/Stepashka37) - поиск по ключевой фразе по названию и режиссеру фильма, тимлид


