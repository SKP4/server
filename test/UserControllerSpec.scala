import model.{UserSignupRequest, User}
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.json.{Json, JsValue}
import play.api.mvc.AnyContentAsJson

import play.api.test._
import play.api.test.Helpers._


/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class UserControllerSpec extends Specification {
  import User._

  "UserController" should {

    "send 404 on a bad request 'user'" in new WithApplication() {
      route(FakeRequest(GET, "/user")) must beSome.which (status(_) == NOT_FOUND)
    }

    "send 400 when given insufficient parameters (name, age)" in new WithApplication() {
      val result1 = buildUsersRequestAndRoute(POST, Json.toJson( """ { "age": 27 } """))
      val result2 = buildUsersRequestAndRoute(POST, Json.toJson( """ { "name": "1ambda" } """))
      val result3 = buildUsersRequestAndRoute(POST, Json.toJson("""{}"""))

      status(result1) must equalTo(BAD_REQUEST)
      status(result2) must equalTo(BAD_REQUEST)
      status(result3) must equalTo(BAD_REQUEST)
    }

    "return 409 when creating a duplicated user" in new WithApplication() {
      val result =
        buildUsersRequestAndRoute(POST, Json.toJson(UserSignupRequest("Hoon", 27)))

      status(result) must equalTo(CONFLICT)
    }

    "return 201 when creating a new user" in new WithApplication() {
      val result =
        buildUsersRequestAndRoute(POST, Json.toJson(UserSignupRequest("Duk", 27)))

      status(result) must equalTo(CREATED)
    }

    "render the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/")).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Your new application is ready!")
    }
  }
  
  def buildUsersRequestAndRoute(method: String, body: JsValue) = {

    val req: FakeRequest[AnyContentAsJson] = FakeRequest(method, "/users")
      .withHeaders(CONTENT_TYPE -> "application/json")
      .withJsonBody(Json.toJson(body))

    route(req).get
  }
}
