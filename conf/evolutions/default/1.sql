
# --- !Ups

CREATE TABLE User (
  "id" BIGINT(20) NOT NULL,
  "name" VARCHAR(255) NOT NULL,
  "age" INT NOT NULL,
  PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE User