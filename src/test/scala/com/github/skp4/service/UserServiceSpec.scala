package com.github.skp4.service

import com.github.skp4.domain.User
import org.specs2.mutable.Specification
import spray.http.StatusCodes._
import spray.http._
import spray.routing.MethodRejection
import spray.testkit.Specs2RouteTest

import MediaTypes._
import HttpCharsets._

class UserServiceSpec extends Specification with Specs2RouteTest with UserService {
  def actorRefFactory = system

  import com.github.skp4.domain.UserJsonProtocol._

  "UserService" should {

    "leave GET requests to other paths unhandled" in {
      Get("/user") ~> userRoute ~> check {
        handled must beFalse
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put("/users") ~> sealRoute(userRoute) ~> check {
        status === MethodNotAllowed

        responseAs[String] === "HTTP method not allowed, supported methods: GET, POST"
      }
    }

    "return users for GET to `hello`" in {
      Get("/users") ~> userRoute ~> check {
        status === OK

        val user = responseAs[User]
        user.id must be equalTo ("1ambda")
      }
    }

    "return Created for POST requests to users" in {
      val user =
        """{
      "id": "1ambda",
      "name": "Hoon",
      "age": 27
      }"""

      Post("/users", HttpEntity(ContentType(`application/json`, `UTF-8`), user)) ~> userRoute ~> check {
        status == Created
      }
    }
  }
}
