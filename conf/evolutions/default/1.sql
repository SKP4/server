
# --- !Ups

CREATE TABLE "user" (
  "id" INT(20) NOT NULL,
  "name" VARCHAR(255) NOT NULL,
  "age" INT NOT NULL,
  PRIMARY KEY ("id")
);

INSERT INTO "user" VALUES (1, 'lambda', 27);
INSERT INTO "user" VALUES (2, 'Noh', 20);

# --- !Downs

DROP TABLE "user"