package com.github.skp4

import akka.actor.{Actor, ActorRefFactory}
import com.github.skp4.service.UserService

class RoutesActor extends Actor with Routes {
  override val actorRefFactory: ActorRefFactory = context

  def receive = runRoute(routes)
}

trait Routes extends UserService {

  val routes = {
    userRoute
  }
}
