package com.github.skp4.service

import akka.actor.{ActorLogging, Actor}

import spray.routing._
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._

import com.github.skp4.domain.UserJsonProtocol._
import com.github.skp4.domain.User

trait UserService extends HttpService {

  val userRoute: Route =
    path("user") {
      get {
        respondWithMediaType(`application/json`) {
          complete {
            User("1ambda", "Johen", 30)
          }
        }
      }
    }

}