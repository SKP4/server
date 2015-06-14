package com.github.skp4.domain

import spray.json.DefaultJsonProtocol

case class Response(status_code: Int, error_code: Int, message: String, more_info: String)

object ResponseJsonProtocol extends DefaultJsonProtocol {
  implicit val responseFormat = jsonFormat4(Response)
}
