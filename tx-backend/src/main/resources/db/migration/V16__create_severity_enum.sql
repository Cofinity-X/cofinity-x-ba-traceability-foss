CREATE TYPE severity AS ENUM ('MINOR','MAJOR','CRITICAL','LIFE_THREATENING');

ALTER TABLE alert_notification ALTER COLUMN "severity" TYPE severity USING ("severity"::severity);
ALTER TABLE investigation_notification ALTER COLUMN "severity" TYPE severity USING ("severity"::severity);

CREATE CAST (varchar AS severity) WITH INOUT AS IMPLICIT;
