package com.github.skp4.domain

import spray.http.MediaTypes
import spray.httpx.unmarshalling.Unmarshaller
import spray.json.DefaultJsonProtocol
import spray.json._

case class Response(status_code: Int, error_code: Int, message: String, more_info: String)

object ResponseJsonProtocol extends DefaultJsonProtocol {
  implicit val responseFormat = jsonFormat4(Response)
  implicit val userUnmarshaller =
    Unmarshaller.delegate[String, Response](MediaTypes.`application/json`) {
      _.parseJson.convertTo[Response]
  }
}
