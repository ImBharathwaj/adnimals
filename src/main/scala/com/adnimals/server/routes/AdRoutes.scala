package com.adnimals.server.routes

import org.apache.pekko.http.scaladsl.server.Directives.*
import org.apache.pekko.http.scaladsl.model.StatusCodes
import org.apache.pekko.http.scaladsl.server.Route
import io.circe.syntax.*
import com.github.pjfanning.pekkohttpcirce.FailFastCirceSupport.*
import com.adnimals.server.store.AdStore
import com.adnimals.server.model.{Ad, AdEvent}
import com.adnimals.server.model.{AdStats, StatsResponse}
import com.adnimals.server.store.EventStore
import io.circe.Encoder
import io.circe.Decoder
import io.circe.generic.semiauto._
import com.github.pjfanning.pekkohttpcirce.FailFastCirceSupport._

import java.time.Instant

class AdRoutes {

  case class AdStats(adId: String, impressions: Int, clicks: Int)
  case class StatsResponse(stats: List[AdStats])

  case class EventResponse(events: List[AdEvent])
  implicit val eventResponseEncoder: Encoder[EventResponse] = deriveEncoder

  implicit val adEventEncoder: Encoder[com.adnimals.server.model.AdEvent] = deriveEncoder
  implicit val adListEncoder: io.circe.Encoder[List[Ad]] = io.circe.Encoder.encodeList[Ad]
  implicit val adEventDecoder: Decoder[AdEvent] = deriveDecoder

  implicit val adStatsEncoder: Encoder[AdStats] = deriveEncoder
  implicit val statsResponseEncoder: Encoder[StatsResponse] = deriveEncoder

  // Schema of advertisement
  //    private val ads: List[Ad] = List(
  //      Ad("1", "https://cdn.example.com/ad1.jpg", "https://click.example.com/1", "home_top", Map("geo" -> "IN")),
  //      Ad("2", "https://cdn.example.com/ad2.jpg", "https://click.example.com/2", "home_top", Map("geo" -> "US")),
  //      Ad("3", "https://cdn.example.com/ad3.jpg", "https://click.example.com/3", "sidebar", Map("geo" -> "IN"))
  //    )

  val routes: Route =
    concat ( path("health") {
      get {
        parameter("slot_id", "geo".?) { (slotId, geo) =>
          val maybeAd = AdStore
            .list()
            .find(
              ad =>
                ad.slotId == slotId &&
                  geo.forall(g => ad.targeting.get("geo").contains(g))
            )

          maybeAd match {
            case Some(ad) => complete(ad)
            case None => complete(404 -> "No ad found")
          }
        }
      }
    },
      path("ads") {
        post{
          entity(as[Ad]) {
            ad => val createdAd = AdStore.create(ad)
              complete(201 -> createdAd)
          }
        }
      },
      path("ads"){
        get{
          complete(AdStore.list())
        }
      },
      path("ads" / Segment){ id =>
        delete {
          val removed = AdStore.delete(id)
          if (removed) complete(200 -> s"Deleted ad $id")
          else complete(404 -> s"Ad $id not found")
        }
      },
      path("event") {
        post {
          entity(as[AdEvent]) { event =>
            val stamped = event.copy(
              timestamp = if (event.timestamp == 0) Instant.now().getEpochSecond else event.timestamp
            )
            EventStore.log(stamped)
            complete(s"Event logged for ad: ${event.adId}")
          }
        }
      },
      path("events") {
        get {
          complete(EventResponse(EventStore.list()))
        }
      },
      path("stats") {
        get {
          val grouped = EventStore.list().groupBy(_.adId)

          val stats = grouped.map { case (adId, events) =>
            val impressions = events.count(_.eventType == "impression")
            val clicks = events.count(_.eventType == "click")
            AdStats(adId, impressions, clicks)
          }.toList

          complete(StatsResponse(stats))
        }
      }

    )
}
