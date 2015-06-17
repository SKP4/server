package controllers


import java.util.concurrent.atomic.{AtomicLong, AtomicReference}

import akka.actor.FSM.->
import domain.{UserSignupRequest, User}
import play.api.libs.json.{Writes, Json, JsValue}
import play.api.mvc.{Action, Controller}

import User._

class UserController extends Controller {

  var users = Map.empty[String, User]
  users += "Hoon" -> User(1L, "Hoon", 27)
  users += "Noh" -> User(2L, "Noh", 20)

  var uid = new AtomicLong(0L)

  def createUid: Long = uid.incrementAndGet()

  def getUser(uid: Long) = Action {
    if (users.values.f)
      Ok(Json.toJson(users(uid)))
    else NotFound
  }

  def getAllUsers = Action {
    Ok(Json.toJson(users.values))
  }

  def createUser = Action(parse.json) { request =>
    val signupReq@UserSignupRequest(name, age) = request.body.as[UserSignupRequest]
    val uid = createUid

    val created = User(uid, name, age)
    users += name -> created
    Ok(Json.toJson(created))
  }
}
