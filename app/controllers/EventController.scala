package controllers

import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

class EventController extends Controller {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  def index = Action.async {
    Future {
      Ok("hello")
    }
  }
}
