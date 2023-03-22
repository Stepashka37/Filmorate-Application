# java-filmorate
Template repository for Filmorate project.

Схема для базы данных: 

<p align="left">
<a href="https://miro.com/app/board/uXjVMbPpfag=/?moveToWidget=3458764549540578678&cot=10" target="_blank" rel="noreferrer"><img src="https://github.com/Stepashka37/java-filmorate/blob/add-friends-likes/fr2MxXix2h0.jpg" width="2000" height="900" alt="Java" /></a>

  </p>

Примеры запросов для основных операций вашего приложения:
1) Получить список всех фильмов
   
   SELECT * 
   FROM Film;
   
2) Получить фильм c конкретным id:
   
   SELECT * 
   FROM Film
   WHERE film_id = <your_id>;
   
3) Получить фильм c конкретным id:
   
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
SELECT *
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

  
