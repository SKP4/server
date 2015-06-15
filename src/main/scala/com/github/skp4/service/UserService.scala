package com.github.skp4.service

import akka.actor.{ActorLogging, Actor}
import spray.http.StatusCodes

import spray.routing._
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._

import com.github.skp4.domain.UserJsonProtocol._
import com.github.skp4.domain.{Response, User}

import scala.collection.mutable.ListBuffer

trait UserService extends HttpService {

  var userIds = Set.empty[String]

  val userRoute: Route =
    path("users") {
      get {
        respondWithMediaType(`application/json`) {
          complete {
            StatusCodes.OK -> User("1ambda", "Johen", 30)
          }
        }
      } ~ post {
        entity(as[User]) { user =>
          if (userIds.contains(user.id))
            complete(Response(409, 40901, s"duplicated id: ${user.id}", ""))


        }
      }
    }
}