package model

import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import play.api.libs.json._
import play.api.libs.functional.syntax._
import slick.driver.JdbcProfile
import slick.lifted.{TableQuery, ProvenShape}
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

case class User(id: Long, name: String, age: Int)
case class UserSignupRequest(name: String, age: Int)

class UserDao extends HasDatabaseConfig[JdbcProfile] {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  private val users = TableQuery[Users]
  import driver.api._

  def findById(id: Long): Future[Option[User]] = {
    db.run(users.result).map(users => users.find(_.id == id))
  }

  def findByName(name: String): Future[Option[User]] = {
    db.run(users.result).map(_.find(_.name == name))
  }
  
  def findAll(): Future[Seq[User]] = {
    db.run(users.result).map(_.toList)
  }

  def insert(user: User): Unit = {
    db.run(users += user).map(_ => ())
  }

  class Users(tag: Tag) extends Table[User](tag, "user") {
    def id = column[Long]("id", O.PrimaryKey)
    def name = column[String]("name")
    def age = column[Int]("age")
    def * : ProvenShape[User] = (id, name, age) <>((User.apply _).tupled, User.unapply)
  }
}

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
