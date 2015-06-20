package model

import play.api
import play.api.db
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

trait UserDao extends UserTable {
  import scala.concurrent.ExecutionContext.Implicits.global
  val users = TableQuery[Users]
  val db = Database.forConfig("h2mem1")

  def initDatabase() = {
    db.run(DBIO.seq(
      users.schema.create,
      users += User(1L, "Hoon", 27),
      users += User(2L, "Noh", 20),
      users.result.map(println)
    ))
    // db.close()
  }

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
