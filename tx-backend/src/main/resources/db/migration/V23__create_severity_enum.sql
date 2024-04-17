CREATE TYPE severity AS ENUM ('MINOR','MAJOR','CRITICAL','LIFE_THREATENING');

ALTER TABLE notification_message ALTER COLUMN "severity" TYPE severity USING ("severity"::severity);

CREATE CAST (varchar AS severity) WITH INOUT AS IMPLICIT;
