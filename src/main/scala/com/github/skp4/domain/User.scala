package com.github.skp4.domain

import spray.json.DefaultJsonProtocol

case class User(id: String, name: String, age: Int)

object UserJsonProtocol extends DefaultJsonProtocol {
  implicit val userFormat = jsonFormat3(User)
}

