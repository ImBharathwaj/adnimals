package com.adnimals.server.routes

import org.apache.pekko.http.scaladsl.server.Directives.*
import org.apache.pekko.http.scaladsl.model.StatusCodes
import org.apache.pekko.http.scaladsl.server.Route
import io.circe.syntax.*
import com.github.pjfanning.pekkohttpcirce.FailFastCirceSupport.*
import com.adnimals.server.store.AdStore
import com.adnimals.server.model.{Ad, AdEvent}
import com.adnimals.server.model.{AdStats, StatsResponse}
import com.adnimals.server.store.{EventStore, RedisAdStore}
import io.circe.Encoder
import io.circe.Decoder
import io.circe.generic.semiauto._
import cats.effect.IO
import cats.effect.unsafe.implicits.global

import java.time.Instant

class AdRoutes(adStore: RedisAdStore[IO]) extends JsonSupport {

    case class AdStats(adId: String, impressions: Int, clicks: Int)

    case class StatsResponse(stats: List[AdStats])

    case class EventResponse(events: List[AdEvent])

    implicit val eventResponseEncoder: Encoder[EventResponse] = deriveEncoder

    implicit val adEventEncoder: Encoder[com.adnimals.server.model.AdEvent] = deriveEncoder
    implicit val adListEncoder: io.circe.Encoder[List[Ad]] = io.circe.Encoder.encodeList[Ad]
    implicit val adEventDecoder: Decoder[AdEvent] = deriveDecoder

    implicit val adStatsEncoder: Encoder[AdStats] = deriveEncoder
    implicit val statsResponseEncoder: Encoder[StatsResponse] = deriveEncoder

    val routes = pathPrefix("ads") {
      concat(
        post {
          entity(as[Ad]) { ad =>
            onSuccess(adStore.add(ad).as("Ad added").unsafeToFuture()) { msg =>
              complete(msg)
            }
          }
        },
        get {
          onSuccess(adStore.getAll.unsafeToFuture()) { ads =>
            complete(ads)
          }
        },
        delete {
          parameter("id") { id =>
            onSuccess(adStore.delete(id).unsafeToFuture()) {
              case true  => complete("Ad deleted")
              case false => complete(StatusCodes.NotFound -> "Ad not found")
            }
          }
        }
      )
    }
  }


//class AdRoutes {
//
//  case class AdStats(adId: String, impressions: Int, clicks: Int)
//
//  case class StatsResponse(stats: List[AdStats])
//
//  case class EventResponse(events: List[AdEvent])
//
//  implicit val eventResponseEncoder: Encoder[EventResponse] = deriveEncoder
//
//  implicit val adEventEncoder: Encoder[com.adnimals.server.model.AdEvent] = deriveEncoder
//  implicit val adListEncoder: io.circe.Encoder[List[Ad]] = io.circe.Encoder.encodeList[Ad]
//  implicit val adEventDecoder: Decoder[AdEvent] = deriveDecoder
//
//  implicit val adStatsEncoder: Encoder[AdStats] = deriveEncoder
//  implicit val statsResponseEncoder: Encoder[StatsResponse] = deriveEncoder
//
//  val routes: Route =
//    concat(
//
//      // Health + greeting
//      pathSingleSlash {
//        get {
//          complete("👋 Welcome to Adnimals Delivery API")
//        }
//      },
//
//      // Ad delivery endpoint with targeting
//      path("ad") {
//        get {
//          parameters("slot_id", "geo".?, "device".?, "userId".?) { (slotId, geo, device, userId) =>
//            val context = Map(
//              "geo" -> geo,
//              "device" -> device,
//              "userId" -> userId
//            ).collect { case (k, Some(v)) => k -> v }
//
//            val maybeAd = AdStore.getBestMatch(slotId, context)
//
//            maybeAd match {
//              case Some(ad) =>
//                val now = Instant.now().getEpochSecond
//                println(s"[Delivery] Ad ${ad.id} served for slot=$slotId geo=${geo.getOrElse("N/A")} user=${userId.getOrElse("anon")} at $now")
//                complete(ad)
//              case None =>
//                complete(StatusCodes.NotFound, "No eligible ad available (possibly due to frequency cap).")
//            }
//          }
//        }
//      },
//
//      // List all ads (admin)
//      path("admin" / "ads") {
//        get {
//          complete(AdStore.list())
//        }
//      },
//
//      // Create ad
//      path("ads") {
//        post {
//          entity(as[Ad]) {
//            ad => val createdAd = AdStore.create(ad)
//              complete(201 -> createdAd)
//          }
//        } ~
//          get {
//            complete(AdStore.list())
//          }
//      },
//
//      // Delete ad by ID
//      path("ads" / Segment) { adId =>
//        delete {
//          val deleted = AdStore.delete(adId)
//          if (deleted) complete("Ad deleted")
//          else complete(404 -> "Ad not found")
//        }
//      },
//
//      // Create event
//      post {
//        entity(as[AdEvent]) { event =>
//          val stamped = event.copy(
//            timestamp = if (event.timestamp == 0) Instant.now().getEpochSecond else event.timestamp
//          )
//          EventStore.log(stamped)
//          complete(s"Event logged for ad: ${event.adId}")
//        }
//      },
//
//      // List events
//      path("admin" / "events") {
//        get {
//          complete(EventResponse(EventStore.list()))
//        }
//      },
//
//      // Basic stats
//      path("stats") {
//        get {
//          val grouped = EventStore.list().groupBy(_.adId)
//
//          val stats = grouped.map { case (adId, events) =>
//            val impressions = events.count(_.eventType == "impression")
//            val clicks = events.count(_.eventType == "click")
//            AdStats(adId, impressions, clicks)
//          }.toList
//
//          complete(StatsResponse(stats))
//        }
//      }
//    )
//}
