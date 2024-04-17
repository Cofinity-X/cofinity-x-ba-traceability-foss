CREATE TYPE status AS ENUM ('CREATED', 'SENT', 'RECEIVED', 'ACKNOWLEDGED', 'CANCELED', 'ACCEPTED', 'DECLINED', 'CLOSED');

ALTER TABLE notification_message ALTER COLUMN "status" TYPE status USING ("status"::status);
ALTER TABLE notification ALTER COLUMN "status" TYPE status USING ("status"::status);

CREATE CAST (varchar AS status) WITH INOUT AS IMPLICIT;
