package model

import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import play.api.libs.json._
import play.api.libs.functional.syntax._
import slick.dbio.DBIO
import slick.driver.JdbcProfile
import slick.lifted.{TableQuery, ProvenShape}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class User(id: Long, name: String, age: Int)
case class UserSignupRequest(name: String, age: Int)

trait UserTable {
  protected val driver: JdbcProfile
  import driver.api._
  class Users(tag: Tag) extends Table[User](tag, "USERS") {
    def id = column[Long]("id", O.PrimaryKey)
    def name = column[String]("name")
    def age = column[Int]("age")
    def * : ProvenShape[User] = (id, name, age) <>((User.apply _).tupled, User.unapply)
  }
}

object UserDao extends UserTable with HasDatabaseConfig[JdbcProfile] {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val users = TableQuery[Users]

  def findById(id: Long): Future[Option[User]] = {
    db.run(users.result).map(users => users.find(_.id == id))
  }

  def findByName(name: String): Future[Option[User]] = {
    db.run(users.result).map(_.find(_.name == name))
  }
  
  def findAll(): Future[Seq[User]] = {
    db.run(users.result)
  }

  def insert(user: User): Unit = {
    db.run(DBIO.seq(users += user))
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
