package com.github.skp4.service

import com.github.skp4.domain.User
import org.specs2.mutable.Specification
import spray.http.StatusCodes._
import spray.http._
import spray.testkit.Specs2RouteTest

class UserServiceSpec extends Specification with Specs2RouteTest with UserService {
  def actorRefFactory = system

  import com.github.skp4.domain.UserJsonProtocol._

  "UserService" should {

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> userRoute ~> check {
        handled must beFalse
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put("/user") ~> sealRoute(userRoute) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }

    "return users for GET to `hello`" in {
      Get("/user") ~> userRoute ~> check {
        val user = responseAs[User]
        user.id must be equalTo ("1ambda")
      }
    }
  }
}
