package controllers


import java.util.concurrent.atomic.{AtomicLong}

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import model._

import scala.concurrent.Future

class UserController extends Controller with UserDao {
  import User._
  import scala.concurrent.ExecutionContext.Implicits.global

  var cache = Map.empty[Long, User]
  cache += 1L -> User(1L, "Hoon", 27)
  cache += 2L -> User(2L, "Noh", 20)

  var uid = new AtomicLong(2L)

  def createUid: Long = uid.incrementAndGet()

  def getUser(id: Long) = Action.async {
    findById(id).map { user =>
      user match {
        case Some(user) => Ok(Json.toJson(user))
        case None       => NotFound
      }
    }
  }

  def getAllUsers = Action.async {
    findAll().map(users => Ok(Json.toJson(users)))
  }

  def createUser = Action.async(parse.json) { request =>
    request.body.asOpt[UserSignupRequest] match {
      case None => Future { BadRequest } // insufficient parameters
      case Some(UserSignupRequest(name, age)) =>
        findByName(name).map(user =>
          user match {
            case Some(user) => Conflict
            case None =>
              val uid = createUid
              val created = User(uid, name, age)
              insert(created)
              Created(Json.toJson(created))
          }
        )
    }
  }
}
