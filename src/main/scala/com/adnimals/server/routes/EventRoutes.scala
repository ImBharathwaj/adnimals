package com.adnimals.server.routes

import org.apache.pekko.http.scaladsl.server.Directives.*
import org.apache.pekko.http.scaladsl.server.Route
import org.apache.pekko.http.scaladsl.model.StatusCodes
import cats.effect.IO
import com.adnimals.server.model.{Ad, AdEvent}
import com.adnimals.server.store.RedisEventStore
import scala.concurrent.ExecutionContext
import cats.effect.unsafe.implicits.global
import com.github.pjfanning.pekkohttpcirce.FailFastCirceSupport.*
import io.circe.Encoder
import io.circe.Decoder
import io.circe.generic.semiauto.*
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.adnimals.server.routes.JsonSupport
import com.adnimals.server.model.Ad
import com.adnimals.server.model.AdEvent
import io.circe.syntax._

class EventRoutes(eventStore: RedisEventStore[IO])(implicit ec: ExecutionContext) {

  case class AdStats(adId: String, impressions: Int, clicks: Int)

  case class StatsResponse(stats: List[AdStats])

  case class EventResponse(events: List[AdEvent])

  implicit val eventResponseEncoder: Encoder[EventResponse] = deriveEncoder

  implicit val adEventEncoder: Encoder[com.adnimals.server.model.AdEvent] = deriveEncoder
  implicit val adListEncoder: Encoder[List[Ad]] = Encoder.encodeList[Ad]
  implicit val adEventDecoder: Decoder[AdEvent] = deriveDecoder

  implicit val adStatsEncoder: Encoder[AdStats] = deriveEncoder
  implicit val statsResponseEncoder: Encoder[StatsResponse] = deriveEncoder

  val routes: Route = pathPrefix("events") {
    concat(
      post {
        entity(as[AdEvent]) { event =>
          onSuccess(eventStore.add(event).as("Event added").unsafeToFuture()) { msg =>
            complete(StatusCodes.Created -> msg)
          }
        }
      },
      get {
        onSuccess(eventStore.list().unsafeToFuture()) { events =>
          complete(Encoder.encodeList[AdEvent].apply(events).asJson)
        }
      }
    )
  }
}
