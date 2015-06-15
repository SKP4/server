package com.github.skp4.domain

import spray.http._
import spray.httpx.unmarshalling.{FromStringDeserializer, Unmarshaller}
import spray.json.{RootJsonReader, JsonParser, DefaultJsonProtocol}

case class User(id: String, name: String, age: Int)
import spray.json._

object UserJsonProtocol extends DefaultJsonProtocol {

  implicit val userFormat = jsonFormat3(User)
  implicit val userUnmarshaller =
    Unmarshaller.delegate[String, User](MediaTypes.`application/json`) {
      _.parseJson.convertTo[User]
    }
}

