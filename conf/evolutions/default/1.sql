
# --- !Ups

CREATE TABLE "user" (
  "id" INT(20) AUTO_INCREMENT NOT NULL,
  "name" VARCHAR(255) NOT NULL,
  "email" VARCHAR(255) NOT NULL,
  "age" INT NOT NULL,
  PRIMARY KEY ("id", "email")
);

INSERT INTO "user" VALUES (NULL, 'Lambda', 'lambda@gmail.com', 27);
INSERT INTO "user" VALUES (NULL, 'Noh', 'noh@gmail.com', 20);

# --- !Downs

DROP TABLE "user"