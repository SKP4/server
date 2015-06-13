package com.github.skp4.service

import akka.actor.{ActorLogging, Actor}
import com.github.skp4.domain.User

import spray.http.MediaTypes._
import spray.routing._

class UserServiceActor extends Actor with UserService with ActorLogging {
  def actorRefFactory = context
  def receive = runRoute(myRoute)
}

trait UserService extends HttpService {
  import com.github.skp4.domain.UserJsonProtocol._
  import spray.httpx.SprayJsonSupport._

  val myRoute: Route =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          complete {
            <html>
              <body>
                <h1>Spray</h1>
              </body>
            </html>
          }
        }
      }
    } ~
      path("hello") {
        get {
          respondWithMediaType(`application/json`) {
            complete {
              User("1ambda", "Hoon", 27)
            }
          }
        }
      }


}