package controllers

import play.api.libs.json.{Writes, Json, JsValue}
import play.api.mvc.{Action, Controller}

case class User(id: String, name: String, age: Int)

object User {
  implicit val userWrites = new Writes[User] {
    override def writes(user: User) = Json.obj(
      "id" -> user.id,
      "name" -> user.name,
      "age" -> user.age
    )
  }
}

class UserController extends Controller {
  import User._

  def getUser = Action {
    val user: JsValue = Json.parse("""
         {  "id": "1ambda!",
            "name": "Hoon",
            "age": 27
         }""")

    Ok(Json.toJson(User("1ambda", "Hoon", 27)))
  }
}
