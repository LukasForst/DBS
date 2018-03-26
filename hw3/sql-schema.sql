DROP TABLE IF EXISTS "guild_membership";
DROP TABLE IF EXISTS "guild_enemy";
DROP TABLE IF EXISTS "guild";
DROP TABLE IF EXISTS "weapon";
DROP TABLE IF EXISTS "character_skills";
DROP TABLE IF EXISTS "skills";
DROP TABLE IF EXISTS "character_in_fight";
DROP TABLE IF EXISTS "fight";
DROP TABLE IF EXISTS "administrators_permissions";
DROP TABLE IF EXISTS "permissions";
DROP TABLE IF EXISTS "character";
DROP TABLE IF EXISTS "administrator";
DROP TABLE IF EXISTS "player";
DROP TABLE IF EXISTS "payment_details";
DROP TABLE IF EXISTS "User";


CREATE TABLE "User"
(
  username VARCHAR(20) NOT NULL UNIQUE,
  id       SERIAL      NOT NULL,
  password VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE payment_details
(
  credit_card_no INTEGER     NOT NULL,
  email          VARCHAR(50) NOT NULL,
  user_id        INTEGER     NOT NULL
    CONSTRAINT payment_details_user__fk REFERENCES "User",
  CONSTRAINT payment_details_email_ch CHECK (email LIKE '_%@_%.__%'),
  PRIMARY KEY (user_id)
);

CREATE TABLE player
(
  user_id INTEGER NOT NULL
    CONSTRAINT player_user_id_fk REFERENCES "User",
  PRIMARY KEY (user_id)
);

CREATE TABLE administrator
(
  user_id INTEGER NOT NULL
    CONSTRAINT administrator_user_id_fk REFERENCES "User",
  PRIMARY KEY (user_id)
);

CREATE TABLE character
(
  id   SERIAL PRIMARY KEY,
  name VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE permissions
(
  permission_name VARCHAR(256) PRIMARY KEY
);

CREATE TABLE administrators_permissions
(
  admin_id   INTEGER      NOT NULL
    CONSTRAINT administrators_permissions_administrator_id_fk
    REFERENCES administrator,
  permission VARCHAR(256) NOT NULL
    CONSTRAINT administrators_permissions_permission_name_fk
    REFERENCES permissions,
  PRIMARY KEY (admin_id, permission)
);

CREATE TABLE fight
(
  place     VARCHAR(256) NOT NULL,
  date_time TIMESTAMP    NOT NULL,
  id        SERIAL PRIMARY KEY
);

CREATE TABLE character_in_fight
(
  character_id INTEGER NOT NULL
    CONSTRAINT character_in_fight_character_id_fk
    REFERENCES character,
  fight_id     INTEGER NOT NULL
    CONSTRAINT character_in_fight_fight_id_fk
    REFERENCES fight,
  PRIMARY KEY (character_id, fight_id)
);

CREATE TABLE skills
(
  name VARCHAR(50) NOT NULL
    CONSTRAINT skills_pkey
    PRIMARY KEY
);

CREATE TABLE character_skills
(
  skill_name   VARCHAR(50) NOT NULL
    CONSTRAINT character_skills_skills_name_fk
    REFERENCES skills,
  character_id INTEGER     NOT NULL
    CONSTRAINT character_skills_character_id_fk
    REFERENCES character,
  PRIMARY KEY (skill_name, character_id)
);

CREATE TABLE weapon
(
  name         VARCHAR(50) NOT NULL,
  character_id INTEGER     NOT NULL
    CONSTRAINT weapon_character_id_fk
    REFERENCES character,
  PRIMARY KEY (name, character_id)
);

CREATE TABLE guild
(
  id           SERIAL PRIMARY KEY,
  name         VARCHAR(50) NOT NULL UNIQUE,
  estate_worth INTEGER     NOT NULL,
  guild_master INTEGER     NOT NULL UNIQUE
    CONSTRAINT guild_character_id_fk
    REFERENCES character,
  CONSTRAINT guild_estate_worth_ch  CHECK (estate_worth >= 0)
);

CREATE TABLE guild_enemy
(
  enemy_of INTEGER NOT NULL
    CONSTRAINT guild_enemy_guild_id_fk
    REFERENCES guild,
  enemy_to INTEGER NOT NULL
    CONSTRAINT guild_enemy_guild_id_fk_2
    REFERENCES guild,
  PRIMARY KEY (enemy_of, enemy_to)
);

CREATE TABLE guild_membership
(
  guild_id  INTEGER NOT NULL
    CONSTRAINT guild_membership_guild_id_fk
    REFERENCES guild,
  member_id INTEGER NOT NULL UNIQUE
    CONSTRAINT guild_membership_character_id_fk
    REFERENCES character,
  PRIMARY KEY (guild_id, member_id)
);