package model

import play.api.libs.json._
import play.api.libs.functional.syntax._
import slick.lifted.ProvenShape
import slick.driver.H2Driver.api._

import scala.concurrent.Future

case class User(id: Long, name: String, age: Int)
case class UserSignupRequest(name: String, age: Int)

trait UserTable {
  class Users(tag: Tag) extends Table[User](tag, "USERS") {
    def id = column[Long]("id", O.PrimaryKey)
    def name = column[String]("name")
    def age = column[Int]("age")
    def * : ProvenShape[User] = (id, name, age) <>((User.apply _).tupled, User.unapply)
  }
}

object User {
  import scala.concurrent.ExecutionContext.Implicits.global

  def findById(id: Long): Option[User] = {
    db.run(u.result)
  }

  implicit val userWrites = (
    (JsPath \ "uid").write[Long] and
    (JsPath \ "name").write[String] and
      (JsPath \ "age").write[Int]
    )(unlift(User.unapply))

  implicit val userSignupRequestReads = (
    (JsPath \ "name").read[String] and
    (JsPath \ "age").read[Int]
    )(UserSignupRequest.apply _)

  implicit val userSignUpRequestWrites = (
    (JsPath \ "name").write[String] and
    (JsPath \ "age").write[Int]
    )(unlift(UserSignupRequest.unapply))
}
