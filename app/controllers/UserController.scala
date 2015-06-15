package controllers

import play.api.libs.json.{Json, JsValue}
import play.api.mvc.{Action, Controller}

class UserController extends Controller {
  def getUser = Action {
    val user: JsValue = Json.parse("""
         {  "id": "1ambda",
            "name": "Hoon",
            "age": 27
         }""")

    Ok(user)
  }
}
