DROP VIEW IF EXISTS rich_guilds;
DROP INDEX IF EXISTS payment_details_email_index;
DROP TRIGGER IF EXISTS guild_tg_estate_worth_check
ON guild;
DROP FUNCTION IF EXISTS check_guild_estate_worth();
DROP FUNCTION IF EXISTS normalize_guilds_month_money();

CREATE VIEW rich_guilds AS
  SELECT *
  FROM guild
  WHERE estate_worth >= 1000; --create new view with guilds that have more than 1000 golds, local check option is enough for this view
--   WITH LOCAL CHECK OPTION; it is currently not possible to execute query with 'WITH LOCAL CHECK OPTION' although it should be possible
-- according to the https://www.postgresql.org/docs/9.2/static/sql-createview.html

CREATE INDEX payment_details_email_index
  ON payment_details (email); --creates index on email column in the payment_details table

CREATE FUNCTION normalize_guilds_month_money()
  RETURNS INTEGER AS $guild_count$
DECLARE guild_count INTEGER;
BEGIN
  SELECT count(*)
  INTO guild_count
  FROM guild
  WHERE estate_worth > 10000;
  UPDATE guild
  SET estate_worth = 10000
  WHERE estate_worth > 10000;

end;
$guild_count$
LANGUAGE plpgsql; -- this function will set estate_worth to 10000 in all guilds that have more than 10000golds and returns count of guilds that were affected


CREATE FUNCTION check_guild_estate_worth()
  RETURNS TRIGGER AS $$
BEGIN
  IF NEW.estate_worth IS NULL
  THEN
    RAISE EXCEPTION 'estate worth cannot be null';
  END IF;
  IF NEW.estate_worth < 0
  THEN
    RAISE EXCEPTION 'cannot have a negative estate worth';
  END IF;
  RETURN NEW;
END;
$$
LANGUAGE plpgsql; --checks whether is column estate_worth not null or positive


CREATE TRIGGER guild_tg_estate_worth_check
  BEFORE INSERT OR UPDATE OF estate_worth
  on guild
  FOR EACH ROW EXECUTE PROCEDURE check_guild_estate_worth(); --trigger that calls check_guild_estate_worth() on every insert or update in the table guild