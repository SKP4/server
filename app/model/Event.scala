package model

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Event(id: Option[Long] = None,
                 eventType: String,
                 status: String,
                 createdAt: Option[String] = None,
                 updatedAt: Option[String] = None,
                 closedAt: Option[String] = None,
                 maxAttendeeCount: Int
//                 ,creator: User
                  )


object EventParser {
  implicit val eventWrites = (
    (JsPath \ "id").write[Option[Long]] and
    (JsPath \ "eventType").write[String] and
    (JsPath \ "status").write[String] and
    (JsPath \ "createdAt").write[Option[String]] and
    (JsPath \ "updatedAt").write[Option[String]] and
    (JsPath \ "closedAt").write[Option[String]] and
    (JsPath \ "maxAttendee").write[Int]
  )(unlift(Event.unapply))

  implicit val eventReads = (
    (JsPath \ "id").readNullable[Long] and
    (JsPath \ "eventType").read[String] and
    (JsPath \ "status").read[String] and
    (JsPath \ "createdAt").readNullable[String] and
    (JsPath \ "updatedAt").readNullable[String] and
    (JsPath \ "closedAt").readNullable[String] and
    (JsPath \ "maxAttendeeCount").read[Int]
  )(Event.apply _)
}



