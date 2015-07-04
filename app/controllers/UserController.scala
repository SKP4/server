package controllers


import java.util.concurrent.atomic.{AtomicLong}

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import model._
import UserParser._
import scala.concurrent.Future

class UserController extends Controller {

  import scala.concurrent.ExecutionContext.Implicits.global
  def dao = new UserDao

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
      case Some(UserSignupRequest(name, email, age)) =>
        dao.findByEmail(email).map(user =>
          user match {
            case Some(user) => Conflict
            case None =>
              dao.insert(User(None, name, email, age))
              Created(Json.toJson(User(None, name, email, age)))
          }
        )

    }
  }
}
