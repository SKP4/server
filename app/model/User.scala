package model

import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import play.api.libs.json._
import play.api.libs.functional.syntax._
import slick.driver.JdbcProfile
import slick.lifted.{TableQuery, ProvenShape}
import scala.concurrent.Future

case class User(id: Option[Long] = None, name: String, email: String, age: Int)
case class UserSignupRequest(name: String, email: String, age: Int)

class UserDao extends HasDatabaseConfig[JdbcProfile] {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  private val users = TableQuery[Users]
  import driver.api._

  def findById(id: Long): Future[Option[User]] = {
    db.run(users.result).map(users => users.find(_.id == Some(id)))
  }

  def findByName(name: String): Future[Option[User]] = {
    db.run(users.result).map(_.find(_.name == name))
  }

  def findByEmail(email: String): Future[Option[User]] = {
    db.run(users.result).map(_.find(_.email == email))
  }

  def findAll(): Future[Seq[User]] = {
    db.run(users.result).map(_.toList)
  }

  def insert(user: User): Future[Long] = {
    db.run(users returning users.map(_.id) += user)
  }

  class Users(tag: Tag) extends Table[User](tag, "user") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def email = column[String]("email", O.PrimaryKey)
    def age = column[Int]("age")
    def * = (id.?, name, email, age) <> (User.tupled, User.unapply)
  }
}

object UserParser {
  implicit val userWrites = (
    (JsPath \ "id").write[Option[Long]] and
    (JsPath \ "name").write[String] and
    (JsPath \ "email").write[String] and
    (JsPath \ "age").write[Int]
    )(unlift(User.unapply))

  implicit val userSignupRequestReads = (
    (JsPath \ "name").read[String] and
    (JsPath \ "email").read[String] and
    (JsPath \ "age").read[Int]
    )(UserSignupRequest.apply _)

  implicit val userSignUpRequestWrites = (
    (JsPath \ "name").write[String] and
    (JsPath \ "email").write[String] and
    (JsPath \ "age").write[Int]
    )(unlift(UserSignupRequest.unapply))
}
