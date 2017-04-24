package fincherror

import com.twitter.server.TwitterServer
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Await

import io.circe.{Encoder, Json}
import io.finch._
import io.circe.generic.semiauto._
import io.circe.syntax._
import io.finch.circe._

object RestServer extends TwitterServer {

  case class Foo(s: String, i: Int)

  implicit val encodefoo: Encoder[Foo] = deriveEncoder

  // comment out this implicit to see the default encoder work.
  implicit val encodeException: Encoder[Exception] = Encoder.instance({
    case e: Error => exceptionToJSONError(e).asJson
    case Errors(nel) =>
      Json.arr(nel.toList.map(exceptionToJSONError).map(_.asJson): _*)
    case e: Exception => Json.obj("message" -> s"bare ex: ${e.getMessage}".asJson)
    case _ => Json.obj("message" -> "default handler".asJson)
  })

  def exceptionToJSONError(e: Exception): Foo = Foo(e.getMessage, 100)

  def ping: Endpoint[Foo] = get("ping") { Ok(Foo("Intentionally working", 1)) }
  def error: Endpoint[String] = get("error" :: paramOption("text")) {
    (t: Option[String]) => t.map(Ok).getOrElse(BadRequest(new Exception("No input")))
  }

  val api: Service[Request, Response] = (ping :+: error).toServiceAs[Application.Json]


  def main() = {
    val server = Http.server
        .serve(s":5000", api)

    onExit { server.close() }

    Await.ready(adminHttpServer)
  }
}
