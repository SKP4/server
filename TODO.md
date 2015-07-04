### Day 4

EventModel
 
 int idx (Index)
 EventTypeModel type (Foriegn Key)
 long createdAt (타임스탬프)
 long updatedAt (타임스탬프)
 int limitPeopleCountd
 long joinTime
 UserModel ownerIdx (FK)
 

CREATE TABLE "event" (
  "id" BIGINT AUTO_INCREMENT NOT NULL,
  "event_type" VARCHAR(255) NOT NULL,
  "createdAt" TIMESTAMP DEFAULT now() NOT NULL,
  "updatedAt" TIMESTAMP DEFAULT now() NOT NULL,
  "max_attendee_count" INT(8) NOT NULL,
  "status" VARCHAR(255) NOT NULL,
  PRIMARY KEY ("id")
);

- db session
- dynamic routing

- ~~event controller hello~~ 
- ~~event case class~~
- event table
- event 

- delete user?

### Day 3

- ~~h2mem db~~
- ~~slick basic~~
- ~~insert if not exist~~
- ~~migrate to play-slick~~ 
- ~~resolve test fail~~
- ~~resolve dep conflict~~

### Day 2

- ~~400 handling~~ 
- ~~UserController test~~

### Day 1

- ~~case class User~~
- ~~User reader, writer~~
- ~~HttpStatus Test~~
