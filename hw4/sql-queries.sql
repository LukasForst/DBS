SELECT guild_id
FROM guild_membership
GROUP BY guild_id
HAVING COUNT(member_id) > 10
ORDER BY guild_id DESC; --select all guild_ids that have more than 10 members in descending order (sorted by guild_id)

SELECT email
FROM payment_details
  NATURAL JOIN character
WHERE character.id = 1; --retrieve email address for user which own character with id 1

SELECT username
FROM "User"
  INNER JOIN administrator a on "User".id = a.user_id; --select all administrators usernames

SELECT name
FROM guild
WHERE id IN (SELECT enemy_of
             FROM guild_enemy); --select all guild names, that has enemy

SELECT id
FROM character
EXCEPT
SELECT character_id
FROM character_in_fight; --selects all character ids that were never in the fight

SELECT username
FROM "User"
  LEFT OUTER JOIN character cr ON "User".id = cr.user_id
WHERE cr IS NULL; --select all usernames from users which have no character