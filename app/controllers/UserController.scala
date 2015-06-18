package controllers


import java.util.concurrent.atomic.{AtomicLong, AtomicReference}

import akka.actor.FSM.->
import domain.{UserSignupRequest, User}
import play.api.libs.json.{Writes, Json, JsValue}
import play.api.mvc.{Action, Controller}

import User._

import scala.collection.immutable.Iterable

class UserController extends Controller {

  var users = Map.empty[Long, User]
  users += 1L -> User(1L, "Hoon", 27)
  users += 2L -> User(2L, "Noh", 20)

  var uid = new AtomicLong(2L)

  def createUid: Long = uid.incrementAndGet()

  def getUser(uid: Long) = Action {
    users.get(uid) match {
      case Some(user) => Ok(Json.toJson(user))
      case None       => NotFound
    }
  }

  def getAllUsers = Action {
    Ok(Json.toJson(users.values))
  }

  def createUser = Action(parse.json) { request =>
    request.body.asOpt[UserSignupRequest] match {
      case None => BadRequest // insufficient parameters
      case Some(UserSignupRequest(name, age)) =>
        val uid = createUid

        users.values.filter(_.name == name).toList match {
          case List(user) => Conflict
          case Nil        =>
            val created = User(uid, name, age)
            users += uid -> created
            Ok(Json.toJson(created))
        }
    }
  }
}
