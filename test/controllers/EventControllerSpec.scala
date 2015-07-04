package controllers

import model.Event
import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.{Json, JsValue}
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class EventControllerSpec extends Specification {
  import model.EventParser._

  "EventController" should {
    "send 404 on a bad request" in new WithApplication() {
      route(FakeRequest(GET, "/event")) must beSome.which (status(_) == NOT_FOUND)
    }

    "request on /events should return 200" in new WithApplication() {
      val events = route(FakeRequest(GET, "/events")).get

      status(events) must equalTo(OK)
    }
  }

  "Event class" should {
    val event: Event = Event(Some(1), "launch", "not full",
      Some("2015-07-04"), Some("2015-07-05"), Some("2015-07-06"), 8)

    val json: JsValue = Json.parse(
      """
        {
          "id": 1,
          "eventType": "launch",
          "status": "not full",
          "createdAt": "2015-07-04",
          "updatedAt": "2015-07-05",
          "closedAt": "2015-07-06",
          "maxAttendeeCount": 8
        }
      """)

    "be able to be parsed JSON" in new WithApplication() {
      val j: JsValue = Json.toJson(event)
      val e: Event = json.as[Event]

      e must equalTo(event)
    }

  }
}
