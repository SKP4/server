package controllers


import java.util.concurrent.atomic.{AtomicLong}

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import model._

import scala.concurrent.Future

class UserController extends Controller {
  import User._

  import scala.concurrent.ExecutionContext.Implicits.global
  def dao = new UserDao

  var uid = new AtomicLong(0L)

  def createUid: Long = uid.incrementAndGet()

  def getUser(id: Long) = Action.async {
    dao.findById(id).map { user =>
      user match {
        case Some(user) => Ok(Json.toJson(user))
        case None       => NotFound
      }
    }
  }

  def getAllUsers = Action.async {
    dao.findAll().map(users => Ok(Json.toJson(users)))
  }

  def createUser = Action.async(parse.json) { request =>
    request.body.asOpt[UserSignupRequest] match {
      case None => Future { BadRequest } // insufficient parameters
      case Some(UserSignupRequest(name, age)) =>
        dao.findByName(name).map(user =>
          user match {
            case Some(user) => Conflict
            case None =>
              val uid = createUid
              val created = User(uid, name, age)
              dao.insert(created)
              Created(Json.toJson(created))
          }
        )
    }
  }
}
