package domain

import play.api.libs.json._
import play.api.libs.functional.syntax._
import slick.driver.JdbcProfile

case class User(uid: Long, name: String, age: Int)
case class UserSignupRequest(name: String, age: Int)

//trait UserTable {
//  protected val driver: JdbcProfile
//  import driver.api._
//
//  class Users(tag: Tag) extends Table[User](tag, "USERS") {
//    def uid  = column[Long]("id", O.PrimaryKey, O.AutoInc)
//    def name = column[String]("NAME")
//    def age  = column[Int]("COLOR")
//  }
//}

object User {
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
